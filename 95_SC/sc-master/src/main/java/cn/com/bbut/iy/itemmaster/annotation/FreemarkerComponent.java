package cn.com.bbut.iy.itemmaster.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * Freemarker 页面中使用的权限标签注解声明
 * 
 * @author songxz
 * @date: 2019年10月10日 - 下午4:04:52
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface FreemarkerComponent {
    String value() default "";
}