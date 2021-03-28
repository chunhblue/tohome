package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.dailysalereferma0020.DaySaleReportService;
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
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName DaySalereportController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/25 10:01
 * @Version 1.0
 */
@Controller
@Secure
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER +"/sellDayReport")
public class DaySalereportLdController extends BaseAction {

    @Autowired
    private DaySaleReportService daySaleReportService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_DAILY_SALES_REPORT";
    private final String EXCEL_EXPORT_NAME = "Store Sales Daily Report.xlsx";

    @Permission(codes = { PermissionCode.CODE_SC_STORE_DAY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView sellDayReport(HttpServletRequest request, HttpSession session,
                                      Map<String, ?> model){
        ModelAndView mv = new ModelAndView("DailySalesReportLd/dailysalereport");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("bsDate", new Date());
        mv.addObject("useMsg", "日销售报表");
        return mv;

    }

    @RequestMapping( value = "/getUserName",method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO getUserName(HttpServletRequest request, HttpSession session) {
        User user = this.getUser(session);
        if (user==null) {
            return new ReturnDTO(false,"没有登录!");
        }
        return new ReturnDTO(true,"ok",user);
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO getReportContent(String SearchJson, HttpServletRequest request, HttpSession session){
        DaySaleReportParamDTO param = null;
        if (SearchJson != null && !StringUtils.isEmpty(SearchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson, DaySaleReportParamDTO.class);
        }
        if (param == null) {
            param = new DaySaleReportParamDTO();
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setEffectiveStartDate(startDate);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        param.setStores(stores);
        Map<String, Object> result = daySaleReportService.search(param);
        return new ReturnDTO(true,"ok",result);
    }

    @RequestMapping(value ="/getCityList",method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO getCityList(HttpServletRequest request, HttpSession session) {
        List<MA0020C> result = daySaleReportService.getCity();
        return new ReturnDTO(true,"ok",result);
    }

    @RequestMapping(value ="/getAMList")
    @ResponseBody
    public List<AutoCompleteDTO> getAMList(HttpServletRequest request, HttpSession session,String v) {
        List<AutoCompleteDTO> result = daySaleReportService.getAMList(v);
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
    @Permission(codes = { PermissionCode.CODE_SC_STORE_DAY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isEmpty(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        DaySaleReportParamDTO param = gson.fromJson(searchJson, DaySaleReportParamDTO.class);

        // 获取当前角色店铺权限
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
        exParam.setPCode(PermissionCode.CODE_SC_STORE_DAY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("sellDayExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,DaySaleReportParamDTO param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd()) && StringUtils.isEmpty(param.getCityCd())
            && StringUtils.isEmpty(param.getDistrictCd()) && StringUtils.isEmpty(param.getStoreCd())){
            stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
            return stores;
        }
        // 画面选择完成，返回已选择店铺
        if(!StringUtils.isEmpty(param.getStoreCd())){
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
