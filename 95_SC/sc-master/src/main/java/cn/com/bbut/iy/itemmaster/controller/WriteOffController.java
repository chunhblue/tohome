package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.WriteOffMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.writeOff.WriteOffService;
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
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 门店报废日报
 *
 * @author mxy
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/writeOff")
public class WriteOffController extends BaseAction {

    @Autowired
    private WriteOffService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private WriteOffMapper writeOffMapper;

    private final String EXCEL_EXPORT_KEY = "EXCEL_WRITE_OFF_DAILY";
    private final String EXCEL_EXPORT_NAME = "Store Inventory Write Off Daily Report.xlsx";

    /**
     * 门店报废日报一览画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_WRITE_OFF_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 门店报废日报", u.getUserId());
        ModelAndView mv = new ModelAndView("information/writeOff/writeOffList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("author", u.getUserName());
        mv.addObject("bsDate", new Date());
        mv.addObject("useMsg", "门店报废日报一览画面");
        return mv;
    }

    /**
     * 查询销售数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getData1")
    public ReturnDTO getData1(HttpServletRequest request, HttpSession session,
                             String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if (StringUtils.isBlank(searchJson)) {
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        WriteOffParamDTO param = gson.fromJson(searchJson, WriteOffParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if (stores.size() == 0) {
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            _return.setMsg("Query failed!");
            return _return;
        }
        param.setStores(stores);
        param.setFlg(false);
        List<WriteOffDTO> _list = service.deleteGetList1(param);
        if (_list == null || _list.size() < 1) {
            _return.setMsg("No data found");
        } else {
            _return.setO(_list);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    @ResponseBody
    @RequestMapping(value = "/getData")
    public ReturnDTO getData(HttpServletRequest request, HttpSession session,
                             String SearchJson){
        WriteOffParamDTO param=null;
        if (SearchJson!=null && SearchJson!=" ") {
            Gson  gson = new Gson();
            param = gson.fromJson(SearchJson, WriteOffParamDTO.class);
        }
        if (param==null){
            param = new WriteOffParamDTO();
        }
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        Map<String,Object> list = service.deleteGetList(param);
        return new ReturnDTO(true,"ok",list);
    }

    /**
     * 门店报废日报打印画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session, String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 门店报废日报打印画面", u.getUserId());
        ModelAndView mv = new ModelAndView("information/writeOff/writeOffListPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("searchJson", searchJson);
        mv.addObject("author", u.getUserName());
        mv.addObject("bsDate", new Date());
        mv.addObject("useMsg", "门店报废日报打印画面");
        return mv;
    }

    /**
     * 打印查询销售数据
     *
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/print/getprintData")
    public ReturnDTO getPrintData(HttpServletRequest request, HttpSession session,
                                String searchJson){
        WriteOffParamDTO param=null;
        if (searchJson!=null && searchJson!="") {
            Gson  gson = new Gson();
            param = gson.fromJson(searchJson, WriteOffParamDTO.class);
        }
        if (param==null){
            param = new WriteOffParamDTO();
        }
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setFlg(false);
        param.setStores(stores);
        Map<String, Object> list = service.deleteGetList(param);
        return new ReturnDTO(true,"ok",list);
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getOffQty")
    public ReturnDTO getOffQty(HttpServletRequest request, HttpSession session,
                              String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if (StringUtils.isBlank(searchJson)) {
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        Gson gson = new Gson();
        WriteOffParamDTO param = gson.fromJson(searchJson, WriteOffParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if (stores.size() == 0) {
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            _return.setMsg("Query failed!");
            return _return;
        }
        param.setStores(stores);
        WriteOffDTO offDto = service.deleteGetOffQty(param);
        if (offDto == null) {
            _return.setMsg("No data found");
        } else {
            _return.setO(offDto);
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
    @Permission(codes = { PermissionCode.CODE_SC_ZD_VMQ_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取资源数据
        Gson gson = new Gson();
        WriteOffParamDTO param = gson.fromJson(searchJson, WriteOffParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_ZD_VMQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("writeOffDailyExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,WriteOffParamDTO param){
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
