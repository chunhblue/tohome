package cn.com.bbut.iy.itemmaster.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 与前台交互的菜单对象，只允许2级
 *
 * MenuDTO包含N个MenuItemDTO
 *
 * 因为要保存到session，所以尽量减小体积
 * 
 * </p>
 * 
 * @author songxz
 * @author shiy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO implements Serializable, Comparable<MenuDTO> {

	private static final long serialVersionUID = -1843790089306147827L;

	/** 菜单id */
	private int id;
	/** 菜单名称 */
	private String name;
	/** url */
	private String url;
	/** 显示顺序 */
	private Short sort;

	/** 子菜单 */
	private Collection<MenuItemDTO> menuItems;

	@Override
	public int compareTo(MenuDTO o) {
		return this.sort - o.sort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof MenuDTO)) {
			return false;
		}

		MenuDTO menuDTO = (MenuDTO) o;

		return sort.equals(menuDTO.sort);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + sort.hashCode();
		return result;
	}
}
