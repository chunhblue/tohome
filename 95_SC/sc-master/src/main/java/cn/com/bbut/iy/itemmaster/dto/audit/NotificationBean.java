package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 流程管理的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationBean {
    /**
     * 待办ID
     */
    private long nRecordid;

    /**
     * 流程ID
     */
    private long nReviewid;

    /**
     * 角色ID
     */
    private long nRoleid;

    /**
     * 优先级
     */
    private int cPriority;

    /**
     * 窗口URL
     */
    private String cUrl;

    /**
     * 发起时间
     */
    private Date cNotificationTime;

    /**
     * 主档类型ID
     */
    private int nTypeid;

    /**
     * 主档ID
     */
    private String cRecordCd;

    /**
     * 适用开始日
     */
    private String effectiveStartDate;

    /**
     * 店铺cd
     */
    private String storeCd;

    /**
     * 步骤
     */
    private int cSubNo;

    /**
     * 记录状态
     */
    private String status;
}
