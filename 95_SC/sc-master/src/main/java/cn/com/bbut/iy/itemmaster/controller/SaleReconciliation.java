package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetailParam;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CashService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
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
 * 销售对账与财务管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/saleReconciliation")
public class SaleReconciliation extends BaseAction {
    @Autowired
    private CashService cashService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_SALE_RECONCILIATION";
    private final String EXCEL_EXPORT_NAME = "Daily Sales Report(Variance).xlsx";

    /**
     * 销售对账与财务管理画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_SL_CF_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 销售对账与财务管理", u.getUserId());
        ModelAndView mv = new ModelAndView("saleReconciliation/saleReconciliationList");
        mv.addObject("typeId", ConstantsAudit.TYPE_CASHIER_AMOUNT);
        mv.addObject("useMsg", "销售对账与财务管理画面");
        return mv;
    }

    /**
     * 获取金种一览
     * @param session
     * @param model
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCashDetailyList")
    @Permission(codes = { PermissionCode.CODE_SC_SL_CF_LIST_VIEW})
    public GridDataDTO<CashDetail> getCashDetailyList(HttpServletRequest request, HttpSession session,
                                                      Map<String, ?> model, CashDetailParam dto) {
        GridDataDTO<CashDetail> grid = new  GridDataDTO<CashDetail>();
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, dto);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<CashDetail>();
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            dto.setVStartDate(startDate);
        }
        dto.setStores(stores);
        grid.setRows(cashService.getCashDetailyList(dto));
        return grid;
    }

    /**
     * 获取营业员一览  根据用户权限获取
     */
    @RequestMapping(value = "/getStaff")
    @ResponseBody
    public List<AutoCompleteDTO> getStaff(HttpSession session, HttpServletRequest request, HttpServletRequest req,
                                           String v) {
        User user = this.getUser(session);
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        log.debug("抓取店铺信息，user:{}", user.getUserId());
        List<AutoCompleteDTO> dtos = null;
        if(stores!=null&&stores.size()>0){
            dtos = cashService.getStaff(v,stores);
        }
        return dtos;
    }

    /**
     * 获取营业员名称
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserName")
    public ResultDto getUserName(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String userId) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(userId)){
            String userName = cashService.getUserName(userId);
            if(StringUtils.isNotBlank(userName)){
                resultDto.setSuccess(true);
                resultDto.setData(userName);
            }else{
                resultDto.setMessage("Salesperson query is empty！");
            }
        }else{
            resultDto.setMessage("Operator ID cannot be empty！");
        }
        return resultDto;
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
    @Permission(codes = { PermissionCode.CODE_SC_SL_CF_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        CashDetailParam param = gson.fromJson(searchJson, CashDetailParam.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_SL_CF_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("slsfExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,CashDetailParam param){
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
