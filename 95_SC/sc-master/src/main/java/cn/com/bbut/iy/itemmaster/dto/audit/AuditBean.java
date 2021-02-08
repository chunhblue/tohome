package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 审核基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditBean {
    /**
     * 审核ID
     */
    private long nRecordid;

    /**
     * 商品ID
     */
    private String articleId;

    /**
     * 店铺ID
     */
    private String storeCd;

    /**
     * 流程ID
     */
    private long nReviewid;

    /**
     * 主档类型ID
     */
    private int nTypeid;

    /**
     * 类别ID
     */
    private String distinguishId;

    /**
     * 主档ID
     */
    private String cRecordCd;

    /**
     * 适用开始日
     */
    private String effectiveStartDate;

    /**
     * 操作序列
     */
    private int subNo;

    /**
     * 步骤项
     */
    private long cStep;

    /**
     * 审核状态值
     */
    private int cAuditstatus;

    /**
     * 待审角色ID
     */
    private long nRoleid;

    /**
     * 审核人ID
     */
    private String nOperatorid;

    /**
     * 审核时间
     */
    private Date dAuditTime;

    /**
     * 审核意见
     */
    private String cAuditContent;

    /**
     * 审核发起时间
     */
    private Date dInsertTime;

    /**
     * 生效标示
     */
    private boolean status;
}
