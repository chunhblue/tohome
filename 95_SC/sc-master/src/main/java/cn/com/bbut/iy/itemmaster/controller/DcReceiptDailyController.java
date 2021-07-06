package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.vendorReceiptDaily.VendorReceiptDailyService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * 门店商品收货日报（Ｆrom 供应商）（含退货）
 *
 * @author ty
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/dcReceiptDaily")
public class DcReceiptDailyController extends BaseAction {

    @Autowired
    private VendorReceiptDailyService vendorReceiptDailyService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_RECEIVING_DAILY_DC";
    private final String EXCEL_EXPORT_NAME = "Store Receiving Daily Report(To DC).xlsx";

    /**
     * 营业日报管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_DC_DAILY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 进入 门店商品收货日报（Ｆrom 供应商）（含退货）", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("dcReceiptDaily/dcReceiptDaily");
        mv.addObject("useMsg", "门店商品收货日报（Ｆrom DC）（含退货）");
        mv.addObject("bsDate", Utils.getFormateDate(ymd));
        mv.addObject("printTime",Utils.getFormateDate(ymd));
        mv.addObject("userName",u.getUserName());
        return mv;
    }

    @RequestMapping("/getUserName")
    @ResponseBody
    public ReturnDTO getUserName(HttpServletRequest request, HttpSession session) {
        User user = this.getUser(session);
        if (user==null) {
            return new ReturnDTO(false,"没有登录!");
        }
        return new ReturnDTO(true,"ok",user);
    }

    @RequestMapping("/search")
    @ResponseBody
    public ReturnDTO search(String jsonStr, HttpServletRequest request, HttpSession session) {
        VendorReceiptDailyParamDTO param = null;
        if (jsonStr!=null&&!StringUtils.isEmpty(jsonStr)) {
            // 实例化查询条件
            Gson gson = new Gson();
            param = gson.fromJson(jsonStr, VendorReceiptDailyParamDTO.class);
        }
        if (param==null) {
            param = new VendorReceiptDailyParamDTO();
        }
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        // 获取DC 收货
        Map<String,Object> result = vendorReceiptDailyService.deleteSearchDcReceipt(param);
        return new ReturnDTO(true,"ok",result);
    }

    @RequestMapping("/getAMList")
    @ResponseBody
    public List<AutoCompleteDTO> getAMList(String v,HttpServletRequest request, HttpSession session) {
        List<AutoCompleteDTO> result = vendorReceiptDailyService.getAMList("1", v);
        return result;
    }

    @RequestMapping("/getPmaList")
    @ResponseBody
    public ReturnDTO getPmaList(HttpServletRequest request, HttpSession session) {
        List<MA0080> result = vendorReceiptDailyService.getPmaList();
        return new ReturnDTO(true,"ok",result);
    }

    @RequestMapping("/getCityList")
    @ResponseBody
    public ReturnDTO getCityList(HttpServletRequest request, HttpSession session) {
        List<MA0020C> result = vendorReceiptDailyService.getCity();
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
    @Permission(codes = { PermissionCode.CODE_SC_DC_DAILY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isEmpty(searchJson)){
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
        exParam.setPCode(PermissionCode.CODE_SC_DC_DAILY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("DCReceiptExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
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
        if(!StringUtils.isEmpty(param.getStoreCd())){
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

    @Permission(codes = { PermissionCode.CODE_SC_DC_DAILY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET,value = "/print")
    public ModelAndView toPrintlist(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        log.debug("User:{} 进入 门店商品收货日报打印画面（Ｆrom 供应商）（含退货）", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("dcReceiptDaily/dcReceiptDailyPrint");
        mv.addObject("useMsg", "门店商品收货日报打印画面（Ｆrom DC）（含退货）");
        mv.addObject("bsDate", Utils.getFormateDate(ymd));
        mv.addObject("printTime",Utils.getFormateDate(ymd) );
        mv.addObject("userName",u.getUserName());
        mv.addObject("searchJson",searchJson);
        return mv;
    }
    @RequestMapping(value = "/print/getprintData")
    @ResponseBody
    public ReturnDTO getprintData(String searchJson, HttpServletRequest request, HttpSession session) {
        VendorReceiptDailyParamDTO param = null;
        if (searchJson!=null&&!StringUtils.isEmpty(searchJson)) {
            // 实例化查询条件
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, VendorReceiptDailyParamDTO.class);
        }
        if (param==null) {
            param = new VendorReceiptDailyParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        // 获取DC 收货
        Map<String,Object> result = vendorReceiptDailyService.deleteGetprintData(param);
        return new ReturnDTO(true,"ok",result);
    }


    @RequestMapping("/getOMList")
    @ResponseBody
    public List<AutoCompleteDTO> getOMList(String v,HttpServletRequest request, HttpSession session) {
        List<AutoCompleteDTO> result = vendorReceiptDailyService.getOMList("1",v);
        return result;
    }

}
