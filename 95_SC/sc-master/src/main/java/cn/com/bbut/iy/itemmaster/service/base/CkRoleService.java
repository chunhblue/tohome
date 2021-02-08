package cn.com.bbut.iy.itemmaster.service.base;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import cn.shiy.common.pmgr.constant.ConstantCache;
import cn.shiy.common.pmgr.dto.PmgrRoleDataDTO;
import cn.shiy.common.pmgr.dto.PmgrRoleParamDTO;
import cn.shiy.common.pmgr.entity.ResourceGroup;
import cn.shiy.common.pmgr.entity.Role;

/**
 * 角色相关功能
 *
 * @author shiy
 */
public interface CkRoleService {

	/**
	 * 添加资源组到角色，<B>必须先删除再添加</B>
	 * <p>
	 * 预算整理好资源组，插入时分配组id
	 *
	 * @param roleid
	 *            要添加资源的角色 不可为空
	 * @param rgs
	 *            要添加的资源组 不可为空
	 */
	void addResource(Integer roleid, Collection<ResourceGroup> rgs);

	/**
	 * 添加权限到角色，<B>必须先删除再添加</B>
	 *
	 * @param roleid
	 *            要添加资源的角色
	 * @param pIds
	 *            要添加的权限
	 */
	@CacheEvict(value = { ConstantCache.CNAME_PCODEROLEID }, allEntries = true)
	void addPermission(Integer roleid, Collection<Integer> pIds);

	/**
	 * 根据角色id删除角色资源关联信息
	 *
	 * @param roleid
	 *            角色id
	 */
	void delResource(Integer roleid);

	/**
	 * 根据角色id删除角色许可关联信息
	 *
	 * @param roleid
	 *            角色id
	 * @return
	 */
	@CacheEvict(value = { ConstantCache.CNAME_PCODEROLEID }, allEntries = true)
	void delPermission(Integer roleid);

	/**
	 * 角色添加
	 * <p>
	 * 
	 * <pre>
	 * 先添加角色表获取角色id,根据角色id添加角色许可关联表、角色资源表（资源：菜单、部门等）
	 * </pre>
	 *
	 * @param role
	 *            要添加的角色
	 * @param rgs
	 *            资源组集合
	 * @param pIds
	 *            权限集合
	 */
	@Caching(evict = { @CacheEvict(value = { ConstantCache.CNAME_PCODEROLEID }, allEntries = true),
			@CacheEvict(value = { ConstantCache.CNAME_SUB_ROLE_ID }, allEntries = true) })
	void addRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds);

	/**
	 * void addRole 的重载（增加菜单）
	 * 
	 * @param role
	 *            要添加的角色
	 * @param rgs
	 *            资源组集合
	 * @param pIds
	 *            权限集合
	 * @param mIds
	 *            菜单集合
	 */
	Integer addRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds,
			Collection<Integer> mIds);

	/**
	 * 角色修改
	 * <p>
	 * 
	 * <pre>
	 * 修改角色表信息
	 * 先删除与角色id相关的角色许可关联、角色资源信息，重新添加角色许可关联、角色资源信息
	 * </pre>
	 *
	 * @param role
	 *            要修改的角色
	 * @param rgs
	 *            资源组集合
	 * @param pIds
	 *            权限集合
	 */
	@Caching(evict = { @CacheEvict(value = { ConstantCache.CNAME_PCODEROLEID }, allEntries = true),
			@CacheEvict(value = { ConstantCache.CNAME_SUB_ROLE_ID }, allEntries = true) })
	void updateRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds);

	/**
	 * 重载 void updateRole（）方法
	 * 
	 * @param role
	 * @param rgs
	 * @param pIds
	 * @param mIds
	 *            ：菜单id
	 * @return
	 */
	Integer updateRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds,
			Collection<Integer> mIds);

	/**
	 * 根据角色id删除角色信息及相关资源、权限
	 *
	 * @param roleid
	 *            要删除的角色
	 */
	@CacheEvict(value = ConstantCache.CNAME_SUB_ROLE_ID, allEntries = true)
	void delRole(Integer roleid);

	/**
	 * 取当前roleId的所有子及子子...roleId
	 *
	 * @param roleId
	 *            当前roleId
	 */
	@Cacheable(ConstantCache.CNAME_SUB_ROLE_ID)
	Collection<Integer> getAllSubRoleId(Collection<Integer> roleId);

	/**
	 * 取当前roleId集合的所有父及父父...不重复的roleId
	 *
	 * @param roleIds
	 *            当前roleId集合
	 */
	Collection<Integer> getAllSuperRoleId(Collection<Integer> roleIds);

	/**
	 * getAllSuperRoleId 的重载
	 * 
	 * @param roleId
	 *            角色id
	 */
	Collection<Integer> getAllSuperRoleId(int roleId);

	/**
	 * 添加角色菜单表
	 * 
	 * @param roleId
	 *            角色id
	 * @param menuId
	 *            菜单id
	 */
	@CacheEvict(value = ConstantCache.CNAME_ROLE_MENU, allEntries = true)
	void addRoleMenu(int roleId, int menuId);

	/**
	 * 添加角色菜单表
	 *
	 * @param roleId
	 *            角色id
	 * @param menuIds
	 *            菜单id集合
	 */
	@CacheEvict(value = ConstantCache.CNAME_ROLE_MENU, allEntries = true)
	void addRoleMenu(int roleId, Collection<Integer> menuIds);

	/**
	 * 删除指定角色的所有菜单关联
	 * 
	 * @param roleId
	 *            角色id
	 */
	@CacheEvict(value = ConstantCache.CNAME_ROLE_MENU, allEntries = true)
	void delRoleMenu(int roleId);

	/**
	 * 获取指定角色的菜单(不重复)
	 *
	 * 如果当前角色拥有父角色，则取得所有对应的菜单
	 *
	 * @param roleId
	 *            角色id
	 */
	@Cacheable(ConstantCache.CNAME_ROLE_MENU)
	Collection<Integer> getMenuIds(int roleId);

	/**
	 * 获取所有roleid对应的菜单（不重复），不考虑父角色，由外部控制
	 * 
	 * @param roleIds
	 *            角色id集合
	 * @return 菜单id集合或null
	 */
	Collection<Integer> getMenuIds(Collection<Integer> roleIds);

	/**
	 * 做成逗号分割的roleid字符串
	 *
	 * @param roleIds
	 *            不为空的角色id集合
	 */
	default String makeRoleIdStr(Collection<Integer> roleIds) {
		return roleIds.stream().map(x -> String.valueOf(x)).collect(Collectors.joining(","));
	}

	/**
	 * 根据参数取得角色集合
	 * 
	 * @param param
	 * @return
	 */
	PmgrRoleDataDTO selectRoleListByParam(PmgrRoleParamDTO param);

	/**
	 * 根据角色id取得角色数据
	 * 
	 * @param roleId
	 * @return
	 */
	Role getRole(Integer roleId);

	/**
	 * 取得可继承的角色集合（不可授权的）
	 * 
	 * @param companyIds
	 *            资源（公司ID）集合,如果有值 则使用该内容进行查找，如果为空，则取得全部
	 * @param ruleOutRoleID
	 *            需要排除在外的id，如果存在则，增加该内容的判断，如果为空则无视
	 * @return
	 */
	Collection<Role> getRoleByGrant(List<Integer> companyIds, Integer ruleOutRoleID);

	/**
	 * 取得角色id集合<br>
	 * 根据角色名称模糊和操作人的公司资源ID集合
	 * 
	 * @param roleName
	 *            角色名称需要传入（%name%）
	 * @param rtype
	 *            资源类型 (1:全公司,2:公司, 3:体系,4:核算单元,5:考核部门,6:考核部门细分)
	 * @param companyIds
	 *            公司资源id的集合
	 * @return
	 */
	Collection<Role> getRoleIdByRoleNameAndRes(String roleName, Integer rtype,
			List<Integer> companyIds);

}
