package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.*;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.OD0000Service;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import cn.com.bbut.iy.itemmaster.service.difference.DifferenceService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
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
 * 配送差异
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/difference")
public class DifferenceController extends BaseAction {

    @Autowired
    private DifferenceService differenceService;

    @Autowired
    private Cm9010Service cm9010Service;

    @Autowired
    private Ma1000Service ma1000Service;

    @Autowired
    private OD0000Service od0000Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_DIFFERENCE";
    private final String EXCEL_EXPORT_NAME = "Delivery Difference Adjustment List.xlsx";

    /**
     * 配送差异管理画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = {PermissionCode.CODE_SC_DIFFERENCE_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 配送差异查询及调整", u.getUserId());
        ModelAndView mv = new ModelAndView("difference/differencelist");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配送差异查询及调整画面");
         mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_STOCK_ADJUSTMENT);
        return mv;
    }

    // 一览页面条件查询
    @ResponseBody
    @RequestMapping(value = "/query",method={RequestMethod.POST,RequestMethod.GET})
    public GridDataDTO<DifferenceListResult> query(HttpServletRequest request, HttpSession session,
                                                   Map<String, ?> model, DifferenceListParamDTO paramDTO) {
        DifferenceListParam param = new Gson().fromJson(paramDTO.getSearchJson(),DifferenceListParam.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<DifferenceListResult>();
        }
        paramDTO.setStores(stores);
        return differenceService.getDifferenceList(paramDTO);
    }

    /**
     * order新增画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {PermissionCode.CODE_SC_DIFFERENCE_ADD})
    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    public ModelAndView toAdd(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model) {
        User u = this.getUser(session);
        ModelAndView mv = new ModelAndView("difference/differenceedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配送差异查询及明细预览画面");
//        String storeCd = "100011";
        String storeCd = session.getAttribute(Constants.SESSION_STORES).toString();
        mv.addObject("storeCd",storeCd);
        if(!ma1000Service.selectByStoreCd(storeCd).isEmpty()){
            String storeName = ma1000Service.selectByStoreCd(storeCd).get(0).getStoreName();
        }

        mv.addObject("userId",u.getUserId());
        mv.addObject("flag",0);

        //目前生成单据id 16位     真实需要采番
        String num = String.format("%02d",new Random().nextInt(99));
        String order_id = num + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        mv.addObject("bOrderId",order_id);
        return mv;
    }

    /**
     * order管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_DIFFERENCE_EDIT})
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,String order_id) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 配送差异查询及调整细管理画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("difference/differenceedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配送差异查询及调整细管理画面");
//        String storeCd = "100011";
        // 从选择的店铺缓存信息中拿取store_cd
        String storeCd = session.getAttribute(Constants.SESSION_STORES).toString();
        mv.addObject("storeCd",storeCd);
        mv.addObject("userId",u.getUserId());
        mv.addObject("flag",1);
        mv.addObject("bOrderId",order_id);
        return mv;
    }

    /**
     * order预览画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {PermissionCode.CODE_SC_DIFFERENCE_VIEW})
    @RequestMapping(value = "/toView", method = RequestMethod.GET)
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model,String order_id) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 配送差异查询及明细预览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("difference/differenceedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配送差异查询及明细预览画面");
//        String storeCd = "100011";
        // 从选择的店铺缓存信息中拿取store_cd
        String storeCd = session.getAttribute(Constants.SESSION_STORES).toString();
        mv.addObject("storeCd",storeCd);
        if (!ma1000Service.selectByStoreCd(storeCd).isEmpty()){
            String storeName = ma1000Service.selectByStoreCd(storeCd).get(0).getStoreName();
            mv.addObject("storeName",storeName);
        }else {
            mv.addObject("storeName","");
        }
        mv.addObject("userId",u.getUserId());
        mv.addObject("flag",2);//2——View
        mv.addObject("bOrderId",order_id);
        return mv;
    }

    /**
     * 根据编号查询商品信息
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getItemsByOrderId", method = RequestMethod.GET)
    public String getItemDetailByOrderId(HttpServletRequest request, HttpSession session,
                                                                     Map<String, ?> model, DifferenceListParamDTO paramDTO) {
        return new Gson().toJson(differenceService.selectByOrderId(paramDTO));
    }

    /**
     * 请求头档信息
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDetails", method = RequestMethod.GET)
    public String getDetails(HttpServletRequest request, HttpSession session,
                             Map<String, ?> model,String orderId) {
        return new Gson().toJson(differenceService.getHeadQuery(orderId));
    }

    /**
     * 查询原单号信息
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDetailsByOrgOrderId", method = RequestMethod.GET)
    public String getDetailsByOrgOrderId(HttpServletRequest request, HttpSession session,
                              Map<String, ?> model,String articleId, String orgOrderId) {
        DifferenceHeadResult differenceHeadResult = differenceService.getHeadQuery(orgOrderId);
        if(null == differenceHeadResult){
            return "false";
        }else{
            return new Gson().toJson(differenceHeadResult);
        }
    }

    /**
     * 新增订单保存
     */
    @ResponseBody
    @RequestMapping(value = "/addOrder", method = RequestMethod.POST)
    public String addOrder(HttpServletRequest request, HttpSession session, Map<String, ?> model, String param) {
        OD0000 od00000 = new Gson().fromJson(param,OD0000.class);
        Integer n = od0000Service.insertOrder(od00000);
        if(n > 0){
            return "true";
        }else{
            return "false";
        }
    }

    /**
     * 修改订单头档保存
     */
    @ResponseBody
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    public String updateOrder(HttpServletRequest request, HttpSession session, Map<String, ?> model, String param) {
        OD0000 od00000 = new Gson().fromJson(param,OD0000.class);
        Integer m = od0000Service.updateOrder(od00000);//更新头档信息
        if(m > 0){
            return "true";
        }else{
            return "false";
        }
    }

    /**
     * 请求该原单号所有可退货商品信息
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllItems", method = RequestMethod.GET)
    public String getAllItems(HttpServletRequest request, HttpSession session,
                              Map<String, ?> model,String orgOrderId) {
        return new Gson().toJson(differenceService.selectAllItemsBy(orgOrderId));
    }

    /**
     * 判断0d0010是否已存在此单号商品
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isExistItem", method = RequestMethod.GET)
    public String isExistItem(HttpServletRequest request, HttpSession session,
                              Map<String, ?> model,String articleId, String orderId) {
        OD0010 od0010 = new OD0010();
        od0010.setOrderId(orderId);
        od0010.setArticleId(articleId);
        List<DifferenceItemsResult> list = differenceService.getExistItemQuery(od0010);
        if(list.size() == 0 || null == list){
            return "false";
        }else{
            return new Gson().toJson(list.get(0));
        }
    }
    /**
     * 请求该店铺可调整商品详细信息--od0010
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getItemDetailsByItemId", method = RequestMethod.GET)
    public String getItemDetailByItemIds(HttpServletRequest request, HttpSession session,
                                         Map<String, ?> model,String articleId,String orgOrderId) {
        return new Gson().toJson(differenceService.selectItemDetailsByItemId(articleId,orgOrderId).get(0));
    }

    /**
     * 添加调整商品
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addItem", method = RequestMethod.POST)
    public String addItem(HttpServletRequest request, HttpSession session,
                          Map<String, ?> model,String param) {
        OD0010 od0010 = new Gson().fromJson(param,OD0010.class);
        Integer n = differenceService.insertItem(od0010);
        if( n >= 1){
            return "true";
        }else{
            return "false";
        }
    }

    /**
     * 更新调整商品
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateItem", method = RequestMethod.POST)
    public String updateItem(HttpServletRequest request, HttpSession session,
                             Map<String, ?> model,String param) {
        OD0010 od0010 = new Gson().fromJson(param,OD0010.class);
        Integer n = differenceService.updateItemBy(od0010);
        if( n >= 1){
            return "true";
        }else{
            return "false";
        }
    }

    /**
     * 删除退货商品--od0010
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteById", method = RequestMethod.GET)
    public boolean deleteById(HttpServletRequest request, HttpSession session,
                              Map<String, ?> model,String articleId,String orderId) {
        OD0010 od0010 = new OD0010();
        od0010.setOrderId(orderId);
        od0010.setArticleId(articleId);
        Integer n = differenceService.deleteItemBy(od0010);
        if( n >= 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 请求差异理由
     * @param request
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllReasons", method = RequestMethod.GET)
    public String getAllItems(HttpServletRequest request, HttpSession session,
                              Map<String, ?> model) {
        return new Gson().toJson(cm9010Service.selectByCodeType("00505"));
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
    @Permission(codes = { PermissionCode.CODE_SC_DIFFERENCE_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        DifferenceListParam param = new Gson().fromJson(searchJson,DifferenceListParam.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_DIFFERENCE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("diffExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 配送差异查询打印一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_DIFFERENCE_PRINT })
    public ModelAndView stocktakeProcessPrint(HttpServletRequest request, HttpSession session,
                                              Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 配送差异查询打印一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("difference/differencePrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("printTime", new Date());
        mv.addObject("useMsg", "配送差异查询打印画面");
        return mv;
    }

    @RequestMapping(value = "/print/getprintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData(DifferenceListParam differenceListParam, HttpServletRequest request, HttpSession session) {
        List<DifferenceListResult> result = differenceService.getPrintList(differenceListParam);
        return new ReturnDTO(true,"ok",result);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,DifferenceListParam param){
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
