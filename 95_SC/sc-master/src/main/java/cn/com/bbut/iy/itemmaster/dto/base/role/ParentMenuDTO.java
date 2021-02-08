package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParentMenuDTO implements Serializable {

	private Integer menuId;

	private String menuName;

	private List<MenuPermDTO> sonMenus;
}
