/**
 * ClassName  AccountGridParamDTO
 * History
 * Create User: admin
 * Create Date: 2017年6月15日
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.dto.base.DefaultRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;

/**
 * @author jyn
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRoleGridDataDTO extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    private Integer id;// id
    private Integer roleId;// 角色id
    private String roleName;
    private String remark; // 角色描述
    // 职务编号
    private String postCode;
    private String postName;

    // 职种编号
    private String occupCode;
    private String occupName;

    private String dpt;
    private String dptName;
    private String store;
    private String storeName;
}
