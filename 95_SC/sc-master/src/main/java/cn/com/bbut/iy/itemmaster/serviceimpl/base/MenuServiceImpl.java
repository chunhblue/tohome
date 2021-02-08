package cn.com.bbut.iy.itemmaster.serviceimpl.base;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.TreeSet;

import javax.validation.constraints.NotNull;

import cn.com.bbut.iy.itemmaster.dto.base.*;
import cn.com.bbut.iy.itemmaster.dto.base.role.MenuPermDTO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.dao.MenuMapper;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import cn.com.bbut.iy.itemmaster.entity.base.MenuExample;
import cn.com.bbut.iy.itemmaster.service.base.MenuService;
import cn.shiy.common.baseutil.Container;
import cn.shiy.common.pmgr.service.PermissionService;
import cn.shiy.common.pmgr.service.RoleService;

/**
 * @author shiy
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    @Setter
    private PermissionService pService;

    @Autowired
    @Setter
    private RoleService roleService;

    @Autowired
    @Setter
    private MenuMapper menuMapper;

    @Override
    public Collection<MenuDTO> getMenus(Collection<Integer> roleIds) {
        // 获取用户所有的角色
        // Collection<Integer> roleIds = pService.getRoleIdsByActorId(userId);
        if (roleIds == null) {
            return null;
        }
        // 获取所有角色（包括父角色）的menuId，
        LinkedHashSet<Integer> menuIdSet = new LinkedHashSet<>();
        // roleIds.parallelStream().forEach(x -> {
        // Collection<Integer> menuIds = roleService.getMenuIds(x);
        // if (menuIds != null && menuIds.size() > 0) {
        // menuIdSet.addAll(menuIds);
        // }
        // });
        for (Integer id : roleIds) {
            Collection<Integer> menuIds = roleService.getMenuIds(id);
            if (menuIds != null && menuIds.size() > 0) {
                menuIdSet.addAll(menuIds);
            }
        }
        if (menuIdSet == null || menuIdSet.size() == 0) {
            return null;
        } else {
            return buildMenuColleciton(menuIdSet);
        }

    }

    @Override
    public Collection<MenuDTO> getMenus(@NotNull String userId) {
        // 获取用户所有的角色
        Collection<Integer> roleIds = pService.getRoleIdsByActorId(userId);
        if (roleIds == null) {
            return null;
        }
        // 获取所有角色（包括父角色）的menuId，
        LinkedHashSet<Integer> menuIdSet = new LinkedHashSet<>();
        // roleIds.parallelStream().forEach(x -> {
        // Collection<Integer> menuIds = roleService.getMenuIds(x);
        // if (menuIds != null && menuIds.size() > 0) {
        // menuIdSet.addAll(menuIds);
        // }
        // });
        for (Integer id : roleIds) {
            Collection<Integer> menuIds = roleService.getMenuIds(id);
            if (menuIds != null && menuIds.size() > 0) {
                menuIdSet.addAll(menuIds);
            }
        }
        if (menuIdSet == null || menuIdSet.size() == 0) {
            return null;
        } else {
            return buildMenuColleciton(menuIdSet);
        }

    }

    @Override
    public Menu getMenu(int id) {
        return menuMapper.selectByPrimaryKey(id);
    }

    /**
     * 按提供的menuIdSet，构建Menu结构
     * 
     * @param menuIdSet
     *            可见菜单集合，含根节点
     * @return 菜单集合或null
     */
    public Collection<MenuDTO> buildMenuColleciton(@NotNull LinkedHashSet<Integer> menuIdSet) {
        MenuService cachedService = Container.getBean(MenuService.class);
        // 获取所有根菜单
        MenuExample ex = new MenuExample();
        ex.or().andParentIdIsNull();
        Collection<Menu> rootMenus = menuMapper.selectByExample(ex);
        if (rootMenus == null || rootMenus.size() == 0) {
            return null;
        }

        // 被注销的方法 原因是：产生了极其偶然的菜单缺失现象
        // rootMenus.parallelStream().filter(x ->
        // menuIdSet.contains(x.getId())).forEach(x -> {
        // MenuDTO menu = new MenuDTO(x.getId(), x.getName(), x.getUrl(),
        // x.getSort(),
        // new TreeSet<>());
        // menus.add(menu);
        // menuIdSet.remove(x.getId());
        // });

        // 遍历每一个根菜单，menuIdSet存在，则构建一个根，构建后从menuIdSet删除这个id
        Collection<MenuDTO> menus = new TreeSet<>();
        for (Menu mu : rootMenus) {
            if (menuIdSet.contains(mu.getId())) {
                MenuDTO menu = new MenuDTO(mu.getId(), mu.getName(), mu.getUrl(), mu.getSort(),
                        new TreeSet<>());
                menus.add(menu);
                menuIdSet.remove(mu.getId());
            }
        }

        // 被注销的方法 原因是：产生了极其偶然的菜单缺失现象
        // menuIdSet.parallelStream().forEach(x -> {
        // Menu menu = cachedService.getMenu(x);
        // MenuItemDTO item = new MenuItemDTO(menu.getName(), menu.getUrl(),
        // menu.getSort());
        // menus.parallelStream().filter(m -> m.getId() ==
        // menu.getParentId().intValue())
        // .forEach(m -> m.getMenuItems().add(item));
        // });

        // 遍历每个菜单id，添加到适合的根菜单中
        log.debug("menuIdSet：{}", menuIdSet);
        for (Integer id : menuIdSet) {
            Menu menu = cachedService.getMenu(id);
            if(menu!=null && menu.getId()!=null){
                MenuItemDTO item = new MenuItemDTO(menu.getName(), menu.getUrl(), menu.getSort());
                for (MenuDTO mu : menus) {
                    if (mu.getId() == menu.getParentId().intValue()) {
                        mu.getMenuItems().add(item);
                    }
                }
            }
        }

        return menus;
    }

    @Override
    public Collection<Menu> getAllMenus() {
        // 首先取得根菜单
        MenuExample ex = new MenuExample();
        Collection<Menu> rootMenus = menuMapper.selectByExample(ex);
        return rootMenus;
    }

    /**
     * 取得所有菜单集合（角色模块专用）
     *
     * @param orderByCause
     *            排序
     * @return
     */
    @Override
    public Collection<Menu> getAllMenus(String orderByCause) {
        MenuExample ex = new MenuExample();
        if (StringUtils.isNotBlank(orderByCause)) {
            ex.setOrderByClause(orderByCause);
        }
        Collection<Menu> rootMenus = menuMapper.selectByExample(ex);
        return rootMenus;
    }

    @Override
    public GridDataDTO<Menu> getData(MenuParamDTO param) {
        if(param!=null){
            if(param.getName()!=null && param.getName().equals("Store Controller System")){ // SC店铺管理系统
                param.setName("");
            }
        }
        List<Menu> list = menuMapper.selectMenuByParam(param);
        int count = menuMapper.selectCountByParam(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }

}
