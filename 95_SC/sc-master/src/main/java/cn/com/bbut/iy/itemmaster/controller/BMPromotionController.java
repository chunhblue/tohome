package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110GridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110ParamDto;
import cn.com.bbut.iy.itemmaster.dto.promotion.*;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.master.PromotionService;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * BM促销
 *
 * @author lz
 */
@Controller
@Slf4j
//@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/mmPromotion")
public class BMPromotionController extends BaseAction {

    @Autowired
    private PromotionService service;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    private final String EXCEL_EXPORT_KEY = "EXCEL_MM_PROMOTION";
    private final String EXCEL_EXPORT_NAME = "MM Promotion List.xlsx";

    /**
     * BM促销一览画面
     *
     * @param request
     * @param session
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @Permission(codes = { PermissionCode.CODE_SC_ZD_BPQ_LIST_VIEW })
    public ModelAndView bmPromotionList(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入BM促销一览画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("bmpromotion/bmPromotion");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("useMsg", "MM促销一览画面");
        return mv;
    }

    /**
     * MM促销详情画面
     *
     * @param request
     * @param session
     * @param promotionCd
     * @return
     */
    @RequestMapping(value = "/view")
    @Permission(codes = { PermissionCode.CODE_SC_ZD_BPQ_VIEW })
    public ModelAndView toView(HttpServletRequest request, HttpSession session, String promotionCd) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 MM促销详情画面", u.getUserId());
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        ModelAndView mv = new ModelAndView("bmpromotion/details");
        mv.addObject("use", 0);
        mv.addObject("identity", 1);
        mv.addObject("promotionCd", promotionCd);
        mv.addObject("useMsg", "MM促销详情画面");
        return mv;
    }

    /**
     * 条件查询
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getList")
    public GridDataDTO<PromotionDTO> getList(HttpServletRequest request, HttpSession session,
                                             int page, int rows, String searchJson) {

        if(this.getUser(session) == null){
            return new GridDataDTO<PromotionDTO>();
        }

        // 转换参数对象
        PromotionParamDTO param;
        if(searchJson == null){
            param = new PromotionParamDTO();
        }else{
            Gson gson = new Gson();
            param = gson.fromJson(searchJson, PromotionParamDTO.class);
        }
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<PromotionDTO>();
        }
        param.setStoreList(stores);
        param.setPage(page);
        param.setRows(rows);
        param.setLimitStart((page - 1)*rows);

        return service.getList(param);
    }

    /**
     * 查询MM促销信息
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
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);

        PromotionDTO _dto = service.getBasicInfo(param);
        if(_dto == null){
            _return.setMsg("Query failed!");/*获取MM促销信息失败*/
        }else{
            _return.setMsg("Query succeeded!");/*获取MM促销信息成功*/
            _return.setSuccess(true);
            _return.setO(_dto);
        }
        return _return;
    }

    /**
     * 查询条件设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCondition")
    public GridDataDTO<Ma4060DTO> getMa4060(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4060DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4060(param);
    }

    /**
     * 查询商品设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getItem")
    public GridDataDTO<Ma4070DTO> getMa4070(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4070DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4070(param);
    }

    /**
     * 查询类别设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCategory")
    public GridDataDTO<Ma4080DTO> getMa4080(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4080DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4080(param);
    }

    /**
     * 查询类别例外设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCategoryEx")
    public GridDataDTO<Ma4081DTO> getMa4081(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4081DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4081(param);
    }

    /**
     * 查询品牌设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getBrand")
    public GridDataDTO<Ma4085DTO> getMa4085(HttpServletRequest request, HttpSession session,
                                            String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4085DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4085(param);
    }

    /**
     * 查询品牌例外设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getBrandEx")
    public GridDataDTO<Ma4086DTO> getMa4086(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4086DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4086(param);
    }

    /**
     * 查询区域设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getArea")
    public GridDataDTO<Ma4090DTO> getMa4090(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4090DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4090(param);
    }

    /**
     * 查询门店例外设定
     *
     * @param request
     * @param session
     * @param searchJson
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getException")
    public GridDataDTO<Ma4100DTO> getMa4100(HttpServletRequest request, HttpSession session,
                                                 String searchJson) {
        if(searchJson == null){
            return new GridDataDTO<Ma4100DTO>();
        }
        // 转换参数对象
        Gson gson = new Gson();
        PromotionParamDTO param = gson.fromJson(searchJson, PromotionParamDTO.class);
        return service.getMa4100(param);
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
    @Permission(codes = { PermissionCode.CODE_SC_ZD_BPQ_EXPORT })
    public String export(HttpServletRequest request, HttpSession session, String searchJson) {
        if(StringUtils.isBlank(searchJson)){
            log.info("Parameter is empty!");
            return null;
        }
        // 设置参数对象
        PromotionExcelParam exParam = new PromotionExcelParam();
        exParam.setParam(searchJson);
        exParam.setPCode(PermissionCode.CODE_SC_ZD_BPQ_EXPORT);
        exParam.setExFileName(EXCEL_EXPORT_NAME);
        ExService service = Container.getBean("mmExcelService", ExService.class);
        return ExportUtil.export(EXCEL_EXPORT_KEY, request, service, exParam);
    }

    /**
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session,  PromotionParamDTO param){
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

}
