package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DptResourceDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.base.DepService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

/**
 * 获取角色、店铺关系
 *
 * @author mxy
 */
@Controller
@Slf4j
@RequestMapping(value = Constants.REQ_HEADER + "/roleStore")
public class MRoleStoreController extends BaseAction {

    @Autowired
    private MRoleStoreService service;

    @Autowired
    @Setter
    private DepService depService;
    @Autowired
    private Ma1000Service ma1000Service;

    /**
     * 根据角色查询Store权限
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getStoreByRole")
    public ReturnDTO getStoreByRole(HttpServletRequest request, HttpSession session) {
        ReturnDTO _return = new ReturnDTO();
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        List<RoleStoreDTO> list = service.getStoreByRole(roleIds);
        if(list.size()==1){
            RoleStoreDTO dto = list.get(0);
            if(!"999999".equals(dto.getStoreCd())){
                _return.setO(list.get(0));
                _return.setSuccess(true);
            }
        }
        return _return;
    }

    /**
     * 获取Region权限数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getRegion")
    public List<AutoCompleteDTO> getRegion(HttpServletRequest request, HttpSession session,
                                           MRoleStoreParam dto) {
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        if(roleIds == null || roleIds.size()<1){
            log.info(">>>>>> roleIds is null");
            return null;
        }
        dto.setRoleIds(roleIds);
        return service.getRegionByRoleId(dto);
    }

    /**
     * 获取City权限数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getCity")
    public List<AutoCompleteDTO> getCity(HttpServletRequest request, HttpSession session,
                                         MRoleStoreParam dto) {
//        if(StringUtils.isBlank(dto.getRegionCd())){
//            log.info(">>>>>> getRegionCd() is blank");
//            return null;
//        }
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        if(roleIds == null || roleIds.size()<1){
            log.info(">>>>>> roleIds is null");
            return null;
        }
        dto.setRoleIds(roleIds);
        return service.getCityByRoleId(dto);
    }

    /**
     * 获取District权限数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getDistrict")
    public List<AutoCompleteDTO> getDistrict(HttpServletRequest request, HttpSession session,
                                         MRoleStoreParam dto) {
//        if(StringUtils.isBlank(dto.getRegionCd())
//                || StringUtils.isBlank(dto.getCityCd())){
//            log.info(">>>>>> getRegionCd()||dto.getCityCd() is blank");
//            return null;
//        }
        Collection<Integer> roleIds = (Collection<Integer>) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        if(roleIds == null || roleIds.size()<1){
            log.info(">>>>>> roleIds is null");
            return null;
        }
        dto.setRoleIds(roleIds);
        return service.getDistrictByRoleId(dto);
    }

    /**
     * 获取Store权限数据
     *
     * @param request
     * @param session
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/getStore")
    public List<AutoCompleteDTO> getStore(HttpServletRequest request, HttpSession session,
                                         MRoleStoreParam dto) {
        Collection<String> stores = (Collection<String>) session.getAttribute(Constants.SESSION_STORES);
        if(stores == null || stores.size()<1){
            log.info(">>>>>> stores is null");
            return null;
        }
        List<AutoCompleteDTO> list = null;
        if(StringUtils.isBlank(dto.getRegionCd())
                && StringUtils.isBlank(dto.getCityCd()) && StringUtils.isBlank(dto.getDistrictCd())){
            list =  ma1000Service.getListByStorePm(stores,dto.getV());
        }else{
            dto.setStoreCds(stores);
            list =  service.getStoreByRoleId(dto);
        }

        return list;
    }


    /**
     * 检索事业部信息（部门）
     *
     * @param session
     * @param req
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/deps")
    @ResponseBody
    public List<DptResourceDTO> getDeps(HttpSession session, HttpServletRequest req, Integer flg,
                                        String v) {
        User user = this.getUser(session);
        log.debug("抓取部信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = depService.getDpts(null,null,null, ConstantsDB.COMMON_THREE, v, v,
                ConstantsDB.COMMON_TWO);
        return dtos;
    }

    /**
     * 检索部信息（大分类）
     *
     * @param session
     * @param req
     * @param depId
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/pmas")
    @ResponseBody
    public List<DptResourceDTO> getPmas(HttpSession session, HttpServletRequest req,
                                        String depId, Integer flg, String v) {
        User user = this.getUser(session);
        log.debug("抓取大分类信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = depService.getDpts(depId,null,null, ConstantsDB.COMMON_TWO, v, v,
                ConstantsDB.COMMON_TWO);
        return dtos;
    }

    /**
     * 检索dpt信息（中分类）
     *
     * @param session
     * @param req
     * @param depId
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/categorys")
    @ResponseBody
    public List<DptResourceDTO> getCategorys(HttpSession session, HttpServletRequest req,
                                             String depId, String pmaId,Integer flg, String v) {
        User user = this.getUser(session);
        log.debug("抓取中分类信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = depService.getDpts(depId,pmaId,null, ConstantsDB.COMMON_ONE, v, v,
                ConstantsDB.COMMON_TWO);
        return dtos;
    }

    /**
     * 检索dpt信息（小分类）
     *
     * @param session
     * @param req
     * @param depId
     * @param flg
     * @param v
     * @return
     */
    @RequestMapping(value = "/subCategorys")
    @ResponseBody
    public List<DptResourceDTO> getSubCategorys(HttpSession session, HttpServletRequest req,
                                                String depId, String pmaId,String categoryId, Integer flg, String v, String pCode) {
        User user = this.getUser(session);
        log.debug("抓取小分类信息，user:{}", user.getUserId());
        List<DptResourceDTO> dtos = depService.getDpts(depId,pmaId,categoryId, ConstantsDB.COMMON_ZERO, v, v,
                ConstantsDB.COMMON_TWO);
        return dtos;
    }
}
