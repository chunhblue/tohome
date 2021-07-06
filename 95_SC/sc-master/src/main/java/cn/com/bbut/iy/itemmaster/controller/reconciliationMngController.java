package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.config.ContextPathConfig;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngDto;
import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.Mb0030;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.reconciliationMng.ReconciliationMngService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeReconciliationMng")
public class reconciliationMngController extends BaseAction  {

    @Autowired
    private ContextPathConfig contextPathConfig;

    @Value("${file.fileDir}")
    private String fileDir;//上传文件路径

    @Autowired
    private ReconciliationMngService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private DefaultRoleService defaultRoleService;
    @Autowired
    private Ma4320Service ma4320Service;

    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.SC_RECONCILE_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 CK与第三方金额比较画面", u.getUserId());
        ModelAndView mv = new ModelAndView("reconciliationMng/reconciliationManagement");
        mv.addObject("user",u.getUserId());
        mv.addObject("useMsg", "CK与第三方金额比较画面");
        this.saveToken(request);
        return mv;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDataByType")
    public GridDataDTO<ReconciliationMngDto> getDataByType(HttpServletRequest req, HttpSession session,HttpServletResponse resp,
                                   int page, int rows, String searchJson) throws ParseException {
        Gson gson = new Gson();
        ReconciliationMngParamDto param = gson.fromJson(searchJson, ReconciliationMngParamDto.class);
        // 为空返回
        if(param == null){
            return new GridDataDTO<>();
        }
        param.setPage(page); // 1
        param.setRows(rows); // 10
        param.setLimitStart((page - 1)*rows);
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<>();
        }
        User u = this.getUser(session);
        /*int i = defaultRoleService.getMaxPosition(u.getUserId());
        if(i == 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            param.setTransStrartDate(startDate);
        }*/
        param.setStores(stores);

        GridDataDTO<ReconciliationMngDto> list = service.getByTypeCondition(param);
        return list;
}

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/uploadFile")
    public AjaxResultDto upload(HttpServletRequest request, HttpSession session,
                                    @RequestParam("fileData") MultipartFile file,String excelGroupCd) {
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        if (file.isEmpty()) {
            ard.setSuccess(false);
            ard.setMessage("Upload file cannot be empty！");
            return ard;
        }

        int size = (int) file.getSize();
        if (size > 52408800) {
            log.debug("File Size:::" + file.getSize());
            ard.setMessage("Upload files must not exceed 50MB !");
            return ard;
        }

        User u = this.getUser(session);
        AjaxResultDto rest = new AjaxResultDto();
        String suf = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!suf.equalsIgnoreCase("xls") && !suf.equalsIgnoreCase("xlsx")) {
            rest.setSuccess(false);
            rest.setMessage("Upload file format error! Expected 'XLS' or'xlsx'!");
            return rest;
        }
        int i = 0;
        switch(excelGroupCd){
            case "201":  // CK SAP (Paybill)
                i = service.insertExcelToMb1200(u.getUserId(), file);
                break;
            case "202":  // CK SAP (Paycode)
                i = service.insertExcelTomb1300(file,u.getUserId());
                break;
            case "302": // Viettel
                i = service.insertMb1700(file,u.getUserId());
                break;
        }
        if(i > 0){
            ard.setMessage("Upload success！");
        }else {
            if(i == -1){
                ard.setMessage("ParseException！");
            }else {
                ard.setMessage("No matching data available！");
            }
        }
        ard.setSuccess(true);

        return ard;
    }

    /**
     * 附件上传
     * @param request
     * @param session
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/upload")
    public AjaxResultDto fileUpload(HttpServletRequest request, HttpSession session,
                                    @RequestParam("fileData") MultipartFile file,String excelGroupCd){
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            ard.setToKen(ard.getToKen());
            return ard;
        }
        if(file.isEmpty()){
            ard.setSuccess(false);
            ard.setMessage("Upload file cannot be empty！");
            return ard;
        }

        int size = (int) file.getSize();
        if(size>52408800){
            log.debug("File Size:::" + file.getSize());
            ard.setMessage("Upload files must not exceed 50MB !");
            return ard;
        }

        AjaxResultDto rest = new AjaxResultDto();
        String suf = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        if(!suf.equalsIgnoreCase("xls")&&!suf.equalsIgnoreCase("xlsx")){
            rest.setSuccess(false);
            rest.setMessage("Upload file format error! Expected 'XLS' or'xlsx'!");
            return rest;
        }

        String oldName = file.getOriginalFilename();
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        try {
            File path = new File(fileDir); // 文件上传路径
            String filePath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date())
                    + oldName;
            if (path.isDirectory()) {
                File outfile =  new File(path.getAbsoluteFile()+ File.separator + filePath);
                if(!outfile.getParentFile().exists()){ //判断文件父目录是否存在
                    outfile.getParentFile().mkdirs();
                }
                //设置文件回显
                Map map = new HashMap();
                map.put("filePath",filePath);
                map.put("fileName",oldName.replace(suffix,""));
                ard.setData(map);
                // MultipartFile 转换 File：mulfile ---->File 保存
                file.transferTo(outfile);
            } else {
                throw new SystemRuntimeException("file path is not exist");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("file path is not exist");
            ard.setMessage("file path is not exist！");
            return ard;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("File failed to upload!");
            ard.setMessage("File failed to upload!");
            return ard;
        } catch (SystemRuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            ard.setMessage(e.getMessage());
            return ard;
        }
        ard.setSuccess(true);
        ard.setMessage("Upload success！");
        return ard;
    }

    /**
     * 下载文件
     * @return
     */
    @RequestMapping(value = "/download")
    public String download(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                           String filePath, String fileName) {
        response.setHeader("content-type", "application/octet-stream");
        //后缀
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        String escaped;
        // 避免直接访问非公开的文件资源
        response.setHeader("X-Accel-Redirect", contextPathConfig.getContextPath()
                + Constants.ATTACHMENT_DOWNLOAD_URL + filePath);
        try {
            escaped = URLEncoder.encode(fileName+suffix, "UTF-8");
            StringBuilder header = new StringBuilder().append("attachment;filename=\"")
                    .append(escaped).append("\"");
            response.setHeader("Content-Disposition", header.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("下载,文件名称错误 user:{}", this.getUserId(session));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/save")
    public ReturnDTO saveFileData(HttpServletRequest request, HttpSession session,
                                           ReconciliationMngDto recon ,String fileDetailJson) {
        ReturnDTO _return = new ReturnDTO();
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            _return.setMsg("Failed to get user information!");
            return _return;
        }
        recon.setCommonDTO(dto);
        recon.setFileDetailJson(fileDetailJson);
        String _id = service.insert(recon);
        if(StringUtils.isBlank(_id)){
            _return.setMsg("Data saved failed！");
        }else {
            _return.setMsg("Data saved successfully！");
            _return.setSuccess(true);
        }
        return _return;
    }

    @ResponseBody
    @RequestMapping(value = "/getMb0010")
    public List<AutoCompleteDTO> getMb0010(HttpServletRequest request, HttpSession session, String v) {
        List<AutoCompleteDTO> list = service.getMb0010(v);
        return list;
    }


    @ResponseBody
    @RequestMapping(value = "/getMb0020")
    public List<AutoCompleteDTO> getMb0020(HttpServletRequest request, HttpSession session,String documentReconCd,String v) {
        if(StringUtils.isBlank(documentReconCd)){
            System.out.println("documentReconCd is null");
            return null;
        }
        List<AutoCompleteDTO> list = service.getMb0020(documentReconCd,v);
        return list;
    }

    @RequestMapping(value = "/getFileList")
    @ResponseBody
    public GridDataDTO<Mb0030> getFileList(String recordCd) {
        GridDataDTO<Mb0030> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(recordCd))
            grid.setRows(service.getMb0030List(recordCd));
        return grid;
    }

    /**
     * 获得文件路径
     * @param excelGroupCd
     * @return
     */
    @RequestMapping(value = "/getFileByExcelCd")
    @ResponseBody
    public Mb0030 getFile(String excelGroupCd){
        if(StringUtils.isBlank(excelGroupCd)){
            log.info(">>>>>>>>>>>>>>>>>>>>> excelGroupCd is null");
            return null;
        }
        Mb0030 list = service.selectFile(excelGroupCd);
        if(list == null){
            log.info(">>>>>>>>>>>>>>>>>>>>> get filePath fail");
            return null;
        }
        String filePath = fileDir +File.separator+ list.getFilePath(); // 获取文件路径
        File file = new File(filePath);
        if(!file.exists()){
            list.setFilePath("");
            log.info(">>>>>>>>>>>>>>>>>>>>> get file isn't exists");
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/ifExistsExcel")
    public ReturnDTO countExcelGroupCd(HttpServletRequest request, HttpSession session,
                                  String excelGroupCd) {
        ReturnDTO _return = new ReturnDTO();

        int i = service.countExcelGroupCd(excelGroupCd);
        if(i > 0){
            _return.setMsg("The file already exists！");
            return _return;
        }else {
            _return.setSuccess(true);
        }
        return _return;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/del")
    public ReturnDTO delFile(String filePath) {
        ReturnDTO _return = new ReturnDTO();
        int i = service.delFile(filePath);
        if(i > 0){
            _return.setSuccess(true);
        }
        String fileTruePath = fileDir +File.separator+ filePath;
        File file = new File(fileTruePath);
        if(!file.exists()){
           log.info("需要删除的文件不存在");
            return _return;
        }else {
            if(file.isFile()){ // 如果目标是文件，则删除该文件
                file.delete();
                log.info("删除文件成功");
            }else {
                if(file.isDirectory()){ // 如果目标是文件
                    File[] fileArrays = file.listFiles();  //列出源文件下所有文件，包括子目录
                    for ( int j = 0 ; i < fileArrays.length ; i++ ){
                        this.delFile(fileArrays[j].getAbsolutePath());//将源文件下的所有文件逐个删除
                    }
                }
            }
        }
        return _return;
    }
    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, ReconciliationMngParamDto param){
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
        // 只选择了一部分参数，生成查询参数，后台查询
        MRoleStoreParam dto = new MRoleStoreParam();
        dto.setRegionCd(param.getRegionCd());
        dto.setCityCd(param.getCityCd());
        dto.setDistrictCd(param.getDistrictCd());
        stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        dto.setStoreCds(stores);
        return mRoleStoreService.getStoreByChoose(dto);
    }

    /**
     * 获取当前操作用户、时间
     */
    private CommonDTO getCommonDTO(HttpSession session){
        User u = this.getUser(session);
        if(u == null){
            return null;
        }
        CommonDTO dto = new CommonDTO();
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(ymd);
        dto.setUpdateYmd(ymd);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(hms);
        dto.setUpdateHms(hms);
        return dto;
    }

    /*
    创建FileItem
     */
    private FileItem createFileItem(File file, String fieldName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
