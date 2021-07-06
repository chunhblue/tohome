package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.transferMod.TransferModService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 店间调拨修正
 *
 * @author mxy
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/transferCorrection")
public class TransferModController extends BaseAction {

    @Autowired
    private TransferModService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;

    /**
     * 店间调拨修正编辑画面
     *
     * @param request
     * @param session
     * @param flag
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_TRANSFER_MOD_ADD,
            PermissionCode.CODE_SC_TRANSFER_MOD_EDIT,
            PermissionCode.CODE_SC_TRANSFER_MOD_LIST_VIEW
    })
    public ModelAndView toDetails(HttpServletRequest request, HttpSession session,
                                  Sk0010DTO sk0010, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调拨修正编辑画面", u.getUserId());
        String _flg = "edit".equals(flag) ? flag : "add";
        ModelAndView mv = new ModelAndView("transfers_modify/transfersModEdit");
        mv.addObject("viewSts", _flg);
        mv.addObject("data", sk0010);
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_TRANSFER_CORRECTION);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_TRANSFER_CORRECTION);
        mv.addObject("useMsg", "店间调拨修正编辑画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 店间调拨修正查看画面
     * @param request
     * @param session
     * @param
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_TRANSFER_MOD_VIEW,
            PermissionCode.CODE_SC_TRANSFER_MOD_LIST_VIEW
    })
    public ModelAndView toView(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 店间调拨修正查看画面", u.getUserId());
        ModelAndView mv = new ModelAndView("transfers_modify/transfersModEdit");
        mv.addObject("use", 0);
        mv.addObject("data", sk0010);
        mv.addObject("identity", 1);
        mv.addObject("viewSts", "view");
        mv.addObject("typeId", ConstantsAudit.TYPE_TRANSFER_CORRECTION);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_TRANSFER_CORRECTION);
        mv.addObject("useMsg", "店间调拨修正查看画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 查询头档
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/get")
    public ReturnDTO getInventoryVouchers(HttpServletRequest request, HttpSession session,
                                          String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        SK0010Key sk0010 = gson.fromJson(searchJson, SK0010Key.class);
        if(sk0010 == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 执行查询
        SK0010 _dto = service.getSk0010(sk0010);
        if(_dto == null){
            _return.setMsg("Query failed!");
        }else {
            _return.setO(_dto);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    /**
     * 查询详情
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDetails")
    public GridDataDTO<Sk0020DTO> getDetails(HttpServletRequest request, HttpSession session,
                                             String searchJson) {
        // 转换参数对象
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            return new GridDataDTO<Sk0020DTO>();
        }
        // 执行查询
        return service.getSk0020(sk0020);
    }

    /**
     * 查询传票下拉列表
     */
    @ResponseBody
    @RequestMapping(value = "/getOrgOrder")
    public List<AutoCompleteDTO> getOrgOrder(HttpServletRequest request, HttpSession session,
                                              String storeCd, String type, String v) {
        if(StringUtils.isBlank(storeCd) || StringUtils.isBlank(type)){
            return new ArrayList<AutoCompleteDTO>();
        }
        List<AutoCompleteDTO> _list = service.getOrgOrderList(storeCd, type, v);
        return _list;
    }

    /**
     * 保存传票
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/save")
    public ReturnDTO saveInventoryVouchers(HttpServletRequest request, HttpSession session,
                                       String searchJson, String listJson) {
        ReturnDTO _return = new ReturnDTO();
        if(StringUtils.isBlank(searchJson) || StringUtils.isBlank(listJson)){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        Sk0010DTO sk0010 = gson.fromJson(searchJson, Sk0010DTO.class);
        if(sk0010 == null){
            _return.setMsg("Failed to get subpoena information");/*获取传票信息失败*/
            return _return;
        }
        List<Sk0020DTO> sk0020List = gson.fromJson(listJson, new TypeToken<List<Sk0020DTO>>(){}.getType());
        if(sk0020List == null || sk0020List.size() == 0){
            _return.setMsg("Failed to get the details of the subpoena!");/*获取传票详情失败*/
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        sk0010.setCommonDTO(dto);
        String _id = service.insert(sk0010, sk0020List);
        if(_id == null){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setSuccess(true);
            _return.setO(_id);
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
