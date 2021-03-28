package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/stampDetailReport")
public class StampDeatilReportController  extends BaseAction {

    @Autowired
    private StampSummaryReportSerivce serivce;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "STAMP_DETAIL_DAILY_REPORT";
    private final String EXCEL_EXPORT_NAME = "Stamp Detail Report At Store Level.xlsx";

    @Permission(codes = { PermissionCode.CODE_SC_STAMP_DETAIL_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toListView(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 印章明细日报", u.getUserId());
        ModelAndView mv = new ModelAndView("stampSummaryReport/stampDetailReport");
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
    public  ReturnDTO getStampDetailReportContent(String SearchJson,HttpServletRequest request, HttpSession session){
        StampSummaryParamReportDto param = null;
        if (SearchJson != null && !StringUtils.isEmpty(SearchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson, StampSummaryParamReportDto.class);
        }
        if (param == null) {
            param = new StampSummaryParamReportDto();
        }

        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setLimitStart((param.getPage() - 1)*param.getRows());
        param.setStores(stores);
        Map<String,Object> result = serivce.searchDetail(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_STAMP_DETAIL_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        StampSummaryParamReportDto param = gson.fromJson(searchJson, StampSummaryParamReportDto.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }

        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setStores(stores);
        exParam.setPCode(PermissionCode.CODE_SC_STAMP_DETAIL_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("stampDetailExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, StampSummaryParamReportDto param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
                && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(StringUtils.isNotBlank(param.getStoreCd())){
            stores.add(param.getStoreCd());
            return stores;
        }
        // 只选择了一部分参数，生成查询参数，后台查询判断
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
        stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }
}
