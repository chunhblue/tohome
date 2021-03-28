package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleDTO;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakeEntryService;
import cn.com.bbut.iy.itemmaster.util.ExcelBaseUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/stockTakeNonCount")
public class StockTakeNonListController extends BaseAction {
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private StocktakeEntryService service;

//    @Permission(codes = { PermissionCode.CODE_SC_PD_SETTING_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView stocktakePlanList(HttpServletRequest request, HttpSession session,
                                          Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入盘点不统计商品一览画面", u.getUserId());
        String businessDate = cm9060Service.getValByKey("0000");
        ModelAndView mv = new ModelAndView("stockTake_non_list/stockTakeNonList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("businessDate", businessDate);
        mv.addObject("useMsg", "盘点不统计商品一览画面");
        this.saveToken(request);
        return mv;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST,value = "/uploadFile")
    public AjaxResultDto upload(HttpServletRequest request, HttpSession session,
                             @RequestParam("fileData") MultipartFile file){
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
        if (size > 5240880) {
            log.debug("File Size:::" + file.getSize());
            ard.setMessage("Upload files must not exceed 5MB !");
            return ard;
        }
        // 获取当前用户、时间
        CommonDTO dto = getCommonDTO(session);
        if(dto == null){
            ard.setMessage("Failed to get user information!");
            return ard;
        }
        AjaxResultDto rest = new AjaxResultDto();
        String suf = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        if (!suf.equalsIgnoreCase("xls") && !suf.equalsIgnoreCase("xlsx")) {
            rest.setSuccess(false);
            rest.setMessage("Upload file format error! Expected 'XLS' or'xlsx'!");
            return rest;
        }
        String msg = service.insertNonCountExcel(file,dto);
        if(msg != null){
            ard.setMessage(msg);
            ard.setSuccess(true);
        }else {
            ard.setMessage("No matching data available！");
            ard.setSuccess(false);
        }
        return ard;
    }

    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<ArticleDTO> getList(HttpServletRequest request, HttpSession session,
                                           int page, int rows, String searchJson) {

        if(this.getUser(session) == null){
            return new GridDataDTO<ArticleDTO>();
        }

        // 转换参数对象
        ArticleParamDTO param;
        if(searchJson == null){
            param = new ArticleParamDTO();
        }else{
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, ArticleParamDTO.class);
        }
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);
        return service.getList(param);
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
        // 当前用户ID
        dto.setUpdateUserId(u.getUserId());
        dto.setCreateUserId(u.getUserId());

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
        // 当前时间年月日
        String date = dateFormat.format(now);
        dto.setCreateYmd(date);
        dto.setUpdateYmd(date);
        // 当前时间时分秒
        String time = timeFormat.format(now);
        dto.setCreateHms(time);
        dto.setUpdateHms(time);
        return dto;
    }
}
