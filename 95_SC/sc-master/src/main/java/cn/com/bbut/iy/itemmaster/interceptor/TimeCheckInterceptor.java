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

import cn.com.bbut.iy.itemmaster.annotation.TimeCheck;
import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.com.bbut.iy.itemmaster.util.TimeUtil;

import com.google.gson.Gson;

/**
 * 时间段可用验证拦截期
 * <p>
 * 存在 @TimeCheck 注解的class做此验证
 *
 * @author shiy
 */
@Component
@Slf4j
public class TimeCheckInterceptor implements HandlerInterceptor {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${iy.timecheck.enabled}")
    private boolean isTimeCheckEnabled;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        TimeCheck timeCheck = handlerMethod.getMethodAnnotation(TimeCheck.class);
        if (timeCheck == null) {
            timeCheck = handlerMethod.getBeanType().getAnnotation(TimeCheck.class);
            if (timeCheck == null) {
                return true;
            }
        }
        // 验证为系统禁用时间时
//        if (isTimeCheckEnabled
//                && TimeUtil.isInTime(timeCheck.from(), timeCheck.to(), timeCheck.isNextDay())) {
        if(TimeUtil.isInTime(timeCheck.from(), timeCheck.to(), timeCheck.isNextDay())){
            // 获取来源
            String source = URLEncoder.encode(request.getRequestURL().toString(),
                    Constants.DEFAULT_ENCODING);
            // 获取报错信息
            String errMsg = applicationContext.getMessage("error.outofservice.system", null, null);
            // 获取跳转目标url
            String jumpTarget = applicationContext.getMessage("common.bm.outofservice", null, null);
            // 拼接url
            StringBuilder sb = new StringBuilder();
            sb.append(applicationContext.getMessage("common.msgpath", null, null)).append("?text=");
            sb.append(URLEncoder.encode(errMsg, Constants.DEFAULT_ENCODING)).append("&targetUrl=")
                    .append(jumpTarget);

            switch (timeCheck.value()) {
            // 页面跳转类型
            case PAGE:
                response.sendRedirect(sb.toString());
                break;
            // JSON跳转类型
            case JSON:
                AjaxResultDto rest = new AjaxResultDto();
                rest.setSuccess(false);
                rest.setSource("sys");
                rest.setMessage(errMsg);
                Gson gson = new Gson();
                String json = gson.toJson(rest);

                response.setCharacterEncoding(Constants.DEFAULT_ENCODING);
                response.setContentType("text/json;charset=" + Constants.DEFAULT_ENCODING);
                OutputStream out = response.getOutputStream();
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(out,
                        Constants.DEFAULT_ENCODING));
                pw.println(json);
                pw.flush();
                pw.close();
                break;
            // HTTP Header类型
            case HEADER:
                // header方式没有提供跳转回原url功能
                response.addIntHeader(Constants.SECURE_HEADER_NAME, Constants.ERR_CODE_OUTOFSERVICE);
                break;
            default:
                log.error("unknown timeCheck type:{}", timeCheck.value());
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
