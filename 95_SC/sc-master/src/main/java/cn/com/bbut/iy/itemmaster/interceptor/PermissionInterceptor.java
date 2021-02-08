/**
 * ClassName  PermissionInterceptor
 *
 * History
 * Create User: Shiy
 * Create Date: 2014年1月17日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.interceptor;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import cn.com.bbut.iy.itemmaster.annotation.Permission;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.service.PermissionService;

import com.google.gson.Gson;

/**
 * Permission验证拦截器
 * 
 * @author Shiy
 */
@Component
@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Permission pm = handlerMethod.getMethodAnnotation(Permission.class);
        if (pm != null) {
            if (!hasAuthorized(request, pm)) {
                log.warn("no permission to {} with {}", request.getRequestURI(), pm.code());
                String source = URLEncoder.encode(request.getRequestURL().toString(),
                        Constants.DEFAULT_ENCODING);
                String targetUrl = applicationContext.getMessage("common.loginpath", null, null);
                String text = URLEncoder.encode(
                        applicationContext.getMessage("error.common.nopermission", null, null),
                        Constants.DEFAULT_ENCODING);
                switch (pm.returnType()) {
                case PAGE:
                    response.sendRedirect(applicationContext.getMessage("common.springpath", null,
                            null)
                            + Constants.REQ_HEADER
                            + "/tomsg?targetUrl="
                            + targetUrl
                            + "&text=" + text);
                    break;
                case JSON:

                    AjaxResultDto rest = new AjaxResultDto();
                    rest.setSuccess(false);
                    rest.setSource("sys");
                    rest.setMessage(applicationContext.getMessage("error.common.nopermission",
                            null, null));
                    Gson gson = new Gson();
                    String json = gson.toJson(rest);

                    response.setCharacterEncoding(Constants.DEFAULT_ENCODING);
                    response.setContentType("text/json;charset=" + Constants.DEFAULT_ENCODING);
                    OutputStream out = response.getOutputStream();
                    PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,
                            Constants.DEFAULT_ENCODING));
                    // pw.println("{\"result\":false,\"errorMessage\":\""
                    // + applicationContext
                    // .getMessage("error.common.nopermission", null, null)
                    // + "\",\"s\":\"" + source + "\"}");
                    pw.println(json);
                    pw.flush();
                    pw.close();
                    break;
                case HEADER:
                    // header方式没有提供跳转回原url功能
                    response.addIntHeader(Constants.SECURE_HEADER_NAME,
                            Constants.ERR_CODE_NOPERMISSION);
                    break;
                default:
                    log.error("unknown secure type:{}", pm.returnType());
                }
                return false;
            }

        }
        return true;
    }

    /**
     * 当前用户未登录、目标ActionBean设定的业务ID与当前用户对应业务ID无对应，都视为无权限
     *
     * @param context
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean hasAuthorized(HttpServletRequest request, Permission permission) {
        SessionACL acl = (SessionACL) request.getSession().getAttribute(Constants.SESSION_USER);
        if (acl == null || acl.getUser() == null) {
            // 当前用户未登录
            return false;
        }
        Collection<Integer> roleIds = (Collection) request.getSession().getAttribute(
                Constants.SESSION_ROLES);
        PermissionService pService = Container.getBean(PermissionService.class);
        if (permission.code() != null && !"".equals(permission.code())) {
            // 如果是一个特定权限
            return pService.isRolesHasPermission(roleIds, permission.code());
        } else {
            // 如果是多个特定权限
            String[] pcodes = permission.codes();
            for (String code : pcodes) {
                boolean isHas = pService.isRolesHasPermission(roleIds, code);
                if (isHas) {
                    return true;
                }
            }
            return false;
        }
    }
}
