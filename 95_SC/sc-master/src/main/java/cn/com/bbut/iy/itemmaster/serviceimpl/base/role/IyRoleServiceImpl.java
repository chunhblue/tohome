/**
 * ClassName  AccountServiceImpl
 * History
 * Create User: admin
 * Create Date: 2017年6月16日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.serviceimpl.base.role;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.constant.ConstantsDB;
import cn.com.bbut.iy.itemmaster.constant.PermissionCode;
import cn.com.bbut.iy.itemmaster.dao.IymActorAssignmentInfoMapper;
import cn.com.bbut.iy.itemmaster.dao.IymRoleRemarkMapper;
import cn.com.bbut.iy.itemmaster.dto.SessionACL;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ConditionDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.*;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.base.IymRoleRemark;
import cn.com.bbut.iy.itemmaster.entity.base.IymRoleRemarkExample;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.entity.ma0090.MA0090;
import cn.com.bbut.iy.itemmaster.entity.ma0100.MA0100;
import cn.com.bbut.iy.itemmaster.exception.SystemRuntimeException;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.StoreService;
import cn.com.bbut.iy.itemmaster.service.base.CkRoleService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.base.DepService;
import cn.com.bbut.iy.itemmaster.service.base.MenuService;
import cn.com.bbut.iy.itemmaster.service.base.role.IyRoleService;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.dao.PermissionMapper;
import cn.shiy.common.pmgr.dao.RoleAssignmentMapper;
import cn.shiy.common.pmgr.dao.RoleMapper;
import cn.shiy.common.pmgr.dao.RoleResourcesMapper;
import cn.shiy.common.pmgr.entity.*;
import cn.shiy.common.pmgr.service.PermissionService;
import cn.shiy.common.pmgr.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author songxz
 *
 */
@Service
@Slf4j
public class IyRoleServiceImpl implements IyRoleService {

    /**
     * ui
     */
    // @Autowired
    // PmgrUiCommService PmgrUiCommService;

    /**
     * 角色核心
     */
    @Autowired
    RoleService roleService;
    /**
     * 角色核心
     */
    @Autowired
    CkRoleService ckService;
    /**
     * 权限核心
     */
    @Autowired
    PermissionService permissService;
    /**
     * 菜单核心
     */
    @Autowired
    MenuService menuService;

    @Autowired
    DepService depService;
    @Autowired
    StoreService storeService;
    @Autowired
    MRoleStoreService mRoleStoreService;
    @Autowired
    DefaultRoleService defService;
    @Autowired
    PermissionService permService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RoleResourcesMapper rrMapper;

    /**
     * 菜单核心
     */
    @Autowired
    RoleMapper mapper;

    @Autowired
    IymRoleRemarkMapper remarkMapper;

    @Autowired
    RoleAssignmentMapper raMapper;

    @Autowired
    PermissionMapper permMapper;
    @Autowired
    IymActorAssignmentInfoMapper aaiMapper;

    @Override
    public GridDataDTO<RoleGridDataDTO> getRoleListByParam(RoleParamDTO param) {
        // 调用角色核心方法，取得角色集合：角色名称，父角色名称，是否可授权，资源类型，资源id
        List<Role> roles = remarkMapper.selectRolesByExample(param);
        long count = remarkMapper.selectCountRolesByExample(param);
        List<RoleGridDataDTO> resultList = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            for (Role rs : roles) {
                RoleGridDataDTO dto = new RoleGridDataDTO();
                dto.setId(rs.getId());
                dto.setName(rs.getName());
                IymRoleRemarkExample rre = new IymRoleRemarkExample();
                IymRoleRemarkExample.Criteria rc = rre.or();
                rc.andRoleIdEqualTo(rs.getId());
                List<IymRoleRemark> rrs = remarkMapper.selectByExample(rre);
                if (rrs != null && rrs.size() > 0) {
                    dto.setRemark(rrs.get(0).getRemark());
                }
                resultList.add(dto);
            }
        }

        GridDataDTO<RoleGridDataDTO> data = new GridDataDTO<RoleGridDataDTO>(resultList,
                param.getPage(), count, param.getRows());
        return data;

    }

    @Override
    public boolean deleteRoleById(Integer roleId) {
        try {
            roleService.delRoleMenu(roleId);
            roleService.delRole(roleId);
            return true;
        } catch (SystemRuntimeException e) {
            throw new SystemRuntimeException("Add角色失败");// 给用户看的
        }
    }

    @Override
    public Boolean isUserPermissRepeat(String userId, Integer roleId, String pCode) {
        // 判断该用户是否有指定的权限
        boolean isActorHasPer = permissService.isActorHasPermission(userId, pCode);
        // 没有指定权限 直接返回true.
        if (isActorHasPer) {
            // 存在指定的权限，再次通过“roleId”取得该角色是否有该权限
            boolean isRoleHasPer = permissService.isRoleHasPermission(roleId, pCode);
            if (isRoleHasPer) {
                // 资源类型
                HashSet<Integer> groupType = new HashSet<>();
                // groupType.add(ResourcesTypeEnum.DEPARTMENTSEG.getIntValue());
                // 指定的用户已经存在了指定的权限，并且指定的角色也存在了指定的权限，需要判断用户的角色资源“考核部门细分”，与指定的角色所属的资源“考核部门细分”是否是一个公司内，如果是则返回false，否则true
                // 得到用户的角色集合
                Collection<Integer> userRoleids = permissService.getRoleIdsByActorId(userId);
                // 如果用户已经存在了该角色则返回false
                if (userRoleids.contains(roleId)) {
                    return false;
                }
                // 得到用户角色的特定资源
                Collection<ResourceGroup> userResource = permissService.getResource(userId,
                        groupType, pCode);
                Collection<String> uStrId = permissService.getResourceIds(userResource);// 得到指定资源id（考核部门细分）
                Collection<Integer> userCompanyIds = new ArrayList<>();
                if (uStrId != null && uStrId.size() > 0) {
                    for (String rid : uStrId) {
                    }
                }

                // 根据指定角色 权限 到的“考核部门细分”类型资源
                Collection<ResourceGroup> roleResource = permissService.getResource(roleId,
                        groupType, pCode);
                Collection<String> rStrId = permissService.getResourceIds(roleResource);// 得到指定资源id（考核部门细分）
                // 考虑到系统中的业务设定，一个用户在同公司下只能有一个“人员部门归属权限”，所以在此进行是否同公司的判断，如果存在则返回false，
                Collection<Integer> roleCompanyIds = new ArrayList<>();
                if (rStrId != null && rStrId.size() > 0) {
                    for (String rid : rStrId) {
                    }
                }
                // 使用交集方式处理
                userCompanyIds.retainAll(roleCompanyIds);
                if (userCompanyIds.size() == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                // 指定的角色也没有指定权限 直接返回true
                return true;
            }
        } else {
            return true;
        }

    }

    @Override
    public boolean isNotExistRoleName(Integer roleId, String roleName) {
        if (StringUtils.isBlank(roleName)) {
            return false;
        }
        RoleExample example = new RoleExample();
        RoleExample.Criteria cri = example.or();
        if (roleId != null) {
            cri.andIdNotEqualTo(roleId);
        }
        cri.andNameEqualTo(roleName);
        long count = mapper.countByExample(example);

        return count == 0 ? true : false;
    }

    /**
     * 根据角色id获取角色下的权限信息
     *
     * @param roleId
     * @return
     */
    @Override
    public List<ParentMenuDTO> getMenuAndPermForRole(Integer roleId) {
        List<RoleAssignment> ras = null;
        if (roleId != null) {
            RoleAssignmentExample e = new RoleAssignmentExample();
            e.or().andRoleIdEqualTo(roleId);
            ras = raMapper.selectByExample(e);
        }
        final List<Integer> permIds = new ArrayList<>();
        if (ras != null && !ras.isEmpty()) {
            ras.forEach(ra -> permIds.add(ra.getPermissionId()));
        }

        Collection<Menu> menus = menuService.getAllMenus("parent_id asc, sort asc");
        Map<Integer, ParentMenuDTO> map = new HashMap<>();
        menus.forEach(menu -> {
            if (menu.getParentId() == null) {
                ParentMenuDTO pm = new ParentMenuDTO(menu.getId(), menu.getName(), null);
                map.put(menu.getId(), pm);
            }
        });
        IyRoleService service = Container.getBean(IyRoleService.class);
        menus.forEach(menu -> {
            if (menu.getParentId() != null) {
                List<PermissionToMenuDTO> perms = service.getPermissionsByMenuId(menu.getGroup());
                if (!permIds.isEmpty() && perms != null && !perms.isEmpty()) {
                    perms.forEach(perm -> {
                        if (permIds.contains(perm.getPermId())) {
                            perm.setFlg(ConstantsDB.COMMON_ONE);
                        } else {
                            perm.setFlg(ConstantsDB.COMMON_TWO);
                        }
                    });
                }
                ParentMenuDTO pm = map.get(menu.getParentId());
                List<MenuPermDTO> sons = null;
                if (pm.getSonMenus() == null) {
                    sons = new ArrayList<>();
                    MenuPermDTO son = new MenuPermDTO(menu.getId(), menu.getName(), perms);
                    sons.add(son);
                } else {
                    sons = pm.getSonMenus();
                    MenuPermDTO son = new MenuPermDTO(menu.getId(), menu.getName(), perms);
                    sons.add(son);
                }
                pm.setSonMenus(sons);
            }
        });
        List<ParentMenuDTO> dtos = new ArrayList<>(map.values());
        List<PermissionToMenuDTO> perms = service.getPermissionsByMenuId(IyRoleService.OTHER_GROUP);
        if (!permIds.isEmpty() && perms != null && !perms.isEmpty()) {
            perms.forEach(perm -> {
                if (permIds.contains(perm.getPermId())) {
                    perm.setFlg(ConstantsDB.COMMON_ONE);
                } else {
                    perm.setFlg(ConstantsDB.COMMON_TWO);
                }
            });
        }
        MenuPermDTO son = new MenuPermDTO(ConstantsDB.COMMON_ZERO, "", perms);
        List<MenuPermDTO> sons = new ArrayList<MenuPermDTO>();
        sons.add(son);
//        ParentMenuDTO dto = new ParentMenuDTO(ConstantsDB.COMMON_ZERO, "Other", sons);
//        dtos.add(ConstantsDB.COMMON_ZERO, dto);
        return dtos;
    }

    /**
     * 根据所属权限组检索权限信息
     *
     * @param groupNumber
     * @return
     */
    @Override
    public List<PermissionToMenuDTO> getPermissionsByMenuId(String groupNumber) {
        if (StringUtils.isBlank(groupNumber)) {
            return null;
        }
        PermissionExample pe = new PermissionExample();
        //权限信息排序
        pe.setOrderByClause("sort asc");
        pe.or().andGroupCodeEqualTo(groupNumber);
        List<Permission> ps = permMapper.selectByExample(pe);
        if (ps != null && !ps.isEmpty()) {
            List<PermissionToMenuDTO> dtos = new ArrayList<PermissionToMenuDTO>() {
                {
                    for (Permission p : ps) {
                        add(new PermissionToMenuDTO(p.getId(), p.getName(), null));
                    }
                }
            };
            return dtos;
        }
        return null;
    }

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
     * @return
     */
    @Override
    public boolean addRole(Role role, IymRoleRemark remark, List<RoleResources> resources,
            String permissionStr, String menuStr, List<RoleStoreDTO> list) {
        IyRoleService service = Container.getBean(IyRoleService.class);
        boolean b = service.isNotExistRoleName(role.getId(), role.getName());
        if (!b) {
            throw new SystemRuntimeException("角色名称重复，不可提交") {
            };
        }
        List<Integer> permIds = null;
        if (StringUtils.isNotBlank(permissionStr)) {
            String[] permArr = permissionStr.split(IyRoleService.COMMA);
            if (permArr != null && permArr.length > 0) {
                permIds = new ArrayList<Integer>() {
                    {
                        for (int i = 0; i < permArr.length; i++) {
                            add(Integer.parseInt(permArr[i]));
                        }
                    }
                };

            }
        }

        List<Integer> menuIds = null;
        if (StringUtils.isNotBlank(menuStr)) {
            String[] menuArr = menuStr.split(IyRoleService.COMMA);
            if (menuArr != null && menuArr.length > 0) {
                menuIds = new ArrayList<Integer>() {
                    {
                        for (int i = 0; i < menuArr.length; i++) {
                            Integer menuId = Integer.parseInt(menuArr[i]);
                            if (menuId != ConstantsDB.COMMON_ZERO) {
                                add(menuId);
                            }
                        }
                    }
                };

            }
        }

        Set<Integer> groupCodes = new HashSet<>();
        if(resources != null && !resources.isEmpty()){
            resources.forEach(resource ->{
                groupCodes.add(resource.getGroupCode());
            });
        }
        List<ResourceGroup> groups = new ArrayList<ResourceGroup>() {
            {
                groupCodes.forEach(groupCode -> {
                    ResourceGroup group = new ResourceGroup();
                    for (RoleResources resource : resources) {
                        if(groupCode.equals(resource.getGroupCode())){
                            group.add(new Resource(resource.getResourceId(), resource.getType()));
                        }
                    }
                    add(group);
                });
            }
        };

        boolean result = false;
        Integer roleId = null;
        if (role.getId() == null) {
            roleId = ckService.addRole(role, groups, permIds, menuIds);
        } else {
            roleId = ckService.updateRole(role, groups, permIds, menuIds);
        }

        if (roleId != null && roleId > 0 && list != null) {
            mRoleStoreService.addRecord(roleId, list);
            result = true;
        }

        IymRoleRemarkExample e = new IymRoleRemarkExample();
        e.or().andRoleIdEqualTo(roleId);
        remark.setRoleId(roleId);
        long count = remarkMapper.countByExample(e);
        if (count > 0) {
            int updateCount = remarkMapper.updateByExample(remark, e);
            if (updateCount > 0) {
                result = true;
            }
        } else {
            int insertCount = remarkMapper.insert(remark);
            if (insertCount > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 获取角色信息
     *
     * @param id
     * @return
     */
    @Override
    public Role getRoleById(Integer id) {
        if (id == null) {
            return null;
        }
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 获取概述信息
     *
     * @param roleId
     * @return
     */
    @Override
    public IymRoleRemark getRoleRemarkByRoleId(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        IymRoleRemarkExample e = new IymRoleRemarkExample();
        e.or().andRoleIdEqualTo(roleId);
        List<IymRoleRemark> remarks = remarkMapper.selectByExample(e);
        if (remarks != null && remarks.size() > 0) {
            return remarks.get(ConstantsDB.COMMON_ZERO);
        }
        return null;
    }

    @Override
    public List<RoleStoreDTO> getStoresByRoleId(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        List<RoleStoreDTO> list = mRoleStoreService.getListById(roleId);
        return list;
    }

    @Override
    public List<ResourceViewDTO> getResourcesByRoleIds(Collection<Integer> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return null;
        }
        RoleResourcesExample ex = new RoleResourcesExample();
        ex.setDistinct(true);
        List<Integer> values = new ArrayList<>(roleIds);
        ex.or().andRoleIdIn(values);
        List<RoleResources> rrList = rrMapper.selectByExample(ex);

        //资源组
        List<ResourceViewDTO> dtos = new ArrayList<>();

        if (rrList != null && rrList.size() > 0) {
            Collection<ResourceGroup> restGroup = new ArrayList<>();
            // 分组 使用map
            Map<String, List<RoleResources>> restMap = new HashMap<>();
            for (RoleResources rr : rrList) {
                //判断是否是全资源
                if (depService.ALL_DEP.equals(rr.getResourceId())) {
                    ResourceViewDTO dto = new ResourceViewDTO();
                    dto.setDepCd(depService.ALL_DEP);
                    dto.setDepName("all");
                    dtos.add(dto);
                    return dtos;
                }
                List<RoleResources> vals = restMap.get(String.valueOf(rr.getRoleId())+rr.getGroupCode());
                if (vals != null && vals.size() > 0) {
                    vals.add(rr);
                } else {
                    vals = new ArrayList<>();
                    vals.add(rr);
                    restMap.put(String.valueOf(rr.getRoleId())+rr.getGroupCode(), vals);
                }
            }

            // 从map中取出对应组内容 然后放入返回对象中。
            Iterator<Map.Entry<String, List<RoleResources>>> entries = restMap.entrySet()
                    .iterator();
            while (entries.hasNext()) {
                Map.Entry<String, List<RoleResources>> entry = entries.next();
                ResourceGroup rGroup = new ResourceGroup();
                for (RoleResources rr : entry.getValue()) {
                    Resource r = new Resource();
                    r.setId(rr.getResourceId());
                    r.setType(rr.getType());
                    rGroup.add(r);
                }
                restGroup.add(rGroup);
            }

            if (restGroup == null || restGroup.isEmpty()) {
                return null;
            }
            restGroup.stream().forEach(group -> {
                ResourceViewDTO dto = new ResourceViewDTO();
                group.getGroup().forEach(resource -> {
                    switch (resource.getType()) {
                        case 1:
                            dto.setDepCd(resource.getId());
                            break;
                        case 2:
                            dto.setPmaCd(resource.getId());
                            break;
                        case 3:
                            dto.setCategoryCd(resource.getId());
                            break;
                        case 4:
                            dto.setSubCategoryCd(resource.getId());
                            break;
                    }
                });
                dtos.add(dto);
            });
            return dtos;
        } else {
            return null;
        }


    }

    /**
     * 获取角色资源信息
     *
     * @param roleId
     * @return
     */
    @Override
    public List<ResourceViewDTO> getResourcesByRoleId(Integer roleId) {
        if (roleId == null) {
            return null;
        }

        Collection<ResourceGroup> groups = permissService.getResourceGroupByRoleId(roleId);
        List<ResourceViewDTO> dtos = new ArrayList<>();
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        groups.stream().forEach(group -> {
            ResourceViewDTO dto = new ResourceViewDTO();
            group.getGroup().forEach(resource -> {
                switch (resource.getType()) {
                case 1:
                    if (depService.ALL_DEP.equals(resource.getId())) {
                        dto.setDepCd(resource.getId());
                        dto.setDepName(depService.ALL_DEP_NAME);
                    } else {
                        dto.setDepCd(resource.getId());
                        MA0070 dep = depService.getDepById(resource.getId());
                        dto.setDepName(
                                resource.getId().concat(" ").concat(dep.getDepName().trim()));
                    }
                    break;
                case 2:
                    dto.setPmaCd(resource.getId());
                    MA0080 pma = depService.getPmaById(null, resource.getId());
                    dto.setPmaName(
                            resource.getId().concat(" ").concat(pma.getPmName().trim()));
                    break;
                case 3:
                    dto.setCategoryCd(resource.getId());
                    MA0090 category = depService.getCategoryById(null, null, resource.getId());
                    dto.setCategoryName(resource.getId().concat(" ").concat(category.getCategoryName().trim()));
                    break;
                case 4:
                    dto.setSubCategoryCd(resource.getId());
                    MA0100 subCategory = depService.getSubCategoryById(null, null, null,resource.getId());
                    dto.setSubCategoryName(resource.getId().concat(" ").concat(subCategory.getSubCategoryName().trim()));
                    break;
                }
                if (dto.getPmaCd() == null) {
                    dto.setPmaName(depService.ALL_PMA_NAME);
                }
                if (dto.getCategoryCd() == null) {
                    dto.setCategoryName(depService.ALL_CATEGORY_NAME);
                }
                if (dto.getSubCategoryCd() == null) {
                    dto.setSubCategoryName(depService.ALL_SUB_CATEGORY_NAME);
                }
            });
            dtos.add(dto);
        });

        return dtos;
    }

    @Override
    public boolean delRole(Integer roleId) {
        boolean b = defService.isRoleUse(roleId);
        if (b) {
            throw new SystemRuntimeException("Role is used and cannot be deleted");
        }
        roleService.delRole(roleId);
        roleService.delRoleMenu(roleId);
        IymRoleRemarkExample e = new IymRoleRemarkExample();
        e.or().andRoleIdEqualTo(roleId);
        remarkMapper.deleteByExample(e);
        mRoleStoreService.deleteById(roleId);
        return true;
    }

    /**
     * 根据角色名称模糊查询角色信息
     *
     * @param roleName
     * @return
     */
    @Override
    public List<IyRoleDTO> getRoleInfosByRoleName(String roleName) {
        IyRoleService service = Container.getBean(IyRoleService.class);
        List<Role> roles = service.getRolesLikeName(roleName, false);
        if (roles != null && roles.size() > 0) {
            List<IyRoleDTO> dtos = new ArrayList<IyRoleDTO>() {
                {
                    roles.stream().forEach(x -> {
                        IyRoleDTO dto = new IyRoleDTO();
                        dto.setId(x.getId());
                        dto.setName(x.getName());
                        IymRoleRemarkExample ie = new IymRoleRemarkExample();
                        ie.or().andRoleIdEqualTo(x.getId());
                        List<IymRoleRemark> remarks = remarkMapper.selectByExample(ie);
                        if (remarks != null && remarks.size() > 0) {
                            dto.setRemark(remarks.get(ConstantsDB.COMMON_ZERO).getRemark());
                        }
                        add(dto);
                    });
                }
            };
            return dtos;
        }
        return null;
    }

    /**
     * 根据角色id查询角色信息
     *
     * @param roleId
     * @return
     */
    @Override
    public IyRoleDTO getRoleInfoByRoleId(Integer roleId) {
        if (roleId == null) {
            return null;
        }
        IyRoleDTO dto = new IyRoleDTO();
        IyRoleService service = Container.getBean(IyRoleService.class);
        Role role = service.getRoleById(roleId);
        dto.setName(role.getName());
        IymRoleRemarkExample ie = new IymRoleRemarkExample();
        ie.or().andRoleIdEqualTo(roleId);
        List<IymRoleRemark> remarks = remarkMapper.selectByExample(ie);
        if (remarks != null && remarks.size() > 0) {
            dto.setRemark(remarks.get(ConstantsDB.COMMON_ZERO).getRemark());
        }
        return dto;
    }

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
    @Override
    public List<IyResourceDTO> getResource(String userId, String userDpt, String post, String occup,
            String permission) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(post)
                || StringUtils.isBlank(permission)) {
            return null;
        }
        Collection<Integer> permRoleIds = permissService.getRoleidsByPCode(permission);
        if (permRoleIds == null || permRoleIds.isEmpty()) {
            return null;
        }
        // 默认授权中的角色id
        Collection<Integer> userRoleIds = defService.getDefRoleId(post);
        if (userRoleIds != null && !userRoleIds.isEmpty()) {
            permRoleIds.retainAll(userRoleIds);
        }
        // 特殊授权中的角色
        Collection<Integer> actorRoleIds = permService.getRoleIdsByActorId(userId);
        if (actorRoleIds != null && !actorRoleIds.isEmpty()) {
            permRoleIds.retainAll(actorRoleIds);
        }
        if (permRoleIds == null || permRoleIds.isEmpty()) {
            return null;
        }
        if (permRoleIds.size() > 0) {
            Collection<ResourceGroup> groups = permissService.getResourceGroup(permRoleIds);
            if (groups != null && !groups.isEmpty()) {
                List<IyResourceDTO> dtos = new ArrayList<IyResourceDTO>() {
                    {
                        groups.stream().forEach(group -> {
                            IyResourceDTO dto = getResourceByGroup(group.getGroup());
                            add(dto);
                        });
                    }
                };
                return dtos != null && !dtos.isEmpty() ? dtos : null;
            }
        }
        return null;
    }

    /**
     * 根据角色名称检索角色数据
     *
     * @param roleName
     *            角色名称
     * @param nullName
     *            角色名称空标识，用来判断角色名称为空时是否检索出数据，true：是，false：否
     * @return
     */
    @Override
    public List<Role> getRolesLikeName(String roleName, boolean nullName) {
        // 因为需要做到不录入数据显示全部, 所以去掉这个判断
        /*if ((roleName == null || "".equals(roleName)) && !nullName) {
            return null;
        }*/
        /*RoleExample e = new RoleExample();*/
        roleName = roleName.trim();
        /*if (StringUtils.isNotBlank(roleName)) {
            e.or().andNameLike("%" + roleName + "%");
        }*/
        return remarkMapper.getRolesLikeName(roleName);
        // return mapper.selectByExample(e);
    }

    @Override
    public List<Integer> getRoleIdsLikeName(String roleName) {
        IyRoleService service = Container.getBean(IyRoleService.class);
        List<Role> roles = service.getRolesLikeName(roleName, false);
        if (roles != null && !roles.isEmpty()) {
            List<Integer> roleIds = new ArrayList<Integer>() {
                {
                    roles.stream().forEach(x -> add(x.getId()));
                }
            };
            return roleIds;
        }
        return null;
    }

    @Override
    public List<AutoCompleteDTO> getRolesLikeName(String userId, String roleName) {
        IyRoleService service = Container.getBean(IyRoleService.class);
        List<Role> roles = service.getRolesLikeName(roleName, false);
        boolean self = permissService.isActorHasPermission(userId,
                PermissionCode.P_CODE_OTHER_SELF);
        boolean system = permissService.isActorHasPermission(userId,
                PermissionCode.P_CODE_OTHER_SYSTEM);
        Collection<Integer> selfRoleIds = null;
        if (!system && self) {
            selfRoleIds = permissService.getRoleIdsByActorId(userId);
        }
        if (roles != null && !roles.isEmpty()) {
            List<AutoCompleteDTO> dtos = new ArrayList<AutoCompleteDTO>() {
                {
                    roles.stream().forEach(x -> add(new AutoCompleteDTO(String.valueOf(x.getId()),
                            x.getName(), String.valueOf(x.getId()))));
                }
            };
            if (selfRoleIds != null) {
                Iterator<AutoCompleteDTO> it = dtos.iterator();
                while (it.hasNext()) {
                    AutoCompleteDTO dto = it.next();
                    if (!selfRoleIds.contains(Integer.parseInt(dto.getK()))) {
                        it.remove();
                    }
                }
            }
            return dtos;
        }
        return null;
    }

    /**
     * 根据资源信息及权限信息检索角色id集合
     *
     * @param groups
     *            资源组
     * @param pCode
     *            权限
     * @return
     */
    @Override
    public Collection<Integer> getRoleIdsByResource(Collection<ResourceGroup> groups,
            String pCode) {
        if (StringUtils.isBlank(pCode) || groups == null || groups.isEmpty()) {
            return null;
        }
        Collection<Integer> pRoleIds = permissService.getRoleidsByPCode(pCode);
        if (pRoleIds == null || pRoleIds.isEmpty()) {
            return null;
        }
        this.complementResourceGroup(groups);
        Collection<Integer> gRoleIds = aaiMapper.getRoleIdsByResource(groups);
        if (gRoleIds == null || gRoleIds.isEmpty()) {
            return null;
        }
        pRoleIds.retainAll(gRoleIds);
        if (pRoleIds != null && !pRoleIds.isEmpty()) {
            return pRoleIds;
        }
        return null;
    }

    /**
     * 根据角色集合及权限code检索对应的资源信息
     *
     * @param roleIds
     * @param pCode
     * @return
     */
    @Override
    public Collection<ResourceGroup> getResourcesByRoleAndPCode(List<Integer> roleIds,
            String pCode) {
        if (roleIds == null || roleIds.isEmpty() || StringUtils.isBlank(pCode)) {
            return null;
        }

        Collection<Integer> pRoleIds = permissService.getRoleidsByPCode(pCode);
        if (pRoleIds == null || pRoleIds.isEmpty()) {
            return null;
        }
        pRoleIds.retainAll(roleIds);
        Collection<ResourceGroup> groups = permissService.getResourceGroup(pRoleIds);
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        return groups;
    }

    /**
     * 根据资源组生成资源组检索条件信息
     *
     * @param groups
     * @return
     */
    @Override
    public ConditionDTO createConditionsFromResourceGroup(Collection<ResourceGroup> groups) {
        if (groups == null || groups.isEmpty()) {
            return null;
        }
        ConditionDTO dto = new ConditionDTO();
        String conditions = "";
        String conditionsDpt = "";
        String conditionsStore = "";
        int i = 0;
        boolean isAllDpt = false;
        boolean isAllStore = false;
        List<IyResourceDTO> ress = new ArrayList<>();
        for (ResourceGroup group : groups) {
            String div = null;
            String d = null;
            String dpt = null;
            String store = null;
            IyResourceDTO iyRes = new IyResourceDTO();
            for (Resource res : group.getGroup()) {
                switch (res.getType()) {
                case ConstantsDB.COMMON_ONE:
                    div = res.getId();
                    break;
                case ConstantsDB.COMMON_TWO:
                    d = res.getId();
                    break;
                case ConstantsDB.COMMON_THREE:
                    dpt = res.getId();
                    break;
                case ConstantsDB.COMMON_FOUR:
                    if (res.getId().equals(ConstantsDB.SELF_STORE)) {
                        HttpSession session = request.getSession();// 从session中拿到当前登录人的角色id集合
                        SessionACL acl = (SessionACL) session.getAttribute(Constants.SESSION_USER);
                        User user = acl.getUser();
//                        store = user.getStore();
                    }
//                    else {
//                        store = res.getId();
//                    }
                    break;
                }
            }
            String condition = "";
            String conditionDpt = "";
            String conditionStore = "";

            if (dpt != null) {
                iyRes.setDpt(dpt);
                condition = "(dpt='".concat(dpt).concat("'");
            }
            if (dpt == null && d != null) {
                condition = "(substr(dpt,1,2)='".concat(d).concat("'");
                iyRes.setDpt(d.concat(String.valueOf(ConstantsDB.COMMON_NINE)));
            }
            if (d == null && div != null && !div.equals(ConstantsDB.ALL_STORE)) {
                String prefix = div.substring(ConstantsDB.COMMON_TWO);
                condition = "(substr(dpt,1,1)='".concat(prefix).concat("'");
                iyRes.setDpt(prefix.concat(String.valueOf(ConstantsDB.COMMON_NINE))
                        .concat(String.valueOf(ConstantsDB.COMMON_NINE)));
            }
            if (StringUtils.isNotBlank(condition)) {
                conditionDpt = conditionDpt.concat(condition).concat(")");
            }
            if (d == null && div != null && div.equals(ConstantsDB.ALL_DPT)) {
                isAllDpt = true;
                iyRes.setDpt(div);
            }

//            if (store != null) {
//                if (store.equals(ConstantsDB.ALL_STORE)) {
//                    isAllStore = true;
//                }
//                if ("".equals(condition)) {
//                    condition = condition.concat("(");
//                } else {
//                    condition = condition.concat(" and ");
//                }
//                String storeStr = "store='".concat(store).concat("'");
//                condition = condition.concat(storeStr);
//                conditionStore = conditionStore.concat("(").concat(storeStr).concat(")");
//                iyRes.setStore(store);
//            }
            if (!"".equals(condition)) {
                condition = condition.concat(")");
            }
            if (!"".equals(condition) && i > 0) {
                condition = " or ".concat(condition);
                conditionDpt = " or ".concat(conditionDpt);
                conditionStore = " or ".concat(conditionStore);
            }
            i++;
            conditions = conditions.concat(condition);
            conditionsDpt = conditionsDpt.concat(conditionDpt);
            conditionsStore = conditionsStore.concat(conditionStore);
            ress.add(iyRes);
        }
        if (!isAllDpt && !isAllStore) {
            dto.setCondition(conditions);
            dto.setConditionDpt(conditionsDpt);
            dto.setConditionStore(conditionsStore);
        } else if (isAllDpt && !isAllStore) {
            dto.setCondition(conditionsStore);
            dto.setConditionDpt("");
            dto.setConditionStore(conditionsStore);
        } else if (!isAllDpt && isAllStore) {
            dto.setCondition(conditionsDpt);
            dto.setConditionDpt(conditionsDpt);
            dto.setConditionStore("");
        } else {
            dto.setCondition("");
            dto.setConditionDpt("");
            dto.setConditionStore("");
        }
        dto.setResources(ress);

        return dto;
    }

    private void complementResourceGroup(Collection<ResourceGroup> groups) {
        Iterator<ResourceGroup> it = groups.iterator();
        while (it.hasNext()) {
            ResourceGroup group = it.next();
            String div = null;
            String department = null;
            String dpt = null;
            for (Resource res : group.getGroup()) {
                switch (res.getType()) {
                case ConstantsDB.COMMON_ONE:
                    div = res.getId();
                    break;
                case ConstantsDB.COMMON_TWO:
                    department = res.getId();
                    break;
                case ConstantsDB.COMMON_THREE:
                    dpt = res.getId();
                    break;
                }
            }
            if (dpt == null) {
                group.getGroup().add(new Resource(null, ConstantsDB.COMMON_THREE));
            }
//            if (department == null) {
//                if (dpt != null) {
//                    IyDpt iyDpt = depService.getIyDPTById(null, null, dpt);
//                    department = iyDpt.getDepartment();
//                    group.getGroup().add(new Resource(department, ConstantsDB.COMMON_TWO));
//                } else {
//                    group.getGroup().add(new Resource(null, ConstantsDB.COMMON_TWO));
//                }
//            }
//            if (div == null) {
//                IyDpt iyDpt = depService.getIyDPTById(null, department, null);
//                div = iyDpt.getGrandDiv();
//                group.getGroup().add(new Resource(div, ConstantsDB.COMMON_ONE));
//            }
        }
    }

    private IyResourceDTO getResourceByGroup(Set<Resource> group) {
        if (group == null || group.isEmpty()) {
            return null;
        }
        String div = null;
        String department = null;
        String dpt = null;
        String store = null;
        for (Resource resource : group) {
            switch (resource.getType()) {
            case 1:
                div = resource.getId();
                break;
            case 2:
                department = resource.getId();
                break;
            case 3:
                dpt = resource.getId();
                break;
            case 4:
                store = resource.getId();
                break;
            }
        }
        IyResourceDTO dto = new IyResourceDTO();
        if (div.equals(depService.ALL_DEP_NAME)) {
            dto.setDpt(depService.ALL_DEP_NAME);
        } else if (StringUtils.isNotBlank(dpt)) {
            dto.setDpt(dpt);
        } else if (StringUtils.isBlank(dpt) && StringUtils.isNotBlank(department)) {
            dto.setDpt(department.concat(String.valueOf(ConstantsDB.COMMON_NINE)));
        } else if (StringUtils.isBlank(dpt) && StringUtils.isBlank(department)
                && StringUtils.isNotBlank(div)) {
            char[] chars = div.toCharArray();
            char last = chars[chars.length - ConstantsDB.COMMON_ONE];
            dto.setDpt(String.valueOf(last).concat(String.valueOf(ConstantsDB.COMMON_NINE))
                    .concat(String.valueOf(ConstantsDB.COMMON_NINE)));
        }

        if (!store.equals(ConstantsDB.ALL_STORE)) {
            dto.setStore(store);
        }

        return dto;
    }

}
