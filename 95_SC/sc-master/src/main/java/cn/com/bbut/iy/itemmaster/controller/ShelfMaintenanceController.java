package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfDto;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfParamDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.POGShelfService;
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
import java.util.Collection;
import java.util.Map;

@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/shelfMaintenance")
public class ShelfMaintenanceController  extends BaseAction {
    @Autowired
    private POGShelfService pogShelfService;

    private final String EXCEL_EXPORT_KEY = "POG_SHELF_MAINTENANCE_MANAGEMENT";
    private final String EXCEL_EXPORT_NAME = "POG Shelf Maintenance Management.xlsx";

//    @Permission(codes = { PermissionCode.CODE_SC_ST_ADJUST_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolist(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 POG维护管理画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("pog_shelf_mm/pogShelfList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "POG维护管理画面");
        return mv;
    }

    @RequestMapping("/getdata")
    @ResponseBody
    public GridDataDTO<PogShelfDto> inquire(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        Gson gson = new Gson();
        PogShelfParamDto param = gson.fromJson(searchJson, PogShelfParamDto.class);
        if(param == null){
            param = new PogShelfParamDto();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        GridDataDTO<PogShelfDto> data = pogShelfService.getPogShelfList(param);
        return data;
    }

    @RequestMapping(method = RequestMethod.GET,value = "/view")
//    @Permission(codes = { PermissionCode.CODE_SC_OD_FAILED_DAY_VIEW})
    public ModelAndView toPogShelfView(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model, PogShelfDto pogShelfDto) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 POG维护管理明细画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("pog_shelf_mm/pogShelfListView");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("dto", pogShelfDto);
        mv.addObject("useMsg", "POG维护管理明细画面");
        return mv;
    }

    @RequestMapping("/view/getDetailData")
    @ResponseBody
    public GridDataDTO<PogShelfDto> pogShelfDetail(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        Gson gson = new Gson();
        PogShelfParamDto param = gson.fromJson(searchJson, PogShelfParamDto.class);
        if(param == null){
            param = new PogShelfParamDto();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        GridDataDTO<PogShelfDto> data = pogShelfService.pogShelfDetail(param);
        return data;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
//    @Permission(codes = { PermissionCode.CODE_SC_OD_FAILED_DAY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
//        exParam.setPCode(PermissionCode.CODE_SC_OD_FAILED_DAY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("shelfMaintenanceExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
}
