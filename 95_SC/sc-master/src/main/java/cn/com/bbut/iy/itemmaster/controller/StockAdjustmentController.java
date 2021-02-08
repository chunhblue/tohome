package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.SaleDetail;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.SaleHeadParam;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersParamDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.StockAdjustmentService;
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
 * 库存调整
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/stockAdjustment")
public class StockAdjustmentController extends BaseAction {

    @Autowired
    private StockAdjustmentService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_INVENTORY_ADJUSTMENT";
    private final String EXCEL_EXPORT_NAME = "Inventory Adjustment Query.xlsx";

    /**
     * 库存调整管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_ST_ADJUST_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 库存调整", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stock/adjustment/adjustmentlist");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_STOCK_ADJUSTMENT);
        mv.addObject("useMsg", "库存调整画面");
        return mv;
    }

    /**
     * 库存调整详细查看画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_ST_ADJUST_VIEW,
            PermissionCode.CODE_SC_ST_KCCPYL_VIEW
    })
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public ModelAndView toupdateView(HttpServletRequest request, HttpSession session, Sk0010DTO sk0010) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 库存调整详细查看画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stock/adjustment/adjustmentedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", "view");
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_STOCK_ADJUSTMENT);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_STOCK_ADJUSTMENT);
        mv.addObject("useMsg", "库存调整详细查看画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 库存调整详细编辑、新增画面
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_ST_ADJUST_ADD,
            PermissionCode.CODE_SC_ST_ADJUST_EDIT,
            PermissionCode.CODE_SC_ST_KCCPYL_EDIT
    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView toupdateEdit(HttpServletRequest request, HttpSession session,
                                     Sk0010DTO sk0010, String flag) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 库存调整详细编辑、新增画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stock/adjustment/adjustmentedit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("data", sk0010);
        mv.addObject("viewSts", flag);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_STOCK_ADJUSTMENT);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_STOCK_ADJUSTMENT);
        mv.addObject("useMsg", "库存调整详细编辑、新增画面");
        this.saveToken(request);
        return mv;
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
    @Permission(codes = { PermissionCode.CODE_SC_ST_ADJUST_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        InventoryVouchersParamDTO param = gson.fromJson(searchJson, InventoryVouchersParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_ST_ADJUST_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("stockAdjustmentExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
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
                                             int page, int rows, String searchJson) {
        // 转换参数对象
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        if(sk0020 == null){
            return new GridDataDTO<Sk0020DTO>();
        }
        // 执行查询
        return service.getSk0020(sk0020);
    }




    @ResponseBody
    @RequestMapping(value = "/getTotal")
    public ReturnDTO getTotal(String searchJson){
        Gson gson = new Gson();
        Sk0020ParamDTO sk0020 = gson.fromJson(searchJson, Sk0020ParamDTO.class);
        Map<String, Object> result=service.Total(sk0020);
        return new ReturnDTO(true,"ok",result);
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
