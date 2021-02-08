package cn.com.bbut.iy.itemmaster.entity.audittask;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 待审核事务的基本信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Tbacklog extends GridParamDTO {
    /**
     * 待办ID
     */
    private long nRecordid;

    /**
     * 角色ID
     */

    private long nRoleid;
    /**
     * 流程ID
     */
    private long nReviewid;


    /**
     * 优先级
     */
    private Integer cPriority;

    /**
     * 窗口URL
     */
    private String cUrl;

    /**
     * 发起(通知)时间
     */
    private Date cNotificationTime;

    /**
     * 实际开始日期
     */
    private String effectiveStartDate;

    /**
     * 主档ID
     */
    private String cRecordCd;

    /**
     * 主档类型ID
     */
    private Integer nTypeid;

    /**
     * 步骤
     */
    private Integer cSubNo;

    /**
     * 记录状态
     */
    private String status;

    // 分页
    private int limitStart;
}
