package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.ItemInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrModify.RRModifyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.ReturnWarehouseDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.ReturnWarehouseParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.inventory.InventoryVouchersService;
import cn.com.bbut.iy.itemmaster.service.receipt.rrModify.IRRModifyService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 进退货修正传票登录
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/rrModifyQuery")
public class RRModifyController extends BaseAction {

    @Autowired
    private IRRModifyService service;
    @Autowired
    private InventoryVouchersService inventoryVouchersService;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_PURCHASE_AND_RETURN";
    private final String EXCEL_EXPORT_NAME = "Receiving and Return Document Correction Query.xlsx";

    /**
     * 进退货修正传票登录画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_RR_MODIF_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView rrModifyList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入进退货修正传票登录画面", u.getUserId());
        ModelAndView mv = new ModelAndView("rrModifyQuery/rrModifyQuery");
        mv.addObject("use", 0);
        mv.addObject("typeId", ConstantsAudit.TYPE_REC_RET_CORRECTION);
        mv.addObject("useMsg", "进退货修正传票登录画面");
        return mv;
    }

    /**
     * 进退货修正传票详情新增画面
     * @param request
     * @param session
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_RR_MODIF_EDIT
    })
    public ModelAndView toAdd(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 进退货修正传票详情新增画面", u.getUserId());
        ModelAndView mv = new ModelAndView("rrModifyQuery/rrModifyEdit");
        mv.addObject("use", 0);
        mv.addObject("viewSts", "add");
        mv.addObject("typeId", ConstantsAudit.TYPE_REC_RET_CORRECTION);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_REC_RET_CORRECTION);
        mv.addObject("useMsg", "进退货修正传票详情新增画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 进退货修正传票详情修改画面
     * @param request
     * @param session
     * @param
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_RR_MODIF_EDIT
    })
    public ModelAndView toEdit(HttpServletRequest request, HttpSession session,
                               String storeCd, String orderId) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 进退货修正传票详情修改画面", u.getUserId());
        ModelAndView mv = new ModelAndView("rrModifyQuery/rrModifyEdit");
        mv.addObject("use", 0);
        mv.addObject("viewSts", "edit");
        mv.addObject("storeCd", storeCd);
        mv.addObject("orderId", orderId);
        mv.addObject("typeId", ConstantsAudit.TYPE_REC_RET_CORRECTION);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_REC_RET_CORRECTION);
        mv.addObject("useMsg", "进退货修正传票详情修改画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 进退货修正传票详情查看画面
     *
     * @param request
     * @param session
     * @param
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_RR_MODIF_VIEW
    })
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                               String storeCd, String orderId) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 进退货修正传票详情查看画面", u.getUserId());
        ModelAndView mv = new ModelAndView("rrModifyQuery/rrModifyEdit");
        mv.addObject("use", 0);
        mv.addObject("storeCd", storeCd);
        mv.addObject("orderId", orderId);
        mv.addObject("viewSts", "view");
        mv.addObject("typeId", ConstantsAudit.TYPE_REC_RET_CORRECTION);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_REC_RET_CORRECTION);
        mv.addObject("useMsg", "进退货修正传票详情查看画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 条件查询记录
     *
     * @param request
     * @param session
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<RRModifyDTO> getList(HttpServletRequest request, HttpSession session,
                                            int page, int rows, String searchJson) {
        Gson gson = new Gson();
        RRModifyParamDTO param = gson.fromJson(searchJson, RRModifyParamDTO.class);
        if(param == null){
            param = new RRModifyParamDTO();
        }
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>> get stores is null");
            return new GridDataDTO<RRModifyDTO>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return service.getList(param);
    }

    /**
     * 查询进退货修正传票基础信息
     *
     * @param request
     * @param session
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get")
    public ReturnDTO getRecord(HttpServletRequest request, HttpSession session,
                               String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        Gson gson = new Gson();
        RRModifyParamDTO param = gson.fromJson(searchJson, RRModifyParamDTO.class);
        if(param == null){
            _return.setMsg("Failed to get request parameters!");
           return _return;
        }
        RRModifyDTO _dto = service.getRecordByKey(param);
        if(_dto == null){
            _return.setMsg("Query data failed!");
        }else{
            _return.setO(_dto);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    /**
     * 查询进退货修正传票详情
     *
     * @param request
     * @param session
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDetails")
    public GridDataDTO<RRModifyDetailsDTO> getDetails(HttpServletRequest request, HttpSession session,
                                                      int page, int rows,String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<RRModifyDetailsDTO>();
        }
        Gson gson = new Gson();
        RRModifyParamDTO param = gson.fromJson(searchJson, RRModifyParamDTO.class);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return service.getDetail(param);
    }

    /**
     * 保存
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/save")
    @Permission(codes = {
            PermissionCode.CODE_SC_RR_MODIF_EDIT,
    })
    public AjaxResultDto save(HttpServletRequest request, HttpSession session,
                                           String searchJson, String listJson) {
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            res.setToKen(res.getToKen());
            res.setMsg(res.getMessage());
            return  res;
        }
        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            res.setMsg("Parameter cannot be empty!");
            return res;
        }
        // 转换参数对象
        Gson gson = new Gson();
        RRModifyDTO _dto = gson.fromJson(searchJson, RRModifyDTO.class);
        if(_dto == null){
            res.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return res;
        }
        List<OD0010> _list = gson.fromJson(listJson, new TypeToken<List<OD0010>>(){}.getType());
        if(_list == null || _list.size()<1){
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
        _dto.setCommonDTO(dto);
        String orderId = service.insertRecord(_dto, _list);
        if(StringUtils.isBlank(orderId)){
            res.setMsg("Data saved failed！");
        }else {
            res.setMsg("Data saved successfully！");
            res.setSuccess(true);
            res.setO(orderId);
        }
        return res;
    }

    /**
     * 查询商品信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getItem")
    public ReturnDTO getItemInfoByCode(HttpServletRequest request, HttpSession session,
                                       String storeCd, String itemCode) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(itemCode)){
            _return.setMsg("The Item Code cannot be empty!");
            return _return;
        }
        ItemInfoDTO _dto = inventoryVouchersService.getItemInfoByCode(storeCd, itemCode);
        if(_dto == null){
            _return.setMsg("No data found!");
        }else{
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    @RequestMapping(value = "/getItemInfo")
    @ResponseBody
    public AjaxResultDto getItemInfo(HttpSession session, HttpServletRequest req,
                                     RRModifyParamDTO param) {
        User user = this.getUser(session);
        log.debug("抓取商品信息，user:{}", user.getUserId());
        AjaxResultDto resultDto = new AjaxResultDto();
        if(param!=null) {
            if (param.getReturnType().equals("10")) {
                RRModifyDTO itemInfo = service.getdirectItemInfo(param);
                if (itemInfo != null) {
                    resultDto.setData(itemInfo);
                    resultDto.setSuccess(true);
                    resultDto.setFlag("1");
                    return resultDto;
                }
            }
        }
        resultDto.setSuccess(false);
        resultDto.setMessage("Failed to load item data!");//商品数据加载失败
        return resultDto;
    }

    /**
     * 查询原订单编号
     *
     */
    @ResponseBody
    @RequestMapping(value = "/getOrgOrder")
    public List<AutoCompleteDTO> getByLevel(HttpServletRequest request, HttpSession session,
                                            String type, String storeCd, String v){
        if(StringUtils.isBlank(type) || StringUtils.isBlank(storeCd)){
            return null;
        }
        List<AutoCompleteDTO> _list = service.getOrgOrderId(type, storeCd, v);
        return _list;
    }

    @ResponseBody
    @RequestMapping(value = "/countOrgOrderId")
    public ReturnDTO countOrgOrderId(HttpServletRequest request, HttpSession session,
                               String receiveId) {
        ReturnDTO _return = new ReturnDTO();
        if(receiveId == null){
            _return.setMsg("Failed to get Original Document No.!");
            _return.setO(0);
            return _return;
        }
        int i = service.countOrderId(receiveId);
            _return.setO(i);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        return _return;
    }

    @ResponseBody
    @RequestMapping(value = "/updateLastCorr")
    public ReturnDTO updateLastCorr(HttpServletRequest request, HttpSession session,
                                     String orderId) {
        ReturnDTO _return = new ReturnDTO();
        if(orderId == null){
            _return.setMsg("Failed to get Document No.!");
            _return.setO(0);
            return _return;
        }
        int i = service.updateLastCorr(orderId);
        _return.setO(i);
        _return.setSuccess(true);
        _return.setMsg("Query succeeded!");
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
    @Permission(codes = { PermissionCode.CODE_SC_RR_MODIF_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        RRModifyParamDTO param = new Gson().fromJson(searchJson, RRModifyParamDTO.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_RR_MODIF_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("rrModifyExService", ExService.class);
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
        CommonDTO dto = new CommonDTO();
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(date);
        dto.setUpdateYmd(date);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(time);
        dto.setUpdateHms(time);
        return dto;
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, RRModifyParamDTO param){
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
