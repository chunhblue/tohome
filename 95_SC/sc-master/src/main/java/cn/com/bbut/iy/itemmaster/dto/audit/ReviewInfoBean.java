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
public class ReviewInfoBean {
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
     * 审核角色名称
     */
    private String cRoleName;

    /**
     * 审核状态
     */
    private int cAuditStatus;

    /**
     * 记录状态
     */
    private boolean status;
}
