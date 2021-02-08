package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuPermDTO implements Serializable {

	private Integer menuId;

	private String menuName;

	private List<PermissionToMenuDTO> perms;

}
