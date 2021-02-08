package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceDataDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceEntryDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceItemsDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.cm9010.Cm9010Service;
import cn.com.bbut.iy.itemmaster.service.invoiceEntry.InvoiceEntryService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 门店向顾客开发票
 *
 * @author ty
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/invoiceEntry",produces ={"application/json;charset=UTF-8"})
public class InvoiceEntryController extends BaseAction {

    @Autowired
    private InvoiceEntryService invoiceEntryService;

    @Autowired
    private Cm9010Service cm9010Service;

    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_INVOICE_LIST";
    private final String EXCEL_EXPORT_NAME = "Invoice Query.xlsx";

    /**
     * 门店向顾客开发票一览画面
     */
    @Permission(codes = { PermissionCode.CODE_SC_INVOICE_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView invoiceEntryList(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店向顾客开发票一览画面", u.getUserId());
        ModelAndView mv = new ModelAndView("invoiceEntry/invoiceEntryList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "门店向顾客开发票一览画面");
        return mv;
    }

    @RequestMapping("/edit")
    @Permission(codes = {
            PermissionCode.CODE_SC_INVOICE_ADD,
            PermissionCode.CODE_SC_INVOICE_VIEW,
    })
    public ModelAndView invoiceEntryEdit(String enterFlag,String accId, String storeNo, HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入门店向顾客开发票详细画面", u.getUserId());
        ModelAndView mv = new ModelAndView("invoiceEntry/invoiceEntryEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("enterFlag", enterFlag);
        mv.addObject("accId", accId);
        mv.addObject("storeNo", storeNo);
        mv.addObject("useMsg", "门店向顾客开发票详细画面");
        return mv;
    }

    /**
     * 修改发票的状态
     */
    @GetMapping("/getStatus")
    @ResponseBody
    public ReturnDTO getStatus(HttpServletRequest request, HttpSession session) {
        List<Cm9010> result = cm9010Service.selectByCodeType("00540");

        if (result==null||result.size()<1) {
            return new ReturnDTO(false,"Query failed!");
        }

        return new ReturnDTO(true,"Query succed!",result);
    }

    /**
     * 修改发票的状态
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_INVOICE_ISSUE,
    })
    public ReturnDTO updateStatus(String accId,String storeNo, HttpServletRequest request, HttpSession session) {
        User user = this.getUser(session);
        if (user==null) {
            return new ReturnDTO(false,"Please login first!");
        }
        return invoiceEntryService.updateStatus(accId,storeNo,user,request,session);
    }

    /**
     * 查询小票详细信息
     * @return
     */
    @GetMapping("/getInvoiceByReceiptNo")
    @ResponseBody
    public GridDataDTO<InvoiceEntryDTO> getInvoiceByReceiptNo(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        return invoiceEntryService.getInvoiceByReceiptNo(searchJson,page,rows);
    }

    /**
     * 查询小票详细信息
     * @return
     */
    @GetMapping("/getData")
    @ResponseBody
    public ReturnDTO getData(String accId, String storeNo, HttpServletRequest request, HttpSession session) {
        return invoiceEntryService.getData(accId,storeNo);
    }

    /**
     * 查询小票一览
     */
    @GetMapping("/searchInvoiceList")
    @ResponseBody
    public GridDataDTO<InvoiceDataDTO> searchInvoiceList(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        InvoiceEntryDTO invoiceEntryParam = null;
        if (searchJson == null || StringUtils.isEmpty(searchJson)) {
            invoiceEntryParam = new InvoiceEntryDTO();
        } else {
            Gson gson = new Gson();
            invoiceEntryParam = gson.fromJson(searchJson, InvoiceEntryDTO.class);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, invoiceEntryParam);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<InvoiceDataDTO>();
        }
        User u = this.getUser(session);
        int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            invoiceEntryParam.setStartDate(startDate);
        }
        invoiceEntryParam.setStores(stores);
        invoiceEntryParam.setPage(page);
        invoiceEntryParam.setRows(rows);
        invoiceEntryParam.setLimitStart((page - 1) * rows);
        return invoiceEntryService.searchInvoiceList(invoiceEntryParam,page,rows);
    }

    /**
     * 查询小票的消费头档
     */
    @GetMapping("/searchInvoice")
    @ResponseBody
    public GridDataDTO<InvoiceEntryDTO> searchInvoice(String searchJson, int page, int rows,HttpServletRequest request, HttpSession session) {
        return invoiceEntryService.searchInvoice(searchJson,page,rows);
    }

    /**
     * 查询小票的商品销售明细档
     */
    @GetMapping("/searchInvoiceItem")
    @ResponseBody
    public GridDataDTO<InvoiceItemsDTO> searchInvoiceItem(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        return invoiceEntryService.searchInvoiceItem(searchJson,page,rows);
    }

    /**
     * 查询小票的商品销售明细档
     */
    @PostMapping("/save")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_INVOICE_ADD,
            PermissionCode.CODE_SC_INVOICE_VIEW,
    })
    public ReturnDTO save(String record,HttpServletRequest request, HttpSession session) {
        User user = this.getUser(session);
        if (user==null) {
            return new ReturnDTO(false,"Please login first!");
        }
        return invoiceEntryService.insertInvoice(record,user,request,session);
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
    @Permission(codes = { PermissionCode.CODE_SC_INVOICE_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isEmpty(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        InvoiceEntryDTO param = gson.fromJson(searchJson, InvoiceEntryDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_INVOICE_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("invoiceListExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,InvoiceEntryDTO param){
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
