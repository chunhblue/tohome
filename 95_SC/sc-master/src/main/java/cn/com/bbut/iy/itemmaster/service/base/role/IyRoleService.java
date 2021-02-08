/**
 * ClassName  AccountService
 * History
 * Create User: admin
 * Create Date: 2017年6月16日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.service.base.role;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.*;
import cn.com.bbut.iy.itemmaster.entity.base.IymRoleRemark;
import cn.shiy.common.pmgr.entity.ResourceGroup;
import cn.shiy.common.pmgr.entity.Role;
import cn.shiy.common.pmgr.entity.RoleResources;

import java.util.Collection;
import java.util.List;

/**
 * @author lilw
 *
 */
public interface IyRoleService {

	public final static String COMMA = ",";

    public final static String OTHER_GROUP = "P-OTHER";

	/**
	 * 根据参数取得对应角色list信息
	 *
	 * @param param
	 * @return
	 */
	GridDataDTO<RoleGridDataDTO> getRoleListByParam(RoleParamDTO param);

	/**
	 * 删除角色
	 *
	 * @param roleId
	 * @return
	 */
	boolean deleteRoleById(Integer roleId);

	/**
	 * 判断用户的所有角色与传入的角色中 是否存在指定权限的重复
	 *
	 * @param userId
	 *            用户
	 * @param roleId
	 *            角色
	 * @param pCode
	 *            权限
	 * @return 不重复 true；重复 ：false
	 */
	Boolean isUserPermissRepeat(String userId, Integer roleId, String pCode);

	/**
	 * 判断角色名称是否重复
	 *
	 * @param roleId
	 * @param roleName
	 * @return
	 */
	boolean isNotExistRoleName(Integer roleId, String roleName);

	/**
	 * 根据角色id获取角色下的权限信息
	 *
	 * @param roleId
	 * @return
	 */
	List<ParentMenuDTO> getMenuAndPermForRole(Integer roleId);

	/**
	 * 根据所属权限组检索权限信息
	 *
	 * @param groupNumber
	 * @return
	 */
	List<PermissionToMenuDTO> getPermissionsByMenuId(String groupNumber);

	/**
	 * 添加或修改角色信息
	 *
	 * @param role
	 *            基础信息
	 * @param remark
	 *            概述信息
	 * @param resources
	 *            资源信息
	 * @param permissionStr
	 *            权限信息
	 * @param menuStr
	 *            菜单信息
	 * @param list
	 *            店铺信息
	 * @return
	 */
	boolean addRole(Role role, IymRoleRemark remark, List<RoleResources> resources,
			String permissionStr, String menuStr, List<RoleStoreDTO> list);

	/**
	 * 获取角色信息
	 *
	 * @param id
	 * @return
	 */
	Role getRoleById(Integer id);

	/**
	 * 获取概述信息
	 *
	 * @param roleId
	 * @return
	 */
	IymRoleRemark getRoleRemarkByRoleId(Integer roleId);



	/**
	 * 获取角色店铺信息
	 *
	 * @param roleId
	 * @return
	 */
	List<RoleStoreDTO> getStoresByRoleId(Integer roleId);



	/**
	 * 获取用户角色组资源信息
	 *
	 * @param roleIds
	 * @return
	 */
	List<ResourceViewDTO> getResourcesByRoleIds(Collection<Integer> roleIds);

	/**
	 * 获取角色资源信息
	 *
	 * @param roleId
	 * @return
	 */
	List<ResourceViewDTO> getResourcesByRoleId(Integer roleId);

	boolean delRole(Integer roleId);

	/**
	 * 根据角色名称模糊查询角色信息
	 * 
	 * @param roleName
	 * @return
	 */
	List<IyRoleDTO> getRoleInfosByRoleName(String roleName);

	/**
	 * 根据角色id查询角色信息
	 *
	 * @param roleId
	 * @return
	 */
	IyRoleDTO getRoleInfoByRoleId(Integer roleId);

	/**
	 * 根据用户id和权限检索资源信息
	 * 
	 * @param userId
	 *            用户id
	 * @param userDpt
	 *            dpt
	 * @param post
	 *            职务
	 * @param occup
	 *            职种
	 * @param permission
	 *            权限
	 * @return
	 */
	List<IyResourceDTO> getResource(String userId, String userDpt, String post, String occup,
			 String permission);

    /**
     * 根据角色名称检索角色数据
     * 
     * @param roleName
     *            角色名称
     * @param nullName
     *            角色名称空标识，用来判断角色名称为空时是否检索出数据，true：是，false：否
     * @return
     */
    List<Role> getRolesLikeName(String roleName, boolean nullName);

    List<Integer> getRoleIdsLikeName(String roleName);

    List<AutoCompleteDTO> getRolesLikeName(String userId, String roleName);

    /**
     * 根据资源信息及权限信息检索角色id集合
     * 
     * @param groups
     *            资源组
     * @param pCode
     *            权限
     * @return
     */
    Collection<Integer> getRoleIdsByResource(Collection<ResourceGroup> groups, String pCode);

    /**
     * 根据角色集合及权限code检索对应的资源信息
     * 
     * @param roleIds
     * @param pCode
     * @return
     */
    Collection<ResourceGroup> getResourcesByRoleAndPCode(List<Integer> roleIds, String pCode);

    /**
     * 根据资源组生成资源组检索条件信息
     * 
     * @param groups
     * @return
     */
    ConditionDTO createConditionsFromResourceGroup(Collection<ResourceGroup> groups);

}