package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakePlanService;
import cn.com.bbut.iy.itemmaster.service.stocktakeProcess.StocktakeProcessService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * 盘点处理
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/stocktakeProcess")
public class StockProcessController extends BaseAction {

    @Autowired
    private StocktakeProcessService stocktakeProcessService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private StocktakePlanService stocktakePlanService;
    private SXSSFWorkbook wb;

    private final String EXCEL_EXPORT_KEY = "EXCEL_STOCKTAKE_VARIANCE";
    private final String EXCEL_EXPORT_NAME = "Stocktake Variance Report.xlsx";

    @RequestMapping("/queryExport")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_SETTING_EXPORT_ITEM,
    })
    public ReturnDTO Export(String piCd, String piDate, String storeCd,HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        ReturnDTO _return = null;
        //获取用户权限
        // 获取session中的操作用户ID
        User _sessionUser = this.getUser(session);
        if(_sessionUser == null){
             log.error("登录失效,请重新登陆");
            _return = new ReturnDTO(false, "Session time out!","001");
            return _return;
        }

        //返回商品的HSSFWorkbook对象
        wb = stocktakeProcessService.getExportHSSFWorkbook(piCd, piDate, storeCd);
        if(wb == null) {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }

        _return = new ReturnDTO(true, "Succeeded!",1);
        return _return;
    }

    /**
     * 导出excle
     */
    @RequestMapping("/download")
    public void export(String store,HttpServletRequest request,HttpServletResponse response) {
        try {
            //下载
            String fileName = "Physical Inventory Result - "+store+".xlsx";

            fileName = URLEncoder.encode(fileName,"utf-8");
            fileName = new String(fileName.getBytes("gbk"),"iso8859-1");

            response.setContentType("application/x-msdownload");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);

            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            wb.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/search")
    @ResponseBody
    public GridDataDTO<StocktakeProcessDTO> search(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        if (searchJson==null|| StringUtils.isEmpty(searchJson)) {
            return null;
        }
        // 实例化查询条件
        Gson gson = new Gson();
        PI0100ParamDTO pi0100 = gson.fromJson(searchJson, PI0100ParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, pi0100);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<StocktakeProcessDTO>();
        }
        // 设置 超过 End Time 没有 submit 提交的 计划过期
        stocktakePlanService.updateStocktakingPlanExpired();
        pi0100.setStores(stores);
        pi0100.setPage(page);
        pi0100.setRows(rows);
        pi0100.setLimitStart((page - 1)*rows);
        return stocktakeProcessService.search(pi0100);
    }

    /**
     * 获得正常盘点的商品
     */
    @RequestMapping("/getTableData1")
    @ResponseBody
    public GridDataDTO<StocktakeProcessItemsDTO> getTableData1(String piCd, String piDate,
                                                               String storeCd,String searchVal,
                                                               Integer startQty,Integer endQty,
                                                               Integer startAmt,Integer endAmt,
                                                               String sidx, String sord,
                                                               int page, int rows, HttpServletRequest request, HttpSession session) {
        return stocktakeProcessService.getTableData1(piCd,piDate,storeCd,searchVal,startQty,endQty,startAmt,endAmt,sidx,sord,page,rows);
    }

    /**
     * 获得未盘到的商品
     */
    @RequestMapping("/getTableData2")
    @ResponseBody
    public GridDataDTO<StocktakeProcessItemsDTO> getTableData2(String piCd, String piDate,
                                                               String storeCd,String searchVal,
                                                               Integer startQty,Integer endQty,
                                                               Integer startAmt,Integer endAmt,
                                                               String sidx, String sord,
                                                               int page, int rows, HttpServletRequest request, HttpSession session) {
        return stocktakeProcessService.getTableData2(piCd,piDate,storeCd,searchVal,startQty,endQty,startAmt,endAmt,sidx,sord,page,rows);
    }

    /**
     * 获得账面上无库存的商品
     */
    @RequestMapping("/getTableData3")
    @ResponseBody
    public GridDataDTO<StocktakeProcessItemsDTO> getTableData3(String piCd, String piDate,
                                                               String storeCd,String searchVal,
                                                               Integer startQty,Integer endQty,
                                                               Integer startAmt,Integer endAmt,
                                                               String sidx, String sord,
                                                               int page, int rows, HttpServletRequest request, HttpSession session) {
        return stocktakeProcessService.getTableData3(piCd,piDate,storeCd,searchVal,startQty,endQty,startAmt,endAmt,sidx,sord,page,rows);
    }

    @RequestMapping("/getData")
    @ResponseBody
    public ReturnDTO getData(String piCd,String piDate, HttpServletRequest request, HttpSession session) {
        PI0100DTO pi0110 = stocktakeProcessService.getData(piCd,piDate);
        if (pi0110==null) {
            // 查询失败
            return new ReturnDTO(false,"error");
        }
        return new ReturnDTO(true,"ok",pi0110);
    }

    /**
     * 盘点处理一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PD_PROCESS_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakeProcess(HttpServletRequest request, HttpSession session,
                                         Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点处理一览画面", u.getUserId());
        String businessDate = cm9060Service.getValByKey("0000");

        ModelAndView mv = new ModelAndView("stocktakeprocess/stocktakeProcess");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("businessDate", businessDate);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "盘点处理一览画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 盘点处理编辑画面
     *
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_PD_PROCESS_VIEW})
    public ModelAndView stocktakeProcessEdit(String piCdParam,String piDateParam,String identity, HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点处理编辑画面", u.getUserId());
        ModelAndView mv = new ModelAndView("stocktakeprocess/stocktakeProcessEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", identity);
        mv.addObject("piCdParam", piCdParam);
        mv.addObject("piDateParam", piDateParam);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "盘点处理编辑画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 盘点处理打印一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_PD_PROCESS_PRINT})
    public ModelAndView stocktakeProcessPrint(HttpServletRequest request, HttpSession session,
                                         Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点处理一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stocktakeprocess/stocktakeProcessPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("printTime", new Date());
        mv.addObject("searchJson", searchJson);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "盘点处理一览打印画面");
        return mv;
    }

    @RequestMapping(value = "/print/getprintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData(String searchJson, HttpServletRequest request, HttpSession session) {
        if (searchJson==null|| StringUtils.isEmpty(searchJson)) {
            return null;
        }
        // 实例化查询条件
        Gson gson = new Gson();
        PI0100ParamDTO pi0100 = gson.fromJson(searchJson, PI0100ParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, pi0100);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"false",null);
        }
        pi0100.setStores(stores);
        pi0100.setFlg(false);
        List<StocktakeProcessDTO> result = stocktakeProcessService.getPrintData(pi0100);
        return new ReturnDTO(true,"ok",result);
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
    @Permission(codes = { PermissionCode.CODE_SC_PD_PROCESS_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isEmpty(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        Gson gson = new Gson();
        PI0100ParamDTO param = gson.fromJson(searchJson, PI0100ParamDTO.class);
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
        exParam.setPCode(PermissionCode.CODE_SC_PD_PROCESS_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("varianceExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,PI0100ParamDTO param){
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
