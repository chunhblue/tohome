package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.StampSummaryReportSerivce;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/stampSummaryReport")
public class StampSummaryReportController extends BaseAction {

    @Autowired
    private StampSummaryReportSerivce serivce;

    private final String EXCEL_EXPORT_KEY = "STAMP_SUMMARY_DAILY_REPORT";
    private final String EXCEL_EXPORT_NAME = "Stamp Summary Report At Store Level.xlsx";

    @Permission(codes = { PermissionCode.CODE_SC_STAMP_SUMMARY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toListView(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 印章总结日报", u.getUserId());
        ModelAndView mv = new ModelAndView("stampSummaryReport/stampSummaryReport");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("author", u.getUserName());
        mv.addObject("bsDate", new Date());
        mv.addObject("useMsg", "门店库存调整日报一览画面");
        return mv;
    }

    @RequestMapping ("/getUserName")
    @ResponseBody
    public ReturnDTO getUserName(HttpServletRequest request, HttpSession session){
        User user = this.getUser(session);
        if (user==null) {
            return  new ReturnDTO(false,"没有登陆");
        }
        return new ReturnDTO(true,"登陆成功",user);
    }

    @RequestMapping(value ="/search",method = RequestMethod.POST)
    @ResponseBody
    public  ReturnDTO getStampSummaryReportContent(String SearchJson,HttpServletRequest request, HttpSession session){
        StampSummaryParamReportDto param = null;
        if (SearchJson != null && !StringUtils.isEmpty(SearchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson, StampSummaryParamReportDto.class);
        }
        if (param == null) {
            param = new StampSummaryParamReportDto();
        }

        param.setLimitStart((param.getPage() - 1)*param.getRows());

        Map<String,Object> result = serivce.search(param);
        return new ReturnDTO(true,"ok",result);
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_STAMP_SUMMARY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        StampSummaryParamReportDto param = gson.fromJson(searchJson, StampSummaryParamReportDto.class);

        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_STAMP_SUMMARY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("stampSummaryExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
}
