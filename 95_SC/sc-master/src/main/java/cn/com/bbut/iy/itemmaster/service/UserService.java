package cn.com.bbut.iy.itemmaster.service;

import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.User;

/**
 * @author songxz
 */
public interface UserService {

    /**
     * 根据用户名取得用户信息
     * 
     * @param userId
     * @return
     */
    User getFullUserById(String userId);

    /**
     * 根据用户名取得登录密码
     * 
     * @param userId
     * @return
     */
    String getPassWordByUserId(String userId);

    /**
     * 根据用户id取得用户状态
     * @param userId
     * @return
     */
    String getUserStatus(String userId);

    List<AutoCompleteDTO> getUsersLikeUserId(String userId);

    /**
     * 根据用户id取得对应用户名
     *
     * @param userId
     * @return
     */
    String getIyedStaffNameByUserId(String userId);

    /**
     * 根据用户id取得对应店铺cd
     * @param userId
     * @return
     */
    Collection<String> getStoresByUserId(String userId);
}
