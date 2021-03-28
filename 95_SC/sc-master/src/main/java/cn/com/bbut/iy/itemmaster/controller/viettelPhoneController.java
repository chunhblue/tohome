package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.viettelParamPhone.ma8407Paramdto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.viettelPhoneService.ViettelPhoneService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
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
import java.util.stream.Collector;

/**
 * @ClassName viettelPhoneController
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/18 14:34
 * @Version 1.0
 * @Description
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/viettelPhoneReport")
public class viettelPhoneController extends BaseAction{
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    @Autowired
    private ViettelPhoneService  viettelPhoneService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_VIETTEL_PHONE_CHECK_REPORT";
    private final String EXCEL_EXPORT_NAME = "Viettel Phone Check Report.xlsx";




    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店ViettelPhoneReport", u.getUserId());

        String businessDate = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("viettelPhoneReport/viettelPhoneReport");
        mv.addObject("useMsg", "viettelPhoneReport");
        mv.addObject("bsDate", new Date());
        mv.addObject("printTime", new Date());
        mv.addObject("userName",u.getUserName());
        mv.addObject("businessDate",businessDate);
        return mv;
    }



    @RequestMapping("/search")
    @ResponseBody
    public ReturnDTO search(String jsonStr, HttpServletRequest request, HttpSession session) {
        ma8407Paramdto param = null;
        if (jsonStr!=null&&!StringUtils.isEmpty(jsonStr)) {
            // 实例化查询条件
            Gson gson = new Gson();
            param = gson.fromJson(jsonStr, ma8407Paramdto.class);
        }
        if (param==null) {
            param = new ma8407Paramdto();
        }
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        // 获取当前角色店铺权限
        Collection<RoleStoreDTO> stores = getStores(session,request, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setStores(stores);
        Map<String,Object> result = viettelPhoneService.searchData(param);
        return new ReturnDTO(true,"ok",result);
    }


    @RequestMapping(value = "/export")
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(org.apache.commons.lang.StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        ma8407Paramdto param = gson.fromJson(searchJson, ma8407Paramdto.class);
        // 获取当前角色店铺权限
        Collection<RoleStoreDTO> stores = getStores(session,request, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        param.setStores(stores);

        User u = this.getUser(session);
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setUserId(u.getUserId());
        exParam.setStores1(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_STORE_SALE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("viettelPhoneExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<RoleStoreDTO> getStores(HttpSession session,HttpServletRequest request,ma8407Paramdto param){
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        List<RoleStoreDTO> stores = new ArrayList<>();
        // 只选择了一部分参数，生成查询参数，后台查询判断
        stores = mRoleStoreService.getStoreByRole(roleIds);
         return stores;
    }
}

