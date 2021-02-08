package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.order.ResultDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.Price;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.SA0070Service;
import cn.com.bbut.iy.itemmaster.service.suspendSale.SuspendSaleService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.StringUtil;
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
import java.util.List;
import java.util.Map;

/**
 * 紧急变价一览
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/suspendSaleDetail")
public class SuspendSaleDetailController extends BaseAction {
    @Autowired
    private SA0070Service sa0070Service;

    @Autowired
    private SuspendSaleService suspendSaleService;

    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_SUSPEND_SALE";
    private final String EXCEL_EXPORT_NAME = "Suspend Sales List.xlsx";

    /**
     * 暂停销售一览画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_SUS_LIST_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toPriceChangeDetail(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 暂停销售一览一览", u.getUserId());
        ModelAndView mv = new ModelAndView("suspendSale/suspendSaleDetail");
        mv.addObject("useMsg", "暂停销售一览画面");
        return mv;
    }

    /**
     * 紧急变价一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getSuspendSaleDetail")
    @ResponseBody
    @Permission(codes = { PermissionCode.CODE_SC_SUS_LIST_LIST_VIEW})
    public GridDataDTO<PriceDetailGridDto> getChangePriceDetail(HttpServletRequest request, HttpSession session,
                                                                PriceDetailParamDto param) {
        if(param == null){
            param = new PriceDetailParamDto();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<PriceDetailGridDto>();
        }
        param.setStores(stores);
        return suspendSaleService.getList(param);
    }

    /**
     * 获取商品名称
     * @param session
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getArticleName")
    public ResultDto getArticleName(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String articleId) {
        ResultDto resultDto = new ResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(articleId)){
            String articleName = sa0070Service.getArticleName(articleId);
            if(StringUtil.isNotBlank(articleName)){
                resultDto.setSuccess(true);
                resultDto.setData(articleName);
            }else{
                resultDto.setMessage("Article name failed to get!");
            }
        }else{
            resultDto.setMessage("Article ID cannot be empty!");
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
    @Permission(codes = { PermissionCode.CODE_SC_SUS_LIST_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        PriceDetailParamDto param = gson.fromJson(searchJson, PriceDetailParamDto.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_SUS_LIST_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("suspendSaleExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,PriceDetailParamDto param){
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
