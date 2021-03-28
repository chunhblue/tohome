package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceHeadResult;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListParam;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListParamDTO;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListResult;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.OD0000Service;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import cn.com.bbut.iy.itemmaster.service.difference.DifferenceService;
import cn.com.bbut.iy.itemmaster.service.difference.DifferenceShoppingOrderService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


/**
 * 发货单与收货单差异查询
 * @author lch
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/shoppingOrderDifference")
public class DifferenceShoppingOrderController extends BaseAction {

    @Autowired
    private DifferenceShoppingOrderService differShopOrderService;

    @Autowired
    private Ma1000Service ma1000Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_DELIVERY_DIFFERENCE";
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
    @Permission(codes = {PermissionCode.CODE_SC_SHOP_DIFFER_VIEW_LIST})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 配送差异查询及调整", u.getUserId());
        ModelAndView mv = new ModelAndView("difference_shopping/shippingOrderDiscrepancy");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配送差异查询及调整画面");
        return mv;
    }

    /**
     * shopping order预览画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {PermissionCode.CODE_SC_SHOP_DIFFER_VIEW})
    @RequestMapping(value = "/toView", method = RequestMethod.GET)
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                               Map<String, ?> model,String order_id) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 配送差异查询及明细预览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("difference_shopping/shippingOrderDiscrepancyEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配送差异查询及明细预览画面");
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

    // 一览页面条件查询
    @ResponseBody
    @RequestMapping(value = "/query",method={RequestMethod.POST,RequestMethod.GET})
    public GridDataDTO<DifferenceListResult> query(HttpServletRequest request, HttpSession session,
                                                   Map<String, ?> model, DifferenceListParamDTO paramDTO,int rows, int page) {
        DifferenceListParam param = new Gson().fromJson(paramDTO.getSearchJson(),DifferenceListParam.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<DifferenceListResult>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return differShopOrderService.getDifferenceList(param);
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
        return new Gson().toJson(differShopOrderService.getHeadQuery(orderId));
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
        DifferenceHeadResult differenceHeadResult = differShopOrderService.getHeadQuery(orgOrderId);
        if(null == differenceHeadResult){
            return "false";
        }else{
            return new Gson().toJson(differenceHeadResult);
        }
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
        return new Gson().toJson(differShopOrderService.selectByOrderId(paramDTO));
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
    @Permission(codes = { PermissionCode.CODE_SC_SHOP_DIFFER_EXPORT })
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
        exParam.setPCode(PermissionCode.CODE_SC_SHOP_DIFFER_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("shopDiffExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
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
