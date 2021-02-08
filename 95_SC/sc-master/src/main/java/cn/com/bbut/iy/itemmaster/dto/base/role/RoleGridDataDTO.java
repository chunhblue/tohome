/**
 * ClassName  AccountGridParamDTO
 * History
 * Create User: admin
 * Create Date: 2017年6月15日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base.role;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jyn
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleGridDataDTO extends GridDataDTO {
	private static final long serialVersionUID = 1L;
	private Integer id;// 角色id
	private String name;// 角色名称
	private String remark; // 角色描述
}
