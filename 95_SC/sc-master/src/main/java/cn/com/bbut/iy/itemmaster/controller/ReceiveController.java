package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.ReceiveService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 收货
 *
 */
@Controller
@Slf4j
@Resource
@RequestMapping(value = Constants.REQ_HEADER + "/receive")
public class ReceiveController extends BaseAction {

    @Autowired
    private ReceiveService service;

    @Autowired
    private Ma4320Service ma4320Service;

    /**
     * 保存收货数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/save")
//    @Permission(codes = { PermissionCode.CODE_SC_RR_QUERY_CONFIRM })
    public AjaxResultDto save(HttpServletRequest request, HttpSession session,
                          String searchJson, String listJson,String fileDetailJson) {
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
        OD0000TDTO _dto = gson.fromJson(searchJson, OD0000TDTO.class);
        if(_dto == null){
            res.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return res;
        }
        List<OD0010TDTO> _list = gson.fromJson(listJson, new TypeToken<List<OD0010TDTO>>(){}.getType());
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
        _dto.setFileDetailJson(fileDetailJson);
        String receiveId = _dto.getReceiveId();
        if(StringUtils.isBlank(receiveId)){
            receiveId = service.insertByReceive(_dto, _list);
        }else{
            service.updateReceive(_dto, _list);
        }
        if(StringUtils.isBlank(receiveId)){
            res.setMsg("Data saved Failure！");
        }else {
            res.setMsg("Data saved Successfully！");
            res.setSuccess(true);
            res.setO(receiveId);
        }
        return res;
    }





    @ResponseBody
    @RequestMapping(value = "/saveDocumentUrl")
//    @Permission(codes = { PermissionCode.CODE_SC_RR_QUERY_CONFIRM })
    public AjaxResultDto saveDocumentUrl(HttpServletRequest request, HttpSession session,
                              String searchJson, String listJson,String fileDetailJson) {
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);

        if(StringUtils.isBlank(searchJson)){
            res.setMsg("Parameter cannot be empty!");
            return res;
        }
        // 转换参数对象
        Gson gson = new Gson();
        OD0000TDTO _dto = gson.fromJson(searchJson, OD0000TDTO.class);
        if(_dto == null){
            res.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
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
        _dto.setFileDetailJson(fileDetailJson);
        Integer count = service.insertDocumentUrl(_dto);
        if(count==0){
            res.setMsg("Data saved Failure！");
        }else {
            res.setMsg("Data saved Successfully！");
            res.setSuccess(true);
        }
        return res;
    }














    /**
     * 快速收货
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/quick")
//    @Permission(codes = { PermissionCode.CODE_SC_XS_USALE_LIST_VIEW})
    public AjaxResultDto quick(HttpServletRequest request, HttpSession session, String searchJson) {
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            res.setToKen(res.getToKen());
            res.setMsg(res.getMessage());
            return  res;
        }
        if(StringUtils.isBlank(searchJson)){
            res.setSuccess(false);
            res.setMsg("Parameter cannot be empty!");
            return res;
        }
        // 转换参数对象
        Gson gson = new Gson();
        List<OD0000TDTO> _list = gson.fromJson(searchJson, new TypeToken<List<OD0000TDTO>>(){}.getType());
        if(_list == null || _list.size()<1){
            res.setSuccess(false);
            res.setMsg("Failed to obtain order information");/*获取订单信息失败*/
            return res;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            res.setSuccess(false);
            res.setMsg("Failed to get login user information");
            return res;
        }
        Map<String,String> map = service.insertByQuick(_list, dto);
        if(map==null||map.isEmpty()){
            res.setSuccess(false);
            res.setMsg("Failed to update receipt information");
        }else{
            res.setO(map);
            res.setSuccess(true);
            res.setMsg("Update receipt information successfully");
        }
        return res;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/updateStatus")
    public AjaxResultDto updateStatus(HttpServletRequest request, HttpSession session, String receiveId) {
        AjaxResultDto res = ajaxRepeatSubmitCheck(request, session);
        if (!res.isSuccess()) {
            res.setToKen(res.getToKen());
            res.setMsg(res.getMessage());
            return  res;
        }
        if(StringUtils.isBlank(receiveId)){
            res.setSuccess(false);
            res.setMsg("Parameter cannot be empty!");
            return res;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            res.setMsg("Failed to get user information!");
            return res;
        }
        int flg = service.updateStatus(receiveId,dto);
        res.setSuccess(true);
        return res;
    }
    /**
     * 根据订货单查询收货单信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getList")
    @ResponseBody
    public GridDataDTO<OD0000TDTO> getList(HttpServletRequest request, HttpSession session,
                                           int page, int rows, String storeCd, String orderId) {
        if(StringUtils.isBlank(storeCd) || StringUtils.isBlank(orderId)){
            log.info(">>>>>>>>>>>>>>>>>>>>> param is null");
            return new GridDataDTO<OD0000TDTO>();
        }
        OD0000TParamDTO param = new OD0000TParamDTO();
        param.setStoreCd(storeCd);
        param.setOrderId(orderId);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return service.getList(param);
    }

    /**
     * 查询验收单头档信息
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get")
    @ResponseBody
    public ReturnDTO get(HttpServletRequest request, HttpSession session, String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        Gson gson = new Gson();
        OD0000TParamDTO param = gson.fromJson(searchJson, OD0000TParamDTO.class);
        if(param == null){
            _return.setMsg("Failed to get request parameters!");
            return _return;
        }
        OD0000TDTO dto = service.getOD0000(param);
        if(dto == null){
            _return.setMsg("No data found");
        }else{
            _return.setO(dto);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    /**
     * 查询验收单商品详情
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/getDetails")
    @ResponseBody
    public GridDataDTO<OD0010TDTO> getDetails(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            return new GridDataDTO<OD0010TDTO>();
        }
        Gson gson = new Gson();
        OD0000TParamDTO param = gson.fromJson(searchJson, OD0000TParamDTO.class);
        return service.getOD0010(param);
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
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
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

}