package cn.com.bbut.iy.itemmaster.controller.hhreport;
import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportParamDto;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.hhtReportService.hhtReportService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.com.bbut.iy.itemmaster.util.Utils;
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
 * @ClassName HhReportController
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/17 16:04
 * @Version 1.0
 * @Description
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/hhtReport")
public class HhReportController extends BaseAction {
    @Autowired
    private MRoleStoreService mRoleStoreService;


    @Autowired
    private hhtReportService service;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma4320Service ma4320Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_PO_STATUS_DAILY_REPORT";
    private final String EXCEL_EXPORT_NAME = "PO Status Daily Report.xlsx";


    @Permission(codes = { PermissionCode.CODE_SC_HHT_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toSummyList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);

        User u = this.getUser(session);
        log.debug("User:{} 进入重点商品销售日报", u.getUserId());
        ModelAndView mv = new ModelAndView("hhreportSummy/hhreportSummy");
        mv.addObject("useMsg", "HHT-Summy-Report");
        mv.addObject("bsDate", Utils.getFormateDate(ymd));
        return mv;
    }

    @RequestMapping(value ="/search",method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO getClassifiedSaleReportContent(String SearchJson, HttpServletRequest request, HttpSession session){
        log.info(">>>>>>>>>>>>>>>>>>>>> 开始");
        hhtReportParamDto param = null;
        if (SearchJson==null && StringUtils.isEmpty(SearchJson)){
            log.info(">>>>>>>>>>>>>>>>>>>>> SearchJson为空");
        }
        if (SearchJson != null && !StringUtils.isEmpty(SearchJson)) {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson, hhtReportParamDto.class);
        }
        if (param == null) {
            param = new hhtReportParamDto();
        }
        User u = this.getUser(session);
        /*int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i == 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setStartDate(startDate);
        }*/
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        param.setBusinessDate(cm9060Service.getValByKey("0000"));
        param.setLimitStart((param.getPage() - 1)*param.getRows());
        param.setStores(stores);
        Map<String, Object> list = service.getList(param);
        Object data = list.get("data");
        if (data==null){
            log.info(">>>>>>>>>>>>>>>>>>>>> get data is null");
        }
        return new ReturnDTO(true,"ok",list);
    }

    private Collection<String> getStores(HttpSession session,hhtReportParamDto param){
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



    @RequestMapping(value ="/export")
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        hhtReportParamDto param = gson.fromJson(searchJson, hhtReportParamDto.class);
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
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("postatusReportExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }





}
