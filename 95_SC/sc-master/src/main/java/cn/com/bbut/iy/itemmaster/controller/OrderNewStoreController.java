package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.order.*;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import cn.com.bbut.iy.itemmaster.service.order.OrderService;
import cn.com.bbut.iy.itemmaster.util.DateConvert;
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
 * 新店铺订货登录
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeOrder")
public class OrderNewStoreController extends BaseAction {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1000Service ma1000Service;
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    private Ma4320Service ma4320Service;

    /**
     * 新店铺订货登录管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_NS_ORDER_LIST_VIEW,
    })
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,OrderItemParamDTO param) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 新店铺订货登录管理画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        ModelAndView mv = new ModelAndView("order_newStore/orderList");
        mv.addObject("data", param);
        mv.addObject("date", date);
        mv.addObject("hms", hms);
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "新店铺订货登录管理画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER);
        this.saveToken(request);
        return mv;
    }

    /**
     * 新店铺订货登录修改管理画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_NS_ORDER_VIEW,
            PermissionCode.CODE_SC_NS_ORDER_EDIT,
    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session,
                                    OrderItemParamDTO param,String use) {
        User u = this.getUser(session);

        log.debug("User:{} 进入 新店铺订货登录明细管理画面", u.getUserId());
        ModelAndView mv = new ModelAndView("order_newStore/orderEdit");
        mv.addObject("use", use);
        mv.addObject("type", 1);
        mv.addObject("data", param);
        //店铺订单数量
        int newStore = orderService.checkNewStore(param.getStoreCd());
        mv.addObject("storeSts", newStore);
        mv.addObject("useMsg", "新店铺订货登录管理画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER);
        this.saveToken(request);
        return mv;
    }

    /**
     * 获没有下过订单的店铺 下拉  根据用户权限获取
     * @param session
     * @param req
     * @param v
     * @return
     */
    @RequestMapping(value = "/getStoreByPM")
    @ResponseBody
    public List<AutoCompleteDTO> getStoreByPM(HttpSession session, HttpServletRequest request, HttpServletRequest req,
                                              String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = orderService.getStoreListNotOrder(stores,v);
        }
        return dtos;
    }

    /**
     * 订货一览 （dc订货，新店订货）
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<OrderItemDTO> getList(HttpServletRequest request, HttpSession session,
                                             Map<String, ?> model, OrderItemParamDTO param) {
        if(param==null){
            param = new OrderItemParamDTO();
        }
        Date date = DateConvert.FromString(param.getOrderDate(),"yyyyMMdd");
        int day = date.getDay();
        param.setScheduleDay(day);
        //设置资源
        GridDataDTO<OrderItemDTO> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(param.getOrderType())&&
                StringUtils.isNotBlank(param.getOrderDate())&&
                StringUtils.isNotBlank(param.getStoreCd())){
            grid = orderService.GetOrderList(param);
        }
        return grid;
    }

    /**
     * 获取订货商品一览 （dc订货，新店订货）
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDetailList")
    public GridDataDTO<OrderItemDetailDTO> getDetailList(HttpServletRequest request, HttpSession session,
                                                             Map<String, ?> model, OrderItemParamDTO param) {
        if(param==null){
            param = new OrderItemParamDTO();
        }
        if(StringUtils.isBlank(param.getStoreCd())||
                StringUtils.isBlank(param.getOrderDate())){
            return new GridDataDTO<>();
        }
        //设置资源
        return orderService.GetOrderDetailList(param);
    }


    @ResponseBody
    @RequestMapping(value = "/getstoreinfo")
    public ResultDto getStoreInfo(HttpSession session, String storeCd) {
        ResultDto res = new ResultDto();
        Ma1000 ma1000 = orderService.getStoreInfoByCode(storeCd);
        if (ma1000!=null){
            res.setData(ma1000);
            res.setSuccess(true);
        }else{
            res.setSuccess(false);
            res.setMessage("Get information is empty！");
        }
        return res;
    }

    /**
     * 获取商品信息（dc订货，新店订货）
     * @param session
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getiteminfo")
    public ResultDto getItemInfo(HttpSession session, OrderItemParamDTO param) {
        ResultDto res = new ResultDto();
        try {
            OrderDetailInfo orderDetailInfo = orderService.getOrderItemDetail(param);
            if (orderDetailInfo!=null){
                res.setData(orderDetailInfo);
                res.setSuccess(true);
            }else{
                res.setSuccess(false);
                res.setMessage("No data found!");
            }
        }catch (Exception e){
            res.setSuccess(false);
            res.setMessage("Failed to get item information!");
        }
        return res;
    }

    /**
     * 修改订货数量 （dc订货，新店订货）
     * @param session
     * @param param
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_NS_ORDER_EDIT,//新开店订货
            PermissionCode.CODE_SC_ORDER_EDIT,//dc订货修改
    })
    @ResponseBody
    @RequestMapping(value = "/updateOrder")
    public ResultDto updateOrder(HttpSession session, OrderItemDetailDTO param) {
        User u = this.getUser(session);
        ResultDto res = new ResultDto();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        String ymd = sdf.format(date);
        String hms = sdf1.format(date);
        if(param==null){
            param = new OrderItemDetailDTO();
        }
        param.setCreateUserId(u.getUserId());
        param.setUpdateUserId(u.getUserId());
        param.setCreateYmd(ymd);
        param.setUpdateYmd(ymd);
        param.setCreateHms(hms);
        param.setUpdateHms(hms);
        if("1".equals(param.getDcItem())){
            param.setOrderSts("2");//dc商品直接生效
        }else{
            param.setOrderSts("1");//vendor参与修改但没生效
        }
        if(StringUtils.isBlank(param.getStoreCd())||
            StringUtils.isBlank(param.getOrderDate())||
                StringUtils.isBlank(param.getVendorId())||
            StringUtils.isBlank(param.getArticleId())){
            res.setSuccess(false);
            res.setMessage("Parameter cannot be empty!");
            return res;
        }
        try {
            int flag = orderService.updateOrder(param);
            if (flag>0){
                res.setMessage("Modify Succeeded!");
                res.setSuccess(true);
            }else{
                res.setSuccess(false);
                res.setMessage("Modify failed！");
            }
        } catch (Exception e){
            e.printStackTrace();
            res.setSuccess(false);
            res.setMessage("Modify failed！");
        }

        return res;
    }


    /**
     * 保存订货
     * @param session
     * @param storeCd
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_NS_ORDER_EDIT,
    })
    @ResponseBody
    @RequestMapping(value = "/save")
    public AjaxResultDto save(HttpServletRequest request,HttpSession session, String storeCd) {
        AjaxResultDto resultDto = ajaxRepeatSubmitCheck(request, session);
        if (! resultDto.isSuccess()) {
            resultDto.setToKen(resultDto.getToKen());
            return  resultDto;
        }
        if(StringUtils.isBlank(storeCd)){
            resultDto.setSuccess(false);
            resultDto.setMessage("Store Cd cannot be empty!");
        }else{
            // msg == null (成功)
            String errorMsg = orderService.updateOrderSts(storeCd);
            resultDto.setSuccess(errorMsg==null);
            resultDto.setMessage(errorMsg);
        }
        return resultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/checkItem")
    public AjaxResultDto checkItem(HttpServletRequest request,HttpSession session, String storeCd) {
        AjaxResultDto resultDto = ajaxRepeatSubmitCheck(request, session);
        if(StringUtils.isBlank(storeCd)){
            resultDto.setSuccess(false);
            resultDto.setMessage("Store Cd cannot be empty!");
        }else{
            String msg = orderService.checkItem(storeCd);
            resultDto.setSuccess(msg==null);
            resultDto.setMessage(msg);
        }
        return resultDto;
    }

    /**
     * 订货发起审核
     * @param session
     * @param storeCd
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_NS_ORDER_EDIT,
    })
    @ResponseBody
    @RequestMapping(value = "/submit")
    public AjaxResultDto submit(HttpSession session, String storeCd,String orderDate) {
        AjaxResultDto resultDto = new AjaxResultDto();
        if(StringUtils.isBlank(storeCd)||StringUtils.isBlank(orderDate)){
            resultDto.setSuccess(false);
            resultDto.setMessage("Parameter cannot be empty!");
        }else{
            int flg = orderService.insertAudit(storeCd,orderDate);
            resultDto.setSuccess(true);
        }
        return resultDto;
    }

    /**
     * 判断当前用户是否能submit
     * @param session
     * @param storeCd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkSubmit")
    public AjaxResultDto checkSubmit(HttpSession session, String storeCd) {
        AjaxResultDto resultDto = new AjaxResultDto();
        User u = this.getUser(session);
        int count = auditServiceImpl.checkAuditByUserId(storeCd,u.getUserId(),4);
        int count2 = auditServiceImpl.checkAuditByUserId(storeCd,u.getUserId(),9);
        resultDto.setSuccess(false);
        if(count>0){
            resultDto.setSuccess(true);
        }
        if(count2>0){
            resultDto.setSuccess(true);
        }
        return resultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/checkVendorOrder")
    public AjaxResultDto checkVendorOrder(HttpSession session, String storeCd,String orderDate) {
        AjaxResultDto resultDto = new AjaxResultDto();
        if(StringUtils.isBlank(storeCd)||StringUtils.isBlank(orderDate)){
            resultDto.setSuccess(false);
            resultDto.setMessage("Parameter cannot be empty!");
        }else{
            String msg = orderService.checkOrder(storeCd,orderDate);
            if(msg != null){
                resultDto.setSuccess(true);
                resultDto.setMessage(msg);
                resultDto.setData("1");
                return resultDto;
            }
        }
        return resultDto;
    }

    @ResponseBody
    @RequestMapping(value = "/getMa1107")
    public List<AutoCompleteDTO> getMa1107(String v,String storeCd) {
        if(StringUtils.isBlank(storeCd)){
            log.info("storeCd is null");
            return null;
        }
        List<AutoCompleteDTO> list = orderService.getMa1107(storeCd,v);
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getMa1108")
    public List<AutoCompleteDTO> getMa1108(String storeCd,String excelName,String v) {
        if(StringUtils.isBlank(storeCd) && StringUtils.isBlank(excelName)){
            log.info("storeCd or excelName is null");
            return null;
        }
        List<AutoCompleteDTO> list = orderService.getMa1108(storeCd,excelName,v);
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getMa1109")
    public List<AutoCompleteDTO> getMa1109(String storeCd,String excelName,String paramShelf,String v) {
        if(StringUtils.isBlank(storeCd) && StringUtils.isBlank(excelName) && StringUtils.isBlank(paramShelf)){
            log.info("excelName or paramShelf is null");
            return null;
        }
        List<AutoCompleteDTO> list = orderService.getMa1109(storeCd,excelName,paramShelf,v);
        return list;
    }

    /**
     * @Description: 获取审核状态
     * @return _return 返回类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRecordStatus")
    public AjaxResultDto getAuditStatus(HttpSession session, HttpServletRequest request,
                                        String recordId,int typeId,String storeCd,int reviewId) {
        AjaxResultDto _return = new AjaxResultDto();
        User u = this.getUser(session);
        String userId = u.getUserId();
        if(reviewId!=0){
            typeId = ConstantsAudit.getTypeIdByReviewId(reviewId);
        }
        if(StringUtils.isBlank(recordId)||typeId==0){
            _return.setSuccess(false);
            _return.setMessage("The parameter cannot be null!");
            return _return;
        }
        Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
        String table = String.valueOf(auditInfo.get("table"));
        String key = String.valueOf(auditInfo.get("key"));
        if("null".equals(table)||"null".equals(key)){
            _return.setSuccess(false);
            _return.setMessage("Failed!");
            return _return;
        }
        List<OrderItemDTO> list = orderService.getUserPosition(storeCd,userId);
        for(OrderItemDTO orderItemDTO : list){
            if(orderItemDTO.getPosition().equals("4")){
                _return.setSuccess(true);
                _return.setMessage("Store managers can modify the content!");
                return _return;
            }
        }
        // 获取审核状态
        Integer status = auditServiceImpl.getAuditStatus(table,key,recordId);
        if(status == null){
            _return.setSuccess(true);
            _return.setMessage("The approval status is empty!");
        }else if(status == 1){//正在审核
            _return.setSuccess(false);
            _return.setMessage("In review, cannot modify!");
        }else if(status == 7){//审核失效
            _return.setSuccess(false);
            _return.setMessage("The approval status is expired and cannot modify!");//审核状态已失效
        }else if(status == 10){//审核成功
            _return.setSuccess(false);
            _return.setMessage("Selected document!");//审核状态已完成
        }else if(status == 15){////收货录入完成，未审核
            _return.setSuccess(true);
        }else if(status == 5 || status == 6){//审核驳回、审核撤回
            _return.setSuccess(true);
        }else{
            _return.setSuccess(false);
            _return.setMessage("Approval status unknown!");
        }
        _return.setData(status);
        return _return;
    }

}
