package cn.com.bbut.iy.itemmaster.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.interceptor.PermissionInterceptor;
import cn.com.bbut.iy.itemmaster.interceptor.SecureInterceptor;
import cn.com.bbut.iy.itemmaster.interceptor.TimeCheckInterceptor;

/**
 * 和springmvc的webmvc拦截配置一样
 * 
 * @author sxz
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private SecureInterceptor secureInterceptor;

    @Autowired
    private PermissionInterceptor permissionInterceptor;

    @Autowired
    private TimeCheckInterceptor timeCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 载入登录拦截器验证
        registry.addInterceptor(SecureInterceptor()).addPathPatterns(Constants.REQ_HEADER + "/**");
        // 权限验证拦截器
        registry.addInterceptor(PermissionInterceptor())
                .addPathPatterns(Constants.REQ_HEADER + "/**");
        // 时间段可用验证拦截器
        registry.addInterceptor(TimeCheckInterceptor())
                .addPathPatterns(Constants.REQ_HEADER + "/**");
    }

    @Bean
    public SecureInterceptor SecureInterceptor() {
        return secureInterceptor;
    }

    @Bean
    public PermissionInterceptor PermissionInterceptor() {
        return permissionInterceptor;
    }

    @Bean
    public TimeCheckInterceptor TimeCheckInterceptor() {
        return timeCheckInterceptor;
    }
}