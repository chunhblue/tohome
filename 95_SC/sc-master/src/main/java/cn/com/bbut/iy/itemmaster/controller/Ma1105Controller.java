package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsAudit;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma1105.Ma1105ParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105Example;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.ImportExcelService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4320Service;
import cn.com.bbut.iy.itemmaster.service.ma1105.Ma1105Service;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订货店铺与货品关系
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/storeItemRelationship")
public class Ma1105Controller extends BaseAction {

    @Autowired
    private ImportExcelService importExcelService;
    @Autowired
    private Ma1105Service service;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private Ma4320Service ma4320Service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_POG_QUERY";
    private final String EXCEL_EXPORT_NAME = "POG Query List.xlsx";


    /**
     * 订货店铺与货品关系画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_S_I_R_LIST_VIEW})
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 订货店铺与货品关系", u.getUserId());
        ModelAndView mv = new ModelAndView("storeItemRelationship/storeItemRelationship");
        mv.addObject("useMsg", "订货店铺与货品关系画面");
        mv.addObject("typeId", ConstantsAudit.TYPE_POG_MNG);
        mv.addObject("reviewId", ConstantsAudit.REVIEW_POG_MNG);
        this.saveToken(request);
        return mv;
    }


    @Permission(codes = { PermissionCode.CODE_SC_S_I_R_UPLOAD})
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/upload")
    public AjaxResultDto withSimple(@RequestParam("fileData")MultipartFile[] fileDatas,String storeCd,boolean isCoverage, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        AjaxResultDto rest = new AjaxResultDto();
        User u = this.getUser(session);
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            rest.setMsg("Failed to get user information!");
            return rest;
        }
        List<Ma1105Example> ma1105Examples = new ArrayList<>();
        for(int i=0;i<fileDatas.length;i++) {
            Ma1105Example example = new Ma1105Example();
            if (StringUtils.isBlank(fileDatas[i].getOriginalFilename())) {
                rest.setSuccess(false);
                rest.setMessage("Upload file cannot be empty！");
                return rest;
            }
            String fileName = fileDatas[i].getOriginalFilename();
            String excelName = fileName.substring(0, fileName.lastIndexOf("."));
            String excelNamePrefix = fileName.split("-")[0].replace(" ", "");
            String suf = fileDatas[i].getOriginalFilename().substring(fileDatas[i].getOriginalFilename().lastIndexOf(".") + 1);
            if (!suf.equalsIgnoreCase("xls") && !suf.equalsIgnoreCase("xlsx")) {
                rest.setSuccess(false);
                rest.setMessage(excelName + " format error! Expected 'XLS' or'xlsx'!");
                return rest;
            }
            if (StringUtils.isBlank(storeCd)) {
                rest.setSuccess(false);
                rest.setMessage("Store No. cannot be empty！");
                return rest;
            }
            int num = service.countPogName(excelName, storeCd);
            if (num > 0) {
                rest.setSuccess(false);
                rest.setMessage(excelName + " has been uploaded today！");
                return rest;
            }

            List<Ma1105> list = new ArrayList<>();

            list = importExcelService.importExcelWithSimple(fileDatas[i],storeCd,u, req, resp);
            if(list == null || list.size() == 0 ) {
                rest.setSuccess(false);
                rest.setMessage("Imported "+excelName+" data file is empty!");
                return rest;
            }else {
                //判断是否确认覆盖
                if(!isCoverage){
                    //去除货架重复
                    int isPogNamePrefix = service.isCovergePogNamePrefix(storeCd,excelNamePrefix);
                    if(isPogNamePrefix>0){
                        rest.setSuccess(false);
                        rest.setSource("shelf");
                        rest.setMessage(" Imported POG file("+excelNamePrefix+")already exists");
                        return rest;
                    }
                 }
            }

            for(Ma1105 ma1105 : list){
                ma1105.setCommonDTO(dto);
                ma1105.setExcelName(excelName); // 设置上传文档名
            }
            example.setStoreCd(storeCd);
            example.setMa1105s(list);
            example.setExcelName(excelName);
            example.setCommonDTO(dto);
            ma1105Examples.add(example);
        }
    try {
                // 保存文件基本信息
                service.insertFileData(ma1105Examples);
                int flag = service.insertData(ma1105Examples);
                rest.setSuccess(true);
                rest.setMessage("Data imported successfully!");

        }catch (Exception e){
            e.printStackTrace();
            rest.setSuccess(false);
            log.info("Excle导入失败！");
            rest.setMessage("Data failed to import!");
        }
        return rest;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getdata")
    public GridDataDTO<Ma1105> getData(HttpServletRequest request, HttpSession session,
                                       Map<String, ?> model, Ma1105ParamDTO param) {
        if(param==null){
            param = new Ma1105ParamDTO();
        }
        if(StringUtils.isBlank(param.getSearchJson())){
            return new GridDataDTO<>();
        }
        return service.getData(param);
    }

    @ResponseBody
    @RequestMapping(value = "/getShelf")
    public List<String> getShelf(HttpServletRequest request, HttpSession session,
                                       Map<String, ?> model, String storeCd) {
        List<String> list = new ArrayList<>();
        if(StringUtils.isNotBlank(storeCd)){
            list = service.getShelf(storeCd);
        }
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getSubShelf")
    public List<String> getSubShelf(HttpServletRequest request, HttpSession session,
                                 Map<String, ?> model, String storeCd,String shelf) {
        List<String> list = new ArrayList<>();
        if(StringUtils.isNotBlank(storeCd)&&StringUtils.isNotBlank(shelf)){
            list = service.getSubShelf(storeCd,shelf);
        }
        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/getStoreInfo")
    public AjaxResultDto getStoreInfo(HttpServletRequest request, HttpSession session,
                                    String storeCd) {
        AjaxResultDto resultDto = new AjaxResultDto();
        resultDto.setSuccess(false);
        if(StringUtils.isNotBlank(storeCd)){
            Ma1105 ma1105 = service.getStoreInfo(storeCd);
            if(ma1105!=null){
                resultDto.setSuccess(true);
                resultDto.setData(ma1105);
            }
        }
        return resultDto;
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
    @Permission(codes = { PermissionCode.CODE_SC_S_I_R_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 获取当前角色店铺权限
        Gson gson = new Gson();
        Ma1105 dto = gson.fromJson(searchJson, Ma1105.class);
        Collection<String> stores = getStores(session, dto);
//        Collection<String> stores = (Collection<String>)request.getSession().getAttribute(
//                Constants.SESSION_STORES);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setStores(stores);
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_S_I_R_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("pogExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,Ma1105 param){
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
        String nowDate = ma4320Service.getNowDate();
        String ymd = nowDate.substring(0,8);
        String hms = nowDate.substring(8,14);
        CommonDTO dto = new CommonDTO();
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
}
