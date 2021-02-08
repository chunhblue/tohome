package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.ResultTypeEnum;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.ma2000.Ma2000Service;
import cn.com.bbut.iy.itemmaster.service.order.OrderVendorService;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 订货登录
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/orderDetails")
public class OrderDetailsController  extends BaseAction {
    @Autowired
    private OrderVendorService service;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma2000Service ma2000Service;

    /**
     * 门店定收货一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
//    @Permission(codes = {
//            PermissionCode.CODE_SC_DETAILS_LIST_VIEW,
//    })
//    @RequestMapping(method = RequestMethod.GET)
//    public ModelAndView orderDetailsList(HttpServletRequest request, HttpSession session,
//                                   Map<String, ?> model,OrderVendorParamDTO paramDTO) {
//        User u = this.getUser(session);
//        log.debug("User:{} 进入门店订收货一览画面", u.getUserId());
//        ModelAndView mv = new ModelAndView("orderdetails/orderdetails");
//        mv.addObject("identity", 1);
//        mv.addObject("useMsg", "门店订收货一览画面");
//        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
//        return mv;
//    }

    /**
     * 门店订收货管理画面新增加
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_DETAILS_Add,
    })
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView orderDetailsAdd(HttpServletRequest request, HttpSession session,
                                         Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 门店订收货明细管理画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("orderdetails/orderdetailsadd");
        mv.addObject("use", 2);
        mv.addObject("date", date);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_DC);
        mv.addObject("useMsg", "门店订收货明细新增管理画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 门店订收货管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
//    @Permission(codes = {
//            PermissionCode.CODE_SC_DETAILS_VIEW,
//            PermissionCode.CODE_SC_DETAILS_EDIT,
//    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView orderDetailsEdit(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model,int use,String orderId) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 门店订收货明细管理画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("orderdetails/orderdetailsedit");
        mv.addObject("use", use);
        mv.addObject("orderId", orderId);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_DC);
        mv.addObject("useMsg", "门店订收货明细管理画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 查询供应商订货一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public GridDataDTO<OrderVendorGridDTO> getReceiptList(HttpServletRequest request, HttpSession session,
                                                               OrderVendorParamDTO param) {
        GridDataDTO<OrderVendorGridDTO> grid = new  GridDataDTO<>();
        if(param==null){
            param = new OrderVendorParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores == null || stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<OrderVendorGridDTO>();
        }
        param.setStores(stores);
        grid = service.GetOrderList(param);
        return grid;
    }

    /**
     * 供应商订货商品一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getItemsByVendor")
    @ResponseBody
    public GridDataDTO<OrderVendorDetailInfo> getItemsByVendor(HttpServletRequest request, HttpSession session,
                                                          OrderVendorParamDTO param) {
        GridDataDTO<OrderVendorDetailInfo> grid = new  GridDataDTO<OrderVendorDetailInfo>();
        if(param!=null){
            if(StringUtils.isNotBlank(param.getStoreCd())&&
                    StringUtils.isNotBlank(param.getOrderDate())&&
                    StringUtils.isNotBlank(param.getVendorId())){
                List<OrderVendorDetailInfo> list = service.getItemsByVendor(param);
                grid.setRows(list);
            }
        }
        return grid;
    }

    /**
     * 自动导入供应商订货一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getItemsByVendorAuto")
    @ResponseBody
    public GridDataDTO<OrderVendorDetailInfo> getItemsByVendorAuto(HttpServletRequest request, HttpSession session,
                                                                   int page, int rows, OrderVendorParamDTO param) {
        GridDataDTO<OrderVendorDetailInfo> grid = new  GridDataDTO<OrderVendorDetailInfo>();
        if(param!=null){

            if(StringUtils.isNotBlank(param.getStoreCd())&&
                    StringUtils.isNotBlank(param.getOrderDate())&&
                    StringUtils.isNotBlank(param.getVendorId())){
                //取消分页
                /*param.setPage(page);
                param.setRows(rows);
                param.setLimitStart((page - 1)*rows);*/
                GridDataDTO<OrderVendorDetailInfo> list = service.getOrderDetailListByAuto(param);
                grid = list;
            }
        }
        return grid;
    }

    /**
     * 获取供应商email，最低订货金额信息
     * @param session
     * @param req
     * @param vendorId
     * @return
     */
    @RequestMapping(value = "/getVendorInfo")
    @ResponseBody
    public AjaxResultDto getVendorInfo(HttpSession session, HttpServletRequest req,
                                String vendorId,String storeCd) {
        AjaxResultDto resultDto = new AjaxResultDto();
        User user = this.getUser(session);
        log.debug("抓取供应商信息，user:{}", user.getUserId());
        if (StringUtils.isBlank(vendorId))
            return null;
        MA2000 ma2000 = service.getVendorInfo(vendorId,storeCd);
        if(ma2000==null){
            resultDto.setSuccess(false);
            resultDto.setMessage("Failed to obtain supplier information!");
        }else {
            resultDto.setData(ma2000);
            resultDto.setSuccess(true);
        }
        return resultDto;
    }

    /**
     * 供应商订货保存
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_DETAILS_Add,
            PermissionCode.CODE_SC_DETAILS_EDIT,
    })
    @RequestMapping(value = "/save")
    @ResponseBody
    public AjaxResultDto orderVendorSave(HttpServletRequest request, HttpSession session,
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
        param.setOrderMethod("20");//订货方式
        param.setDeliveryTypeCd("000");//配送类型CD
        param.setDeliveryCenterId("00000");//物流中心ID
        param.setDeliveryAreaCd("000000");//配送区域CD
        if(StringUtils.isBlank(param.getStoreCd())||
            StringUtils.isBlank(param.getVendorId())){
            res.setSuccess(false);
            res.setMessage("Submitted failed,The store or supplier cannot be empty!");
            return res;
        }
        //获取订货截止时间
        /*MA2000 ma2000 = ma2000Service.getVendorById(param.getVendorId());
        if(ma2000!=null&&StringUtils.isNotBlank(ma2000.getCutoffTime())){
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
            try {
                Date cutoff = sdf2.parse(ma2000.getCutoffTime());
                Date now = sdf2.parse(sdf2.format(date));
                //判断当前时间是否小于截止时间
                if(now.getTime()>cutoff.getTime()){
                    res.setMessage("The current time is greater than the cutoff time!");
                    res.setSuccess(false);
                    return res;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                res.setMessage("Supplier order Cutoff Time error!");
                res.setSuccess(false);
                return res;
            }
        }*/
        try {
            if("1".equals(use)){//修改
                if(StringUtils.isBlank(param.getOrderId())){
                    res.setMessage("Modification failed,The order I/getItemInfod cannot be empty!");
                    res.setSuccess(false);
                    return res;
                }
                param.setUpdateYmd(ymd);
                param.setUpdateHms(hms);
                param.setUpdateUserId(u.getUserId());
                param = service.updateOrderByVendor(param,orderDetailJson);
            }else if("2".equals(use)){//新增
                param.setCreateYmd(ymd);
                param.setCreateHms(hms);
                param.setCreateUserId(u.getUserId());
                param.setUpdateYmd(ymd);
                param.setUpdateHms(hms);
                param.setUpdateUserId(u.getUserId());
                param = service.insertOrderByVendor(param,orderDetailJson);
            }
        }catch (Exception e){
            res.setMessage("Submitted failed！");
            res.setSuccess(false);
            log.error("供应商订单保存失败 {}",e.getMessage());
            return res;
        }

        param.setUserName(u.getUserName());
        res.setSuccess(true);
        res.setData(param);
        return res;
    }

    /**
     * 供应商订货信息
     * @param session
     * @param req
     * @param orderId 订单号
     * @return
     */
    @RequestMapping(value = "/getOrderVendorInfo")
    @ResponseBody
    public AjaxResultDto getOrderVendorInfo(HttpSession session, HttpServletRequest req,
                                 String orderId) {
        AjaxResultDto res = new AjaxResultDto();
        OrderVendorInfo dto = null;
        if(StringUtils.isNotBlank(orderId)) {
            dto = service.updateAndGetVendorInfoByOrderId(orderId);
        }else {
            res.setSuccess(false);
            res.setMessage("PO NO. is empty!");
            return res;
        }
        if (dto!=null){
            res.setData(dto);
            res.setSuccess(true);
        }else{
            res.setSuccess(false);
            res.setMessage("No data is loaded!");
        }
        return res;
    }

    /**
     * 获取商品信息
     * @param session
     * @param req
     * @param param
     * @return
     */
    @RequestMapping(value = "/getItemInfo")
    @ResponseBody
    public AjaxResultDto getItemInfo(HttpSession session, HttpServletRequest req,
                                             OrderVendorParamDTO param) {
        User user = this.getUser(session);
        log.debug("抓取商品信息，user:{}", user.getUserId());
        AjaxResultDto resultDto = new AjaxResultDto();
        if(param!=null){
            if(StringUtils.isNotBlank(param.getStoreCd())&&
                    StringUtils.isNotBlank(param.getOrderDate())&&
                    StringUtils.isNotBlank(param.getArticleId())&&
                    StringUtils.isNotBlank(param.getVendorId())){
                OrderVendorDetailInfo itemInfo = service.getItemInfo(param);
                if(itemInfo!=null){
                    resultDto.setData(itemInfo);
                    resultDto.setSuccess(true);
                    return resultDto;
                }
            }
        }
        resultDto.setSuccess(false);
        resultDto.setMessage("Failed to load item data!");//商品数据加载失败
        return resultDto;
    }


    /**
     * 供应商订货获取商品搜索框信息
     * @param session
     * @param req
     * @param param
     * @return
     */
    @RequestMapping(value = "/getItems")
    @ResponseBody
    public List<AutoCompleteDTO> getItems(HttpSession session, HttpServletRequest req,
                                 OrderVendorParamDTO param) {
        List<AutoCompleteDTO> list = new ArrayList<>();
        if(param!=null){
            if(StringUtils.isNotBlank(param.getStoreCd())&&
                    StringUtils.isNotBlank(param.getOrderDate())&&
                    StringUtils.isNotBlank(param.getVendorId())){
                list = service.getItems(param);
            }
        }
        return list;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,OrderVendorParamDTO param){
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
     * 验证当前时间是否禁用
     * @param session
     * @param req
     * @return
     */
    @RequestMapping(value = "/timeCheck")
    @ResponseBody
    public AjaxResultDto timeCheck(HttpSession session, HttpServletRequest req) {
        AjaxResultDto resultDto = new AjaxResultDto();
        //验证当前时间是否禁用
        if(TimeUtil.isInTime(Constants.TIME_ORDER_HALT_FROM, Constants.TIME_ORDER_HALT_TO, Constants.IS_NEXT_DAY)){
            //当前时间段禁用
            resultDto.setMessage("The current time period is not operational！");//当前时间段不可操作
            resultDto.setSuccess(false);
        }else{
            resultDto.setSuccess(true);
        }
        return resultDto;
    }

}
