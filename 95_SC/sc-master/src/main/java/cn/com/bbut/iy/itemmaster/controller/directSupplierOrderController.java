package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010viewDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.od0010.directOd0010Service;
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
 * @ClassName directSupplierOrderController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/16 15:34
 * @Version 1.0
 */

@Slf4j
@Controller
@Secure
@RequestMapping(value = Constants.REQ_HEADER +"/directSupplierOrder")
public class directSupplierOrderController extends BaseAction{
    @Autowired
    private directOd0010Service od0010Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_DIRECT_ORDER";
    private final String EXCEL_EXPORT_NAME = "Direct Store Purchase Order Query.xlsx";


    @Permission(codes = {
            PermissionCode.CODE_SC_DIRECR_ORDER_LIST_VIEW,
    })
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model){
        ModelAndView mv = new ModelAndView("orderdirectsupplierLd/orderdirectsupplier");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_DC);
        mv.addObject("useMsg", "订单查询画面");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/getOrderInformation",method = RequestMethod.GET)
    public GridDataDTO<od0010DTO> getOrderInforOd0010(HttpServletRequest request, HttpSession session,
                                                      String searchJson,int rows,int page){
        od0010ParamDTO param = null;
        if (!org.springframework.util.StringUtils.isEmpty(searchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, od0010ParamDTO.class);
        }
        if (param==null) {
            param = new od0010ParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<od0010DTO>();
        }
        param.setStores(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
//        return od0010Service.getOrderInformation(param);
        return od0010Service.getdirectOrderInformation(param);
    }
    // update by ty end

    @Permission(codes = {
            PermissionCode.CODE_SC_DIRECR_ORDER_PRINT,
    })
    @ResponseBody
    @RequestMapping(value = "/print")
    public ModelAndView orderPrint(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model,String searchJson) {
        Gson gson = new Gson();
        od0010ParamDTO  od0010ParamDTO = gson.fromJson(searchJson, od0010ParamDTO.class);
        User u = this.getUser(session);
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("orderdirectsupplierLd/orderdirectsupplierprint");
        mv.addObject("time", date);
        mv.addObject("userName", u.getUserName());
        mv.addObject("identity", 1);
        mv.addObject("printTime", new Date());
        mv.addObject("dto",od0010ParamDTO);
        mv.addObject("useMsg", "直送供应商订单打印画面");
        return mv;
    }

    @RequestMapping(value = "/getprintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData( HttpServletRequest request, HttpSession session,String searchJson) {
        od0010ParamDTO param = null;
        if (!org.springframework.util.StringUtils.isEmpty(searchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, od0010ParamDTO.class);
        }
        if (param==null) {
            param = new od0010ParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        List<od0010DTO> result = od0010Service.getSupplierPrintData(param);
        return new ReturnDTO(true,"ok",result);
    }

    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_DIRECR_ORDER_EXPORT  })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        od0010ParamDTO param = gson.fromJson(searchJson, od0010ParamDTO.class);
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_DIRECR_ORDER_EXPORT );
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("directSupplierEx", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }


    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,od0010ParamDTO param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
                && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(!org.springframework.util.StringUtils.isEmpty(param.getStoreCd())){
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
    @RequestMapping(value = "/supperOrderView", method = RequestMethod.GET)
    public ModelAndView cdOrderView(HttpServletRequest request, HttpSession session,
                                    Map<String, ?> model, od0010viewDTO od0010){
        User u = this.getUser(session);
        log.debug("User:{} 进入 退仓库申请单详细管理画面", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("orderdirectsupplierLd/ordersupperview");
        //  ReturnDTO view = View(searchJson);
//        mv.addObject("searchJson",searchJson);
        mv.addObject("data",od0010);

        //   mv.addObject("orderDate",date);
        mv.addObject("useMsg", "退仓库申请单详细管理");
        return mv;

    }
    @ResponseBody
    @RequestMapping(value = "/getViewsupperInfor")
    public GridDataDTO<od0010viewDTO> view(HttpServletRequest request, HttpSession session,
                                           Map<String, ?> model, String searchJson,int rows, int page){
        od0010viewDTO param = null;
        if (!org.springframework.util.StringUtils.isEmpty(searchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, od0010viewDTO.class);
        }
        if (param==null) {
            param = new od0010viewDTO();
        }
        ArrayList<od0010viewDTO> od0010ParamDTOS = new ArrayList<>();
        od0010ParamDTOS.add(param);
        GridDataDTO<od0010viewDTO> result = new GridDataDTO<od0010viewDTO>(od0010ParamDTOS,1,1,2);
        return   result;
    }

    /**
     * 检索下拉供应商
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getSupplier")
    public List<AutoCompleteDTO> get(String v){
        return od0010Service.getList(v);
    }
}
