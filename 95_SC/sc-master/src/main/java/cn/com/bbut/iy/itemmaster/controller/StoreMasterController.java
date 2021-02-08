package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.store.*;
import cn.com.bbut.iy.itemmaster.entity.MA0050;
import cn.com.bbut.iy.itemmaster.entity.MA1020;
import cn.com.bbut.iy.itemmaster.entity.MA1040;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.master.ArticleMasterService;
import cn.com.bbut.iy.itemmaster.service.master.StoreMasterService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 店铺主档检索
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeMaster")
public class StoreMasterController extends BaseAction {

    @Autowired
    private StoreMasterService service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_STORE_MASTER";
    private final String EXCEL_EXPORT_NAME = "Store Master List.xlsx";

    /**
     * 店铺主档检索画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ZD_SMQ_LIST_VIEW })
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店铺主档检索画面", u.getUserId());
        ModelAndView mv = new ModelAndView("master/store/storeList");
        mv.addObject("useMsg", "店铺主档检索画面");
        return mv;
    }

    /**
     * 店铺主档详情画面
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/view")
    @Permission(codes = { PermissionCode.CODE_SC_ZD_SMQ_VIEW })
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                               StoreParamDTO param) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店铺主档详情画面", u.getUserId());
        ModelAndView mv = new ModelAndView("master/store/storeDetails");
        mv.addObject("dto", param);
        mv.addObject("useMsg", "店铺主档详情画面");
        return mv;
    }

    /**
     * 条件查询店铺主档
     * @param request
     * @param session
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<StoreGridDTO> getList(HttpServletRequest request, HttpSession session,
                                             StoreParamDTO param) {
        //获取店铺
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        if(stores!=null&&stores.size()>0){
            if(param == null){
                param = new StoreParamDTO();
            }
            param.setStores(stores);
            return service.getList(param);
        }
        return new GridDataDTO<>();
    }

    /**
     * 查询主档单选框信息
     * @param request
     * @param session
     * @param code
     * @param attributeType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAttachedGroup")
    public AjaxResultDto getAttachedGroup(HttpServletRequest request, HttpSession session,
                                          String code, String attributeType) {
        AjaxResultDto _return = new AjaxResultDto();
        if(StringUtils.isBlank(attributeType)&&StringUtils.isBlank(code)){
            _return.setMessage("Parameter cannot be empty!");
            return _return;
        }
        List<MA0050> list = service.getAttachedGroup(code,attributeType);
        if(list == null){
            _return.setSuccess(false);
            _return.setMessage("获取主档单选框信息");
        }else{
            _return.setMessage("获取主档单选框信息");
            _return.setSuccess(true);
            _return.setData(list);
        }
        return _return;
    }

    /**
     * 查询主档基本信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getData")
    public AjaxResultDto getBasicInfo(HttpServletRequest request, HttpSession session,
                                                String storeCd,String effectiveStartDate) {
        AjaxResultDto _return = new AjaxResultDto();
        if(StringUtils.isBlank(storeCd)&&
                StringUtils.isBlank(effectiveStartDate)){
            _return.setMessage("Parameter cannot be empty!");
            return _return;
        }
        StoreInfoDTO storeInfoDTO = service.getStoreInfo(storeCd,effectiveStartDate);
        if(storeInfoDTO == null){
            _return.setSuccess(false);
            _return.setMessage("获取店铺主档信息失败");
        }else{
            _return.setMessage("获取店铺主档信息成功");
            _return.setSuccess(true);
            _return.setData(storeInfoDTO);
        }
        return _return;
    }

    /**
     * 获取附加信息
     * @param request
     * @param session
     * @param attributeType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAttachedInfo")
    public AjaxResultDto getAttachedGroup(HttpServletRequest request, HttpSession session,
                                          String storeCd, String effectiveStartDate, String attributeType) {
        AjaxResultDto _return = new AjaxResultDto();
        if(StringUtils.isBlank(storeCd)&&
                StringUtils.isBlank(effectiveStartDate)&&
                StringUtils.isBlank(attributeType)){
            _return.setMessage("Parameter cannot be empty!");
            return _return;
        }
        List<MA1020> list = service.getAttachedInfo(storeCd,effectiveStartDate,attributeType);
        if(list == null){
            _return.setSuccess(false);
            _return.setMessage("获取主档单选框信息");
        }else{
            _return.setMessage("获取主档单选框信息");
            _return.setSuccess(true);
            _return.setData(list);
        }
        return _return;
    }

    /**
     * 获取License信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getLicenseInfo")
    public GridDataDTO<MA1040> getLicenseInfo(HttpServletRequest request, HttpSession session,
                                              String storeCd, String effectiveStartDate) {
        if(StringUtils.isBlank(storeCd)&&
                StringUtils.isBlank(effectiveStartDate)){
            return new GridDataDTO<>();
        }
        GridDataDTO<MA1040> result= new GridDataDTO<>();
        List<MA1040> list = service.getLicenseInfo(storeCd,effectiveStartDate);
        result.setRows(list);
        return result;
    }

    /**
     * 获取Accounting信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAccountingInfo")
    public GridDataDTO<MA1050GridDTO> getAccountingInfo(HttpServletRequest request, HttpSession session,
                                                        String storeCd, String effectiveStartDate) {
        if(StringUtils.isBlank(storeCd)&&
                StringUtils.isBlank(effectiveStartDate)){
            return new GridDataDTO<>();
        }
        GridDataDTO<MA1050GridDTO> result= new GridDataDTO<>();
        List<MA1050GridDTO> list = service.getAccountingInfo(storeCd,effectiveStartDate);
        result.setRows(list);
        return result;
    }

    /**
     * 获取竞争者信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCompetitorInfo")
    public GridDataDTO<MA1060GridDTO> getCompetitorInfo(HttpServletRequest request, HttpSession session,
                                                        String storeCd, String effectiveStartDate) {
        if(StringUtils.isBlank(storeCd)&&
                StringUtils.isBlank(effectiveStartDate)){
            return new GridDataDTO<>();
        }
        GridDataDTO<MA1060GridDTO> result= new GridDataDTO<>();
        List<MA1060GridDTO> list = service.getCompetitorInfo(storeCd,effectiveStartDate);
        result.setRows(list);
        return result;
    }


    /**
     * 获取法人信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMa0010")
    public List<AutoCompleteDTO> getMa0010(HttpServletRequest request, HttpSession session) {
        List<AutoCompleteDTO> list = service.getMa0010();
        return list;
    }

    /**
     * 获取price group信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMa0200")
    public List<AutoCompleteDTO> getMa0200(HttpServletRequest request, HttpSession session) {
        List<AutoCompleteDTO> list = service.getMa0200();
        return list;
    }

    /**
     * 获取Store Cluster信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMa0030")
    public List<AutoCompleteDTO> getMa0030(HttpServletRequest request, HttpSession session,String v) {
        List<AutoCompleteDTO> list = service.getMa0030(v);
        return list;
    }

    /**
     * 获取Store Group信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMa0035")
    public List<AutoCompleteDTO> getMa0035(HttpServletRequest request, HttpSession session,String storeTypeCd,String v) {
        if(StringUtils.isBlank(storeTypeCd)){
            log.info("storeTypeCd is null");
            return null;
        }
        List<AutoCompleteDTO> list = service.getMa0035(storeTypeCd,v);
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getCluster")
    public ReturnDTO getClusterByStoreCd(HttpServletRequest request, HttpSession session,
                                                     @RequestParam("a_store")String storeCd) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(storeCd)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        StoreInfoDTO list = service.getClusterByStoreCd(storeCd);
        if(list == null){
            _return.setMsg("No data found!");
        }else{
            _return.setSuccess(true);
            _return.setO(list);
        }
        return _return;
    }

    @ResponseBody
    @RequestMapping(value = "/getGroup")
    public ReturnDTO getGroupByStoreCd(HttpServletRequest request, HttpSession session,
                                         @RequestParam("a_store")String storeCd) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(storeCd)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        StoreInfoDTO list = service.getGroupByStoreCd(storeCd);
        if(list == null){
            _return.setMsg("No data found!");
        }else{
            _return.setSuccess(true);
            _return.setO(list);
        }
        return _return;
    }

    /**
     * 获取附加信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMa0050")
    public List<AutoCompleteDTO> getMa0050(HttpServletRequest request, HttpSession session,@RequestParam String attributeType) {
        List<AutoCompleteDTO> list = service.getMa0050(attributeType);
        return list;
    }

    /**
     * 获取省地区信息
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMa0025")
    public List<AutoCompleteDTO> getMa0025(HttpServletRequest request, HttpSession session,
                                           String level,String adminAddressCd,String v) {
        if(StringUtils.isBlank(level)){
            level = "0";
            adminAddressCd = null;
        }
        List<AutoCompleteDTO> list = service.getMa0025(level,adminAddressCd,v);
        return list;
    }

    /**
     * 导出查询结果
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_ZD_SMQ_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        //获取店铺
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        if(stores==null||stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ZD_SMQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        exParam.setStores(stores);
        ExService service = Container.getBean("storeExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

}
