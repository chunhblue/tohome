package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.fsInvenoryentry.fsInventoryEntryService;
import cn.com.bbut.iy.itemmaster.service.opreationmanagement.CustOfEntryPlanService;
import cn.com.bbut.iy.itemmaster.service.opreationmanagement.CustOfEntryService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName CustOfEntryController
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/19 11:59
 * @Version 1.0
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/custEntry")
public class CustEntryController extends BaseAction{

    @Autowired
    private CustOfEntryService custOfEntryService;
    @Autowired
    private CustOfEntryPlanService custOfEntryPlanService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private fsInventoryEntryService fsInventoryentryService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_EXPENSE_LIST";
    private final String EXCEL_EXPORT_NAME = "Expense Query List.xlsx";

    /**
     * 导入商品
     */
    @Permission(codes = { PermissionCode.CODE_SC_COST_ENTRY_IMPROT})
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload", produces = "text/html;charset=UTF-8")
    public String fileUpload(HttpServletRequest request, HttpSession session
            ,@RequestParam("fileData") MultipartFile file,@RequestParam("storeCd") String storeCd) {
        return fsInventoryentryService.fileUpload(file,request,session,storeCd);
    }

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_COST_ENTRY_LIST_VIEW})
    public ModelAndView  CustOfEntry(HttpServletRequest request, HttpSession session,
                                       Map<String, ?> model,String enterFlag,String piCdParam) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点结果一览画面", u.getUserId());
        ModelAndView mv = new ModelAndView("costdataentryLd/costPlanEntry");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "费用录入一览画面");
        mv.addObject("enterFlag",enterFlag); // 操作状态
        mv.addObject("piCdParam",piCdParam);
        mv.addObject("typeId", ConstantsAudit.TYPE_CUST_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_CUST_STOCK);
        this.saveToken(request);
        return mv;
    }
    /**
     * 费用录入编辑画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_COST_ENTRY_ADD,
            PermissionCode.CODE_SC_COST_ENTRY_EDIT,
            PermissionCode.CODE_SC_COST_ENTRY_VIEW,
    })
    public ModelAndView stocktakeDataEdit(String enterFlag,String piCdParam,HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入费用录入编辑画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("costdataentryLd/costDataEntry");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("enterFlag", enterFlag);
        mv.addObject("piCdParam", piCdParam);
        mv.addObject("useMsg", "费用录入编辑画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_CUST_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_CUST_STOCK);
        this.saveToken(request);
        return mv;
    }
    @GetMapping("/getData")
    @ResponseBody
    public ReturnDTO getData(String piCd, HttpServletRequest request, HttpSession session) {
        PI0100DTOC pi0100c = custOfEntryService.getData(piCd);
        if (pi0100c==null) {
            return new ReturnDTO(false,"Query result is empty!");
        }
        return new ReturnDTO(true,"Query successful!",pi0100c);
    }
    /**
     * 获得盘点计划中分类数据下的商品数据
     */
    @GetMapping("/getItemInfo")
    @ResponseBody
    public ReturnDTO getItemInfo(String itemCode, String storeCd) {
        StocktakeItemDTOC item =custOfEntryService.getItemInfo(itemCode,storeCd);
        if (item==null) {
            return new ReturnDTO(false,"查询为空!");
        }
        return new ReturnDTO(true,"Query succeeded!",item);
    }

    @PostMapping("/save")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_COST_ENTRY_ADD,
            PermissionCode.CODE_SC_COST_ENTRY_EDIT,
    })
    public ReturnDTO saveData(String record,String data, HttpServletRequest request, HttpSession session) {
        User loginUser = this.getUser(session);
        if (StringUtils.isEmpty(record) || StringUtils.isEmpty(data)) {
            return new ReturnDTO(false,"Parameter cannot be empty!");
        }

        String _record= null;
        String _data= null;
        try {
            _record= URLDecoder.decode(record,"UTF-8");
            _data= URLDecoder.decode(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (_record==null||_data==null) {
            return new ReturnDTO(false,"Parameter exception!");
        }

        Gson gson = new Gson();
        // 明细
        List<StocktakeItemDTOC> stocktakeItemList = gson.fromJson(_record, new TypeToken<List<StocktakeItemDTOC>>(){}.getType());
        // 头档
        PI0100DTOC item = gson.fromJson(_data, PI0100DTOC.class);
        if (item.getPiCd()==null||StringUtils.isEmpty(item.getPiCd())) {
            item.setCreateUserId(loginUser.getUserId());
        } else {
            item.setUpdateUserId(loginUser.getUserId());
        }
        if (stocktakeItemList==null||stocktakeItemList.size()<1) {
            return new ReturnDTO(false,"Parameter exception!");
        }

        PI0100DTOC result = custOfEntryService.insertData(stocktakeItemList,item,request,session);

        if (result!=null) {
            return new ReturnDTO(true,"Data saved successfully！",result);
        }
        return new ReturnDTO(false,"Data saved failed！");
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
    @RequestMapping(value = "/excel")
    @Permission(codes = { PermissionCode.CODE_SC_COST_ENTRY_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        System.out.println(searchJson);
        if(org.apache.commons.lang.StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        PI0100ParamDTOC param = gson.fromJson(searchJson, PI0100ParamDTOC.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_COST_ENTRY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("custQueryExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 费用录入打印画面
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_COST_ENTRY_PRINT,
    })
    public ModelAndView stocktakeProcessPrint(HttpServletRequest request, HttpSession session,String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入费用录入打印一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("costdataentryLd/costPlanPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("printTime", new Date());
        mv.addObject("searchJson",searchJson); // 操作状态
        mv.addObject("useMsg", "费用录入打印画面");
        return mv;
    }

    @RequestMapping(value = "/print/getprintData", method = RequestMethod.POST)
    @Permission(codes = { PermissionCode.CODE_SC_COST_ENTRY_PRINT })
    @ResponseBody
    public ReturnDTO printData(String searchJson, HttpServletRequest request, HttpSession session) {
        if(org.apache.commons.lang.StringUtils.isBlank(searchJson)){
            log.info("打印参数为空");
            return null;
        }
        Gson gson = new Gson();
        PI0100ParamDTOC pi0100Paramc = gson.fromJson(searchJson, PI0100ParamDTOC.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, pi0100Paramc);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"Query Fail!",null);
        }
        pi0100Paramc.setStores(stores);
        pi0100Paramc.setFlg(false);
        List<PI0100DTOC> result = custOfEntryPlanService.getPrintData(pi0100Paramc);
        return new ReturnDTO(true, "ok", result);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,PI0100ParamDTOC param){
        Collection<String> stores = new ArrayList<>();
        // 画面未选择，直接返回所有权限店铺
        if(StringUtils.isEmpty(param.getRegionCd())) {
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
