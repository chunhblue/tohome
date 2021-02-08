package cn.com.bbut.iy.itemmaster.controller.fileCommon;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.config.ContextPathConfig;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.controller.BaseAction;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.entity.ContentType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/file")
public class fileCommonController extends BaseAction {
    @Autowired
    private ContextPathConfig contextPathConfig;

    @Autowired
    private Ma4320Service ma4320Service;

    @Value("${file.fileDir}")
    private String fileDir;//上传文件路径


    /**
     * 附件上传
     * @param request
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/upload")
    public AjaxResultDto insertFileToTable(HttpServletRequest request, HttpSession session,
                                    @RequestParam("fileData") MultipartFile file){
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
        String oldName = file.getOriginalFilename();
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        try {
            File path = new File(fileDir);
            String filePath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date())
                    + Utils.getRandomFileName()
                    + suffix;
            File outfile =  new File(path.getAbsoluteFile()+ File.separator + filePath);
            if(!outfile.getParentFile().exists()){ //判断文件父目录是否存在
                outfile.getParentFile().mkdirs();
            }
            if (path.isDirectory()) {
                //设置文件回显
                Map map = new HashMap();
                map.put("filePath",filePath);
                map.put("fileName",oldName.replace(suffix,""));
                ard.setData(map);
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
        String escaped = null;


        // 避免直接访问非公开的文件资源
        response.setHeader("X-Accel-Redirect", contextPathConfig.getContextPath()
                + Constants.ATTACHMENT_DOWNLOAD_URL + filePath);
        try {
            escaped = URLEncoder.encode(fileName+suffix, "UTF-8");
            StringBuilder header = new StringBuilder().append("inline;filename=\"")
                    .append(escaped).append("\"");
            response.setHeader("Content-Disposition", header.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("下载,文件名称错误 user:{}", this.getUserId(session));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    /**
     * 预览文件
     * @return
     */
    @RequestMapping(value = "/preview")
    public String preview(HttpSession session, HttpServletRequest request, HttpServletResponse response,
                           String filePath, String fileName) {
        //后缀
        String suffix = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
        String escaped = null;
//        response.setHeader("content-type", "application/octet-stream");
        // 设置响应MIME
        if (".doc".equals(suffix)) {
            response.setContentType("application/msword");
        } else if (".docx".equals(suffix)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        } else if (".pdf".equals(suffix)) {
            response.setContentType("application/pdf");
        } else if (".xls".equals(suffix)) {
            response.setContentType("application/vnd.ms-excel");
        } else if (".xlsx".equals(suffix)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } else if (".ppt".equals(suffix)) {
            response.setContentType("application/vnd.ms-powerpoint");
        } else if (".pptx".equals(suffix)) {
            response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
        } else if (".bmp".equals(suffix)) {
            response.setContentType("image/bmp");
        } else if (".gif".equals(suffix)) {
            response.setContentType("image/gif");
        } else if (".ief".equals(suffix)) {
            response.setContentType("image/ief");
        } else if (".jpeg".equals(suffix)) {
            response.setContentType("image/jpeg");
        } else if (".jpg".equals(suffix)) {
            response.setContentType("image/jpeg");
        } else if (".png".equals(suffix)) {
            response.setContentType("image/png");
        } else if (".tiff".equals(suffix)) {
            response.setContentType("image/tiff");
        } else if (".tif".equals(suffix)) {
            response.setContentType("image/tif");
        }

        // 这里要对应nginx配置中的localtion
        response.setHeader("X-Accel-Redirect", contextPathConfig.getContextPath()
                + Constants.ATTACHMENT_DOWNLOAD_URL + filePath);
        try {
            escaped = URLEncoder.encode(fileName+suffix, "UTF-8");
            StringBuilder header = new StringBuilder().append("inline;filename=\"")
                    .append(escaped).append("\"");
            response.setHeader("Content-Disposition", header.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("预览,文件名称错误 user:{}", this.getUserId(session));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return null;
    }

    /**
     * 附件一览
     * @param recordCd
     * @param fileType 附件类型
     * @return
     */
    @RequestMapping(value = "/getFileList")
    @ResponseBody
    public GridDataDTO<MA4320> getFileList(String recordCd, String fileType) {
        GridDataDTO<MA4320> grid = new GridDataDTO<>();
        if(StringUtils.isNotBlank(recordCd))
            grid.setRows(ma4320Service.getMa4320List(recordCd,fileType));
        return grid;
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

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }


}
