package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4350.Ma4350DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.ma4360.Ma4360DetailGridDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4350Service;
import cn.com.bbut.iy.itemmaster.service.Ma4360Service;
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
 * 配方销售单
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/maItemSale")
public class MaItemSaleController extends BaseAction {
    @Autowired
    private Ma4350Service ma4350Service;
    @Autowired
    private Ma4360Service ma4360Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_FORMULA_SALE";
    private final String EXCEL_EXPORT_NAME = "Formula sales order list.xlsx";

    /**
     * 配方销售单一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_XS_ORDER_LIST_VIEW})
    public ModelAndView maItemSaleList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入配方销售单一览画面", u.getUserId());
        ModelAndView mv = new ModelAndView("maitemsale/maItemSale");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "配方销售单一览画面");
        return mv;
    }

    /**
     * 配方销售单头档一览
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<Ma4350DetailGridDto> getList(HttpServletRequest request, HttpSession session,
                                                    Ma4350DetailParamDto param) {
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<Ma4350DetailGridDto>();
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setSaleStartDate(startDate);
        }
        param.setStores(stores);
        return ma4350Service.getList(param);
    }

    @ResponseBody
    @RequestMapping(value = "/getMa4350")
    public AjaxResultDto getMa4350ByVoucherCd(HttpServletRequest request, HttpSession session,
                                 String voucherCd) {
        AjaxResultDto ard = new AjaxResultDto();
        if(StringUtils.isNotBlank(voucherCd)){
            Ma4350DetailGridDto dto = ma4350Service.getMa4350ByVoucherCd(voucherCd);
            if(dto!=null){
                ard.setData(dto);
                ard.setSuccess(true);
            }else{
                ard.setSuccess(false);
                ard.setMessage("Queried data does not exist!");
            }
        }else{
            ard.setSuccess(false);
            ard.setMessage("Document No. cannot be empty!");
        }
        return ard;
    }

    /**
     * 配方销售单详情画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_XS_ORDER_VIEW })
    public ModelAndView view(HttpServletRequest request, HttpSession session,
                                     Map<String, ?> model,String voucherCd) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 配方销售单详细画面", u.getUserId());
        ModelAndView mv = new ModelAndView("maitemsale/maItemSaleEdit");
        mv.addObject("voucherCd", voucherCd);
        mv.addObject("useMsg", "配方销售单详细画面");
        return mv;
    }

    /**
     * 配方销售单商品详细一览
     * @param request
     * @param session
     * @param voucherCd 单据号
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDetailList")
    public GridDataDTO<Ma4360DetailGridDto> getDetailList(HttpServletRequest request, HttpSession session,
                                                          String voucherCd) {
        GridDataDTO<Ma4360DetailGridDto> result = new GridDataDTO<>();
        if(StringUtils.isNotBlank(voucherCd)){
            List<Ma4360DetailGridDto> list = ma4360Service.getList(voucherCd);
            if(list!=null&&list.size()>0)
            result.setRows(list);
        }
        return result;
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
    @Permission(codes = { PermissionCode.CODE_SC_XS_ORDER_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        Ma4350DetailParamDto dto = gson.fromJson(searchJson,Ma4350DetailParamDto.class);
        Collection<String> stores = getStores(session,dto);
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
        exParam.setPCode(PermissionCode.CODE_SC_XS_ORDER_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("maItemExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,Ma4350DetailParamDto param){
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
