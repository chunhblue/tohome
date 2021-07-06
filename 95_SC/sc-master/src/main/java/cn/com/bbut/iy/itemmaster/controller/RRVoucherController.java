package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.receipt.rrVoucher.IRRVoucherService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 进退货传票登录
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/rrVoucherQuery")
public class RRVoucherController extends BaseAction {

    @Autowired
    private IRRVoucherService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    private Ma4320Service ma4320Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_RECEIVING_RETURN_QUERY";
    private final String EXCEL_EXPORT_NAME = "Receiving And Return Query.xlsx";

    /**
     * 进退货传票一览画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_RR_QUERY_LIST_VIEW})
    public ModelAndView rrVoucherList(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入进退货修正传票一览画面", u.getUserId());
        ModelAndView mv = new ModelAndView("rrVoucherQuery/rrVoucherQuery");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "进退货传票一览画面");
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
    public GridDataDTO<RRVoucherDTO> getList(HttpServletRequest request, HttpSession session,
                                             int page, int rows, String searchJson) {
        Gson gson = new Gson();
        RRVoucherParamDTO param = gson.fromJson(searchJson, RRVoucherParamDTO.class);
        if(param == null){
            param = new RRVoucherParamDTO();
        }
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>> get stores is null");
            return new GridDataDTO<RRVoucherDTO>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return service.getList(param);
    }

    /**
     * 查询进退货传票基础信息
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
        RRVoucherParamDTO param = gson.fromJson(searchJson, RRVoucherParamDTO.class);
        if(param == null){
            _return.setMsg("Failed to get request parameters!");
            return _return;
        }
        RRVoucherDTO _dto = service.getRecordByKey(param);
        if(_dto == null){
            _return.setMsg("No data found");
        }else{
            _return.setSuccess(true);
            _return.setO(_dto);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    /**
     * 查询传票详情
     *
     * @param request
     * @param session
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDetails")
    public GridDataDTO<RRVoucherDetailsDTO> getDetails(HttpServletRequest request, HttpSession session,
                                                       int page, int rows, String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<RRVoucherDetailsDTO>();
        }
        Gson gson = new Gson();
        RRVoucherParamDTO param = gson.fromJson(searchJson, RRVoucherParamDTO.class);
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
            PermissionCode.CODE_SC_RR_QUERY_CONFIRM
    })
    public ReturnDTO save(HttpServletRequest request, HttpSession session,
                          String searchJson, String listJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        RRVoucherDTO _dto = gson.fromJson(searchJson, RRVoucherDTO.class);
        if(_dto == null){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        List<RRVoucherDetailsDTO> _list = gson.fromJson(listJson, new TypeToken<List<RRVoucherDetailsDTO>>(){}.getType());
        if(_list == null || _list.size()<1){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 执行保存
        _dto.setCommonDTO(dto);
        int i = service.updateRecord(_dto, _list);
        if(i < 1){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 快速收货
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/quick")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_RR_QUERY_RECEIVE
    })
    public ReturnDTO changeStatus(HttpServletRequest request, HttpSession session, String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        List<RRVoucherDTO> _list = gson.fromJson(searchJson, new TypeToken<List<RRVoucherDTO>>(){}.getType());
        if(_list == null || _list.size()<1){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get login user information");
            return _return;
        }
        int count = service.updateRecordByQuick(_list, dto);
        if(count < 1){
            _return.setMsg("更新收货状态失败");
        }else{
            _return.setO(count);
            _return.setSuccess(true);
            _return.setMsg("更新收货状态成功");
        }
        return _return;
    }

    /**
     * 打印
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_PD_PROCESS_PRINT})
    public ModelAndView rrVoucherQueryPrint(HttpServletRequest request, HttpSession session,
                                              Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 进入收货查询打印一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("rrVoucherQuery/rrVoucherQueryPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("printTime", Utils.getFormateDate(ymd));
        mv.addObject("searchJson", searchJson);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "收货查询打印一览打印画面");
        return mv;
    }

    @RequestMapping(value = "/print/getprintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData(HttpServletRequest request, HttpSession session,String searchJson) {
        Gson gson = new Gson();
        RRVoucherParamDTO param = gson.fromJson(searchJson, RRVoucherParamDTO.class);
        if(param == null){
            param = new RRVoucherParamDTO();
        }
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"The query data is empty!", null);
        }
        param.setStores(stores);
        param.setFlg(false);
        List<RRVoucherDTO> result = service.getPrintData(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_RR_QUERY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        RRVoucherParamDTO param = new Gson().fromJson(searchJson, RRVoucherParamDTO.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_RR_QUERY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("rrQueryExService", ExService.class);
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
    private Collection<String> getStores(HttpSession session, RRVoucherParamDTO param){
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
     * @Description: 获取审核状态
     * @return _return 返回类型
     * @return
     */
    @ResponseBody
    @RequestMapping("/getRRVoucherRecordStatus")
    public AjaxResultDto getAuditStatus(HttpSession session, HttpServletRequest request,
                                        String recordId, @RequestParam("status")int returnStatus, int typeId, int reviewId) {
        AjaxResultDto _return = new AjaxResultDto();
        if(reviewId!=0){
            typeId = ConstantsAudit.getTypeIdByReviewId(reviewId);
        }
        if(StringUtils.isBlank(recordId)||typeId==0){
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
            if(returnStatus != 0) {
                switch (returnStatus) {
                    case 1:
                        _return.setSuccess(false);
                        _return.setMessage("In review, cannot modify!");
                        break;
                    case 5:
                        _return.setSuccess(true);
                        break;
                    case 6:
                        _return.setSuccess(true);
                        break;
                    case 7:
                        _return.setSuccess(false);
                        _return.setMessage("The approval status is expired and cannot modify!");//审核状态已失效
                    default:
                        _return.setSuccess(false);
                        _return.setMessage("Selected document!");//审核状态已完成
                }
            }
        }else if(status == 15){////收货录入完成，未审核
            _return.setSuccess(true);
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
