package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.storeTransferDaily.StoreTransferDailyService;
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
 * 门店库存日报
 *
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeInventoryDaily")
public class StoreInventoryDailyController extends BaseAction {

    @Autowired
    private StoreTransferDailyService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma4320Service ma4320Service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_INVENTORY_REPORT";
    private final String EXCEL_EXPORT_NAME = "Store Inventory Daily Report.xlsx";

    /**
     * 门店库存日报一览画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_INVENTORY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 进入 门店报废日报", u.getUserId());
        ModelAndView mv = new ModelAndView("storeInventoryDaily/storeInventoryDaily");
        String businessDate = cm9060Service.getValByKey("0000");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("author", u.getUserName());
        mv.addObject("bsDate", Utils.getFormateDate(ymd));
        mv.addObject("businessDate", businessDate);
        mv.addObject("useMsg", "门店报废日报一览画面");
        return mv;
    }

    /**
     * 门店日报打印画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session,
                                     String searchJson,int page,int rows) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 进入 门店报废日报打印画面", u.getUserId());
        ModelAndView mv = new ModelAndView("storeInventoryDaily/storeInventoryDailyPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("author", u.getUserName());
        mv.addObject("bsDate", Utils.getFormateDate(ymd));
        mv.addObject("searchJson", searchJson);
        mv.addObject("page", page);
        mv.addObject("rows", rows);
//        mv.addObject("printTime", new Date());
        mv.addObject("useMsg", "门店报废日报打印画面");
        return mv;
    }

    /**
     * 查询门店库存数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getData")
    public ReturnDTO getData(HttpServletRequest request, HttpSession session,
                             String searchJson,int page,int rows) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        VendorReceiptDailyParamDTO param = gson.fromJson(searchJson, VendorReceiptDailyParamDTO.class);

        if (page<1) {
            page = 1;
        }
        if (rows<1) {
            rows = 10;
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1) * rows);
        Map<String,Object> _list = service.getList(param);
        if(_list == null || _list.size() < 1){
            _return.setMsg("No data found");
        }else{
            _return.setO(_list);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
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
    @Permission(codes = { PermissionCode.CODE_SC_INVENTORY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        VendorReceiptDailyParamDTO param = gson.fromJson(searchJson, VendorReceiptDailyParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_INVENTORY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("inventoryReportExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,VendorReceiptDailyParamDTO param){
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
     * 查询门店库存数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/print/getprintData")
    public ReturnDTO getPrintData(HttpServletRequest request, HttpSession session,
                             String searchJson, int page,int rows) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        VendorReceiptDailyParamDTO param = gson.fromJson(searchJson, VendorReceiptDailyParamDTO.class);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1) * rows);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            _return.setMsg("Query failed!");
            return _return;
        }
        param.setStores(stores);
        List<Sk0020DTO> _list = service.getListPrintData(param);
        if(_list == null || _list.size() < 1){
            _return.setMsg("No data found");
        }else{
            _return.setO(_list);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }
}
