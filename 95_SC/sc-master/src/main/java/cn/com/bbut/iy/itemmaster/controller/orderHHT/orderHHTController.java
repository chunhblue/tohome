package cn.com.bbut.iy.itemmaster.controller.orderHHT;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.AdjustmentDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedParamDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTParamDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.orderHHT.OrderHHTService;
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
 * @ClassName orderHHTController
 * @Description TODO
 * @Author Ldd
 * @Date 2021/5/25 11:38
 * @Version 1.0
 * @Description
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/orderHHTR")
public class orderHHTController extends BaseAction {
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private OrderHHTService service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_HHT_ORDER";
    private final String EXCEL_EXPORT_NAME = "HHT Order List.xlsx";
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                  Map<String, ?> model){

        User u = this.getUser(session);
        log.debug("User:{} 进入 订货失败", u.getUserId());
        //获取业务时间
        String date = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("hhtorder/hhtorder");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("date", date);
        mv.addObject("useMsg", "HHT order画面");
        return mv;
    }
    @RequestMapping(value = "/getdata")
    @ResponseBody
    public GridDataDTO<OrderHHTGridDTO> getFailedList(HttpServletRequest request, HttpSession session,

                                                      int page, int rows, String searchJson) {

        Gson gson = new Gson();
        OrderHHTParamDto param = gson.fromJson(searchJson, OrderHHTParamDto.class);
        if(param == null){
            param = new OrderHHTParamDto();
        }
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return service.getDataList(param);
    }

    @RequestMapping(value = "/export")
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        OrderHHTParamDto param = gson.fromJson(searchJson, OrderHHTParamDto.class);
        if(param == null){
            param = new OrderHHTParamDto();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setStores(stores);
        exParam.setPCode(PermissionCode.CODE_SC_OD_FAILED_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("orderHHTExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
    private Collection<String> getStores(HttpSession session, OrderHHTParamDto param){
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
