package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.*;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.RWHItemsGridResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.RWHListResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.ReturnWarehouseDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.ReturnWarehouseParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 退供应商申请单
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/returnVendor")
public class ReturnVendorController extends BaseAction {

    private final String EXCEL_EXPORT_KEY = "EXCEL_RETURN_VENDOR";
    private final String EXCEL_EXPORT_NAME = "Item Return Request Query(To Supplier).xlsx";
    private final String EXCEL_EXPORT_KEY1 = "EXCEL_RETURN_VENDOR_RECEIPT";
    private final String EXCEL_EXPORT_NAME1 = "Item Return Entry(To Supplier).xlsx";

    @Autowired
    private ReturnVendorService returnVendorService;

    @Autowired
    private ReturnWarehouseService returnWarehouseService;

    @Autowired
    private CM9060Service cm9060Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;

    /**
     * 退供应商申请单管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_V_RETURN_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退供应商申请单", u.getUserId());
        ModelAndView mv = new ModelAndView("return/vendor/returnlist");
        mv.addObject("useMsg", "退供应商申请单画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_RETURN_VENDOR);
        return mv;
    }

    // 一览页面条件查询
    @ResponseBody
    @RequestMapping(value = "/query",method={RequestMethod.POST,RequestMethod.GET})
    public GridDataDTO<RVListResult> query(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model, RVListParamDTO param) {
        // 获取当前角色店铺权限
      RVListParam dto = new Gson().fromJson(param.getSearchJson(),RVListParam.class);
        Collection<String> stores = getStores(session,dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<RVListResult>();
        }
        param.setStores(stores);
        return returnVendorService.getReturnVQueryList(param);
    }


    /**
     * 退供应商申请单详细管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/cancelOrderDetail", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_V_RETURN_ADD,
            PermissionCode.CODE_SC_V_RETURN_EDIT,
            PermissionCode.CODE_SC_V_RETURN_VIEW
    })
    public ModelAndView cancelOrderDetail(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model,String flag,String orderId) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退供应商申请单详细管理画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("return/vendor/returnedit");
        mv.addObject("flag",flag);
        mv.addObject("orderId",orderId);
        mv.addObject("orderDate",date);
        mv.addObject("useMsg", "退供应商申请单详细管理");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_RETURN_VENDOR);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_RETURN_VENDOR);
        this.saveToken(request);
        return mv;
    }

    /**
     * 原单据编号list
     * @param session
     * @param req
     * @param param
     * @return
     */
    @RequestMapping(value = "/getOrgOrderIdList")
    @ResponseBody
    public List<AutoCompleteDTO> getOrgOrderIdList(HttpSession session, HttpServletRequest req,
                                                   ReturnWarehouseParamDTO param) {
        List<AutoCompleteDTO> list = new ArrayList<>();
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        if(stores ==null ||stores.size()<1){
            return list;
        }
        if(param!=null){
            if(StringUtils.isNotBlank(param.getOrderDifferentiate())){
                param.setStores(stores);
                list = returnWarehouseService.getOrgOrderIdList(param);
            }
        }
        return list;
    }

    /**
     * 获取原单号信息
     * @param session
     * @param req
     * @param orgOrderId 原单号
     * @return
     */
    @RequestMapping(value = "/getOrderInfo")
    @ResponseBody
    public AjaxResultDto getOrderInfo(HttpSession session, HttpServletRequest req,
                                      String orgOrderId,String returnDiff) {
        AjaxResultDto resultDto = new AjaxResultDto();
        if(StringUtils.isNotBlank(orgOrderId)){
            OrderInfoDTO orderInfoDTO = returnWarehouseService.getOrderInfo(orgOrderId,returnDiff);
            if(orderInfoDTO!=null){
                resultDto.setData(orderInfoDTO);
                resultDto.setSuccess(true);
                return resultDto;
            }
        }
        resultDto.setSuccess(false);
        resultDto.setMessage("Failed to load order data!");//订单数据加载失败
        return resultDto;
    }

    /**
     * 该店铺退货商品信息
     * @param session
     * @param req
     * @param param
     * @return
     */
    @RequestMapping(value = "/getItems")
    @ResponseBody
    public List<AutoCompleteDTO> getItems(HttpSession session, HttpServletRequest req,
                                          ReturnWarehouseParamDTO param,String returnType,String storeCd) {

        List<AutoCompleteDTO> list = new ArrayList<>();

        if (returnType.equals("10")){
            param.setStoreCd(storeCd);
            //获取业务时间
            String date = cm9060Service.getValByKey("0000");
            param.setOrderDate(date);
            list = returnWarehouseService.getdirectItems(param);
        }
        if(param!=null){
            if(StringUtils.isNotBlank(param.getOrderId())){
                list = returnWarehouseService.getItems(param);
            }
        }
        return list;
    }

    /**
     * 该店铺退货商品详细信息
     * @param session
     * @param req
     * @param param
     * @return
     */
    @RequestMapping(value = "/getItemInfo")
    @ResponseBody
    public AjaxResultDto getItemInfo(HttpSession session, HttpServletRequest req,
                                     ReturnWarehouseParamDTO param) {
        User user = this.getUser(session);
        log.debug("抓取商品信息，user:{}", user.getUserId());
        AjaxResultDto resultDto = new AjaxResultDto();
        if(param!=null){

            if (param.getReturnType().equals("10")){
                ReturnWarehouseDetailInfo itemInfo = returnWarehouseService.getdirectItemInfo(param);
                if(itemInfo!=null){
                    resultDto.setData(itemInfo);
                    resultDto.setSuccess(true);
                    resultDto.setFlag("1");
                    return resultDto;
                }
            }
            if(StringUtils.isNotBlank(param.getArticleId())&&
                    StringUtils.isNotBlank(param.getOrderId())){
                ReturnWarehouseDetailInfo itemInfo = returnWarehouseService.getItemInfo(param);
                if(itemInfo!=null){
                    resultDto.setData(itemInfo);
                    resultDto.setSuccess(true);
                    resultDto.setFlag("2");
                    return resultDto;
                }
            }
        }
        resultDto.setSuccess(false);
        resultDto.setMessage("Failed to load item data!");//商品数据加载失败
        return resultDto;
    }

    /**
     * 供应商退货保存
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_V_RETURN_ADD,
            PermissionCode.CODE_SC_V_RETURN_EDIT,
    })
    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResultDto returnVendorSave(HttpServletRequest request, HttpSession session,
                                         OD0000 param, String use, String orderDetailJson) {
        User u = this.getUser(session);
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            res.setToKen(res.getToKen());
            return res;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        if (!param.getReturnType().equals("10")){
            if(StringUtils.isBlank(param.getStoreCd())||
                    StringUtils.isBlank(param.getOrderDate())||
                    StringUtils.isBlank(param.getOrgOrderId())){
                res.setSuccess(false);
                res.setMessage("Submitted failed,Parameter cannot be empty!");
                return res;
            }
        }
        try {
            if("1".equals(use)){//修改退货单
                if(StringUtils.isBlank(param.getOrderId())){
                    res.setMessage("Modification failed,The order Id cannot be empty!");
                    res.setSuccess(false);
                    return res;
                }
                param.setUpdateYmd(ymd);
                param.setUpdateHms(hms);
                param.setUpdateUserId(u.getUserId());
                param = returnWarehouseService.updateReturnOrder(param,orderDetailJson);
            }else if("2".equals(use)){//新增退货单
                param.setOrderDifferentiate("0"); //退货区分 0供应商
                param.setCreateYmd(ymd);
                param.setCreateHms(hms);
                param.setCreateUserId(u.getUserId());
                param.setUpdateYmd(ymd);
                param.setUpdateHms(hms);
                param.setUpdateUserId(u.getUserId());
                param = returnWarehouseService.insertReturnOrder(param,orderDetailJson);
            }
        }catch (Exception e){
            res.setMessage("Submitted failed！");
            res.setSuccess(false);
            log.error("退货单保存失败 {}",e.getMessage());
            return res;
        }

        param.setUserName(u.getUserName());
        res.setSuccess(true);
        res.setData(param);
        return res;
    }

    /**
     * 请求退货单头档信息
     * @param request
     * @param session
     * @param orderId 订单号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOrderDetails", method = RequestMethod.GET)
    public AjaxResultDto getDetails(HttpServletRequest request, HttpSession session,String orderId) {
        User u = this.getUser(session);
        AjaxResultDto res = new AjaxResultDto();
        if(StringUtils.isNotBlank(orderId)){
            ReturnHeadResult result = returnVendorService.getRVHeadQuery(orderId);
            if(result!=null){
                res.setSuccess(true);
                res.setData(result);
            }else{
                res.setSuccess(false);
                res.setMessage("Failed to load order data!");
            }
        }else{
            res.setSuccess(false);
            res.setMessage("Document No. cannot be empty!");
        }
        return res;
    }

    /**
     * 根据编号查询退货单商品信息
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getItemDetailByOrderId", method = RequestMethod.GET)
    public GridDataDTO<RWHItemsGridResult> getItemDetailByOrderId(HttpServletRequest request, HttpSession session,
                                                                  Map<String, ?> model, String orderId,String flag) {
        GridDataDTO<RWHItemsGridResult> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(orderId)){
            List<RWHItemsGridResult> list = returnWarehouseService.getReturnItems(orderId,flag);
            grid.setRows(list);
        }
        return grid;
    }

    /**
     * 验收  退货登录(供应商)
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_RV_LIST_VIEW})
    @RequestMapping(value = "/receipt",method = RequestMethod.GET)
    public ModelAndView returnVendorView(HttpServletRequest request, HttpSession session,
                                         Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退货登录(自送供应商)", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/return_vendor/returnlist");
        mv.addObject("typeId", ConstantsAudit.TYPE_RETURN_ENTRY_VENDOR);
        mv.addObject("useMsg", "退退货登录(自送供应商)画面");
        return mv;
    }

    /**
     * 验收  退货登录(供应商)详情画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_RV_EDIT,
            PermissionCode.CODE_SC_RV_VIEW,
    })
    @RequestMapping(value = "/receipt/edit", method = RequestMethod.GET)
    public ModelAndView returnVendorUpdateView(HttpServletRequest request, HttpSession session,
                                               Map<String, ?> model,String orderId,int flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退货登录(自送供应商)详细管理画面", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/return_vendor/returnedit");
        mv.addObject("flag",flag);
        mv.addObject("orderId",orderId);
        mv.addObject("useMsg", "退货登录(自送供应商)详细管理画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_RETURN_ENTRY_VENDOR);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_RETURN_ENTRY_VENDOR);
        this.saveToken(request);
        return mv;
    }

    /**
     * 更新退货商品
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_RV_EDIT
    })
    @ResponseBody
    @RequestMapping(value = "/updateActualReturnQty", method = RequestMethod.POST)
    public AjaxResultDto updateActualReturnQty(HttpServletRequest request, HttpSession session,
                                               Map<String, ?> model,String orderDetailJson,String orderRemark) {
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            res.setToKen(res.getToKen());
            return res;
        }
        if(StringUtils.isNotBlank(orderDetailJson)){
            try {
                returnWarehouseService.updateActualReturnQty(orderDetailJson,orderRemark);
                res.setSuccess(true);
            }catch (Exception e){
                res.setSuccess(false);
                res.setMessage("Data updated failed!");
            }
        }else{
            res.setSuccess(false);
            res.setMessage("Paramter is null!");
        }
        return  res;
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
    @Permission(codes = {
            PermissionCode.CODE_SC_V_RETURN_EXPORT
    })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        RVListParam dto = new Gson().fromJson(searchJson,RVListParam.class);
        if (dto.getReturnType().equals("20")){

        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_V_RETURN_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("rtVendorExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    } else {
            Collection<String> stores = getStores(session, dto);
            if(stores.size() == 0){
                log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
                return null;
            }
            // 设置参数对象
            ExcelParam exParam = new ExcelParam();
            exParam.setStores(stores);
            exParam.setParam(searchJson);
            exParam.setPCode(PermissionCode.CODE_SC_V_RETURN_EXPORT);
            exParam.setExFileName(EXCEL_EXPORT_NAME);
            ExService service = Container.getBean("rtVendorOrgExService", ExService.class);
            return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
        }
    }

    @RequestMapping(value = "/print")
    @Permission(codes = { PermissionCode.CODE_SC_V_RETURN_LIST_VIEW})
    public ModelAndView toPrintView(HttpServletRequest request, HttpSession session,
                                   String searchJson,String returnType) {
        if(returnType.equals("20")){
            User u = this.getUser(session);
            log.debug("User:{} 进入 退供应商申请单打印画面", u.getUserId());
            ModelAndView mv = new ModelAndView("return/vendor/returnPrint");
            mv.addObject("searchJson", searchJson);
            mv.addObject("userName", u.getUserName());
            mv.addObject("returnType", returnType);
            mv.addObject("printTime", new Date());
            mv.addObject("useMsg", "退供应商申请单打印画面");
            return mv;
        }else {
            User u = this.getUser(session);
            log.debug("User:{} 进入 退供应商申请单打印画面", u.getUserId());
            ModelAndView mv = new ModelAndView("return/vendor/returnOrgPrint");
            mv.addObject("searchJson", searchJson);
            mv.addObject("userName", u.getUserName());
            mv.addObject("returnType", returnType);
            mv.addObject("printTime", new Date());
            mv.addObject("useMsg", "退供应商申请单打印画面");
            return mv;
        }

    }

    @RequestMapping(value = "/print/getPrintData")
    @ResponseBody
    public AjaxResultDto printData(HttpServletRequest request, HttpSession session, RVListParamDTO param) {
        AjaxResultDto resultDto = new AjaxResultDto();
        // 获取当前角色店铺权限
        RVListParam dto = new Gson().fromJson(param.getSearchJson(),RVListParam.class);
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return resultDto;
        }
        param.setStores(stores);
        param.setPage(-1);
        param.setRows(1);
        GridDataDTO<RVListResult> grid = returnVendorService.getReturnVQueryList(param);
        if(grid.getRows()!=null&&grid.getRows().size()>0){
            resultDto.setSuccess(true);
        }else{
            resultDto.setSuccess(false);
        }
        resultDto.setData(grid.getRows());
        return resultDto;
    }

    @RequestMapping(value = "/receipt/print")
    @Permission(codes = { PermissionCode.CODE_SC_RV_PRINT})
    public ModelAndView toReceiptPrintView(HttpServletRequest request, HttpSession session,
                                           String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 确认退供应商申请单打印画面", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/return_vendor/returnPrint");
        mv.addObject("searchJson", searchJson);
        mv.addObject("userName", u.getUserName());
        mv.addObject("printTime", new Date());
        mv.addObject("useMsg", "确认退供应商申请单打印画面");
        return mv;
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
    @RequestMapping(value = "/receipt/export")
    @Permission(codes = {
            PermissionCode.CODE_SC_RV_EXPORT
    })
    public String receiptExport(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        RVListParam dto = new Gson().fromJson(searchJson,RVListParam.class);
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_RV_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME1);
        ExService service = Container.getBean("rtReceiptVendorExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY1, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,RVListParam param){
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
