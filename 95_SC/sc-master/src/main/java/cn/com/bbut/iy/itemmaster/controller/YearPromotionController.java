package cn.com.bbut.iy.itemmaster.controller;

/**
 * @ClassName YearPromotionController
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/2 16:29
 * @Version 1.0
 * @Description
 */

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;

import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import cn.com.bbut.iy.itemmaster.dto.yearendpromotion.yearEndPromotionParamDto;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.yearPromotionService.yearEndPromotionService;
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
import java.util.Date;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/yearEndPromotionRede")
public class YearPromotionController extends BaseAction{
    @Autowired
    private yearEndPromotionService serivce;

    @Autowired
    private MRoleStoreService mRoleStoreService;


    private final String EXCEL_EXPORT_KEY = "EXCEL_YEARPROMOTION_DAILY";
    private final String EXCEL_EXPORT_NAME = "Store Year Promotion Report.xlsx";



    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店促销年报", u.getUserId());
        ModelAndView mv = new ModelAndView("year_promotion/yearPromotionReport");
        mv.addObject("useMsg", "门店促销年报");
        mv.addObject("bsDate", new Date());
        mv.addObject("printTime", new Date());
        mv.addObject("userName",u.getUserName());
        return mv;
    }

    @RequestMapping(value ="/search")
    @ResponseBody
    public ReturnDTO searchData(String jsonStr, HttpServletRequest request, HttpSession session){
        yearEndPromotionParamDto param = null;
        if (jsonStr != null && !StringUtils.isEmpty(jsonStr)) {
            Gson gson = new Gson();
            param = gson.fromJson(jsonStr, yearEndPromotionParamDto.class);
        }
        if (param == null) {
            param = new yearEndPromotionParamDto();
        }
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        Map<String,Object> result = serivce.search(param);
        return new ReturnDTO(true,"ok",result);
    }

    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_ZD_VMQ_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取资源数据
        Gson gson = new Gson();
        WriteOffParamDTO param = gson.fromJson(searchJson, WriteOffParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_ZD_VMQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("promotionYearEx", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }
    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,WriteOffParamDTO param){
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
