/**
 * ClassName  Secure
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
 * 是否登录验证用注解
 * <p>
 * 参数指定返回类型 ResultTypeEnum.PAGE : 默认类型，普通页面调整 ResultTypeEnum.JSON :
 * 对应JSON请求，返回包含跳转地址的JSON ResultTypeEnum.HEADER : 对应特殊情况，在HTTP Header中标记报错信息
 * </p>
 * 
 * @author Shiy
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Secure {
    ResultTypeEnum value() default ResultTypeEnum.PAGE;
}
