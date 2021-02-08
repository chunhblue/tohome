/**
 * ClassName  AccountGridParamDTO
 * History
 * Create User: admin
 * Create Date: 2017年6月16日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songxz
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSubmitDataDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private PmgrUiRole roleBasic;// 角色基本信息
	private List<ResourcesDataDTO> resources;// 资源
	private String permissionsStr;// 权限字符串：1,2,3,4,5,6
	private String menusStr;// 菜单:1,2,3,4,5,6
}
