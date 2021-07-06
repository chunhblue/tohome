package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
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
 * 店间调拨
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeTransferIn")
public class StoreTransfersInController extends BaseAction {

    @Autowired
    private InventoryVouchersService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_STORE_TRANSFER_IN";
    private final String EXCEL_EXPORT_NAME = "Store Transfer In Query.xlsx";

    /**
     * 店间调拨管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFERIN_LIST_VIEW})
    public ModelAndView storeTransfersList(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调入一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storeTransfersInEntry/storetransfersIn");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFERIN);
        mv.addObject("useMsg", "店间调入一览画面");
        return mv;
    }

    /**
     * 店间调拨管理详情查看画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_ST_TRANSFERIN_VIEW,
            PermissionCode.CODE_SC_ST_KCCPYL_VIEW
    })
    public ModelAndView view(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调拨转入管理详情查看画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storeTransfersInEntry/storetransfersInedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", "view");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFERIN);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TRANSFERIN);
        mv.addObject("useMsg", "店间调入管理详情查看画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 店间调拨管理详情编辑、新增画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_ST_TRANSFERIN_ADD,
            PermissionCode.CODE_SC_ST_TRANSFERIN_EDIT,
            PermissionCode.CODE_SC_ST_KCCPYL_EDIT
    })
    public ModelAndView edit(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调拨管理详情编辑、新增画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("storeTransfersInEntry/storetransfersInedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", flag);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TRANSFERIN);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TRANSFERIN);
        mv.addObject("useMsg", "店间调入管理详情编辑、新增画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 店间调拨管理商品详情更新
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update")
    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFERIN_EDIT })
    public AjaxResultDto updateDetails(HttpServletRequest request, HttpSession session, String listJson, String flg) {

        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if(!res.isSuccess()){
            res.setToKen(res.getToKen());
            res.setMsg(res.getMessage());
            return  res;
        }
        if(!"1".equals(flg)){
            // 未修改数量，不执行更新
            res.setMsg("Don't need to update data!");
            res.setSuccess(true);
            return res;
        }
        if(StringUtils.isBlank(listJson)){
            res.setMsg("Parameter cannot be empty!");
            return res;
        }
        // 转换参数对象
        Gson gson = new Gson();
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            res.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return res;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            res.setMsg("Failed to get user information!");
            return res;
        }
        // 执行保存
        int i = service.updateSk0020(dto, sk0020List);
        if(i < 1){
            res.setMsg("Data saved failed！");
        }else {
            res.setMsg("Data saved successfully！");
            res.setSuccess(true);
        }
        return res;
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
    @Permission(codes = { PermissionCode.CODE_SC_ST_TRANSFERIN_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_ST_TRANSFERIN_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("storeTransferInExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 获取当前操作用户、时间
     */
    private CommonDTO getCommonDTO(HttpSession session){
        User u = this.getUser(session);
        if(u == null){
            return null;
        }
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        CommonDTO dto = new CommonDTO();
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(ymd);
        dto.setUpdateYmd(ymd);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(hms);
        dto.setUpdateHms(hms);
        return dto;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,InventoryVouchersParamDTO param){
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

    @ResponseBody
    @RequestMapping(value = "/getDifferenQty" )
    public ReturnDTO getDifferenQty(Sk0020DTO sk0020DTO){
        BigDecimal differenQty = sk0020DTO.getQty2().subtract(sk0020DTO.getQty1());
        sk0020DTO.setDifferenQty(differenQty);
        ReturnDTO returnDTO = new ReturnDTO();
        returnDTO.setO(sk0020DTO);
        return  returnDTO;
    }
}
