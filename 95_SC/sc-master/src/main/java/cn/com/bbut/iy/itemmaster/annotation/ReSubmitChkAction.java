/**
 * ClassName  ReSubmitChkAction
 *
 * History
 * Create User: Shiy
 * Create Date: 2014-09-23
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.annotation;

/**
 * 重提交验证用enum
 * 
 * @author Shiy
 */

public enum ReSubmitChkAction {
    /** 保存token */
    SAVE,
    /** ajax方式验证并删除token，错误时返回rsErr */
    CHECK_AJAX,
}
