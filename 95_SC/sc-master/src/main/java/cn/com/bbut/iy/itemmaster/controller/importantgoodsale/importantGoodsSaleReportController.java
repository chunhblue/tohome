package cn.com.bbut.iy.itemmaster.controller.importantgoodsale;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.importantgoodsale.importantGoodsSaleReportService;
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
 * @ClassName GoodsSaleReportController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/30 10:29
 * @Version 1.0
 */

@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/importantGoodsSaleReport")
public class importantGoodsSaleReportController extends BaseAction {

    @Autowired
    private importantGoodsSaleReportService goodsSaleReportService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_CORE_ITEM_DAILY";
    private final String EXCEL_EXPORT_NAME = "Core Item Daily Report.xlsx";

    @Permission(codes = { PermissionCode.CODE_SC_IM_SALE_RE_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
    log.debug("User:{} 进入商品销售日报", u.getUserId());
        ModelAndView mv = new ModelAndView("importantgoodsale/importantgoodsalereport");
        mv.addObject("useMsg", "商品销售日报");
        mv.addObject("bsDate", new Date());
//        mv.addObject("printTime", new Date());
//        mv.addObject("storeName", "117007 胡志明店");
        return mv;
    }


    @RequestMapping ("/getUserName")
    @ResponseBody
    public ReturnDTO getUserName(HttpServletRequest request,  HttpSession session){
        User user = this.getUser(session);
        if (user==null) {
            return  new ReturnDTO(false,"没有登陆");
        }
        return new ReturnDTO(true,"登陆成功",user);
    }

    @RequestMapping("/getPamList")
    @ResponseBody
    public ReturnDTO getPamList(HttpServletRequest request,  HttpSession session){
        List<MA0080> pamList = goodsSaleReportService.getPamList();
        return  new ReturnDTO(true,"ok",pamList);

    }

    @RequestMapping(value ="/getAMList",method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO getAMlist(HttpServletRequest request,  HttpSession session){

        List<Ma1000> aMlist = goodsSaleReportService.getAMlist();
        return  new ReturnDTO(true,"ok",aMlist);
    }

    @RequestMapping(value ="/getCityList",method = RequestMethod.POST)
    @ResponseBody
    public  ReturnDTO getCityList(HttpServletRequest request,  HttpSession session){
        List<MA0020C> clityList = goodsSaleReportService.getClityList();
        return  new ReturnDTO(true,"ok",clityList);
    }

    @RequestMapping(value ="/search",method = RequestMethod.POST)
    @ResponseBody
    public  ReturnDTO getGoodSaleReportContent(String SearchJson,HttpServletRequest request,  HttpSession session){
        importantgoodSaleReportParamDTO param = null;
        if (SearchJson!=null && SearchJson!="") {
            Gson gson = new Gson();
            param = gson.fromJson(SearchJson, importantgoodSaleReportParamDTO.class);

        }
        if (param==null){
            param = new importantgoodSaleReportParamDTO();
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
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
            param.setStartDate(startDate);
        }
        param.setStores(stores);
        param.setLimitStart((param.getPage() - 1) * param.getRows());
        Map<String,Object> goodSaleReportContent = goodsSaleReportService.getGoodSaleReportContent(param);
        return new ReturnDTO(true,"ok",goodSaleReportContent);

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
    @Permission(codes = { PermissionCode.CODE_SC_IM_SALE_RE_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        importantgoodSaleReportParamDTO param = gson.fromJson(searchJson, importantgoodSaleReportParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_IM_SALE_RE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("coreItemExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,importantgoodSaleReportParamDTO param){
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
