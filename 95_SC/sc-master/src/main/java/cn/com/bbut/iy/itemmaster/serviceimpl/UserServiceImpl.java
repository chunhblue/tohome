package cn.com.bbut.iy.itemmaster.serviceimpl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.dao.UserMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.UserService;

/**
 * @author songxz
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getFullUserById(String userId) {
        return userMapper.getFullUserInfo(userId);
    }

    @Override
    public String getPassWordByUserId(String userId) {
        return userMapper.getPassWordByUserId(userId);
    }

    @Override
    public String getUserStatus(String userId) {
        return userMapper.getUserStatus(userId);
    }

    @Override
    public List<AutoCompleteDTO> getUsersLikeUserId(String userId) {
        String param = "%" + userId + "%";
        List<AutoCompleteDTO> dtos = userMapper.getUsersLikeUserId(param);
        if (dtos != null && !dtos.isEmpty()) {
            for (AutoCompleteDTO dto : dtos) {
                dto.setV(dto.getK().concat(" ").concat(dto.getV()));
            }
        }
        return dtos;
    }

    /**
     * 根据用户id取得对应用户名
     *
     * @param userId
     * @return
     */
    @Override
    public String getIyedStaffNameByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return null;
        }

        return userMapper.getIyedStaffNameByUserId(userId);
    }

    /**
     * 根据用户id取得对应店铺cd
     * @param userId
     * @return
     */
    @Override
    public Collection<String> getStoresByUserId(String userId) {
        return userMapper.getStoresByUserId(userId);
    }

}
