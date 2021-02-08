package cn.com.bbut.iy.itemmaster.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bm.BmViewMainDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisExcelParam;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisInitDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisListGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.bmhis.BmHisService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;

/**
 * BM历史查询
 * 
 * @author songxz
 */
@Slf4j
@Controller
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/bmhistory")
public class BmHisController extends BaseAction {
    @Autowired
    private BmHisService service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_BM_HIS";
    private final String EXCEL_EXPORT_NAME = "BM历史查询一览导出.xlsx";

    @SuppressWarnings("unchecked")
    @Permission(code = PermissionCode.P_CODE_BM_HIS_VIEW)
    @RequestMapping
    public ModelAndView toListView(HttpServletRequest request, HttpSession session,
            Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 BM历史查询画面", u.getUserId());
        ModelAndView mv = new ModelAndView("/other_m/bm_his/bmHisList");
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        BmHisInitDTO dto = service.getInitData(roleIds, u);
        mv.addObject("init", dto);
        return mv;
    }

    /**
     * 得到部集合 <br>
     * 画面中的dpt实际是部，这里特殊
     * 
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getdepartments")
    public List<AutoCompleteDTO> getDepartments(HttpServletRequest request, HttpSession session,
            String division) {
        List<AutoCompleteDTO> department = service.getdepartments(
                PermissionCode.P_CODE_BM_HIS_VIEW, division);
        return department;
    }

    /**
     * 查看画面/审核跳转 <br>
     * 当前画面中包含了 查看和审核的功能
     * 
     * @param req
     * @param session
     * @param model
     * @param bmCode
     *            bm编码
     * @param bmType
     *            bm类型
     * @param identity
     *            由list页面带入，1：采购样式，2:事业部部长，3：系统部，4：店铺
     * @return
     */
    @Permission(code = PermissionCode.P_CODE_BM_HIS_VIEW)
    @RequestMapping(value = "/view")
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
            Map<String, ?> model, String bmType, String newNo, String newNoSub) {
        String userId = this.getUserId(session);
        log.debug("User:{} 进入 BM历史查询画面 ", userId);
        ModelAndView mv = new ModelAndView("/other_m/bm_his/bmhisview");
        // 设定画面类型
        BmViewMainDTO data = service.getBmHisViewData(bmType, newNo, newNoSub);
        mv.addObject("bmType", bmType);
        mv.addObject("data", data);
        this.saveToken(request);
        return mv;
    }

    /**
     * EXPORT
     *
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @Permission(codes = { PermissionCode.P_CODE_BM_HIS_EXCEL })
    @RequestMapping(value = "/excel")
    public String excel(HttpServletRequest request, HttpSession session, BmHisParamDTO param) {
        List<Integer> roleIds = (List<Integer>) session.getAttribute(Constants.SESSION_ROLES);
        BmHisExcelParam exParam = new BmHisExcelParam();
        exParam.setRoleIds(roleIds);
        exParam.setPCode(PermissionCode.P_CODE_BM_HIS_EXCEL);
        exParam.setParam(param);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("bmHisExcelService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 得到zggrid数据
     * 
     * @param session
     * @param model
     * @param v
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getdata")
    public GridDataDTO<BmHisListGridDataDTO> getData(HttpServletRequest request,
            HttpSession session, Map<String, ?> model, BmHisParamDTO param) {
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        param.setRoleIds(roleIds);
        param.setPCode(PermissionCode.P_CODE_BM_HIS_VIEW);
        return service.getData(param);
    }
}
