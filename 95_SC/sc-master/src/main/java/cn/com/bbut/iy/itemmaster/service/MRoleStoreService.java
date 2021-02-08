package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;

import java.util.Collection;
import java.util.List;
 
/**
 * @author mxy
 */
public interface MRoleStoreService {

    /**
     * 根据角色查询Store权限<br/>
     * @param roleIds
     */
    List<RoleStoreDTO> getStoreByRole(Collection<Integer> roleIds);

    /**
     * 根据已选择条件获取Store权限<br/>
     * @param param
     */
    Collection<String> getStoreByChoose(MRoleStoreParam param);

    /**
     * 获取全部Store权限<br/>
     * @param param
     */
    Collection<String> getAllStore(MRoleStoreParam param);

    /**
     * 根据角色ID查询Region权限<br/>
     * 所有查询画面Query Condition --> Region栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> getRegionByRoleId(MRoleStoreParam dto);

    /**
     * 根据角色ID查询City权限<br/>
     * 所有查询画面Query Condition --> City栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> getCityByRoleId(MRoleStoreParam dto);

    /**
     * 根据角色ID查询District权限<br/>
     * 所有查询画面Query Condition --> District栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> getDistrictByRoleId(MRoleStoreParam dto);

    /**
     * 根据角色ID查询Store权限<br/>
     * 所有查询画面Query Condition --> Store栏位请求数据
     * @param dto
     */
    List<AutoCompleteDTO> getStoreByRoleId(MRoleStoreParam dto);

    /**
     * 根据角色ID查询已添加店铺权限<br/>
     * Role Management 画面
     * @param roleId
     */
    List<RoleStoreDTO> getListById(Integer roleId);

    /**
     * 新增记录
     * @param roleId
     * @param list
     */
    int addRecord(Integer roleId, List<RoleStoreDTO> list);

    /**
     * 根据角色ID删除记录
     * @param roleId
     */
    void deleteById(Integer roleId);

}