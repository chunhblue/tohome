package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureDTO;
import cn.com.bbut.iy.itemmaster.dto.expenditure.ExpenditureParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.expenditure.ExpenditureService;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 经费登录
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/fundEntry")
public class FundEntryController extends BaseAction {

    @Autowired
    private ExpenditureService service;
    @Autowired
    private Ma4320Service ma4320Service;

    /**
     * 经费新增画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_JF_QUERY_ADD,
            PermissionCode.CODE_SC_JF_ENTRY_ADD
    })
    public ModelAndView fundEntry(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入经费新增画面", u.getUserId());
        ModelAndView mv = new ModelAndView("fundentry/fundEntryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", new ExpenditureParamDTO());
        mv.addObject("viewSts", "add");
        mv.addObject("useMsg", "经费新增画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_EXPENDITURE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_EXPENDITURE);
        this.saveToken(request);
        return mv;
    }

    /**
     * 经费详情查看画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_JF_QUERY_VIEW})
    public ModelAndView fundEntryView(HttpServletRequest request, HttpSession session,
                                      ExpenditureParamDTO dto) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 经费详情查看画面", u.getUserId());
        ModelAndView mv = new ModelAndView("fundentry/fundEntryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", dto);
        mv.addObject("viewSts", "view");
        mv.addObject("useMsg", "经费详情查看画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_EXPENDITURE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_EXPENDITURE);
        this.saveToken(request);
        return mv;
    }

    /**
     * 经费详情编辑画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_JF_QUERY_EDIT,
    })
    public ModelAndView fundEntryEdit(HttpServletRequest request, HttpSession session,
                                      ExpenditureParamDTO dto) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 经费详情编辑画面", u.getUserId());
        ModelAndView mv = new ModelAndView("fundentry/fundEntryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", dto);
        mv.addObject("viewSts", "edit");
        mv.addObject("useMsg", "经费详情编辑画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_EXPENDITURE);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_EXPENDITURE);
        this.saveToken(request);
        return mv;
    }

    /**
     * 主键查询记录
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getData")
    public ReturnDTO getData(HttpServletRequest request, HttpSession session,
                             String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(this.getUser(session) == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 参数转换
        Gson gson = new Gson();
        ExpenditureParamDTO param = gson.fromJson(searchJson, ExpenditureParamDTO.class);
        // 查询数据
        ExpenditureDTO _dto = service.getByKey(param);
        if(_dto == null){
            _return.setMsg("Failed to query record information!");
        }else{
            _return.setO(_dto);
            _return.setSuccess(true);
            _return.setMsg("Query record information succeeded!");
        }
        return _return;
    }

    /**
     * 检查编号是否存在
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/check")
    public ReturnDTO check(HttpServletRequest request, HttpSession session,
                         String voucherNo) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(voucherNo)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 执行保存
        int count = service.getByVoucherNo(voucherNo);
        if(count > 0){
            _return.setMsg("The number already exists!");
        }else{
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 新增记录
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/add")
    @Permission(codes = {
            PermissionCode.CODE_SC_JF_QUERY_ADD,
            PermissionCode.CODE_SC_JF_ENTRY_ADD,
    })
    public ReturnDTO add(HttpServletRequest request, HttpSession session,
                         String searchJson,String fileDetailJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 转换、设置参数
        Gson gson = new Gson();
        ExpenditureDTO param = gson.fromJson(searchJson, ExpenditureDTO.class);
        param.setCreateUser(dto.getCreateUserId());
        param.setCreateYmd(dto.getCreateYmd());
        param.setCreateHms(dto.getCreateHms());
        param.setFileDetailJson(fileDetailJson);
        // 执行保存
        try {
            int count = service.insert(param);
            _return.setSuccess(true);
            _return.setMsg("Data saved successfully!");
        }catch (Exception e){
            _return.setSuccess(false);
            _return.setMsg("Data saved failed!");
            e.printStackTrace();
        }
        return _return;
    }

    /**
     * 更新记录
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/update")
    @Permission(codes = {
            PermissionCode.CODE_SC_JF_QUERY_EDIT,
    })
    public ReturnDTO modify(HttpServletRequest request, HttpSession session,
                            String searchJson,String fileDetailJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 转换、设置参数
        Gson gson = new Gson();
        ExpenditureDTO param = gson.fromJson(searchJson, ExpenditureDTO.class);
        param.setUpdateUser(dto.getUpdateUserId());
        param.setUpdateYmd(dto.getUpdateYmd());
        param.setUpdateHms(dto.getUpdateHms());
        param.setFileDetailJson(fileDetailJson);
        // 执行更新
        try {
            int count = service.update(param);
            _return.setSuccess(true);
            _return.setMsg("Update data succeeded!");
        }catch (Exception e){
            _return.setSuccess(false);
            _return.setMsg("Failed to update data!");
        }
        return _return;
    }

    /**
     * 删除记录
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/del")
    @Permission(codes = { PermissionCode.CODE_SC_JF_QUERY_DEL})
    public ReturnDTO del(HttpServletRequest request, HttpSession session,
                             String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(this.getUser(session) == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 参数转换
        Gson gson = new Gson();
        List<ExpenditureParamDTO> _list = gson.fromJson(searchJson, new TypeToken<List<ExpenditureParamDTO>>(){}.getType());
        // 执行删除
        int count = service.delete(_list);
        if(count == 0){
            _return.setMsg("Failed to delete data!");
        }else{
            _return.setSuccess(true);
            _return.setMsg("Delete data succeeded!");
        }
        return _return;
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

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getExpenditureList")
    public List<AutoCompleteDTO> getCashierList(String v,@RequestParam("accDate") String accDate,@RequestParam("storeCd") String storeCd) {
        return service.getExpenditureList(v,accDate,storeCd);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getFundEntryData")
    public ReturnDTO getFundEntryData(String voucherNo,String storeCd,String accDate){
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(voucherNo)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        ExpenditureDTO list = service.getFundEntryData(voucherNo,storeCd,accDate);
        if(list == null){
            _return.setMsg("Get additional or offsetClaim fail!");
        }else{
            _return.setO(list);
            _return.setSuccess(true);
        }
        return _return;
    }

    /**
     * 通过voucherNo获取description
     * @param voucherNo
     * @param storeCd
     * @param accDate
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDescription")
    public ReturnDTO getDescription(String voucherNo,String storeCd,String accDate){
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(voucherNo) && StringUtils.isBlank(storeCd) && StringUtils.isBlank(accDate)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        ExpenditureDTO list = service.getDescription(voucherNo,storeCd,accDate);
        if(list == null){
            _return.setMsg("Get description information fail!");
        }else{
            _return.setO(list);
            _return.setSuccess(true);
        }
        return _return;
    }
}
