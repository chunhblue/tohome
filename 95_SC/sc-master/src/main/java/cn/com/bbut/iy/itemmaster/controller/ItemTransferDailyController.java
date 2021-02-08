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
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.itemTransferDaily.ItemTransferDailyService;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * 店内调拨日报
 * @author lch
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/itemTransferDaily")
public class ItemTransferDailyController  extends BaseAction {

    @Autowired
    private ItemTransferDailyService itemTransferDailyService;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "ITEM_TRANSFER_DAILY_REPORT";
    private final String EXCEL_EXPORT_NAME = "Item Transfer Daily Report.xlsx";

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_IT_TR_LIST_VIEW
    })
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店内调拨日报(按商品)", u.getUserId());
        ModelAndView mv = new ModelAndView("itemTransferDaily/itemTransferDaily");
        mv.addObject("useMsg", "店内调拨日报(按商品)");
        mv.addObject("bsDate", new Date());
        mv.addObject("printTime", new Date());
        mv.addObject("userName",u.getUserName());
        return mv;
    }

    @RequestMapping("/search")
    @ResponseBody
    public ReturnDTO search(String jsonStr, HttpServletRequest request, HttpSession session) {
        VendorReceiptDailyParamDTO param = null;
        if (jsonStr==null||"".equals(jsonStr)) {
            param=new VendorReceiptDailyParamDTO();
        } else {
            Gson gson = new Gson();
            param = gson.fromJson(jsonStr, VendorReceiptDailyParamDTO.class);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        param.setStores(stores);
        Map<String,Object> result = itemTransferDailyService.search(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_IT_TR_EXPORT })
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
        exParam.setPCode(PermissionCode.CODE_SC_IT_TR_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("itemTransferDailyExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 门店调拨日报(按商品)打印画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_IT_TR_PRINT})
    @RequestMapping(value = "/print")
    public ModelAndView toPrintlistView(HttpServletRequest request, HttpSession session, HttpServletResponse response
                                    ,String searchJson) throws IOException {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店内调拨日报(按商品)打印", u.getUserId());
        ModelAndView mv = new ModelAndView("itemTransferDaily/itemTransferDailyPrint");
        mv.addObject("useMsg", "店内调拨日报(按商品)打印");
        mv.addObject("bsDate", new Date());
        mv.addObject("printTime", new Date());
        mv.addObject("searchJson", searchJson);
        mv.addObject("userName",u.getUserName());
        return mv;
    }

    @RequestMapping("/print/getprintData")
    @ResponseBody
    public ReturnDTO printData(String searchJson, HttpServletRequest request, HttpSession session) {
        VendorReceiptDailyParamDTO param = null;
        if (searchJson==null||"".equals(searchJson)) {
            param=new VendorReceiptDailyParamDTO();
        } else {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, VendorReceiptDailyParamDTO.class);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        List<Sk0020DTO> result = itemTransferDailyService.getPrintData(param);
        return new ReturnDTO(true,"ok",result);
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
}
