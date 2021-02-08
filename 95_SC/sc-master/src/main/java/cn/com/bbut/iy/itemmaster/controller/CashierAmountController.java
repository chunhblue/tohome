package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.CashService;
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
 * 收银员收款金额登录
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/cashierAmount")
public class CashierAmountController extends BaseAction {
    @Autowired
    private CashService cashService;
    @Autowired
    private CM9060Service cm9060Service;
    /**
     * 收银员收款金额登录画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_CA_EDIT,
            PermissionCode.CODE_SC_CA_LIST_VIEW
    })
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model, String flag,CashDetail cashDetail) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 收银员收款金额登录", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("cashierAmount/cashierAmount");
        mv.addObject("date",date);
        mv.addObject("data",cashDetail);
        mv.addObject("viewSts", flag);
        mv.addObject("typeId", ConstantsAudit.TYPE_CASHIER_AMOUNT);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_CASHIER_AMOUNT);
        mv.addObject("useMsg", "收银员收款金额登录画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 获取收银员下拉列表
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCashierList")
    public List<AutoCompleteDTO> getCashierList(String v, String storeCd,HttpServletRequest request, HttpSession session) {
        return cashService.getCashierList(v,storeCd);
    }

    /**
     * 获取支付类型信息
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
//    @ResponseBody
//    @RequestMapping(value = "/getPayList1")
//    public GridDataDTO<CashDetail> getPayList1(HttpServletRequest request, HttpSession session,
//                                                      Map<String, ?> model,String searchJson) {
//        GridDataDTO<CashDetail> grid = new  GridDataDTO<CashDetail>();
//        CashDetail cashDetail = new Gson().fromJson(searchJson, CashDetail.class);
//        grid.setRows(cashService.getPayList(cashDetail));
//        return grid;
//    }
    @ResponseBody
    @RequestMapping(value = "/getPayList")
    public GridDataDTO<CashDetail> getPayList(HttpServletRequest request, HttpSession session,
                                              Map<String, ?> model,String searchJson,String payList) {
        GridDataDTO<CashDetail> grid = new GridDataDTO<CashDetail>();
        CashDetail cashDetail = new Gson().fromJson(searchJson, CashDetail.class);
        List<CashDetail>  payList1 = null;
            payList1 = new Gson().fromJson(payList, new TypeToken<List<CashDetail>>() {
            }.getType());
            List<CashDetail> payList2 = cashService.getPayList(cashDetail, payList1);
            grid.setRows(payList2);
            return grid;
    }
        /**
         * 获取现金信息
         * @param session
         * @param model
         * @return
         */
        @SuppressWarnings("unchecked")
        @ResponseBody
        @RequestMapping(value = "/getCashList")
        public GridDataDTO<CashDetail> getCashList (HttpServletRequest request, HttpSession session,
                Map < String, ?>model, String searchJson){
            GridDataDTO<CashDetail> grid = new GridDataDTO<CashDetail>();
            CashDetail cashDetail = new Gson().fromJson(searchJson, CashDetail.class);
            grid.setRows(cashService.getCashList(cashDetail));
            return grid;
        }

        /**
         * 获取营业员名称
         * @param session
         * @param model
         * @return
         */
        @ResponseBody
        @RequestMapping(value = "/getUserName")
        public ResultDto getUserName (HttpServletRequest request, HttpSession session,
                Map < String, ?>model, String userId){
            ResultDto resultDto = new ResultDto();
            resultDto.setSuccess(false);
            if (StringUtils.isNotBlank(userId)) {
                String userName = cashService.getUserName(userId);
                if (StringUtils.isNotBlank(userName)) {
                    resultDto.setSuccess(true);
                    resultDto.setData(userName);
                } else {
                    resultDto.setMessage("Salesperson query is empty！");
                }
            } else {
                resultDto.setMessage("Operator ID cannot be empty！");
            }
            return resultDto;
        }

    /**
     * 查询收银员收款登录信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCashDetail")
    public ResultDto get(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String searchJson) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        CashDetail cashDetail = new Gson().fromJson(searchJson, CashDetail.class);
        CashDetail result = cashService.getCashDetail(cashDetail);
        if(result!=null){
            resultDto.setSuccess(true);
            resultDto.setData(result);
        }else{
            resultDto.setMessage("Payment information is empty!");
        }
        return resultDto;
    }
    /**
     * 保存收银员收款信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/save")
    @Permission(codes = { PermissionCode.CODE_SC_CA_EDIT})
    public AjaxResultDto save(HttpServletRequest request, HttpSession session,
                              Map<String, ?> model, CashDetail cashDetail,
                              String cashList, String payList) {
        User u = this.getUser(session);
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            return res;
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("HHmmss");
        cashDetail.setUpdateUserId(u.getUserId());
        cashDetail.setUpdateYmd(sdf.format(date));
        cashDetail.setUpdateHms(sdf1.format(date));
        cashDetail = cashService.insertOrUpdate(cashDetail,payList,cashList);
        if(cashDetail!=null){
            res.setSuccess(true);
            res.setData(cashDetail.getPayId());
        }
        return res;
    }

}
