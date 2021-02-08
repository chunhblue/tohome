package cn.com.bbut.iy.itemmaster.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 子菜单项
 * 
 * @author songxz
 * @author shiy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemDTO implements Serializable, Comparable<MenuItemDTO> {

	private static final long serialVersionUID = -7316906429181855115L;

	/** 菜单名称 */
	private String name;
	/** 路径 */
	private String url;
	/** 顺序 */
	private short sort;

	@Override
	public int compareTo(MenuItemDTO o) {
		return this.sort - o.sort;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof MenuItemDTO)) {
			return false;
		}

		MenuItemDTO that = (MenuItemDTO) o;

		return sort == that.sort;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (int) sort;
		return result;
	}
}
