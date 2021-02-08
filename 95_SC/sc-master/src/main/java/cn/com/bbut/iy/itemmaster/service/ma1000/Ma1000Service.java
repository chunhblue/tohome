package cn.com.bbut.iy.itemmaster.service.ma1000;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

public interface Ma1000Service {
    List<Ma1000> getList();

    List<Ma1000> selectByStoreCd(String storeCd);

    Integer getIsStoreAll(Collection<Integer> roleIds);


    /**
     * 根据权限获取店铺
     * @param storeCds 店铺权限id
     * @param v  模糊搜索店铺cd 店铺名称 可为空
     * @return
     */
    List<AutoCompleteDTO> getListByStorePm(Collection<String> storeCds, String v);

    /**
     * 根据权限获取店铺
     * @param roleId 角色集合
     * @param v  模糊搜索店铺cd 店铺名称 可为空
     * @return
     */
    List<AutoCompleteDTO> getListByPM(Collection<Integer> roleId, String v);

    /**
     * 根据角色获取店铺
     * @param roleId 角色集合
     * @return
     */
    Collection<String> getStoreListByRole(Collection<Integer> roleId);

    /**
     * 获取全店铺
     * @param v  模糊搜索店铺cd 店铺名称 可为空
     * @return
     */
    List<AutoCompleteDTO> getListAll(String v);

    List<RoleStoreDTO> getListByRoleId(Integer roleId);

    Integer addStorebyRole(Integer roleId,List<String> stores);

    void delRoleStroe(int roleId);
    List<Ma1000> selectStoreByStoreCd(String storeCd);

    List<AutoCompleteDTO> getAMByPM(Collection<String> stores, String v);

    List<AutoCompleteDTO> getOm(Collection<String> stores, String v);

    List<AutoCompleteDTO> getOc(Collection<String> stores, String v);
}
