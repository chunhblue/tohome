package cn.com.bbut.iy.itemmaster.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleEditDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleGridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.DefaultRole.DefaultRoleParamDTO;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.base.DptService;
import cn.com.bbut.iy.itemmaster.service.base.OccupService;
import cn.com.bbut.iy.itemmaster.service.base.PostService;

/**
 * 默认授权
 * 
 * @author songxz
 */
@Controller
@Slf4j
@Secure
@RequestMapping(value = Constants.REQ_HEADER + "/defaultrole")
public class DefaultRoleController extends BaseAction {
    @Autowired
    private DptService dptService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private PostService postService;
    @Autowired
    private OccupService occupService;
    @Autowired
    private DefaultRoleService service;

    @Permission(code = PermissionCode.P_CODE_DEFASS_VIEW)
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView view(HttpServletRequest req, HttpSession session, Map<String, ?> model) {
        String userId = this.getUserId(session);
        log.debug("User:{} 进入 默认角色授权画面", userId);
        ModelAndView mv = new ModelAndView("/base/defaultRole/defaultRole");
        this.saveToken(req);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/getrolename")
    public List<AutoCompleteDTO> getRoleName(HttpSession session, Map<String, ?> model, String v) {
        return service.getRolesAutoByParam(v);
    }

    /**
     * 得到 默认角色授权grid集合对象
     * 
     * @param session
     * @param model
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getdate")
    public GridDataDTO<DefaultRoleGridDataDTO> getData(HttpSession session, Map<String, ?> model,
            DefaultRoleParamDTO param) {
        return service.getDataByParam(param);
    }

    /**
     * 提交编辑授权
     * 
     * @param session
     * @param model
     * @param v
     * @return
     */
    @ResponseBody
    @Permission(code = PermissionCode.P_CODE_DEFASS_EDIT)
    @RequestMapping(value = "/editdate")
    public AjaxResultDto editData(HttpServletRequest request, HttpSession session,
            Map<String, ?> model, DefaultRoleEditDTO param) {
        String userId = this.getUserId(session);
        log.debug("默认角色编辑提交,User:{}", userId);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            return ard;
        }
        String tempToken = ard.getToKen();
        param.setUserid(userId);
        ard = service.updateDataByParam(param);
        ard.setToKen(tempToken);
        return ard;
    }

    /**
     * 取消授权
     * 
     * @param session
     * @param model
     * @param v
     * @return
     */
    @ResponseBody
    @Permission(code = PermissionCode.P_CODE_DEFASS_CANCEL)
    @RequestMapping(value = "/cancel")
    public AjaxResultDto cancel(HttpServletRequest request, HttpSession session,
            Map<String, ?> model, Integer id) {
        String userId = this.getUserId(session);
        log.debug("默认角色-取消授权 提交,User:{} 默认角色ID：{}，", userId, id);
        AjaxResultDto ard = ajaxRepeatSubmitCheck(request, session);
        if (!ard.isSuccess()) {
            return ard;
        }
        String tempToken = ard.getToKen();
        ard = service.updateCancelDataById(id);
        ard.setToKen(tempToken);
        return ard;
    }

    /**
     * 得到 根据code或名称得到职务集合
     * 
     * @param session
     * @param model
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getpost")
    public List<AutoCompleteDTO> getPost(HttpSession session, Map<String, ?> model, String v) {
        return postService.getPostAutoByParam(v);
    }

    /**
     * 得到 根据code或名称得到店铺集合
     * 
     * @param session
     * @param model
     * @param v
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getstore")
    public List<AutoCompleteDTO> getStore(HttpSession session, Map<String, ?> model, String v) {
        return storeService.getStoreAutoByParam(v);
    }

}
