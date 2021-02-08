package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.SA0050Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.returnsDaily.ReturnsDailyService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 门店商品收货日报（Ｆrom 供应商）（含退货）
 *
 * @author ty
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/returnsDaily")
public class ReturnsDailyController extends BaseAction {

    @Autowired
    private ReturnsDailyService returnsDailyService;

    @Autowired
    private SA0050Service sa0050Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_CUSTOMER_RETURN";
    private final String EXCEL_EXPORT_NAME = "Customer Refund Daily Report.xlsx";

    /**
     * 营业日报管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_RETURNS_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        String checkFlg = "0";
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            checkFlg = "1";
        }
        log.debug("User:{} 进入 每日顾客退货明细表", u.getUserId());
        SA0050 cashier = sa0050Service.getCashier(u.getUserId());
        ModelAndView mv = new ModelAndView("returnsDaily/returnsDaily");
        mv.addObject("useMsg", "每日顾客退货明细表");
        mv.addObject("bsDate", new Date());
        mv.addObject("printTime", new Date());
        mv.addObject("userName",u.getUserName());
        mv.addObject("businessDate",cm9060Service.getValByKey("0000"));
        mv.addObject("checkFlg", checkFlg);
        if (cashier!=null) {
            Ma1000 ma1000 = returnsDailyService.selectByStoreCd(cashier.getStoreCd());
            if (ma1000!=null) {
                mv.addObject("userStoreCd",ma1000.getStoreCd());
                mv.addObject("userStoreName",ma1000.getStoreName());
            }
        }
        return mv;
    }

    @RequestMapping("/search")
    @ResponseBody
    public ReturnDTO search(String jsonStr, HttpServletRequest request, HttpSession session) {
        ReturnsDailyParamDTO param = null;
        if (jsonStr!=null&&!"".equals(jsonStr)) {
            param = new Gson().fromJson(jsonStr, ReturnsDailyParamDTO.class);
        }
        if (param==null) {
            param = new ReturnsDailyParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        List<ReturnsDailyDTO> result = returnsDailyService.search(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_RETURNS_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        ReturnsDailyParamDTO param = gson.fromJson(searchJson, ReturnsDailyParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_RETURNS_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("customerReturnExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,ReturnsDailyParamDTO param){
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
     * 营业日报管理打印画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_RETURNS_LIST_VIEW})
    @RequestMapping(value = "/print")
    public ModelAndView toPrintlistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 每日顾客退货明细打印画面", u.getUserId());
        SA0050 cashier = sa0050Service.getCashier(u.getUserId());
        ModelAndView mv = new ModelAndView("returnsDaily/returnsDailyPrint");
        mv.addObject("useMsg", "每日顾客退货明细打印画面");
        mv.addObject("bsDate", new Date());
        mv.addObject("searchJson", searchJson);
        mv.addObject("printTime", new Date());
        mv.addObject("userName",u.getUserName());
        if (cashier!=null) {
            Ma1000 ma1000 = returnsDailyService.selectByStoreCd(cashier.getStoreCd());
            if (ma1000!=null) {
                mv.addObject("userStoreCd",ma1000.getStoreCd());
                mv.addObject("userStoreName",ma1000.getStoreName());
            }
        }
        return mv;
    }

    @RequestMapping(value = "/print/getprintData")
    @ResponseBody
    public ReturnDTO getPrintData(String jsonStr, HttpServletRequest request, HttpSession session) {
        ReturnsDailyParamDTO param = null;
        if (jsonStr!=null&&!"".equals(jsonStr)) {
            param = new Gson().fromJson(jsonStr, ReturnsDailyParamDTO.class);
        }
        if (param==null) {
            param = new ReturnsDailyParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        List<ReturnsDailyDTO> result = returnsDailyService.search(param);
        return new ReturnDTO(true,"ok",result);
    }
}
