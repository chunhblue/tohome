package cn.com.bbut.iy.itemmaster.dto.audit;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 审核任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewTaskParamDTO extends GridParamDTO {
    /*
    * 文件名字
    * */
    private String recordCd;
    /**
     * 审核类型id
     */
    private Integer typeId;
    /**
     * 发起时间 开始
     */
    private String startDate;
    /**
     * 发起时间 结束
     */
    private String endDate;
    /**
     * 用户code
     */
    private String userCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 登录用户id
     */
    private String operatorId;
    /**
     * status
     */
    private String status;

    /**
     * 用户角色
     */
    private Collection<Integer> roleIds;
}
