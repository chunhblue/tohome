package cn.com.bbut.iy.itemmaster.dto.task;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.*;

/**
 * 待办任务
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoTaskGridDto  extends GridDataDTO {
    /**
     * 类型
     */
    private String type;

    /**
     * url跳转地址
     */
    private String url;

    /**
     * 通知标题
     */
    private String notificationTitle;

    /**
     * 通知数量
     */
    private long notificationCount;
}
