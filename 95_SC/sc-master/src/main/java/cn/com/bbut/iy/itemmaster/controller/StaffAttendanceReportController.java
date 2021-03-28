package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.StaffAttendanceDailySerivce;
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
import java.util.Date;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/staffAttendanceReport")
public class StaffAttendanceReportController  extends BaseAction {

    @Autowired
    private StaffAttendanceDailySerivce serivce;

    private final String EXCEL_EXPORT_KEY = "STAFF_ATTENDANCE_DETAIL_REPORT";
    private final String EXCEL_EXPORT_NAME = "Store Staff Attendance Detail Report HCM.xlsx";

    @Permission(codes = { PermissionCode.CODE_SC_STAFF_ATTENDANCE_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toListView(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 印章明细日报", u.getUserId());
        ModelAndView mv = new ModelAndView("storeStaffAttendanceDaily/StaffAttendanceReport");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("author", u.getUserName());
        mv.addObject("bsDate", new Date());
        mv.addObject("useMsg", "门店库存调整日报一览画面");
        return mv;
    }

    @RequestMapping(value ="/search",method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO getStaffAttendanceReport(String SearchJson, HttpServletRequest request, HttpSession session){
        StaffAttendanceParamDailyDto param = null;
        if (SearchJson != null && !StringUtils.isEmpty(SearchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson, StaffAttendanceParamDailyDto.class);
        }
        if (param == null) {
            param = new StaffAttendanceParamDailyDto();
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
    @Permission(codes = { PermissionCode.CODE_SC_STAFF_ATTENDANCE__EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }

        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_STAFF_ATTENDANCE__EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("staffAttendanceExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
}
