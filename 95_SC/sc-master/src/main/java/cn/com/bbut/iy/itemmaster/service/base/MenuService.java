package cn.com.bbut.iy.itemmaster.service.base;

import cn.com.bbut.iy.itemmaster.constant.ConstantsCache;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.MenuDTO;
import cn.com.bbut.iy.itemmaster.dto.base.MenuParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.MenuPermDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Menu;
import org.springframework.cache.annotation.Cacheable;

import java.util.Collection;

/**
 * 菜单相关功能
 * 
 * @author shiy
 */
public interface MenuService {

    /**
     * 取得指定用户可见菜单(全部菜单体系)
     *
     * <p>
     * 0. 取得用户id对应的所有角色<br/>
     * 1. 取得所有角色对应的初级菜单（不重复）<br/>
     * 2. 取得各初级菜单对应的子菜单<br/>
     * 3. 拼接返回<br/>
     * </p>
     * 
     * @param userId
     *            用户名
     * @return 菜单集合或null
     */
    Collection<MenuDTO> getMenus(String userId);

    /**
     * 取得指定角色用户可见菜单(全部菜单体系)
     *
     * <p>
     * 1. 取得所有角色对应的初级菜单（不重复）<br/>
     * 2. 取得各初级菜单对应的子菜单<br/>
     * 3. 拼接返回<br/>
     * </p>
     * 
     * @param roleIds
     *            角色id集合
     * 
     * @return 菜单集合或null
     */
    Collection<MenuDTO> getMenus(Collection<Integer> roleIds);

    /**
     * 获取指定菜单信息
     * 
     * @param id
     *            menuid
     * @return 菜单对象或null
     */
    //@Cacheable(ConstantsCache.CACHE_MENU)
    Menu getMenu(int id);

	/**
	 * 取得所有菜单集合（角色模块专用）
	 * 
	 * @return
	 */
	Collection<Menu> getAllMenus();

	/**
	 * 取得所有菜单集合（角色模块专用）
	 * 
	 * @param orderByCause
	 *            排序
	 *
	 * @return
	 */
	Collection<Menu> getAllMenus(String orderByCause);

    /**
     * 得到检索数据
     *
     * @param param
     * @return
     */
    GridDataDTO<Menu> getData(MenuParamDTO param);
}
