package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.cashierDetail.CashierDetailService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
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
 * 收银明细管理画面
 *
 * @author zcz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/cashierDetail")
public class CashierDetailController extends BaseAction {
    @Autowired
    private CashierDetailService service;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_SALE_INFORMATION_QUERY";
    private final String EXCEL_EXPORT_NAME = "Sale Information Report.xlsx";

    @ResponseBody
    @RequestMapping(value = "/getCashier")
    @Permission(codes = { PermissionCode.CODE_SC_CS_DETAIL_LIST_VIEW})
    public ReturnDTO getCashier(String storeCd, String posId, HttpServletRequest request, HttpSession session) {
        List<SA0050> list = service.getCashier(storeCd, posId);

        if (list==null||list.size()<1) {
            return new ReturnDTO(false,"error");
        }
        return new ReturnDTO(true,"ok",list);
    }


    /**
     * 收银明细管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_CS_DETAIL_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 收银明细管理", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        //获取支付类型
        List<PayMethod> payList =  service.getAllPay();
        ModelAndView mv = new ModelAndView("cashierDetail/cashierDetailList");
        mv.addObject("useMsg", "收银明细管理画面");
        mv.addObject("payList",payList);
        mv.addObject("date",date);
        return mv;
    }

    /**
     * 获取销售头档信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSaleHeadList")
    @Permission(codes = { PermissionCode.CODE_SC_CS_DETAIL_LIST_VIEW})
    public GridDataDTO<SaleHead> getSaleHeadList(HttpServletRequest request, HttpSession session,
                                             CashierDetailParam param) {
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<SaleHead>();
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setStartDate(startDate);
        }
        param.setStores(stores);
        GridDataDTO<SaleHead> grid = service.getSaleHeadList(param);
        return grid;
    }

    /**
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param param
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_CS_DETAIL_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, CashierDetailParam param) {
        if(org.springframework.util.StringUtils.isEmpty(param)){
            log.info("导出查询参数为空");
            return null;
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setStartDate(startDate);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        param.setStores(stores);
        param.setFlg(false);
        // 对象转json
        String jsonStr = new Gson().toJson(param);
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(jsonStr);
        exParam.setPCode(PermissionCode.CODE_SC_CS_DETAIL_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("cashierDetailExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 获取销售支付类型信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getPayDetail")
    @Permission(codes = { PermissionCode.CODE_SC_CS_DETAIL_LIST_VIEW})
    public GridDataDTO<PayMethod> getPayDetail(HttpServletRequest request, HttpSession session,
                                                 Map<String, ?> model, SaleHeadParam param,int page, int rows) {
        GridDataDTO<PayMethod> grid = new GridDataDTO<>();
        if(param!=null&&StringUtils.isNotBlank(param.getStoreCd())&&
                StringUtils.isNotBlank(param.getPosId())&&
                StringUtils.isNotBlank(param.getAccDate())&&
                param.getTranSerialNo()>0){
            grid.setRows(service.getPayDetail(param));
        }
        return grid;
    }

    /**
     * 获取销售详细信息
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSaleDetailList")
    @Permission(codes = { PermissionCode.CODE_SC_CS_DETAIL_LIST_VIEW})
    public GridDataDTO<SaleDetail> getSaleDetail(HttpServletRequest request, HttpSession session,
                                               Map<String, ?> model, SaleHeadParam param,int page, int rows) {
        GridDataDTO<SaleDetail> grid = new GridDataDTO<>();
        if(param!=null&&StringUtils.isNotBlank(param.getStoreCd())&&
                StringUtils.isNotBlank(param.getPosId())&&
                StringUtils.isNotBlank(param.getAccDate())&&
                param.getTranSerialNo()>0){
            param.setPage(page);
            param.setLimitStart((page - 1)*rows);
            grid = service.getSaleDetailList(param);
        }
        return grid;
    }


    @RequestMapping(value = "/getSaleDetailTotal")
    @ResponseBody
    public ReturnDTO getSaleDetailTotal(SaleHeadParam param){
        SaleDetail data = service.getSaleDetailTotal(param);
        if(data == null){
            return new ReturnDTO(false,"ok",null);
        }
        return new ReturnDTO(true,"ok",data);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, CashierDetailParam param){
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
}
