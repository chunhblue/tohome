/**
 * ClassName  SecureInterceptor
 * <p>
 * History
 * Create User: Shiy
 * Create Date: 2014年1月3日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.interceptor;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import cn.com.bbut.iy.itemmaster.annotation.Secure;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;

import com.google.gson.Gson;

/**
 * 是否登录的验证注解
 * <p>
 * 存在 @Secure 注解的class验证当前用户是否已登录 若已登录，继续后续操作;若未登录，按设定返回类型返回/跳转<br/>
 *
 * @author songxz
 */
@Component
@Slf4j
public class SecureInterceptor implements HandlerInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${iy.timecheck.enabled}")
    private boolean isTimeCheckEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Secure secure = handlerMethod.getMethodAnnotation(Secure.class);
        if (secure == null) {
            secure = handlerMethod.getBeanType().getAnnotation(Secure.class);
            if (secure == null) {
                return true;
            }
        }

        if (!hasAuthorized(request)) {
            String targetUrl = applicationContext.getMessage("common.loginpath", null, null);
            String text = URLEncoder.encode(
                    applicationContext.getMessage("error.common.notlogin", null, null),
                    Constants.DEFAULT_ENCODING);
            // String source =
            // URLEncoder.encode(request.getRequestURL().toString(),
            // Constants.DEFAULT_ENCODING);
            switch (secure.value()) {
            // 页面跳转类型
            case PAGE:
                response.sendRedirect(applicationContext.getMessage("common.syspath", null, null)
                        + "/tomsg?targetUrl=" + targetUrl + "&text=" + text);
                break;
            // JSON跳转类型
            case JSON:
                AjaxResultDto rest = new AjaxResultDto();
                rest.setSuccess(false);
                rest.setSource("sys");
                rest.setMessage(applicationContext.getMessage("error.common.notlogin", null, null));
                rest.setS(applicationContext.getMessage("common.syspath", null, null)
                        + "/tomsg?targetUrl=" + targetUrl + "&text=" + text);
                Gson gson = new Gson();
                String json = gson.toJson(rest);

                response.setCharacterEncoding(Constants.DEFAULT_ENCODING);
                response.setContentType("text/json;charset=" + Constants.DEFAULT_ENCODING);
                OutputStream out = response.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,
                        Constants.DEFAULT_ENCODING));
                // pw.println("{\"result\":false,\"errorMessage\":\""
                // + applicationContext.getMessage("error.common.notlogin",
                // null, null)
                // + "\",\"s\":\""
                // + applicationContext.getMessage("common.syspath", null, null)
                // + "/tomsg?targetUrl=" + targetUrl + "&text=" + text + "\"}");
                pw.println(json);
                pw.flush();
                pw.close();
                break;
            // HTTP Header类型
            case HEADER:
                // header方式没有提供跳转回原url功能
                response.addIntHeader(Constants.SECURE_HEADER_NAME, Constants.ERR_CODE_NOTLOGIN);
                break;
            default:
                log.error("unknown secure type:{}", secure.value());
            }
            return false;
        }

        // 验证为系统禁用时间时
        if (isTimeCheckEnabled
                && TimeUtil.isInTime(Constants.TIME_HALT_FROM, Constants.TIME_HALT_TO)) {
            // 获取来源
            String source = URLEncoder.encode(request.getRequestURL().toString(),
                    Constants.DEFAULT_ENCODING);
            // 获取报错信息
            String errMsg = applicationContext.getMessage("error.outofservice.system", null, null);
            // 获取跳转目标url
            String jumpTarget = applicationContext.getMessage("common.system.outofservice", null,
                    null);
            // 拼接url
            StringBuilder sb = new StringBuilder();
            sb.append(applicationContext.getMessage("common.msgpath", null, null)).append("?text=");
            sb.append(URLEncoder.encode(errMsg, Constants.DEFAULT_ENCODING)).append("&targetUrl=")
                    .append(jumpTarget);

            switch (secure.value()) {
            // 页面跳转类型
            case PAGE:
                response.sendRedirect(sb.toString());
                break;
            // JSON跳转类型
            case JSON:
                response.setCharacterEncoding(Constants.DEFAULT_ENCODING);
                response.setContentType("text/json;charset=" + Constants.DEFAULT_ENCODING);
                OutputStream out = response.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,
                        Constants.DEFAULT_ENCODING));
                pw.println("{\"result\":false,\"errorMessage\":\"" + errMsg + "\",\"s\":\""
                        + source + "\"}");
                pw.flush();
                pw.close();
                break;
            // HTTP Header类型
            case HEADER:
                // header方式没有提供跳转回原url功能
                response.addIntHeader(Constants.SECURE_HEADER_NAME, Constants.ERR_CODE_OUTOFSERVICE);
                break;
            default:
                log.error("unknown secure type:{}", secure.value());
            }
            return false;
        }

        return true;
    }

    /**
     * 用session判断用户是否登录
     *
     * @param request
     * @return
     */
    private boolean hasAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SessionACL acl = (SessionACL) session.getAttribute(Constants.SESSION_USER);
        return acl == null ? false : true;
    }

}
