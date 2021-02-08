package cn.com.bbut.iy.itemmaster.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 时间段可用验证注解
 * 
 * @author Shiy
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeCheck {
    /** 返回类型 */
    ResultTypeEnum value() default ResultTypeEnum.PAGE;

    /** 起始时间段 */
    String from() default "";

    /** 截止时间段 */
    String to() default "";

    /** 截止时间是否跨天，默认为同一天，不跨天 */
    boolean isNextDay() default false;
}
