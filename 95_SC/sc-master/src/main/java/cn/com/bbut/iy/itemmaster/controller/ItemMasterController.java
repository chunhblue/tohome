package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.master.ArticleMasterService;
import cn.com.bbut.iy.itemmaster.util.ExportUtil;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品主档检索
 *
 * @author mxy
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/itemMaster")
public class ItemMasterController extends BaseAction {

    @Autowired
    private ArticleMasterService service;

    private final String EXCEL_EXPORT_KEY = "EXCEL_ITEM_MASTER";
    private final String EXCEL_EXPORT_NAME = "Item Master List.xlsx";

    /**
     * 商品主档检索画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ZD_IMQ_LIST_VIEW })
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 商品主档检索画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("master/item/itemList");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "商品主档检索画面");
        return mv;
    }

    /**
     * 商品主档详情画面
     * @param request
     * @param session
     * @param param
     * @return
     */
    @RequestMapping(value = "/view")
    @Permission(codes = { PermissionCode.CODE_SC_ZD_IMQ_VIEW })
    public ModelAndView toView(HttpServletRequest request, HttpSession session,
                               ArticleParamDTO param) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 商品主档详情画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("master/item/itemDetails");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("dto", param);
        mv.addObject("useMsg", "商品主档详情画面");
        return mv;
    }

    /**
     * 条件查询商品主档
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
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
     * 查询主档基本信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getData")
    public ReturnDTO getBasicInfo(HttpServletRequest request, HttpSession session,
                                  String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        if(this.getUser(session) == null){
            _return.setMsg("Failed to get login user information");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        ArticleDTO _dto = service.getBasicInfo(param);
        if(_dto == null){
            _return.setMsg("获取商品主档信息失败");
        }else{
            _return.setMsg("获取商品主档信息成功");
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    /**
     * 查询商品条码信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getBarcode")
    public GridDataDTO<BarcodeDTO> getBarcode(HttpServletRequest request, HttpSession session,
                                                   String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<BarcodeDTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        return service.getBarcode(param);
    }

    /**
     * 查询商品进货控制信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getOrderControl")
    public GridDataDTO<OrderControlDTO> getOrderControl(HttpServletRequest request, HttpSession session,
                                                        String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<OrderControlDTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        return service.getOrderControl(param);
    }

    /**
     * 查询商品销售信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getSalesControl")
    public GridDataDTO<SalesControlDTO> getSalesControl(HttpServletRequest request, HttpSession session,
                                                        String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<SalesControlDTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        return service.getSalesControl(param);
    }

    /**
     * 查询口味关系信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getFlavor")
    public GridDataDTO<FlavorDTO> getFlavor(HttpServletRequest request, HttpSession session,
                                                        String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<FlavorDTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        return service.getFlavor(param);
    }

    /**
     * 查询厨打关系信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getFoodService")
    public ReturnDTO getFoodService(HttpServletRequest request, HttpSession session,
                                  String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        FoodServiceDTO _dto = service.getFoodService(param);
        if(_dto == null){
            _return.setMsg("获取商品厨打关系信息失败");
        }else{
            _return.setMsg("获取商品厨打关系信息成功");
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    /**
     * 查询包装规格信息
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCartonSpecification")
    public ReturnDTO getCartonSpecification(HttpServletRequest request, HttpSession session,
                                  String searchJson) {
        ReturnDTO _return = new ReturnDTO();
        if(searchJson == null){
            _return.setMsg("Parameter cannot be empty!");
            return _return;
        }
        // 转换参数对象
        Gson gson = new Gson();
        ArticleParamDTO param = gson.fromJson(searchJson, ArticleParamDTO.class);

        CartonSpecificationDTO _dto = service.getCartonSpecification(param);
        if(_dto == null){
            _return.setMsg("获取商品包装规格信息失败");
        }else{
            _return.setMsg("获取商品包装规格信息成功");
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
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
    @Permission(codes = { PermissionCode.CODE_SC_ZD_IMQ_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("导出查询参数为空");
            return null;
        }
        // 设置参数对象
        ExcelParam exParam = new ExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ZD_IMQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("articleExService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 检索供应商数据
     *
     * @param request
     * @param session
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/get")
    public List<AutoCompleteDTO> get(HttpServletRequest request, HttpSession session, String v){
        return service.getList(v);
    }

    /**
     * 品牌查询
     *
     * @param request
     * @param session
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/brands")
    public List<AutoCompleteDTO> getBrand(HttpServletRequest request, HttpSession session, String v){
        return service.getBrand(v);
    }

    /**
     * 品牌查询
     *
     * @param request
     * @param session
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMaterialType")
    public List<Cm9010> getMaterialType(HttpServletRequest request, HttpSession session){
        return service.getMaterialType();
    }

}
