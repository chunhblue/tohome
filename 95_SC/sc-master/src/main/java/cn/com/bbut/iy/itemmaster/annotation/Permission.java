/**
 * ClassName  Permission
 *
 * History
 * Create User: Shiy
 * Create Date: 2014-09-23
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Shiy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Permission {
    /**
     * 一个方法仅允许一个特定权限许可
     * 
     * @return
     */
    String code() default "";

    /**
     * 一个方法仅允许多个特定权限许可
     * 
     * @return
     */
    String[] codes() default {};

    /**
     * 返回类型
     * 
     * @return
     */
    ResultTypeEnum returnType() default ResultTypeEnum.PAGE;
}
