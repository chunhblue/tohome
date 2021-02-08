package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.*;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.master.VendorMasterService;
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
import java.util.List;
import java.util.Map;

/**
 * 供应商主档检索
 *
 * @author mxy
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/vendorMaster")
public class VendorMasterController extends BaseAction {

    @Autowired
    private VendorMasterService service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_VENDOR_MASTER";
    private final String EXCEL_EXPORT_NAME = "Vendor Master List.xlsx";

    /**
     * 供应商主档检索画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ZD_VMQ_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 供应商主档检索画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("master/vendor/vendorList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "供应商主档检索画面");
        return mv;
    }

    /**
     * 供应商主档详情画面
     *
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/view")
    @Permission(codes = { PermissionCode.CODE_SC_ZD_VMQ_VIEW})
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                               VendorParamDTO param) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 供应商主档详情画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("master/vendor/vendorDetails");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("dto", param);
        mv.addObject("useMsg", "供应商主档详情画面");
        return mv;
    }

    /**
     * 条件查询供应商主档
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<VendorDTO> getList(HttpServletRequest request, HttpSession session,
                                          int page, int rows, String searchJson) {

        if(this.getUser(session) == null){
            return new GridDataDTO<VendorDTO>();
        }

        // 转换参数对象
        VendorParamDTO param;
        if(searchJson == null){
            param = new VendorParamDTO();
        }else{
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, VendorParamDTO.class);
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);

        return service.getList(param);
    }

    /**
     * 查询主档基本信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getData")
    public ReturnDTO getBasicInfo(HttpServletRequest request, HttpSession session,
                                  String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        if(this.getUser(session) == null){
            _return.setMsg("Failed to get login user information");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        VendorDTO _dto = service.getBasicInfo(param);
        if(_dto == null){
            _return.setMsg("Vendor information obtained failed!");
        }else{
            _return.setMsg("Vendor information obtained succeed!");
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    /**
     * 查询主档配送信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDeliveryType")
    public GridDataDTO<DeliveryTypeDTO> getDeliveryType(HttpServletRequest request, HttpSession session,
                                                        String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<DeliveryTypeDTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getDeliveryType(param);
    }

    /**
     * 查询主档最低订户数量/金额
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getMinOtyAmt")
    public GridDataDTO<Ma2002DTO> getMa2002List(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2002DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getMa2002List(param);
    }

    /**
     * 查询主档大分类信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDepartment")
    public GridDataDTO<Ma2003DTO> getMa2003List(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2003DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getMa2003List(param);
    }

    /**
     * 查询主档银行信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getBankInfo")
    public GridDataDTO<Ma2004DTO> getMa2004List(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2004DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getMa2004List(param);
    }

    /**
     * 查询主档商品分类信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getProductGroup")
    public GridDataDTO<Ma2005DTO> getMa2005List(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2005DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getMa2005List(param);
    }

    /**
     * 查询Vendor email
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getVendorEmail")
    public GridDataDTO<Ma2008DTO> getVendorEmail(HttpServletRequest request, HttpSession session,
                                                String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2008DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getVendorEmail(param);
    }

    /**
     * 查询 Trading term数据
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getTradingTerm")
    public GridDataDTO<Ma2007DTO> getTradingTerm(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2007DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getTradingTerm(param);
    }

    /**
     * 查询 account数据
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getAccountInfo")
    public GridDataDTO<Ma2009DTO> getAccountInfo(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma2009DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        VendorParamDTO param = gson.fromJson(searchJson, VendorParamDTO.class);

        return service.getAccountInfo(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_ZD_VMQ_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ZD_VMQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("vendorExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 检索供应商数据
     *
     * @param request
     * @param session
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get")
    public List<AutoCompleteDTO> get(HttpServletRequest request, HttpSession session, String v){
        return service.getList(v);
    }

}
