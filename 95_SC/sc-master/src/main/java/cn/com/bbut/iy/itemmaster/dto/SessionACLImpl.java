/**
 * ClassName  SessionACLImpl
 * History
 * Create User: shiy
 * Create Date: 2015年3月26日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.entity.User;

/**
 * Session Access Control List 实现类
 * 
 * @author shiy
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionACLImpl implements SessionACL, Serializable {

    private static final long serialVersionUID = 1L;

    /** 当前登录用户 */
    private User user;

    /**
     * 当前用户
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }

}
