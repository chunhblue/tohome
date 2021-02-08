package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import cn.com.bbut.iy.itemmaster.service.base.CkRoleService;
import cn.shiy.common.pmgr.dao.RoleAssignmentMapper;
import cn.shiy.common.pmgr.dao.RoleMapper;
import cn.shiy.common.pmgr.dao.RoleMenuMapper;
import cn.shiy.common.pmgr.dao.RoleResourcesMapper;
import cn.shiy.common.pmgr.dto.PmgrRoleDataDTO;
import cn.shiy.common.pmgr.dto.PmgrRoleParamDTO;
import cn.shiy.common.pmgr.entity.*;
import cn.shiy.common.pmgr.entity.RoleExample.Criteria;
import cn.shiy.common.pmgr.enums.RoleGrantEnum;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author shiy
 */
@Repository
public class CkRoleServiceImpl implements CkRoleService {

    /**
     * 资源组id起始数字
     */
    private static final int GROUP_INIT = 1;

    @Autowired
    @Setter
    private RoleMapper rMapper;

    @Autowired
    @Setter
    private RoleResourcesMapper rrMapper;

    @Autowired
    @Setter
    private RoleAssignmentMapper raMapper;

    @Autowired
    @Setter
    private RoleMenuMapper rmMapper;

    @Autowired
    @Setter
    private ApplicationContext context;

    @Override
    public void addResource(Integer roleid, Collection<ResourceGroup> rgs) {

        int groupCode = GROUP_INIT;
        // 循环所有的资源组
        for (ResourceGroup rg : rgs) {
            // 循环资源组内的每个资源，groupCode相同
            for (Resource resource : rg) {
                RoleResources rr = new RoleResources();
                rr.setRoleId(roleid);
                rr.setResourceId(resource.getId());
                rr.setType(resource.getType());
                rr.setGroupCode(groupCode);
                rrMapper.insert(rr);
            }
            groupCode++;
        }
    }

    @Override
    public void addPermission(Integer roleid, Collection<Integer> pIds) {
        for (Integer pId : pIds) {
            RoleAssignment ra = new RoleAssignment();
            ra.setRoleId(roleid);
            ra.setPermissionId(pId);
            raMapper.insert(ra);
        }
    }

    @Override
    public void delResource(Integer roleid) {
        RoleResourcesExample ex = new RoleResourcesExample();
        ex.or().andRoleIdEqualTo(roleid);
        rrMapper.deleteByExample(ex);
    }

    @Override
    public void delPermission(Integer roleid) {
        RoleAssignmentExample ex = new RoleAssignmentExample();
        ex.or().andRoleIdEqualTo(roleid);
        raMapper.deleteByExample(ex);
    }

    @Override
    public void addRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds) {
        saveRole(role, rgs, pIds, true);
    }

    @Override
    public Integer addRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds,
            Collection<Integer> mIds) {
        CkRoleService service = context.getBean(CkRoleService.class);
        service.addRole(role, rgs, pIds);
        // 增加角色与菜单的关联关系
        if (mIds != null && mIds.size() > 0) {
            service.addRoleMenu(role.getId(), mIds);
        }
        return role.getId();
    }

    @Override
    public void updateRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds) {
        saveRole(role, rgs, pIds, false);
    }

    @Override
    public Integer updateRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds,
            Collection<Integer> mIds) {
        CkRoleService service = context.getBean(CkRoleService.class);
        service.updateRole(role, rgs, pIds);
        // 增加角色与菜单的关联关系
        service.delRoleMenu(role.getId());
        if (mIds != null && mIds.size() > 0) {
            service.addRoleMenu(role.getId(), mIds);
        }
        return role.getId();
    }

    /**
     * insert or update 角色
     *
     * @param role
     *            角色
     * @param rgs
     *            资源组
     * @param pIds
     *            权限
     * @param isNew
     *            是否新增
     */
    private void saveRole(Role role, Collection<ResourceGroup> rgs, Collection<Integer> pIds,
            boolean isNew) {
        CkRoleService service = context.getBean(CkRoleService.class);
        if (isNew) {
            rMapper.insert(role);
            RoleExample pex = new RoleExample();
            pex.or().andNameEqualTo(role.getName()).andGrantEqualTo(role.getGrant());
            List<Role> plist = rMapper.selectByExample(pex);
            role.setId(plist.get(0).getId());
        } else {
            rMapper.updateByPrimaryKey(role);
            service.delResource(role.getId());
            service.delPermission(role.getId());
        }
        // if (isNew) {
        // service.delResource(role.getId());
        // service.delPermission(role.getId());
        // }
        if (rgs != null && rgs.size() > 0) {
            service.addResource(role.getId(), rgs);
        }
        if (pIds != null && pIds.size() > 0) {
            service.addPermission(role.getId(), pIds);
        }
    }

    @Override
    public void delRole(Integer roleid) {
        CkRoleService service = context.getBean(CkRoleService.class);
        rMapper.deleteByPrimaryKey(roleid);
        service.delResource(roleid);
        service.delPermission(roleid);
    }

    @Override
    public Collection<Integer> getAllSubRoleId(Collection<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return null;
        } else {
            Set<Integer> allRoleIds = new HashSet<>();
            Collection<Integer> rs;
            Collection<Integer> tRoleIds = roleIds;
            // 目前没有限制最多有多少层，考虑到性能，由外部限定循环层级
			while ((rs = rMapper.selectSubRoleIdByRoleIdStr(tRoleIds)) != null
                    && rs.size() > 0) {
                allRoleIds.addAll(rs);
                tRoleIds = rs;
            }
            return allRoleIds;
        }
    }

    @Override
    public Collection<Integer> getAllSuperRoleId(Collection<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return null;
        }
        Set<Integer> allRoleIds = new HashSet<>();
        Collection<Integer> superIds;
        Collection<Integer> tRoleIds = roleIds;

        while ((superIds = rMapper.selectSuperRoleId(tRoleIds)) != null && superIds.size() > 0) {
            allRoleIds.addAll(superIds);
            tRoleIds = superIds;
        }
        return allRoleIds;
    }

    @Override
    public Collection<Integer> getAllSuperRoleId(int roleId) {
        Collection<Integer> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        CkRoleService service = context.getBean(CkRoleService.class);
        return service.getAllSuperRoleId(roleIds);
    }

    @Override
    public void addRoleMenu(int roleId, int menuId) {
        RoleMenu rm = new RoleMenu(roleId, menuId);
        rmMapper.insert(rm);
    }

    @Override
    public void addRoleMenu(int roleId, Collection<Integer> menuIds) {
        Collection<RoleMenu> roleMenus = new ArrayList<>(menuIds.size());
        menuIds.stream().forEach(x -> roleMenus.add(new RoleMenu(roleId, x)));
		for (RoleMenu rm : roleMenus) {
			rmMapper.insert(rm);
		}
		// rmMapper.insertBatch(roleMenus);
    }

    @Override
    public void delRoleMenu(int roleId) {
        RoleMenuExample ex = new RoleMenuExample();
        ex.or().andRoleIdEqualTo(roleId);
        rmMapper.deleteByExample(ex);
    }

    @Override
    public Collection<Integer> getMenuIds(int roleId) {
        CkRoleService service = context.getBean(CkRoleService.class);
        Collection<Integer> superRoleIds = service.getAllSuperRoleId(roleId);
        Collection<Integer> roleIds;
        if (superRoleIds == null || superRoleIds.size() == 0) {
            roleIds = new ArrayList<>(1);
        } else {
            roleIds = new ArrayList<>(1 + superRoleIds.size());
            roleIds.addAll(superRoleIds);
        }
        roleIds.add(roleId);
        return getMenuIds(roleIds);
    }

    @Override
    public Collection<Integer> getMenuIds(Collection<Integer> roleIds) {
        if (roleIds == null || roleIds.size() == 0) {
            return null;
        }

        RoleMenuExample ex = new RoleMenuExample();
        roleIds.stream().forEach(x -> ex.or().andRoleIdEqualTo(x));
        Collection<RoleMenu> rMenus = rmMapper.selectByExample(ex);
        if (rMenus == null || rMenus.size() == 0) {
            return null;
        } else {
            Set<Integer> set = new HashSet<>();
            rMenus.stream().forEach(x -> set.add(x.getMenuId()));
            return set;
        }
    }

    @Override
    public PmgrRoleDataDTO selectRoleListByParam(PmgrRoleParamDTO param) {
        PmgrRoleDataDTO result = new PmgrRoleDataDTO();
        RoleExample example = new RoleExample();
        Criteria criteria = example.or();
        if (param != null) {
            if (StringUtils.isNotBlank(param.getName())) {
                criteria.andNameLike("%" + param.getName() + "%");
            }
            if (StringUtils.isNotBlank(param.getPname())) {
                // 父级id集合
                RoleExample pex = new RoleExample();
                pex.or().andNameLike("%" + param.getPname() + "%");
                List<Role> plist = rMapper.selectByExample(pex);
                List<Integer> rids = new ArrayList<>();
                if (plist != null && plist.size() > 0) {
                    for (Role r : plist) {
                        rids.add(r.getId());
                    }
                } else {
                    rids.add(-1);
                }
                criteria.andSuperIdIn(rids);
            }
            if (param.getGrantFlg() != null && !"".equals(param.getGrantFlg())) {
                // 是否可授权
				criteria.andGrantEqualTo(param.getGrantFlg());
            }
            if (param.getRtypeid() != null && !"".equals(param.getRtypeid())) {
                // 所属资源类型id
                criteria.andRTypeEqualTo(param.getRtypeid());
            }

            if (param.getRidStr() != null && param.getRidStr().size() > 0) {
                // 所属资源id
                criteria.andRIdIn(param.getRidStr());
            }
        }
        example.setDistinct(true);
        example.setLimitStart(param.getLimitStart());
        example.setLimitEnd(param.getLimitEnd());
        example.setOrderByClause(param.getOrderByClause());
        example.setNeedFoundRows(true);
        List<Role> roleList = rMapper.selectByExample(example);
        int count = rMapper.countLastSelect();

        if (roleList != null && roleList.size() > 0) {
            result.setRoleList(roleList);// 数据
            result.setCount(count);// 总数量
            result.setPage(param.getPage());// 当前页
            result.setRows(param.getRows());// 每页条数
            result.setSidx(param.getSidx());// 排序列
            result.setSord(param.getSord());// 升序降序
        }

        return result;
    }

    @Override
    public Role getRole(Integer roleId) {
        return rMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public Collection<Role> getRoleByGrant(List<Integer> companyIds, Integer ruleOutRoleID) {
        RoleExample example = new RoleExample();
        Criteria criteria = example.or();
        if (companyIds != null && companyIds.size() > 0) {
            List<String> companyIdStrs = new ArrayList<>();
            for (Integer id : companyIds) {
                companyIdStrs.add(id.toString());
            }
            criteria.andRIdIn(companyIdStrs);
        }
        if (ruleOutRoleID != null && !"".equals(ruleOutRoleID)) {
            criteria.andIdNotEqualTo(ruleOutRoleID);
        }
		criteria.andGrantEqualTo(RoleGrantEnum.DISABLED);
        Collection<Role> roles = rMapper.selectByExample(example);
        return roles;
    }

    @Override
    public Collection<Role> getRoleIdByRoleNameAndRes(String roleName, Integer rtype,
            List<Integer> companyIds) {
        RoleExample example = new RoleExample();
        Criteria criteria = example.or();
        if (StringUtils.isNotBlank(roleName)) {
            criteria.andNameLike(roleName);
        }
        if (companyIds != null && companyIds.size() > 0) {
            // 当公司id集合只有1条数据，并且该数据的值为0.则视为超级权限。无需不需要设定公司的资源。
            if (!(companyIds.size() == 1 && companyIds.get(0).equals(0))) {
                List<String> strCompanyIds = new ArrayList<>();
                for (Integer cid : companyIds) {
                    strCompanyIds.add(cid.toString());
                }
                criteria.andRTypeEqualTo(rtype).andRIdIn(strCompanyIds);
            }
        } else {
            return null;
        }
		criteria.andGrantEqualTo(RoleGrantEnum.ENABLED);
        List<Role> roles = rMapper.selectByExample(example);
        return roles;
    }

}
