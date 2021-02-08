package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.ItemTransferService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 店内商品调拨
 * @author lch
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/itemTransfer")
public class ItemTransferController  extends BaseAction {

    @Autowired
    private ItemTransferService service;
    @Autowired
    private InventoryVouchersService InventoryService;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_ITEM_TRANSFER";
    private final String EXCEL_EXPORT_NAME = "Item Transfer Query.xlsx";

    /**
     * 店内商品调拨一览
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ITEM_TRANSFER_LIST_VIEW})
    public ModelAndView storeTransfersList(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店内商品调拨一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("itemTransfer/itemTransfer");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_ITEM_TRANSFER);
        mv.addObject("useMsg", "店内商品调拨一览画面");
        return mv;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDataByType")
    public GridDataDTO<InventoryVouchersGridDTO> getDataByType(HttpServletRequest request, HttpSession session,
                                                               int page, int rows, String searchJson) {
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        // 为空返回
        if(param == null){
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<InventoryVouchersGridDTO>();
        }
        param.setStores(stores);

        return service.getByTypeCondition(param);
    }

    /**
     * 店间调拨管理详情查看画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_ITEM_TRANSFER_VIEW
    })
    public ModelAndView view(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调出管理详情查看画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("itemTransfer/itemTransferedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", "view");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_ITEM_TRANSFER); // 23
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_ITEM_TRANSFER);
        mv.addObject("useMsg", "店内商品调拨详情画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 店间调拨管理详情编辑、新增画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_ITEM_TRANSFER_ADD,
            PermissionCode.CODE_SC_ITEM_TRANSFER_EDIT
    })
    public ModelAndView edit(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调出管理详情编辑、新增画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("itemTransfer/itemTransferedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", flag);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_ITEM_TRANSFER); // 23
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_ITEM_TRANSFER);
        mv.addObject("useMsg", "店内商品调拨详情编辑画面、新增画面");
        this.saveToken(request);
        return mv;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_ITEM_TRANSFER_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
//        exParam.setPCode(PermissionCode.CODE_SC_ST_TRANSFEROUT_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("itemTransferExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 店内调拨商品详情更新
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update")
//    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFEROUT_EDIT })
    public ReturnDTO updateDetails(HttpServletRequest request, HttpSession session, String listJson, String flg) {
        ReturnDTO _return = new ReturnDTO();
        if(!"1".equals(flg)){
            // 未修改数量，不执行更新
            _return.setMsg("Don't need to update data!");
            _return.setSuccess(true);
            return _return;
        }
        if(StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 执行保存
        int i = InventoryService.updateSk0020(dto, sk0020List);
        if(i < 1){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setSuccess(true);
        }
        return _return;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/insert")
    public ReturnDTO saveInventoryVouchers(HttpServletRequest request, HttpSession session,
                                           String searchJson, String listJson,String fileDetailJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        Sk0010DTO sk0010 = gson.fromJson(searchJson, Sk0010DTO.class);
        if(sk0010 == null){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        sk0010.setCommonDTO(dto);
        String _id = service.insert(sk0010, sk0020List);
        if(StringUtils.isBlank(_id)){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setSuccess(true);
            _return.setO(_id);
        }
        return _return;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/modify")
    public ReturnDTO modifyInventoryVouchers(HttpServletRequest request, HttpSession session,
                                             String searchJson, String listJson ,String fileDetailJson , String pageType) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        SK0010 sk0010 = gson.fromJson(searchJson, SK0010.class);
        if(sk0010 == null){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }

        // 执行保存
        sk0010.setUpdateUserId(dto.getUpdateUserId());
        sk0010.setUpdateYmd(dto.getUpdateYmd());
        sk0010.setUpdateHms(dto.getUpdateHms());
        int i = service.update(sk0010, sk0020List);
        if(i < 1){
            _return.setMsg("Detail Data saved failed！");
        }else {
            _return.setMsg("Detail Data saved successfully！");
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 获取当前操作用户、时间
     */
    private CommonDTO getCommonDTO(HttpSession session){
        User u = this.getUser(session);
        if(u == null){
            return null;
        }
        CommonDTO dto = new CommonDTO();
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(date);
        dto.setUpdateYmd(date);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(time);
        dto.setUpdateHms(time);
        return dto;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,InventoryVouchersParamDTO param){
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
        // 只选择了一部分参数，生成查询参数，后台查询
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
        stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }

    /**
     * 获得 Voucher Type 列表
     */
    @ResponseBody
    @RequestMapping(value = "/getTypeList")
    public List<AutoCompleteDTO> getTypeList(HttpServletRequest request, HttpSession session,
                                             String v) {
        List<AutoCompleteDTO> _list = service.getTypeList(v);
        return _list;
    }

    /**
     * 查询明细详情
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDetails")
    public GridDataDTO<Sk0020DTO> getDetails(HttpServletRequest request, HttpSession session,
                                             int page, int rows, String searchJson) {
        // 转换参数对象
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            return new GridDataDTO<Sk0020DTO>();
        }
        // 执行查询
        return service.getSk0020(sk0020);
    }
}
