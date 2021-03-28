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
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import cn.com.bbut.iy.itemmaster.service.order.OrderService;
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
 * 订货登录
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/order")
public class OrderController  extends BaseAction {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1000Service ma1000Service;
    @Autowired
    private IAuditService auditServiceImpl;
    /**
     * order管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_ORDER_LIST_VIEW,
    })
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,OrderItemParamDTO param) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 订货登录管理画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("order/orderList");
        mv.addObject("data", param);
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("date", date);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER);
        mv.addObject("useMsg", "订货登录管理画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * order管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_ORDER_VIEW,
            PermissionCode.CODE_SC_ORDER_EDIT,
    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model, OrderItemParamDTO param,String use) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 订货登录明细管理画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("order/orderEdit");
        mv.addObject("useMsg", "");
        mv.addObject("use", use);
        mv.addObject("type", 1);
        mv.addObject("data", param);
        mv.addObject("useMsg", "订货登录管理画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER);
        this.saveToken(request);
        return mv;
    }

    /**
     * 获取店铺下拉  根据用户权限获取
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
            dtos = ma1000Service.getListByStorePm(stores,v);
        }
        return dtos;
    }

    /**
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getReferenceInfo")
    public GridDataDTO<OrderInvoicingDTO> getReferenceInfo(HttpServletRequest request, HttpSession session,
                                                           Map<String, ?> model, OrderItemParamDTO param) {
        GridDataDTO<OrderInvoicingDTO> grid = new  GridDataDTO<OrderInvoicingDTO>();
        if(param!=null){
            if(StringUtils.isNotBlank(param.getOrderDate())
                &&StringUtils.isNotBlank((param.getArticleId()))
                &&StringUtils.isNotBlank(param.getStoreCd())){
                grid.setRows(orderService.getReferenceInfo(param));
            }
        }
        return grid;
    }

    /**
     * 订货一览 （新店特殊订单订货）
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
        //设置资源
        GridDataDTO<OrderItemDTO> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(param.getOrderType())&&
                StringUtils.isNotBlank(param.getOrderDate())&&
                StringUtils.isNotBlank(param.getStoreCd())){
            grid = orderService.GetOrderSpecialList(param);
        }
        return grid;
    }

    /**
     * 获取特殊订货商品一览 （新店订货）
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
        return orderService.GetOrderSpecialListDetail(param);
    }

    /**
     * 获取跨天商品信息（新店订货）
     * @param session
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getItemInfo")
    public ResultDto getItemInfo(HttpSession session, OrderItemParamDTO param) {
        ResultDto res = new ResultDto();
        try {
            OrderDetailInfo orderDetailInfo = orderService.getOrderSecialItemDetail(param);
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
            int flag = orderService.updateSpecialOrder(param);
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
     * 保存跨天订货
     * @param request
     * @param session
     * @param storeCd
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save")
    public AjaxResultDto save(HttpServletRequest request, HttpSession session, String storeCd) {
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
            String errorMsg = orderService.updateSpecialOrderSts(storeCd);
            resultDto.setSuccess(errorMsg==null);
            resultDto.setMessage(errorMsg);
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
            String msg = orderService.checkSpecialOrder(storeCd,orderDate);
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
    @RequestMapping(value = "/checkItem")
    public AjaxResultDto checkItem(HttpServletRequest request,HttpSession session, String storeCd) {
        AjaxResultDto resultDto = ajaxRepeatSubmitCheck(request, session);
        if(StringUtils.isBlank(storeCd)){
            resultDto.setSuccess(false);
            resultDto.setMessage("Store Cd cannot be empty!");
        }else{
            String msg = orderService.checkSpecialItem(storeCd);
            resultDto.setSuccess(msg==null);
            resultDto.setMessage(msg);
        }
        return resultDto;
    }

    @RequestMapping(value = "/insertQuickCopy")
    @ResponseBody
    public AjaxResultDto insertQuickCopy(String storeCd,String orderDate){
        AjaxResultDto resultDto = new AjaxResultDto();
        if(StringUtils.isBlank(storeCd)||StringUtils.isBlank(orderDate)){
            resultDto.setSuccess(false);
            resultDto.setMessage("Parameter cannot be empty!");
        }else {
            int temp = orderService.insertQuickCopy(storeCd,orderDate);
            if(temp == 0 ){
                resultDto.setSuccess(false);
                resultDto.setMessage("You haven't ordered a product yet!");
            }else {
                resultDto.setSuccess(true);
            }
        }
        return resultDto;
    }

    @ResponseBody
    @RequestMapping("/getStoreRecordStatus")
    public AjaxResultDto getAuditStatus(HttpSession session, HttpServletRequest request,
                                        String recordId,int typeId,int reviewId) {
        AjaxResultDto _return = new AjaxResultDto();
        if(reviewId!=0){
            typeId = ConstantsAudit.getTypeIdByReviewId(reviewId);
        }
        Map<String,Object> auditInfo= ConstantsAudit.getAuditInfo(typeId);
        String table = String.valueOf(auditInfo.get("table"));
        String key = String.valueOf(auditInfo.get("key"));
        if("null".equals(table)||"null".equals(key)){
            _return.setSuccess(false);
            _return.setMessage("Failed to get table name!");
            return _return;
        }
        // 获取审核状态
        Integer status = auditServiceImpl.getAuditStatus(table,key,recordId);
        if(status == null){
            _return.setSuccess(true);
        }else {
            _return.setSuccess(false);
            _return.setMessage("Orders from this store have been reviewed today," +"<br>"+
                    "so please continue the review process on the Order Entry screen.!");
        }
        _return.setData(status);
        return _return;
    }
}
