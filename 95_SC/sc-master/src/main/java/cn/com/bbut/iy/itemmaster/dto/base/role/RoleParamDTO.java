/**
 * ClassName  AccountGridParamDTO
 * History
 * Create User: admin
 * Create Date: 2017年6月16日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base.role;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
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
public class RoleParamDTO extends GridParamDTO {
	private static final long serialVersionUID = 1L;

	private String name;// 角色名称
}
