package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseDetailsParamDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.receipt.warehouse.IWarehouseService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 仓库配送验收单
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/receipt")
public class ReceiptController extends BaseAction {

    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;


    private final String EXCEL_EXPORT_KEY = "EXCEL_ITEM_RECEIPT_DC";
    private final String EXCEL_EXPORT_NAME = "Item Receiving Query(From DC).xlsx";

    /**
     * 仓库配送验收单画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_RECEIPT_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 仓库配送验收单", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("receipt/warehouse/receiptlist");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "仓库配送验收单画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_RECEIPT_WAREHOUSE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE);
        this.saveToken(request);
        return mv;
    }

    /**
     * order管理查看画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_RECEIPT_VIEW,
            PermissionCode.CODE_SC_RR_QUERY_VIEW
    })
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session,
                                   WarehouseReceiptParamDTO param) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 仓库配送验收单明细管理查看画面", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/warehouse/receiptedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("type", param.getType());
        mv.addObject("storeCd", param.getStoreCd());
        mv.addObject("orderId", param.getOrderId());
        mv.addObject("receiveId", param.getReceiveId());
		mv.addObject("orderSts", "view");
        mv.addObject("useMsg", "仓库配送验收单明细管理查看画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_RECEIPT_WAREHOUSE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE);
        this.saveToken(request);
        return mv;
    }
    /**
     * order管理编辑画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_RECEIPT_CONFIRM,
            PermissionCode.CODE_SC_RR_QUERY_CONFIRM
    })
    public ModelAndView details(HttpServletRequest request, HttpSession session,
                                   WarehouseReceiptParamDTO param,String orderSts,String reviewSts) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 仓库配送验收单明细管理编辑画面", u.getUserId());
        ModelAndView mv = new ModelAndView("receipt/warehouse/receiptedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("type", param.getType());
        mv.addObject("storeCd", param.getStoreCd());
        mv.addObject("orderId", param.getOrderId());
        mv.addObject("receiveId", param.getReceiveId());
		mv.addObject("orderSts", orderSts);
		mv.addObject("reviewSts", reviewSts);
        mv.addObject("useMsg", "仓库配送验收单明细管理编辑画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_RECEIPT_WAREHOUSE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_RECEIPT_WAREHOUSE);
        this.saveToken(request);
        return mv;
    }
    /**
     * 条件查询仓库配送验收单
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getdata")
    @ResponseBody
    public GridDataDTO<WarehouseReceiptGridDTO> getReceiptList(HttpServletRequest request, HttpSession session,
                                                            int page, int rows, String searchJson) {
        Gson gson = new Gson();
        WarehouseReceiptParamDTO param = gson.fromJson(searchJson, WarehouseReceiptParamDTO.class);
        if(param == null){
            param = new WarehouseReceiptParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<WarehouseReceiptGridDTO>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return warehouseService.getReceipt(param);
    }

    /**
     * 查询仓库配送验收单详情
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get")
    @ResponseBody
    public ReturnDTO get(HttpServletRequest request, HttpSession session, String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        Gson gson = new Gson();
        WarehouseReceiptParamDTO param = gson.fromJson(searchJson, WarehouseReceiptParamDTO.class);
        if(param == null){
            _return.setMsg("Failed to get request parameters!");
            return _return;
        }
        WarehouseReceiptGridDTO dto = warehouseService.getReceiptByKey(param);
        if(dto == null){
            _return.setMsg("No data found");
        }else{
            _return.setSuccess(true);
            _return.setO(dto);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    /**
     * 查询仓库配送验收单详情
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getDetails")
    @ResponseBody
    public GridDataDTO<WarehouseDetailsGridDTO> getReceiptDetail(HttpServletRequest request, HttpSession session, String searchJson) {
        Gson gson = new Gson();
        WarehouseDetailsParamDTO param = gson.fromJson(searchJson, WarehouseDetailsParamDTO.class);
        if(param == null){
            param = new WarehouseDetailsParamDTO();
        }
        return warehouseService.getReceiptDetail(param);
    }
    /**
     * 修改商品实收数量
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/changeQty")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_RECEIPT_CONFIRM,
            PermissionCode.CODE_SC_RR_QUERY_CONFIRM
    })
    public String changeReceivingQty(HttpServletRequest request, HttpSession session, String searchJson) {
        Gson gson = new Gson();
        WarehouseDetailsGridDTO param = gson.fromJson(searchJson, WarehouseDetailsGridDTO.class);
        if(param == null){
            log.debug("param 为null");
            return null;
        }
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            log.debug("getCommonDTO() 获取为null");
            return null;
        }
        param.setCommonDTO(dto);

        int count = warehouseService.updateQty(param);
        return ""+count;
    }
    /**
     * 修改订单状态
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/changeSts")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_RECEIPT_RECEIVE
    })
    public String changeStatus(HttpServletRequest request, HttpSession session, String searchJson) {
        Gson gson = new Gson();
        List<WarehouseReceiptParamDTO> _list = gson.fromJson(searchJson, new TypeToken<List<WarehouseReceiptParamDTO>>(){}.getType());
        if(_list == null || _list.size() == 0){
            log.debug("params is null");
            return null;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            log.debug("getCommonDTO() 获取为null");
            return null;
        }
        int count = 0;
        for(WarehouseReceiptParamDTO _bean : _list){
            // 判断记录是否存在&是否为待收状态
            WarehouseReceiptGridDTO bean = warehouseService.getReceiptByKey(_bean);
            if(bean == null || !"00".equals(bean.getOrderSts())){
                log.debug("getReceiptByKey()查询为null || 订单非待收状态");
                continue;
            }
            _bean.setVoucherStatus("04");
            _bean.setCommonDTO(dto);
            _bean.setReceivingStartDate(dto.getCreateYmd());
            count += warehouseService.updateSts(_bean);
        }

        return ""+count;
    }

    /**
     * 打印
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_RECEIPT_PRINT})
    public ModelAndView receiptPrint(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 进入仓库配送验收单明细管理打印一览", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("receipt/warehouse/receiptPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("searchJson", searchJson);
        mv.addObject("printTime", Utils.getFormateDate(ymd));
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "仓库配送验收单明细管理打印画面");
        return mv;
    }

    @RequestMapping(value = "/print/getprintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData( HttpServletRequest request, HttpSession session,String searchJson) {
        Gson gson = new Gson();
        WarehouseReceiptParamDTO param = gson.fromJson(searchJson, WarehouseReceiptParamDTO.class);
        if(param == null){
            param = new WarehouseReceiptParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        param.setFlg(false);
        List<WarehouseReceiptGridDTO> result = warehouseService.getPrintData(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_RECEIPT_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        WarehouseReceiptParamDTO param = gson.fromJson(searchJson, WarehouseReceiptParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_RECEIPT_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("receiptFromDCExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
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
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(ymd);
        dto.setUpdateYmd(ymd);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(hms);
        dto.setUpdateHms(hms);
        return dto;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,WarehouseReceiptParamDTO param){
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
