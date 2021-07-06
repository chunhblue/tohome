package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010viewDTO;
import cn.com.bbut.iy.itemmaster.entity.OD0010C;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.od0010.directOd0010Service;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName directSupplierOrderController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/16 15:34
 * @Version 1.0
 */

@Slf4j
@Controller
@Secure
@RequestMapping(value = Constants.REQ_HEADER +"/cdOrder")
public class OrderCdController extends BaseAction{
    @Autowired
    private directOd0010Service od0010Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma4320Service ma4320Service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_ORDER_DC";
    private final String EXCEL_EXPORT_NAME = "DC Store Order Query.xlsx";

    @Permission(codes = {
            PermissionCode.CODE_SC_ORDER_CD_LIST_VIEW,
    })
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model){
        ModelAndView mv = new ModelAndView("ordercdLd/ordercd");
        // mv.addObject("data", param);
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
       // mv.addObject("date", date);
        mv.addObject("useMsg", "订单查询画面");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/getOrderCdInfor")
    public GridDataDTO<od0010DTO> getCdOrderInforaboutOd0010(HttpServletRequest request, HttpSession session,
                                                             String searchJson,int rows, int page){
        od0010ParamDTO param = null;
        if (!StringUtils.isEmpty(searchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, od0010ParamDTO.class);
        }
        if (param==null) {
            param = new od0010ParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<od0010DTO>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return od0010Service.getCdOrderInformation(param);
    }
    @ResponseBody
    @RequestMapping(value = "/getPrintInfor")
    public ReturnDTO getPrintInfor(HttpServletRequest request, HttpSession session,String searchJson){
        od0010ParamDTO param = null;
        if (!StringUtils.isEmpty(searchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, od0010ParamDTO.class);
        }
        if (param==null) {
            param = new od0010ParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        List<od0010DTO> printData = od0010Service.getDCPrintData(param);
        return  new ReturnDTO(true,"ok",printData);
    }

    @RequestMapping(value = "/print")
    public ModelAndView orderPrint(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,String searchJson) {

        Gson gson = new Gson();
        od0010ParamDTO  od0010ParamDTO = gson.fromJson(searchJson, od0010ParamDTO.class);
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("ordercdLd/ordercdprint");
        mv.addObject("time", date);
        mv.addObject("userName", u.getUserName());
        mv.addObject("identity", 1);
        mv.addObject("printTime",  Utils.getFormateDate(ymd));
        mv.addObject("useMsg", "打印画面");
        mv.addObject("dto",od0010ParamDTO);
        return mv;
    }

    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_ORDER_CD_EXPORT  })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(org.apache.commons.lang.StringUtils.isBlank(searchJson)){
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        od0010ParamDTO param = gson.fromJson(searchJson, od0010ParamDTO.class);

        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ORDER_CD_EXPORT );
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("orderDCEx", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,od0010ParamDTO param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
            && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(!StringUtils.isEmpty(param.getStoreCd())){
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



    @RequestMapping(value = "/cdOrderView",method = RequestMethod.GET)
    public ModelAndView cdOrderView(HttpServletRequest request, HttpSession session,
                                    Map<String, ?> model,od0010viewDTO od0010){
        User u = this.getUser(session);

        log.debug("User:{} 进入 退仓库申请单详细管理画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("ordercdLd/orderview");
        mv.addObject("use", u);
        mv.addObject("data", od0010);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
        mv.addObject("useMsg", "门店订收货明细管理画面");
        return mv;

    }
    @ResponseBody
    @RequestMapping(value = "/getItemsByOrder")
    public GridDataDTO<OD0010C> view(HttpServletRequest request, HttpSession session,
                          Map<String, ?> model, String searchJson){
        od0010ParamDTO  param = null;
        if (!StringUtils.isEmpty(searchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, od0010ParamDTO.class);
        }
        if (param==null) {
            param = new od0010ParamDTO();
        }

        List<OD0010C> itemsByOrder = od0010Service.getItemsByOrder(param);

        GridDataDTO<OD0010C> result = new GridDataDTO<OD0010C>();

        result.setRows(itemsByOrder);

        return result;
    }

    /**
     * @Description: 获取审核状态
     * @return _return 返回类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRecordStatus")
    public AjaxResultDto getAuditStatus(HttpSession session, HttpServletRequest request,
                                        String recordId, int typeId, int reviewId) {
        AjaxResultDto _return = new AjaxResultDto();
        if(reviewId!=0){
            typeId = ConstantsAudit.getTypeIdByReviewId(reviewId);
        }
        if(org.apache.commons.lang.StringUtils.isBlank(recordId)||typeId==0){
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
            _return.setSuccess(false);
            _return.setMessage("Selected PO is during receiving process, cannot modify!");
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
