package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.config.ContextPathConfig;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

/**
 * 盘点计划
 *
 * @author lz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/stocktakePlanEntry",produces ={"application/json;charset=UTF-8"})
public class StockPlanController extends BaseAction {

    @Autowired
    private StocktakePlanService stocktakePlanService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private StocktakePlanMapper stocktakePlanMapper;
    @Autowired
    private StocktakeEntryService stocktakeEntryService;
    @Value("${file.fileDir}")
    private String fileDir;//target
    @Autowired
    private ContextPathConfig contextPathConfig;
    

    private final String EXCEL_EXPORT_KEY = "EXCEL_STOCKTAKE_PLAN_QUERY";
    private final String EXCEL_EXPORT_NAME = "Stocktake Plan Query.xlsx";


    /**
     * 盘点计划一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PD_SETTING_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakePlanList(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点计划一览画面", u.getUserId());
        String businessDate = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("stocktakeplan/stocktakePlan");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("businessDate", businessDate);
        mv.addObject("typeId", ConstantsAudit.TYPE_PLAN_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_PLAN_STOCK);
        mv.addObject("useMsg", "盘点计划一览画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 盘点计划编辑画面
     *
     * @param request
     * @param session
     * @return
     */
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_SETTING_ADD,
            PermissionCode.CODE_SC_PD_SETTING_EDIT,
            PermissionCode.CODE_SC_PD_SETTING_VIEW,
    })
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView stocktakePlanEdit(String identity,String enterFlag,String piCdParam, String piDateParam,HttpServletRequest request, HttpSession session) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点计划编辑画面", u.getUserId());
        String businessDate = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("stocktakeplan/stocktakePlanEdit");
        mv.addObject("use", 0);
        mv.addObject("identity", identity);
        mv.addObject("enterFlag",enterFlag); // 操作状态
        mv.addObject("piCdParam",piCdParam);
        mv.addObject("piDateParam",piDateParam);
        mv.addObject("businessDate",businessDate);
        mv.addObject("useMsg", "盘点计划编辑画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_PLAN_STOCK);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_PLAN_STOCK);
        this.saveToken(request);
        return mv;
    }

    @GetMapping("/getComboxData")
    @ResponseBody
    public ReturnDTO getComboxData(HttpServletRequest request, HttpSession session) {
        Map<String,List<Map<String,String>>> result = stocktakePlanService.getComboxData();
        return new ReturnDTO(true,"ok",result);
    }

    /**
     * 获得 店铺下所有的商品分类
     */
    @GetMapping("/getAllItemDepartmentByStore")
    @ResponseBody
    public ReturnDTO getAllItemDepartmentByStore(String storeCd, HttpServletRequest request, HttpSession session) {
        return stocktakePlanService.getAllItemDepartmentByStore(storeCd);
    }

    @GetMapping("/getData")
    @ResponseBody
    public ReturnDTO getData(String piCd,String piDate, HttpServletRequest request, HttpSession session) {
        PI0100DTO pi0110 = stocktakePlanService.getData(piCd,piDate);
        if (pi0110==null) {
            // 查询失败
            return new ReturnDTO(false,"error");
        }
        return new ReturnDTO(true,"ok",pi0110);
    }


    @GetMapping("/getPmaList")
    @ResponseBody
    public GridDataDTO<PI0110DTO> getPmaList(HttpServletRequest request, HttpSession session, int page, int rows) {
        GridDataDTO<PI0110DTO> pmaList = stocktakePlanService.getPmaList(page,rows,request, session);
         return pmaList;
    }

    @GetMapping("/inquire")
    @ResponseBody
    public GridDataDTO<PI0100DTO> inquire(String searchJson, int page, int rows,HttpServletRequest request, HttpSession session) throws ParseException {
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
        GridDataDTO<PI0100DTO> data = stocktakePlanService.search(pi0100Param);
        return data;
    }

    @PostMapping("/save")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_SETTING_ADD,
            PermissionCode.CODE_SC_PD_SETTING_EDIT,
    })
    public ReturnDTO save(String record, HttpServletRequest request, HttpSession session) {

        User loginUser = this.getUser(session);

        if (loginUser==null) {
            // 没有登陆
            return new ReturnDTO(false,"请先登录!");
        }

        if (StringUtils.isEmpty(record)) {
            return new ReturnDTO(false,"Parameter cannot be empty!");
        }

        String _record= null;
        try {
            // _record = URLEncoder.encode(URLEncoder.encode(record,"UTF-8"),"UTF-8");
            _record=URLDecoder.decode(record,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (_record==null) {
            return new ReturnDTO(false,"Parameter exception!");
        }

        String businessDate = cm9060Service.getValByKey("0000");
        Gson gson = new Gson();
        PI0100DTO pi0100 = gson.fromJson(_record, PI0100DTO.class);

        if (pi0100==null) {
            return new ReturnDTO(false,"Parameter exception!");
        }
        if (pi0100.getDetails().size()<1) {
            return new ReturnDTO(false,"明细数据不能为空!");
        }

        if (StringUtils.isEmpty(pi0100.getFlag()) ||
                (!"add".equals(pi0100.getFlag()) &&
                        !"update".equals(pi0100.getFlag()))) {
            return new ReturnDTO(false,"操作状态错误!");
        }
        String flag = pi0100.getFlag();

        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String dateStr = sdf.format(now);
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];

        // 明细数据
        List<PI0110DTO> pi0110List = pi0100.getDetails();

        int row = -1;
        String piCd = "";
        if ("add".equals(flag)) {
            // 新增
            String storeNo = getStoreNo(pi0100);
            pi0100.setPiCd(storeNo);
            // pi0100.setPiDate(businessDate);
            pi0100.setCreateUserId(loginUser.getUserId());
            pi0100.setCreateYmd(ymd);
            pi0100.setCreateHms(hms);
            piCd = stocktakePlanService.insert(pi0100, pi0110List);
        } else {
            // 修改
            pi0100.setUpdateUserId(loginUser.getUserId());
            pi0100.setUpdateYmd(ymd);
            pi0100.setUpdateHms(hms);
            piCd = stocktakePlanService.update(pi0100, pi0110List);
        }

        if (piCd==null||"".equals(piCd)) {
            return new ReturnDTO(false,"保存盘点计划失败!");
        }
        return new ReturnDTO(true,"保存盘点计划成功",pi0100.getPiCd());
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
            _id = "PD" + _type + date;
        }
        return _id;
    }

    /**
     * 校验id是否重复
     * @param piCd
     * @param request
     * @param session
     * @return
     */
    @PostMapping("/checkPicd")
    @ResponseBody
    public ReturnDTO checkPicd(String piCd, HttpServletRequest request, HttpSession session) {
        int count = stocktakePlanService.checkPicd(piCd);
        if (count<1) {
            // 可以使用, 没有重复
            return new ReturnDTO(true,"ok",count);
        } else {
            // 重复
            return new ReturnDTO(false,"no",count);
        }
    }

    private HSSFWorkbook wb;

    @RequestMapping("/queryExport")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_SETTING_EXPORT_ITEM,
    })
    public ReturnDTO Export(HttpServletRequest request, HttpServletResponse response,HttpSession session, String jsonStr, String excleFormCondition,String piCd,String piDate,String storeCd) {
        ReturnDTO _return = null;
        //获取用户权限
        // 获取session中的操作用户ID
        User _sessionUser = this.getUser(session);
        if(_sessionUser == null){
            // logger.error("登录失效,请重新登陆");
            _return = new ReturnDTO(false, "Session time out!","001");
            return _return;
        }

        if(jsonStr == null || "".equals(jsonStr)) {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }
        //前端查询条件
        Gson gson = new Gson();
        PI0100ParamDTO pi0100Param = gson.fromJson(jsonStr, PI0100ParamDTO.class);

        // 查询需要导出的数据
        List<StocktakeItemDTO> list = stocktakePlanService.queryExport(piCd, piDate, storeCd);

        //返回商品的HSSFWorkbook对象
        wb = stocktakePlanService.getExportHSSFWorkbook(list);
        if(wb == null) {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }
        try {
            // 将导出的数据存入盘点差异表
            Future<String> stringFuture = stocktakePlanService.insertExportItemsToDB(piCd, piDate, storeCd, list);
            if (stringFuture==null) {
                _return = new ReturnDTO(false, "Failed to export item!",0);
                return _return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            _return = new ReturnDTO(false, "Failed to export item!",0);
            return _return;
        }

        _return = new ReturnDTO(true, "Succeeded!",1);
        return _return;
    }

    /**
     * 导出excle
     */
    @RequestMapping("/download")
    public void export(String piCd,HttpServletRequest request,HttpServletResponse response) {
        String fileName = "Stocktake Items_"+piCd+".xlsx";
        try {
            //下载
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            OutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File outFile;
    @RequestMapping("/queryCsv")
    @ResponseBody
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_SETTING_EXPORT_ITEM,
    })
    public ReturnDTO toTxt(HttpServletRequest request,HttpSession session, String jsonStr,
                           String piCd,String piDate,String storeCd) {
        ReturnDTO _return = null;
        //获取用户权限
        // 获取session中的操作用户ID
        User _sessionUser = this.getUser(session);
        if(_sessionUser == null){
            // logger.error("登录失效,请重新登陆");
            _return = new ReturnDTO(false, "Session time out!","001");
            return _return;
        }

        if(jsonStr == null || "".equals(jsonStr)) {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }
        //前端查询条件
        Gson gson = new Gson();
        PI0100ParamDTO pi0100Param = gson.fromJson(jsonStr, PI0100ParamDTO.class);

        // 查询需要导出的数据
        List<StocktakeItemDTO> list = stocktakePlanService.queryExport(piCd, piDate, storeCd);
        if(list == null) {
            _return = new ReturnDTO(false, "No data found!",0);
            return _return;
        }
        log.info("list:"+list.size());
        //返回商品的csv对象
        outFile = stocktakePlanService.writeToTXTFile(request,list,piCd);

        try {
            // 将导出的数据存入盘点差异表
            Future<String> stringFuture = stocktakePlanService.insertExportItemsToDB(piCd, piDate, storeCd, list);
            if (stringFuture==null) {
                _return = new ReturnDTO(false, "Failed to txt item!",0);
                return _return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            _return = new ReturnDTO(false, "Failed to txt item!",0);
            return _return;
        }

        _return = new ReturnDTO(true, "Succeeded!",1);
        return _return;
    }

    @RequestMapping("/downloadCsv")
    public void downloadTxt(String piCd,HttpServletResponse response) {
        String fileName = "ItemsmasterStocktake.txt";
        FileInputStream fis = null;
        log.info("fileName---"+fileName);
        try {
            OutputStream out = response.getOutputStream();
            response.reset();

            response.setContentType("text/plain;charset=UTF-8");
            // 避免直接访问非公开的文件资源
            response.setHeader("Content-Disposition","attachment; filename="
                    + URLEncoder.encode(fileName, "UTF-8"));
            response.setCharacterEncoding("UTF-8");

            fis = new FileInputStream(outFile);
            log.info("outFile.path:"+outFile.getAbsolutePath());
            int len = 0;
            byte[] buffer = new byte[4096]; // 缓冲区
            while ((len = fis.read(buffer)) > -1) {
                out.write(buffer, 0, len);
            }
            fis.close();
            out.flush();
            out.close();
        } catch (IOException ex) {
            log.info("下载,文件名称错误");
            ex.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("fis is null");
        }
    }
	
	// 打印
    @RequestMapping(value = "/print", method = RequestMethod.GET)
    @Permission(codes = {
            PermissionCode.CODE_SC_PD_SETTING_PRINT,
    })
    public ModelAndView stocktakeProcessPrint(HttpServletRequest request, HttpSession session,
                                              Map<String, ?> model,String searchJson) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点计划打印一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("stocktakeplan/stocktakePlanPrint");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("userName", u.getUserName());
        mv.addObject("searchJson", searchJson);
        mv.addObject("printTime", new Date());
        mv.addObject("useMsg", "盘点计划打印画面");
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
     * 导出查询结果
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/export")
    @Permission(codes = { PermissionCode.CODE_SC_PD_SETTING_EXPORT_BASIC })
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
        exParam.setPCode(PermissionCode.CODE_SC_PD_SETTING_EXPORT_BASIC);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("stocktakePlanExService", ExService.class);
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
