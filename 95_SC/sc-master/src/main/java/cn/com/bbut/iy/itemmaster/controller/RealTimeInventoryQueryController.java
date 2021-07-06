package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.service.ma1140.MA1140Service;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 实时库存查询
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/rtInventoryQuery")
public class RealTimeInventoryQueryController extends BaseAction {
    @Autowired
    RealTimeInventoryQueryService realTimeInventoryQueryService;
    @Autowired
    private MA2000Service ma2000Service;

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_REALTIME_INVENTORY";
    private final String EXCEL_EXPORT_NAME = "Real-time Inventory Query.xlsx";

    /**
     * 实时库存一览画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_RF_INVENTORY_LIST_VIEW})
    public ModelAndView toList(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店实时库存查询一览画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("rtInventory_query/rtInventoryQuery");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "实时库存查询画面");
        mv.addObject("date",date);
        return mv;
    }



    /**
     * 实时库存一览
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/getInventory")
    @ResponseBody
    public GridDataDTO<RTInventoryQueryDTO> getRecord(HttpServletRequest request, HttpSession session,
                                                      int page, int rows, String searchJson) {
        Gson gson = new Gson();
        RTInventoryQueryParamDTO rTParamDTO = gson.fromJson(searchJson, RTInventoryQueryParamDTO.class);
        if (rTParamDTO == null) {
            rTParamDTO = new RTInventoryQueryParamDTO();
        }
        if(StringUtils.isBlank(rTParamDTO.getStoreCd())){
            return new GridDataDTO<>();
        }
        rTParamDTO.setPage(page);
        rTParamDTO.setRows(rows);
        rTParamDTO.setLimitStart((page - 1) * rows);

        return realTimeInventoryQueryService.getInventoryList(rTParamDTO);
    }

    /**
     * 输入框供应商取Name
     */
    @ResponseBody
    @RequestMapping(value = "/selectVendorName", method = RequestMethod.GET)
    public String selectVendorName(HttpServletRequest request, HttpSession session,Map<String, ?> model,String deliveryCenterId) {
        String vendorName = ma2000Service.selectByVendorId(deliveryCenterId);
        return new Gson().toJson(vendorName);
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
    @Permission(codes = { PermissionCode.CODE_SC_RF_INVENTORY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        RTInventoryQueryParamDTO param = gson.fromJson(searchJson, RTInventoryQueryParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_RF_INVENTORY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("realTimeExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,RTInventoryQueryParamDTO param){
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
