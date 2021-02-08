package cn.com.bbut.iy.itemmaster.controller.importantgoodsale;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.coreItem.coreItemParamDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.importantgoodsale.CoreItemService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.NEW;
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
 * @ClassName CoreItemController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/8/5 11:37
 * @Version 1.0
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/coreItemByhi")

public class CoreItemController extends BaseAction {

    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Autowired
    private CoreItemService service;
    private final String EXCEL_EXPORT_KEY = "EXCEL_CORE_ITEM_BY_DAILY";
    private final String EXCEL_EXPORT_NAME = "Core Item Daily(By Hierarchy)Report.xlsx";

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_IM_SALE_LIST_VIEW })
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入重点商品销售日报", u.getUserId());
        ModelAndView mv = new ModelAndView("importantgoodsale/coreItemByhi");
        mv.addObject("useMsg", "重点商品销售日报(by Hierarchy)");
        mv.addObject("bsDate", new Date());
        return mv;
    }

    @RequestMapping ("/search")
    @ResponseBody
    public ReturnDTO search(String SearchJson,HttpServletRequest request,  HttpSession session) {
        coreItemParamDTO paramDTO = null;
        ReturnDTO _return = new ReturnDTO();
        if (SearchJson != null && !SearchJson.equals("")) {
            Gson gson = new Gson();
            paramDTO = gson.fromJson(SearchJson, coreItemParamDTO.class);

        }
        if (SearchJson == null || SearchJson.equals("")) {
            paramDTO = new coreItemParamDTO();
        }
        Collection<String> stores = getStores(session, paramDTO);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query failed!",null);
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            paramDTO.setStartDate(startDate);
        }
        paramDTO.setStores(stores);
        paramDTO.setLimitStart((paramDTO.getPage() - 1) * paramDTO.getRows());
        Map<String,Object>  _list=service.getData(paramDTO);
        if(_list == null || _list.size() < 1){
            _return.setMsg("No data found");
        }else{
            _return.setO(_list);
            _return.setSuccess(true);
            _return.setMsg("Query succeeded!");
        }
        return _return;
    }

    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_IM_SALE_RE_HEXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson){
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        coreItemParamDTO param = gson.fromJson(searchJson, coreItemParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_IM_SALE_RE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("coreItemByExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }





    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,  coreItemParamDTO param){
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
