package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.clearInventory.ClearInventoryDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.service.clearInventory.ClearInventoryService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/clearInventory")
public class ClearInventoryController extends BaseAction {

    @Autowired
    private ClearInventoryService service;

    @Permission(codes = { PermissionCode.CODE_SC_ST_CLEAR_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session) {
        ModelAndView mv = new ModelAndView("clear_stock/clearInventory");
        mv.addObject("useMsg", "清空库存");
        mv.addObject("bsDate", new Date());
        return mv;
    }

    @RequestMapping("/getItemList")
    @ResponseBody
    public List<AutoCompleteDTO> getItemList(String v, HttpServletRequest request, HttpSession session) {
        List<AutoCompleteDTO> result = service.getItemList(v);
        return result;
    }

    /**
     * 获得商品详细信息
     */
    @RequestMapping("/getItemInfo")
    @ResponseBody
    public ReturnDTO getItemInfo(String articleId, HttpServletRequest request, HttpSession session) {
        return service.getItemInfo(articleId,request,session);
    }

    /**
     * 保存商品
     */
    @Permission(codes = { PermissionCode.CODE_SC_ST_CLEAR_ADD})
    @RequestMapping("/save")
    @ResponseBody
    public ReturnDTO save(ClearInventoryDTO dto, HttpServletRequest request, HttpSession session) {
        User user = this.getUser(session);
        return service.insert(dto,user,request,session);
    }

    /**
     * 导入商品
     */
    @Permission(codes = { PermissionCode.CODE_SC_ST_CLEAR_IMPORT})
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/import", produces = "text/html;charset=UTF-8")
    public String fileUpload(HttpServletRequest request, HttpSession session, @RequestParam("fileData") MultipartFile file) {
        User user = this.getUser(session);
        return service.insertImportExcel(user,file,request,session);
    }

    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping("/inquire")
    public GridDataDTO<ClearInventoryDTO> inquire(String searchJson, int page, int rows, HttpServletRequest request, HttpSession session) {
        User user = this.getUser(session);
        return service.inquire(user,searchJson,page,rows,request,session);
    }

}
