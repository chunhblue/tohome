package cn.com.bbut.iy.itemmaster.dao;

import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.User;

public interface UserMapper {
    /**
     * 获取完全的User信息，包含STORE,DPT
     * 
     * @param userid
     * @return
     */
    User getFullUserInfo(String userId);

    /**
     * 获取完全的User信息，包含STORE,DPT
     * 
     * @param userid
     *            password
     * 
     * @return
     */
    User getFullUserInfoNew(User user);

    /**
     * 根据用户名取得对应用户密码
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

    /**
     * 根据用户id模糊查询用户信息
     * 
     * @param userId
     * @return
     */
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