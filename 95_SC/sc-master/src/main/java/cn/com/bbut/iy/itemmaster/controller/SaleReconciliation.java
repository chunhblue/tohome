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
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetailParam;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.CashierDetailParam;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.dto.promotion.PromotionParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CashService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 销售对账与财务管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/saleReconciliation")
public class SaleReconciliation extends BaseAction {
    @Autowired
    private CashService cashService;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_SALE_RECONCILIATION";
    private final String EXCEL_EXPORT_NAME = "Daily Sales Report(Variance).xlsx";

    /**
     * 销售对账与财务管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_SL_CF_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 销售对账与财务管理", u.getUserId());
        ModelAndView mv = new ModelAndView("saleReconciliation/saleReconciliationList");
        mv.addObject("typeId", ConstantsAudit.TYPE_CASHIER_AMOUNT);
        mv.addObject("useMsg", "销售对账与财务管理画面");
        return mv;
    }

    /**
     * 获取金种一览
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCashDetailyList")
    @Permission(codes = { PermissionCode.CODE_SC_SL_CF_LIST_VIEW})
    public GridDataDTO<CashDetail> getCashDetailyList(HttpServletRequest request, HttpSession session,
                                                      Map<String, ?> model, CashDetailParam dto,int rows, int page) {

        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<CashDetail>();
              }
        dto.setStores(stores);
        List<CashDetail> _list = cashService.getCashDetailList(dto);

        dto.setPage(page);
        dto.setRows(rows);
        dto.setLimitStart((page - 1)*rows);
        dto.setFlg(true);
        GridDataDTO<CashDetail> grid = cashService.getCashDetailyList(dto);
        CashDetail cashTotal = grid.getRows().get(0);

        BigDecimal payInAmt0 = BigDecimal.ZERO,payInAmt1 = BigDecimal.ZERO,payInAmt2 = BigDecimal.ZERO,
                additional=BigDecimal.ZERO,offsetClaim=BigDecimal.ZERO;
        BigDecimal payAmt0 = BigDecimal.ZERO,payAmt1 = BigDecimal.ZERO,payAmt2 = BigDecimal.ZERO,payInAmt = BigDecimal.ZERO,
                payAmt = BigDecimal.ZERO,payAmtDiff = BigDecimal.ZERO;
        BigDecimal customerQty=BigDecimal.ZERO;

        for (CashDetail ls : _list) {
            if(ls.getCustomerQty() != null){
                customerQty = customerQty.add(ls.getCustomerQty()) ;
            }
            if(ls.getPayInAmt0() != null){
                payInAmt0 = payInAmt0.add(ls.getPayInAmt0()) ;
            }
            if(ls.getPayInAmt1() != null){
                payInAmt1 = payInAmt1.add(ls.getPayInAmt1()) ;
            }
            if(ls.getPayInAmt2() != null){
                payInAmt2 = payInAmt2.add(ls.getPayInAmt2()) ;
            }
            if(ls.getAdditional() != null){
                additional = additional.add(ls.getAdditional()) ;
            }
            if(ls.getAdditional() != null){
                offsetClaim = offsetClaim.add(ls.getOffsetClaim()) ;
            }
            if(ls.getPayAmt() != null){
                payAmt = payAmt.add(ls.getPayAmt()) ;
            }
            if(ls.getPayAmt0() != null){
                payAmt0 = payAmt0.add(ls.getPayAmt0()) ;
            }
            if(ls.getPayAmt1() != null){
                payAmt1 = payAmt1.add(ls.getPayAmt1()) ;
            }
            if(ls.getPayAmt2() != null){
                payAmt2 = payAmt2.add(ls.getPayAmt2()) ;
            }
            if(ls.getPayAmtDiff() != null){
                payAmtDiff = payAmtDiff.add(ls.getPayAmtDiff()) ;
            }
            if(ls.getPayInAmt() != null){
                payInAmt = payInAmt.add(ls.getPayInAmt()) ;
            }
        }
        cashTotal.setTcustomerQty(customerQty);
        cashTotal.setTpayInAmt0(payInAmt0);
        cashTotal.setTpayInAmt1(payInAmt1);
        cashTotal.setTpayInAmt2(payInAmt2);
        cashTotal.setTadditional(additional);
        cashTotal.setToffsetClaim(offsetClaim);
        cashTotal.setTpayAmt(payAmt);
        cashTotal.setTpayAmt0(payAmt0);
        cashTotal.setTpayAmt1(payAmt1);
        cashTotal.setTpayAmt2(payAmt2);
        cashTotal.setTpayAmtDiff(payAmtDiff);
        cashTotal.setTpayInAmt(payInAmt);
        return grid;
    }

    @ResponseBody
    @RequestMapping(value = "/getTotalData")
    public AjaxResultDto getToalAmount(HttpServletRequest request, HttpSession session,
                                       String searchJson,int rows, int page){
        AjaxResultDto ajaxResultDto = new AjaxResultDto();
        CashDetailParam dto;
        if(searchJson == null){
            dto = new CashDetailParam();
        }else{
            Gson gson = new Gson();
            dto = gson.fromJson(searchJson, CashDetailParam.class);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return ajaxResultDto;
        }
        dto.setPage(page);
        dto.setRows(rows);
        dto.setLimitStart((page - 1)*rows);
        dto.setStores(stores);
        dto.setFlg(false);
        List<CashDetail> _list = cashService.getCashDetailyList(dto).getRows();
        BigDecimal payInAmt0 = BigDecimal.ZERO,payInAmt1 = BigDecimal.ZERO,payInAmt2 = BigDecimal.ZERO,
                additional=BigDecimal.ZERO,offsetClaim=BigDecimal.ZERO;
        BigDecimal payAmt0 = BigDecimal.ZERO,payAmt1 = BigDecimal.ZERO,payAmt2 = BigDecimal.ZERO,payInAmt = BigDecimal.ZERO,
                payAmt = BigDecimal.ZERO,payAmtDiff = BigDecimal.ZERO;
        BigDecimal customerQty=BigDecimal.ZERO;

        for (CashDetail ls : _list) {
            if(ls.getCustomerQty() != null){
                customerQty = customerQty.add(ls.getCustomerQty()) ;
            }
            if(ls.getPayInAmt0() != null){
                payInAmt0 = payInAmt0.add(ls.getPayInAmt0()) ;
            }
            if(ls.getPayInAmt1() != null){
                payInAmt1 = payInAmt1.add(ls.getPayInAmt1()) ;
            }
            if(ls.getPayInAmt2() != null){
                payInAmt2 = payInAmt2.add(ls.getPayInAmt2()) ;
            }
            if(ls.getAdditional() != null){
                additional = additional.add(ls.getAdditional()) ;
            }
            if(ls.getAdditional() != null){
                offsetClaim = offsetClaim.add(ls.getOffsetClaim()) ;
            }
            if(ls.getPayAmt() != null){
                payAmt = payAmt.add(ls.getPayAmt()) ;
            }
            if(ls.getPayAmt0() != null){
                payAmt0 = payAmt0.add(ls.getPayAmt0()) ;
            }
            if(ls.getPayAmt1() != null){
                payAmt1 = payAmt1.add(ls.getPayAmt1()) ;
            }
            if(ls.getPayAmt2() != null){
                payAmt2 = payAmt2.add(ls.getPayAmt2()) ;
            }
            if(ls.getPayAmtDiff() != null){
                payAmtDiff = payAmtDiff.add(ls.getPayAmtDiff()) ;
            }
            if(ls.getPayInAmt() != null){
                payInAmt = payInAmt.add(ls.getPayInAmt()) ;
            }
        }
        CashDetail cashDetail = new CashDetail();
        cashDetail.setCustomerQty(customerQty);
        cashDetail.setPayInAmt0(payInAmt0);
        cashDetail.setPayInAmt1(payInAmt1);
        cashDetail.setPayInAmt2(payInAmt2);
        cashDetail.setAdditional(additional);
        cashDetail.setOffsetClaim(offsetClaim);
        cashDetail.setPayAmt(payAmt);
        cashDetail.setPayAmt0(payAmt0);
        cashDetail.setPayAmt1(payAmt1);
        cashDetail.setPayAmt2(payAmt2);
        cashDetail.setPayAmtDiff(payAmtDiff);
        cashDetail.setPayInAmt(payInAmt);
        // 获取总页数
//        long totalPage = (count % 10 == 0) ? (count / 10) : (count / 10) + 1;
//        cashDetail.setTotal(totalPage);

        ajaxResultDto.setData(cashDetail);
        ajaxResultDto.setSuccess(true);
        return ajaxResultDto;
    }

    /**
     * 获取营业员一览  根据用户权限获取
     */
    @RequestMapping(value = "/getStaff")
    @ResponseBody
    public List<AutoCompleteDTO> getStaff(HttpSession session, HttpServletRequest request, HttpServletRequest req,
                                           String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = cashService.getStaff(v,stores);
        }
        return dtos;
    }

    /**
     * 获取营业员名称
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserName")
    public ResultDto getUserName(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String userId) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(userId)){
            String userName = cashService.getUserName(userId);
            if(StringUtils.isNotBlank(userName)){
                resultDto.setSuccess(true);
                resultDto.setData(userName);
            }else{
                resultDto.setMessage("Salesperson query is empty！");
            }
        }else{
            resultDto.setMessage("Operator ID cannot be empty！");
        }
        return resultDto;
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
    @Permission(codes = { PermissionCode.CODE_SC_SL_CF_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        CashDetailParam param = gson.fromJson(searchJson, CashDetailParam.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        User u = this.getUser(session);
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setUserId(u.getUserId());
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_SL_CF_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("slsfExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,CashDetailParam param){
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
