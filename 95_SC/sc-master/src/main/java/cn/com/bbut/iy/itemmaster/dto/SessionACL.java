/**
 * ClassName  SessionACL
 * History
 * Create User: shiy
 * Create Date: 2015年4月8日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto;

import cn.com.bbut.iy.itemmaster.entity.User;

/**
 * Session Access Control List
 * 
 * <p>
 * 对各业务模块只读
 * 
 * @author shiy
 *
 */
public interface SessionACL {
    /** 获取当前用户信息 */
    User getUser();
}
