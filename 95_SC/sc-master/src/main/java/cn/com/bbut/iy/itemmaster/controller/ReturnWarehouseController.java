package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListResult;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVListParam;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.*;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
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
 * 退仓库申请单
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/returnWarehouse")
public class ReturnWarehouseController extends BaseAction {

    private final String EXCEL_EXPORT_KEY = "EXCEL_RETURN_WAREHOUSE";
    private final String EXCEL_EXPORT_NAME = "Item Return Request Query(To DC).xlsx";
    private final String EXCEL_EXPORT_KEY1 = "EXCEL_RETURN_WAREHOUSE_RECEIPT";
    private final String EXCEL_EXPORT_NAME1 = "Item Return Entry(To DC).xlsx";

    @Autowired
    private ReturnWarehouseService returnWarehouseService;

    @Autowired
    private CM9060Service cm9060Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;

    /**
     * 退供应商申请单管理一览画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_W_RETURN_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退供应商申请单", u.getUserId());
        ModelAndView mv = new ModelAndView("return/warehouse/returnlist");
        mv.addObject("useMsg", "退供应商申请单画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_RETURN_WAREHOUSE);
        return mv;
    }

    /**
     * 一览页面条件查询
     * @param request
     * @param session
     * @param model
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/query")
    public GridDataDTO<RWHListResult> query(HttpServletRequest request, HttpSession session,
                                            Map<String, ?> model, RWHListParamDTO param) {
        RWHListParam dto = new Gson().fromJson(param.getSearchJson(),RWHListParam.class);
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<RWHListResult>();
        }
        param.setStores(stores);
        return returnWarehouseService.getReturnWHQueryList(param);
    }


    @RequestMapping(value = "/printDcDocument")
    @Permission(codes = { PermissionCode.CODE_SC_V_RETURN_LIST_VIEW})
    public ModelAndView toprintDocumentView(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        Gson gson = new Gson();
        ReturnWarehouseParamDTO returnParamDTO = gson.fromJson(searchJson,ReturnWarehouseParamDTO.class);
        User u = this.getUser(session);
        log.debug("User:{} 进入 退供应商申请单打印画面", u.getUserId());
        ModelAndView mv = new ModelAndView("return/warehouse/returnPrintDocument");
        mv.addObject("searchJson", searchJson);
        mv.addObject("returnParamDTO", returnParamDTO);
        mv.addObject("userName", u.getUserName());
        mv.addObject("orderRemark", returnParamDTO.getOrderRemark());
        mv.addObject("printTime", Utils.getFormateDate(ymd));
        mv.addObject("useMsg", "退供应商申请单打印画面");
        return mv;


    }



    /**
     * 退货单详情管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/cancelOrderDetail", method = RequestMethod.GET)
     @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_W_RETURN_ADD,
            PermissionCode.CODE_SC_W_RETURN_VIEW,
            PermissionCode.CODE_SC_W_RETURN_EDIT,
    })
    public ModelAndView cancelOrderDetail(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model,String flag,String orderId,String reType) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退仓库申请单详细管理画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("return/warehouse/returnedit");
        mv.addObject("flag",flag);
        mv.addObject("orderId",orderId);
        mv.addObject("orderDate",date);
        mv.addObject("returnflag",reType);
        mv.addObject("useMsg", "退仓库申请单详细管理");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_RETURN_WAREHOUSE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_RETURN_WAREHOUSE);
        this.saveToken(request);
        return mv;
    }

    //  2021/7/26开始
    @ResponseBody
    @RequestMapping(value = "/getDocumentPrintData",method={RequestMethod.POST,RequestMethod.GET})
    public   AjaxResultDto getDocumentPrintData(HttpServletRequest request, HttpSession session,
                                                Map<String, ?> model, String searchJson) {
        AjaxResultDto resultDto = new AjaxResultDto();
        GridDataDTO<RWHItemsGridResult> grid = new GridDataDTO<>();

        Gson gson = new Gson();
        ReturnWarehouseParamDTO returnParamDTO = gson.fromJson(searchJson, ReturnWarehouseParamDTO.class);
        if (StringUtils.isNotBlank(returnParamDTO.getOrderId())) {
            List<RWHItemsGridResult> list = returnWarehouseService.getDcReturnItemsDetail(returnParamDTO);
            grid.setRows(list);
            resultDto.setData(grid.getRows());
            resultDto.setSuccess(true);
        }else {
            resultDto.setSuccess(false);
        }

        return resultDto;

    }


    @RequestMapping(value = "/exportDcBySupplier")
    @Permission(codes = {
            PermissionCode.CODE_SC_V_RETURN_EXPORT
    })
    public String exportDcBySupplier(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        RWHListParam dto = new Gson().fromJson(searchJson,RWHListParam.class);
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
        ExService service = Container.getBean("rDcBySupplier", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);

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
//            resultDto.setMessage("You do not have store privileges!");//你没有店铺权限
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
     * 仓库退货保存
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_W_RETURN_ADD,
            PermissionCode.CODE_SC_W_RETURN_EDIT,
    })
    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResultDto returnWarehouseSave(HttpServletRequest request, HttpSession session,
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
                param.setOrderDifferentiate("1"); //退货区分 1仓库
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
            ReturnHeadResult result = returnWarehouseService.getRWHHeadQuery(orderId);
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
                                                                  Map<String, ?> model,String orderId,String flag) {
        GridDataDTO<RWHItemsGridResult> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(orderId)){
            List<RWHItemsGridResult> list = returnWarehouseService.getReturnItems(orderId,flag);
            grid.setRows(list);
        }
        return grid;
    }

    /**
     * 验收 退货登录(退物流)画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_RW_LIST_VIEW})
    @RequestMapping(value = "/receipt",method = RequestMethod.GET)
    public ModelAndView returnWarehouseView(HttpServletRequest request, HttpSession session,
                                            Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退货登录(退物流)", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/return_warehouse/returnlist");
        mv.addObject("typeId", ConstantsAudit.TYPE_RETURN_ENTRY_WAREHOUSE);
        mv.addObject("useMsg", "退货登录(退物流)画面");
        return mv;
    }

    /**
     * 验收 退货登录(退物流)详情画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_RW_EDIT,
            PermissionCode.CODE_SC_RW_VIEW,
    })
    @RequestMapping(value = "/receipt/edit", method = RequestMethod.GET)
    public ModelAndView returnWarehouseUpdateView(HttpServletRequest request, HttpSession session,
                                                  Map<String, ?> model,String orderId,String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 退货登录(退物流)详细管理画面", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/return_warehouse/returnedit");
        mv.addObject("flag",flag);
        mv.addObject("orderId",orderId);
        mv.addObject("useMsg", "退货登录(退物流-收货)详细管理画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_RETURN_ENTRY_WAREHOUSE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_RETURN_ENTRY_WAREHOUSE);
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
            PermissionCode.CODE_SC_RW_EDIT
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
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_W_RETURN_EXPORT
    })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        RWHListParam dto = new Gson().fromJson(searchJson,RWHListParam.class);
        if (dto.getReturnType().equals("20")) {
            Collection<String> stores = getStores(session, dto);
            if (stores.size() == 0) {
                log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
                return null;
            }

            // 设置参数对象
            ExcelParam exParam = new ExcelParam();
            exParam.setStores(stores);
            exParam.setParam(searchJson);
            exParam.setPCode(PermissionCode.CODE_SC_W_RETURN_EXPORT);
            exParam.setExFileName(EXCEL_EXPORT_NAME);
            ExService service = Container.getBean("rtDCExService", ExService.class);
            return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
        }else {
            Collection<String> stores = getStores(session, dto);
            if (stores.size() == 0) {
                log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
                return null;
            }

            ExcelParam exParam = new ExcelParam();
            exParam.setStores(stores);
            exParam.setParam(searchJson);
            exParam.setPCode(PermissionCode.CODE_SC_W_RETURN_EXPORT);
            exParam.setExFileName(EXCEL_EXPORT_NAME);
            ExService service = Container.getBean("rtDCOrgExService", ExService.class);
            return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
        }
    }

    /**
     * 退仓库申请单打印一览画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_W_RETURN_PRINT })
    @RequestMapping(value = "/print")
    public ModelAndView toPrintView(HttpServletRequest request, HttpSession session,
                                              String searchJson,String returnType) {

        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);

      if(returnType.equals("20")) {
          log.debug("User:{} 退仓库申请单打印一览画面", u.getUserId());
          ModelAndView mv = new ModelAndView("return/warehouse/returnPrint");
          mv.addObject("searchJson", searchJson);
          mv.addObject("reType", returnType);
          mv.addObject("userName", u.getUserName());
          mv.addObject("printTime", Utils.getFormateDate(ymd));
          mv.addObject("useMsg", "退仓库申请单打印画面");
          return mv;
      }else {
          log.debug("User:{} 退仓库申请单打印一览画面", u.getUserId());
          ModelAndView mv = new ModelAndView("return/warehouse/returnOrgPrint");
          mv.addObject("searchJson", searchJson);
          mv.addObject("reType", returnType);
          mv.addObject("userName", u.getUserName());
          mv.addObject("printTime", Utils.getFormateDate(ymd));
          mv.addObject("useMsg", "退仓库申请单打印画面");
          return mv;
      }
    }

    /**
     * 退仓库申请单打印一览画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_RW_PRINT })
    @RequestMapping(value = "/receipt/print")
    public ModelAndView toReceiptPrintView(HttpServletRequest request, HttpSession session,
                                              String searchJson,RWHListParamDTO param) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 退仓库确认退货打印一览画面", u.getUserId());
        // 获取当前角色店铺权限
        RWHListParam dto = new Gson().fromJson(param.getSearchJson(),RWHListParam.class);

        ModelAndView mv = new ModelAndView("receipt/return_warehouse/returnPrint");
        mv.addObject("searchJson", searchJson);
        mv.addObject("userName", u.getUserName());
        mv.addObject("printTime",  Utils.getFormateDate(ymd));
        mv.addObject("dto", dto);

        mv.addObject("useMsg", "退仓库确认退货打印画面");
        return mv;
    }

    @RequestMapping(value = "/print/getPrintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model,  RWHListParamDTO param) {
        // 获取当前角色店铺权限
        RWHListParam dto = new Gson().fromJson(param.getSearchJson(),RWHListParam.class);
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"The query data is empty!", null);
        }
        param.setStores(stores);
        dto.setFlg(false);
        List<RWHListResult> result = returnWarehouseService.getPrintReturnWHList(param);
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
    @RequestMapping(value = "/receipt/export")
    @Permission(codes = {
            PermissionCode.CODE_SC_RW_EXPORT
    })
    public String receiptExport(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        RWHListParam dto = new Gson().fromJson(searchJson,RWHListParam.class);
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_RW_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME1);
        ExService service = Container.getBean("rtReceiptDCExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY1, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,RWHListParam param){
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

    /**
     * 获取dc一览
     * @param session
     * @param req
     * @return
     */
    @RequestMapping(value = "/getDc")
    @ResponseBody
    public List<AutoCompleteDTO> getDc(HttpSession session, HttpServletRequest req) {
        List<AutoCompleteDTO> list = new ArrayList<>();
        list = returnWarehouseService.getDc();
        return list;
    }
}
