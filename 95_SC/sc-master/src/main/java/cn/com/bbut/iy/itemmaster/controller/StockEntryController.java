package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakeEntryService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakePlanService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 盘点录入结果及查看
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/stocktakeEntry")
public class StockEntryController extends BaseAction {

    @Autowired
    private StocktakePlanService stocktakePlanService;
    @Autowired
    private StocktakeEntryService stocktakeEntryService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;
    private SXSSFWorkbook wb;

    private final String EXCEL_EXPORT_KEY = "EXCEL_STOCKTAKE_RESULT_QUERY";
    private final String EXCEL_EXPORT_NAME = "Stocktake Result Query.xlsx";

    /**
     * 导入商品
     */
    @Permission(codes = { PermissionCode.CODE_SC_PD_ENTRY_IMOIRT})
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/uploadResult", produces = "text/html;charset=UTF-8")
    public String fileUpload(HttpServletRequest request, HttpSession session,@RequestParam("fileData") MultipartFile file
            ,@RequestParam("storeCd")String storeCd,@RequestParam("piCd")String piCd) {
        return stocktakeEntryService.insertFileUpload(file,request,session,storeCd,piCd);
    }

    /**
     * 盘点结果一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PD_ENTRY_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakeEntry(HttpServletRequest request, HttpSession session,
                                       Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点结果一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        String businessDate = cm9060Service.getValByKey("0000");

        ModelAndView mv = new ModelAndView("stocktakeentry/stocktakePlanEntry");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("businessDate", businessDate);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "盘点结果一览画面");
        this.saveToken(request);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/updateStatus")
    public ReturnDTO updateStatus(String piCd, String piDate, int typeId, HttpServletRequest request, HttpSession session) {
        return stocktakeEntryService.updateStatus(piCd,piDate,typeId);
    }

    @GetMapping("/inquire")
    @ResponseBody
    public GridDataDTO<PI0100DTO> inquire(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        Gson gson = new Gson();
        PI0100ParamDTO pi0100Param = gson.fromJson(searchJson, PI0100ParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, pi0100Param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<PI0100DTO>();
        }
        // 设置 超过 End Time 没有 submit 提交的 计划过期
        stocktakePlanService.updateStocktakingPlanExpired();
        pi0100Param.setStores(stores);
        pi0100Param.setPage(page);
        pi0100Param.setRows(rows);
        pi0100Param.setLimitStart((page - 1)*rows);
        GridDataDTO<PI0100DTO> data = stocktakeEntryService.search(pi0100Param);
        return data;
    }

    /**
     * 导入商品
     *
     * @param request
     * @param session
     * @param file
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PD_ENTRY_IMOIRT})
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload", produces = "text/html;charset=UTF-8")
    public String fileUpload(HttpServletRequest request, HttpSession session,
                             @RequestParam("fileData") MultipartFile file,
                             @RequestParam("piCd") String piCd,
                             @RequestParam("storeCd") String storeCd,
                             @RequestParam("piDate") String piDate) {

        Gson gson = new Gson();
        String msg = null;
        // 获取文件路径
        AjaxResultDto _extjsFormResult = new AjaxResultDto();
        if (StringUtils.isEmpty(piCd) || StringUtils.isEmpty(piDate)) {
            _extjsFormResult.setMessage("Parameter cannot be empty!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }
        try {
            if (file.getSize() > 0) {
                // 上传文件大小不能超过5M！
                if (file.getSize() > 5242880) {
                    _extjsFormResult.setMessage("Uploaded file size cannot exceed 5M!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }

                // 上传文件格式错误！
                /*if (!ExcelBaseUtil.isExcel(file)) {
                    _extjsFormResult.setMessage("Upload file format error!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }*/

                // 判断是否为csv格式
                String oldName = file.getOriginalFilename();
                if(oldName == null){
                    _extjsFormResult.setMessage("No file found!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }
                String suffix = oldName.substring(oldName.lastIndexOf("."));
                // 上传文件格式错误！
                if (!".csv".equals(suffix)) {
                    _extjsFormResult.setMessage("Upload file format error!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }
                List<StocktakeItemDTO> itemList = null;

                Map<String,Object> map = stocktakeEntryService.insertImportCsv(file,storeCd,piCd);
                for(Map.Entry<String,Object> entry : map.entrySet()){
                    if("list".equals(entry.getKey()) ){
                        Object list = entry.getValue();
                        itemList = (List<StocktakeItemDTO>) list;
                    }else if("msg".equals(entry.getKey())){
                        Object mapMsg = entry.getValue();
                        msg = (String) mapMsg;
                    }
                }

                if (itemList==null||itemList.size()<1) {
                    _extjsFormResult.setMessage(msg);
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }

                for (StocktakeItemDTO item : itemList) {
                    item.setPiCd(piCd);
                    item.setPiDate(piDate);
                    item.setStoreCd(storeCd);
                }

                /**
                 * 第 5个参数为修改主档数据 修改人,修改时间,评论相关, 导出为null
                 */
                // 保存到数据库
                int count = stocktakeEntryService.insert(piCd,piDate, storeCd, itemList, null);
                if (count==-1) {
                    throw new RuntimeException("批量保存出错!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 上传失败
            _extjsFormResult.setMessage("Upload failed!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }
        // 上传成功
        _extjsFormResult.setMessage(msg);
        _extjsFormResult.setSuccess(true);
        return gson.toJson(_extjsFormResult);
    }

    /**
     * 格式化盘点时间
     * 03/08/2020 10:36:31 -> 20200803103631
     * @param time
     * @return
     */
    private String formatStockTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return "";
        }
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(time);
            return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        } catch (ParseException e) {
            log.error("盘点时间格式化错误--"+time);
            return "";
        }
    }


    /**
     * 盘点结果编辑画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_ENTRY_VIEW,
            PermissionCode.CODE_SC_PD_ENTRY_EDIT,
    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView stocktakeDataEdit(String enterFlag, String piCdParam, String piDateParam, HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点结果编辑画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        String businessDate = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("stocktakeentry/stocktakeDataEntry");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("enterFlag", enterFlag);
        mv.addObject("piCdParam", piCdParam);
        mv.addObject("piDateParam", piDateParam);
        mv.addObject("businessDate", businessDate);
        mv.addObject("typeId", ConstantsAudit.TYPE_ORDER_TAKE_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_ORDER_TAKE_STOCK);
        mv.addObject("useMsg", "盘点结果编辑画面");
        this.saveToken(request);
        return mv;
    }

    @GetMapping("/getData")
    @ResponseBody
    public ReturnDTO getData(String piCd, String piDate, HttpServletRequest request, HttpSession session) {
        PI0100DTO pi0100 = stocktakeEntryService.getData(piCd, piDate);
        if (pi0100 == null) {
            return new ReturnDTO(false, "Query result is empty!");
        }
        return new ReturnDTO(true, "Query successful!", pi0100);
    }

    /**
     * 获得盘点计划中分类数据下的商品数据 自动加载列表显示
     */
    @GetMapping("/getItemList")
    @ResponseBody
    public List<AutoCompleteDTO> getItemList(String piCd, String piDate,String piStoreCd, String v, HttpServletRequest request, HttpSession session) {
        return stocktakeEntryService.getItemList(piCd,piDate,piStoreCd,v);
    }

    /**
     * 获得盘点计划中分类数据下的商品数据
     */
    @GetMapping("/getItemInfo")
    @ResponseBody
    public ReturnDTO getItemInfo(String itemCode, String piCd, String piDate, HttpServletRequest request, HttpSession session) {
        StocktakeItemDTO item = stocktakeEntryService.getItemInfo(itemCode, piCd, piDate);
        if (item == null) {
            return new ReturnDTO(false, "查询为空!");
        }
        return new ReturnDTO(true, "Query succeeded!", item);
    }

    /**
     * 同步盘点计划中数据下的盘点结束时间
     */
    @PostMapping("/modifyEndTime")
    @ResponseBody
    public void synchronizedTime(String piCd, String piDate,String storeCd){
        stocktakeEntryService.updateEndTime(piCd,piDate,storeCd);
    }

    /**
     * 获得盘点计划中分类数据下的商品数据
     */
    @PostMapping("/save")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_ENTRY_EDIT,
    })
    public ReturnDTO save(String record, String param, HttpServletRequest request, HttpSession session) {
        User loginUser = this.getUser(session);

        if (loginUser == null) {
            // 没有登陆
            return new ReturnDTO(false, "请先登录!");
        }

        if (StringUtils.isEmpty(record) || StringUtils.isEmpty(param)) {
            return new ReturnDTO(false, "Parameter cannot be empty!");
        }

        String _record = null;
        try {
            _record = URLDecoder.decode(record, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (_record == null) {
            return new ReturnDTO(false, "Parameter exception!");
        }

        Gson gson = new Gson();
        // 商品明细数据
        List<StocktakeItemDTO> stocktakeItemList = gson.fromJson(_record, new TypeToken<List<StocktakeItemDTO>>(){}.getType());
        // 盘点头档数据
        PI0100DTO pi0100 = gson.fromJson(param, PI0100DTO.class);

        if (stocktakeItemList == null || stocktakeItemList.size() < 1 || pi0100 == null) {
            return new ReturnDTO(false, "Parameter exception!");
        }
        // 设置修改时间
        String str = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String ymd = str.split("-")[0];
        String hms = str.split("-")[1];
        pi0100.setUpdateUserId(loginUser.getUserId());
        pi0100.setUpdateYmd(ymd);
        pi0100.setUpdateHms(hms);

        int count = -1;
        String piCd = pi0100.getPiCd();
        String piDate = pi0100.getPiDate();
        String storeCd = pi0100.getStoreCd();
        // 添加明细
        count = stocktakeEntryService.insert(piCd, piDate,storeCd, stocktakeItemList,pi0100);
        if (count == -1) {
            return new ReturnDTO(false, "Data saved failed!");
        }

        return new ReturnDTO(true, "Data saved successfully!");
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
    @Permission(codes = { PermissionCode.CODE_SC_PD_ENTRY_EXPORT })
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
        exParam.setPCode(PermissionCode.CODE_SC_PD_ENTRY_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("stocktakeResultExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    // 打印
    @Permission(codes = { PermissionCode.CODE_SC_PD_ENTRY_PRINT})
    @RequestMapping(method = RequestMethod.GET,value = "/print")
    public ModelAndView stocktakeEntryPrint(HttpServletRequest request, HttpSession session,
                                              Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点结果打印一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stocktakeentry/stocktakePlanEntryPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("searchJson", searchJson);
        mv.addObject("printTime", new Date());
        mv.addObject("useMsg", "盘点结果打印一览画面");
        return mv;
    }

    @RequestMapping(value = "/print/getprintData", method = RequestMethod.POST)
    @ResponseBody
    public ReturnDTO printData(String searchJson,HttpServletRequest request, HttpSession session) {
        Gson gson = new Gson();
        PI0100ParamDTO pi0100Param = gson.fromJson(searchJson, PI0100ParamDTO.class);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, pi0100Param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new ReturnDTO(false,"false",null);
        }
        // 设置 超过 End Time 没有 submit 提交的 计划过期
        stocktakePlanService.updateStocktakingPlanExpired();
        pi0100Param.setStores(stores);
        List<PI0100DTO> result = stocktakePlanService.getPrintData(pi0100Param);
        return new ReturnDTO(true,"ok",result);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, PI0100ParamDTO param){
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
    private String getStoreNo(PI0100DTO pi0100DTO){
        String _id = null;
        // 时间转换
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        // 当前时间年月日
        String date = dateFormat.format(now);
        String _type =pi0100DTO.getPiType();
        if(org.apache.commons.lang.StringUtils.isNotBlank(_type)){
            _id = "D" + _type + date;
        }
        return _id;
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
    @RequestMapping("/exportDetail")
    @ResponseBody
    @Permission(codes = { PermissionCode.CODE_SC_PD_ENTRY_EXPORT })
    public ReturnDTO exportDetail(HttpServletRequest request, HttpSession session, String searchJson, String listJson) {
        ReturnDTO _return = null;
        if(org.apache.commons.lang.StringUtils.isBlank(searchJson)
                || org.apache.commons.lang.StringUtils.isBlank(listJson)){
            log.info("导出查询参数为空");
            _return = new ReturnDTO(false, "Export query parameter is empty",0);
            return _return;
        }
        Gson gson = new Gson();
        PI0100DTO pi0100DTO = gson.fromJson(searchJson,PI0100DTO.class);
        if(pi0100DTO == null) {
            log.info("Failed to get head information");
            _return = new ReturnDTO(false, "Failed to get head information", 0);
            return _return;
        }
        List<StocktakeItemDTO> stockList = gson.fromJson(listJson, new TypeToken<List<StocktakeItemDTO>>(){}.getType());
        if(stockList == null || stockList.size() == 0){
            log.info("Failed to get the details of the item!");
            _return = new ReturnDTO(false, "Failed to get the details of the item!", 0);
            return _return;
        }

        wb =  stocktakeEntryService.getExcel(pi0100DTO,stockList);
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
    public void export(HttpServletRequest request, HttpServletResponse response) {
        try {
            //下载
            String fileName = "STOCK_TAKE_RESULT.xlsx";

            fileName = URLEncoder.encode(fileName,"utf-8");
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8),"iso8859-1");

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
}
