package cn.com.bbut.iy.itemmaster.dto.task;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import lombok.*;

import java.util.Collection;
import java.util.List;

/**
 * 待办任务
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class TodoTaskParamDto {
    /**
     * 店铺权限列表
     */
    private Collection<String> storeList;

    /**
     * 角色列表
     */
    private Collection<Integer> roleList;

    /**
     * 业务时间
     */
    private String businessDate;

    /**
     * 通知是否回复
     */
    private String isReply;

    /**
     * 用户id
     */
    private String userId;
}
