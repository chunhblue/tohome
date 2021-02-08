package cn.com.bbut.iy.itemmaster.controller;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.com.bbut.iy.itemmaster.dto.SessionACLImpl;
import cn.com.bbut.iy.itemmaster.dto.base.MenuDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.UserService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.base.IymActorAssignmentService;
import cn.com.bbut.iy.itemmaster.service.base.MenuService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import cn.com.bbut.iy.itemmaster.util.ImageUtil;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;
import cn.com.bbut.iy.itemmaster.util.common.Code;
import cn.shiy.common.pmgr.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author songxz
 */
@Controller
@RequestMapping(value = Constants.REQ_HEADER)
@Slf4j
public class LoginController extends BaseAction {

    @Autowired
    private Code code;
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DefaultRoleService defAssignmentService;
    @Autowired
    private IyRoleService iyRoleService;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private IymActorAssignmentService iymActorAssignmentService;

    @Autowired
    private Ma1000Service ma1000Service;
    /**
     * 管理userid和sessionId的对应关系（利用缓存管理）
     */
    @Autowired
    private CacheManager sessionIdMgr;


    /** 用户被锁定 返回页面的标记 */
    private final String LOGIN_USER_LOCKED = "login_user_locked";
    /** 用户被锁定的状态 */
    private final String USER_LOCKED_STATUS = "20";
    /** 登录验证码输入错误 返回页面的标记 */
    private final String LOGIN_CAPTCHA_ERROR = "login_captcha_error";
    /** 登录失败 */
    private final String LOGIN_ERROR = "login_error";
    /** 登录成功 */
    private final String LOGIN_SUCCESS = "login_success";

    /**
     * 进入登录页面
     * 
     * @param request
     * @param session
     * @return
     */
    @RequestMapping(value = "/login")
    public ModelAndView loginView(HttpServletRequest request, HttpSession session,
            @RequestParam(required = false) String userid,
            @RequestParam(required = false) String errMsg) {
        ModelAndView mv = new ModelAndView("login");

        if (errMsg != null) {
            mv.addObject(Constants.P_ERR_MSG, errMsg);
        }
        if (userid != null) {
            mv.addObject(Constants.P_USER_ID, userid);
        }
        String remoteIP = getRemoteIP(request);
        log.debug("{} in login", remoteIP);
        return mv;
    }

    /**
     * 用户登录验证
     * 
     * @param userid
     *            账号
     * @param password
     *            密码
     * @return
     */
    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response, HttpSession session,
            Map<String, ?> model, String userid, String password,
            @RequestParam(required = false) String captcha) {

        String remoteIP = getRemoteIP(request);
        // 验证码检验
        String capchaSession = (String) session.getAttribute(Constants.S_CAPTCHA_CODE);
        if (capchaSession != null && !capchaSession.equals(captcha)) {
            return getRestMode(userid, LOGIN_CAPTCHA_ERROR);
        }

        User user = userService.getFullUserById(userid);
        String dbPassword = userService.getPassWordByUserId(userid);

        //验证用户是否锁定
        String userStatus = userService.getUserStatus(userid);
        if (StringUtils.isNotBlank(userStatus)&&USER_LOCKED_STATUS.equals(userStatus)) {//用户被锁定
            return getRestMode(userid, LOGIN_USER_LOCKED);
        }

        if (user == null) {
            // 显示验证码
            session.setAttribute(Constants.S_SHOW_CAPTCHA, Boolean.TRUE.toString());
            return getRestMode(userid, LOGIN_ERROR);
        } else {
            if (isAuthorizedPass(dbPassword, password)) {
                session.invalidate();
                session = request.getSession(true);
                SessionACL acl = new SessionACLImpl(user);

                // 将用户信息放入session中
                session.setAttribute(Constants.SESSION_USER, acl);

                Collection<Integer> roleIds = new ArrayList<Integer>();

                Collection<Integer> tempRoleIds = iymActorAssignmentService
                        .getSpecialRoleIdsByActorId(user.getUserId());
                if (tempRoleIds == null) {
                    // 如果特殊角色为空，则取得该用户所属的默认角色，通过该人员的 职务，职种，dpt
                    tempRoleIds = defAssignmentService.getDefRoleId(user.getJobTypeCd());
                }
                if (tempRoleIds != null) {
                    roleIds.addAll(tempRoleIds);
                }
                // 取得该用户的待审角色，
                Collection<Integer> replaceRoleIds = iymActorAssignmentService
                        .getSpecialAlternateRoleIdsByActorId(user.getUserId(), TimeUtil.getDate());
                if (replaceRoleIds != null) {
                    roleIds.addAll(replaceRoleIds);
                }
                // 将用户角色放入session中
                session.setAttribute(Constants.SESSION_ROLES, roleIds);

                // 菜单放入session
                Collection<MenuDTO> menus = getMenusByRoleIds(roleIds);
                if (menus != null) {
                    session.setAttribute(Constants.SESSION_MENUS, menus);
                }
                // 将用户角色权限店铺放入session
                List<ResourceViewDTO> resources = getResourcesByRoleIds(roleIds);
                if (resources!=null){
                    session.setAttribute(Constants.SESSION_RESOURCES, resources);
                }
                // 将用户角色权限店铺放入session
                Collection<String> stores = new HashSet<>();
                //判断用户是否设置默认店铺
                if(StringUtils.isNotBlank(user.getStoreCd())){
                    stores.add(user.getStoreCd());
                }else{
//                    stores = getStoresByRoleIds(roleIds);
                    //获取用户角色权限店铺 by ma4210
                    stores = getStoresByUserId(user.getUserId());
                }
                if (stores!=null&&stores.size()>0){
                    session.setAttribute(Constants.SESSION_STORES, stores);
                }
                log.debug("user: {} login success from tried : from {}", userid, remoteIP);
                return getRestMode(userid, LOGIN_SUCCESS);
            } else {
                session.setAttribute(Constants.S_SHOW_CAPTCHA, Boolean.TRUE.toString());
                return getRestMode(userid, LOGIN_ERROR);
            }
        }
    }

    /**
     * 
     * @return
     */
    private ModelAndView getRestMode(String userid, String type) {
        ModelAndView mv = null;
        switch (type) {
        case LOGIN_USER_LOCKED:
            // 用户被锁定
            mv = new ModelAndView("redirect:" + Constants.REQ_HEADER + "/login");
            mv.addObject(Constants.P_USER_ID, userid);
            mv.addObject(Constants.P_CODE, 1);
            mv.addObject(Constants.P_ERR_MSG,
                    applicationContext.getMessage("error.user.locked", null, null));
            break;
        case LOGIN_CAPTCHA_ERROR:
            // 验证码错误
            mv = new ModelAndView("redirect:" + Constants.REQ_HEADER + "/login");
            mv.addObject(Constants.P_USER_ID, userid);
            mv.addObject(Constants.P_CODE, 1);
            mv.addObject(Constants.P_ERR_MSG,
                    applicationContext.getMessage("error.login.capcha", null, null));
            break;
        case LOGIN_ERROR:
            // 登录失败
            mv = new ModelAndView("redirect:" + Constants.REQ_HEADER + "/login");
            mv.addObject(Constants.P_USER_ID, userid);
            mv.addObject(Constants.P_CODE, 1);
            mv.addObject(Constants.P_ERR_MSG,
                    applicationContext.getMessage("error.login.fail", null, null));
            break;
        case LOGIN_SUCCESS:
            // 登录成功
            mv = new ModelAndView("redirect:" + Constants.REQ_HEADER + "/index");
            break;
        }
        return mv;
    }

    /**
     * 根据当前登录用户的角色集合取得菜单
     * 
     * @param roleId
     *            角色id集合
     * @return 当前用户权限对应的菜单组或null
     */
    private Collection<MenuDTO> getMenusByRoleIds(Collection<Integer> roleId) {
        return menuService.getMenus(roleId);
    }

    /**
     * 根据当前登录用户的角色集合取得资源（部门，大中小分类）
     *
     * @param roleId
     *            角色id集合
     * @return 当前用户权限对应的资源或null
     */
    private List<ResourceViewDTO> getResourcesByRoleIds(Collection<Integer> roleId) {
        return iyRoleService.getResourcesByRoleIds(roleId);
    }

    /**
     * 根据当前登录用户的角色集合取得店铺
     *
     * @param roleId
     *            角色id集合
     * @return 当前用户权限对应的店铺或null
     */
    private Collection<String> getStoresByRoleIds(Collection<Integer> roleId) {
        MRoleStoreParam param = new MRoleStoreParam();
        param.setRoleIds(roleId);
        return mRoleStoreService.getAllStore(param);
    }

    /**
     * 根据当前登录用户的id取得店铺
     * @param userId 用户id
     * @return 当前用户权限对应的店铺或null
     */
    private Collection<String> getStoresByUserId(String userId) {
        return userService.getStoresByUserId(userId);
    }

    /**
     * 显示验证码
     */
    @RequestMapping(value = "/captcha")
    public void showCaptcha(HttpSession session,HttpServletRequest request, HttpServletResponse response) throws IOException {
        //设置图片格式
        response.setContentType("image/png");
        Object[] objs = ImageUtil.createImage2();
        // 将验证码存入Session
        session.setAttribute(Constants.S_CAPTCHA_CODE, objs[0]);
        // 将图片输出给浏览器
        BufferedImage image = (BufferedImage) objs[1];
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, Constants.L_PNG, os);
        response.flushBuffer();
    }

    /**
     * 退出
     * <p>
     * 1. 清理session 2. 清理用户id对应的缓存
     */
    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpServletRequest request, HttpSession session) {
        // SessionACL acl = (SessionACL)
        // session.getAttribute(Constants.SESSION_USER);
        // if (acl != null) {
        // String userId = acl.getUser().getUserId();
        // Cache sessionCache =
        // sessionIdMgr.getCache(ConstantsCache.CAHCE_SESSION_ID);
        // sessionCache.evict(userId);
        // }
        session.invalidate();
        ModelAndView mv = new ModelAndView("redirect:" + Constants.REQ_HEADER + "/login");
        return mv;
    }

    /**
     * 验证密码是否一致
     * 
     * @param pass
     *            原始密码
     * @param pass2chk
     *            指定密码
     * @return
     */
    private boolean isAuthorizedPass(String pass, String pass2chk) {
        pass2chk = DigestUtils.md5DigestAsHex(pass2chk.getBytes());
        return StringUtils.equals(pass2chk, pass) ? true : false;
    }
}
