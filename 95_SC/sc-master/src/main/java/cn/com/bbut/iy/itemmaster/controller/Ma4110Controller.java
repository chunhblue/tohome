package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersGridDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersParamDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110GridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4110.Ma4110ParamDto;
import cn.com.bbut.iy.itemmaster.dto.promotion.PromotionParamDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.Ma4110Service;
import cn.com.bbut.iy.itemmaster.service.Ma4340Service;
import cn.com.bbut.iy.itemmaster.service.master.PromotionService;
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
import java.util.List;
import java.util.Map;

/**
 * 促销信息管理
 *
 * @author zcz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/informPromotion")
public class Ma4110Controller extends BaseAction {
    @Autowired
    private Ma4110Service ma4110Service;
    @Autowired
    private Ma4340Service ma4340Service;
    @Autowired
    private MRoleStoreService mRoleStoreService;

    /**
     * 促销信息画面
     * @param request
     * @param session
     * @param model
     * @return
     */
    @Permission(codes = { PermissionCode.CODE_SC_PROMOTION_LIST_VIEW})
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView tolistView(HttpServletRequest request, HttpSession session,
                                   Map<String, ?> model) {
        User u = this.getUser(session);
        log.debug("User:{} 进入 促销信息", u.getUserId());
        ModelAndView mv = new ModelAndView("inform_promotion/promotionList");
        mv.addObject("useMsg", "促销信息画面");
        this.saveToken(request);
        return mv;
    }

    /**
     * 促销信息一览
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/getList")
    @ResponseBody
    public GridDataDTO<Ma4110GridDto> getList(HttpServletRequest request, HttpSession session,
                                              Ma4110ParamDto param) {
        if(param == null){
            param = new Ma4110ParamDto();
        }

        User u = this.getUser(session);
        param.setUserId(u.getUserId());
        // 获取当前角色店铺权限
        Collection<String> stores = getStores(session, param);
        if(stores.size() == 0){
            log.info(">>>>>>>>>>>>>>>>>>>>> get stores is null");
            return new GridDataDTO<Ma4110GridDto>();
        }
        param.setStoreList(stores);
        return ma4110Service.insertInformAndGetList(param);
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
     * 根据画面选择，获取Store权限
     */
    private Collection<String> getStores(HttpSession session, Ma4110ParamDto param){
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
     * 获取区域信息
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getRegion")
    public List<AutoCompleteDTO> getRegion(HttpSession session) {
        return ma4110Service.getRegionList();
    }
}
