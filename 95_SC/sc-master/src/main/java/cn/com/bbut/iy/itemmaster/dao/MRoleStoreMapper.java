package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 角色&店铺关系Mapper
 *
 */
@Component
public interface MRoleStoreMapper{

    /**
     * 根据角色查询Store权限<br/>
     * 包含每级的名称
     * @param roleIds
     */
    List<RoleStoreDTO> selectStoreByRole(@Param("roleIds") Collection<Integer> roleIds);

    /**
     * 查询角色拥有的All权限
     * @param roleIds
     */
    List<RoleStoreDTO> selectListByAllCode(@Param("roleIds") Collection<Integer> roleIds);

    /**
     * 根据角色ID查询Region权限<br/>
     * 所有查询画面Query Condition --> Region栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> selectRegionByRoleId(@Param("dto")MRoleStoreParam dto);

    /**
     * 根据角色ID查询City权限<br/>
     * 所有查询画面Query Condition --> City栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> selectCityByRoleId(@Param("dto")MRoleStoreParam dto);

    /**
     * 根据角色ID查询District权限<br/>
     * 所有查询画面Query Condition --> District栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> selectDistrictByRoleId(@Param("dto")MRoleStoreParam dto);

    /**
     * 根据角色ID查询Store权限<br/>
     * 所有查询画面Query Condition --> Store栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> selectStoreByRoleId(@Param("dto")MRoleStoreParam dto);

    /**
     * 条件查询拥有店铺权限
     * @param dto
     */
    List<RoleStoreDTO> selectByCondition(@Param("dto")MRoleStoreParam dto);

    /**
     * 根据角色ID查询已添加店铺权限<br/>
     * Role Management 画面
     * @param roleId
     */
    List<RoleStoreDTO> selectListById(@Param("roleId")Integer roleId);

    /**
     * 新增记录
     * @param roleId
     * @param list
     */
    void insertRecord(@Param("roleId")Integer roleId, @Param("list")List<RoleStoreDTO> list);

    /**
     * 根据角色ID删除记录
     * @param roleId
     */
    void deleteById(@Param("roleId")Integer roleId);
}