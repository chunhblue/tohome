package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackDetailsDTO;
import cn.com.bbut.iy.itemmaster.dto.unpack.UnpackParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.UnpackService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
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
 * 拆包销售
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/unpackSale")
public class UnpackSaleController extends BaseAction {

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private UnpackService unpackService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;
    @Autowired
    private Ma4320Service ma4320Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_UNPACK_SALE";
    private final String EXCEL_EXPORT_NAME = "Unpacking sales List.xlsx";

    /**
     * 拆包销售一览画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_XS_USALE_LIST_VIEW})
    public ModelAndView unpackSaleList(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入拆包销售一览画面", u.getUserId());
        // 判断画面按钮是否显示
        String isHide = cm9060Service.getValByKey("0634");
        isHide = StringUtils.isBlank(isHide) ? "1" : isHide;
        ModelAndView mv = new ModelAndView("unpacksale/unpackSale");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("isHide", isHide);
        mv.addObject("useMsg", "拆包销售一览画面");
        return mv;
    }

    /**
     * 拆包销售管理查看明细画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_XS_USALE_VIEW})
    public ModelAndView view(HttpServletRequest request, HttpSession session,
                                       UnpackParamDTO dto) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 拆包销售管理查看明细画面", u.getUserId());
        // 判断画面按钮是否显示
        String isHide = cm9060Service.getValByKey("0634");
        isHide = StringUtils.isBlank(isHide) ? "1" : isHide;
        ModelAndView mv = new ModelAndView("unpacksale/unpackSaleEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("dto", dto);
        mv.addObject("isHide", isHide);
        mv.addObject("viewSts", "view");
        mv.addObject("useMsg", "拆包销售管理查看明细画面");
        return mv;
    }

    /**
     * 拆包销售管理编辑、新增画面
     *
     * @param request
     * @param session
     * @param flag
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_XS_USALE_EDIT,
            PermissionCode.CODE_SC_XS_USALE_ADD
    })
    public ModelAndView edit(HttpServletRequest request, HttpSession session,
                                       UnpackParamDTO dto, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 拆包销售管理编辑、新增画面", u.getUserId());
        // 判断画面按钮是否显示
        String isHide = cm9060Service.getValByKey("0634");
        isHide = StringUtils.isBlank(isHide) ? "1" : isHide;
        ModelAndView mv = new ModelAndView("unpacksale/unpackSaleEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("dto", dto);
        mv.addObject("viewSts", flag);
        mv.addObject("isHide", isHide);
        mv.addObject("useMsg", "拆包销售管理编辑、新增画面");
        return mv;
    }

    /**
     * 条件查询记录
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getList")
    @Permission(codes = { PermissionCode.CODE_SC_XS_USALE_LIST_VIEW})
    public GridDataDTO<UnpackDTO> getList(HttpServletRequest request, HttpSession session,
                                          int page, int rows, String searchJson) {
        if(this.getUser(session) == null){
            return new GridDataDTO<UnpackDTO>();
        }
        UnpackParamDTO param;
        if(StringUtils.isBlank(searchJson)){
            param = new UnpackParamDTO();
        }else{
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, UnpackParamDTO.class);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<UnpackDTO>();
        }
        User u = this.getUser(session);
       /* int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i == 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setUnpackStartDate(startDate);
        }*/
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return unpackService.getList(param);
    }

    /**
     * 查询记录详情
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/get")
    public ReturnDTO get(HttpServletRequest request, HttpSession session,
                         String searchJson, String flag) {
        ReturnDTO _return = new ReturnDTO();
        if(this.getUser(session) == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        UnpackParamDTO param = gson.fromJson(searchJson, UnpackParamDTO.class);
        UnpackDTO _dto = unpackService.getByKey(param, flag);
        if(_dto == null){
            _return.setMsg("Query failed!");
        }else{
            _return.setO(_dto);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    /**
     * 查询明细记录
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDetails")
    public GridDataDTO<UnpackDetailsDTO> getDetails(HttpServletRequest request, HttpSession session,
                                                    String searchJson, String flag) {
        if(this.getUser(session) == null){
            log.debug("Failed to get user information!");
            return new GridDataDTO<UnpackDetailsDTO>();
        }
        if(StringUtils.isBlank(searchJson)){
            log.debug("获取参数信息失败");
            return new GridDataDTO<UnpackDetailsDTO>();
        }
        Gson gson = new Gson();
        UnpackParamDTO param = gson.fromJson(searchJson, UnpackParamDTO.class);
        return unpackService.getDetails(param, flag);
    }

    /**
     * 新增记录
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/save")
    @Permission(codes = {
            PermissionCode.CODE_SC_XS_USALE_EDIT,
            PermissionCode.CODE_SC_XS_USALE_ADD
    })
    public ReturnDTO add(HttpServletRequest request, HttpSession session,
                         String searchJson, String listJson, String flag) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null || listJson == null){
            _return.setMsg("获取保存数据失败");
            return _return;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        // 参数转换
        Gson gson = new Gson();
        // 详细信息
        List<UnpackDetailsDTO> list = gson.fromJson(listJson, new TypeToken<List<UnpackDetailsDTO>>(){}.getType());
        if(list == null || list.size() == 0){
            _return.setMsg("获取子商品数据失败");
            return _return;
        }
        // 基础信息
        UnpackDTO param = gson.fromJson(searchJson, UnpackDTO.class);
        param.setCommonDTO(dto);
        // 判断后台是否允许操作母货号
        String isHide = cm9060Service.getValByKey("0634");
        if(!"0".equals(isHide)){
            _return.setMsg("The current state cannot be saved!");
            return _return;
        }
        // 执行保存
        String unpackId;
        if("add".equals(flag)){
            unpackId = unpackService.insert(session, request, param, list);
        }else if("edit".equals(flag)){
            unpackId = unpackService.update(param, list);
        }else{
            unpackId = null;
        }
        if(unpackId == null){
            _return.setMsg("Data saved failed!");
        }else{
            _return.setO(unpackId);
            _return.setSuccess(true);
            _return.setMsg("Data saved successfully!");
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
    @Permission(codes = { PermissionCode.CODE_SC_XS_USALE_DEL})
    public ReturnDTO del(HttpServletRequest request, HttpSession session,
                         String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 参数转换
        Gson gson = new Gson();
        // 详细信息
        List<UnpackParamDTO> list = gson.fromJson(searchJson, new TypeToken<List<UnpackParamDTO>>(){}.getType());
        // 判断后台是否允许操作母货号
        String isHide = cm9060Service.getValByKey("0634");
        if(!"0".equals(isHide)){
            _return.setMsg("The current state cannot be deleted!");
            return _return;
        }
        // 执行删除
        int count = unpackService.delete(list);
        if(count == 0){
            _return.setMsg("Failed to delete data!");
        }else{
            _return.setSuccess(true);
            _return.setMsg("Delete data succeeded!");
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
    @Permission(codes = { PermissionCode.CODE_SC_XS_USALE_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        UnpackParamDTO param = gson.fromJson(searchJson, UnpackParamDTO.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }

        User u = this.getUser(session);
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setUserId(u.getUserId());
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_XS_USALE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("unpackExService", ExService.class);
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
    private Collection<String> getStores(HttpSession session,UnpackParamDTO param){
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