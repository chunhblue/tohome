package cn.com.bbut.iy.itemmaster.dto.mySubmissions;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor // 有参构造
@NoArgsConstructor  // 无参
public class MySubbmitterGridDTO extends GridDataDTO {
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
     * 步骤
     */
    private int cSubNo;

    /**
     * 记录状态
     */
    private String status;

}
