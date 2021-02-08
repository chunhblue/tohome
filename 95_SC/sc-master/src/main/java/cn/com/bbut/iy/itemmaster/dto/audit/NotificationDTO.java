package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 待办事务的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    /**
     * 待办ID
     */
    private long nNotificationid;

    /**
     * 窗口URL
     */
    private String cUrl;

    /**
     * 主档ID
     */
    private String recordCd;

    /**
     * 时间
     */
    private String startDate;

    /**
     *
     */
    private String cNotificationTitle;

    /**
     * 发起时间
     */
    private String cNotificationTime;

    /**
     * 记录状态
     */
    private String status;
}
