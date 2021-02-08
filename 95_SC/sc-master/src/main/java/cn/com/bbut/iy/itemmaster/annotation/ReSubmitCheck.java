/**
 * ClassName  DisableReSubmit
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
 * 避免重复提交
 * 
 * @author Shiy
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReSubmitCheck {
    ReSubmitChkAction value();
}
