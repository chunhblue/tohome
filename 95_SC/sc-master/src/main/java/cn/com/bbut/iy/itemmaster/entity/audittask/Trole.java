package cn.com.bbut.iy.itemmaster.entity.audittask;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色基本信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Trole extends GridParamDTO {

    /*
     * 角色id
     */
    private long nRoleid;

    private String cRoleCode;

    private String cRoleName;
    // 角色描述
    private String cRoleDesc;

    /**
     * 记录状态
     */
    private String status;

}
