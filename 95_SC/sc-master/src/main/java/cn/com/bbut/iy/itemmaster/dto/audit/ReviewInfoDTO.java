package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程管理的步骤信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewInfoDTO {
    /**
     * 记录主键
     */
    private long nRecordid;

    /**
     * 流程ID
     */
    private long nReviewid;

    /**
     * 步骤编号
     */
    private long cStep;

    /**
     * 步骤名称
     */
    private String cStepName;

    /**
     * 审核角色ID
     */
    private int nRoleid;

    /**
     * 权限名称
     */
    private String cRoleName;

    private String cAuditStatus;

    private String status;
}
