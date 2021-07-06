package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;

import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.orderreport.OrderReportService;
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
import java.util.*;

/**
 * @ClassName OrderReportController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/7/30 16:44
 * @Version 1.0
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/orderReportQuery")
public class OrderReportController extends BaseAction{

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private OrderReportService orderReportService;
    @Autowired
    private Ma4320Service ma4320Service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_ORDER_REPORT_REPORT";
    private final String EXCEL_EXPORT_NAME = "Order Report.xlsx";

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.SC_ORDER_REPORT_LIST_VIEW })
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String date = cm9060Service.getValByKey("0000");
      //  log.debug("User:{} 进入商品销售日报", u.getUserId());
        ModelAndView mv = new ModelAndView("orderReport/orderreport");
        mv.addObject("useMsg", "商品销售日报");
        mv.addObject("date", date);
        mv.addObject("bsDate", Utils.getFormateDate(ymd));
        mv.addObject("userName",u.getUserName());
        return mv;
    }
    @RequestMapping(value ="/search",method = RequestMethod.POST)
    @ResponseBody
    public  ReturnDTO getOrderReport(String SearchJson, HttpServletRequest request, HttpSession session){
        ReturnDTO _return = new ReturnDTO();
        OrderReportParamDTO param=null;
        if (SearchJson!=null && SearchJson!="") {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson,    OrderReportParamDTO.class);

        }
        if (param==null){
            param= new   OrderReportParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        Map<String,Object> _list=   orderReportService.getData(param);
        if(_list == null || _list.size() < 1){
            _return.setMsg("No data found");
        }else{
            _return.setO(_list);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;

    }


    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_GS_RE_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        OrderReportParamDTO param = gson.fromJson(searchJson,  OrderReportParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_GS_RE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("orderReportExservice", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }









    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, OrderReportParamDTO param){
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
