package cn.com.bbut.iy.itemmaster.dto.audit;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 审核记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRecordsParamDTO  extends GridParamDTO {
    /**
     * 数据id
     */
    private String id;
    /**
     * 审核类型数据
     */
    private int[] typeIdArray;
    /**
     * 类型
     */
    private String auditType;
    /**
     * 审核流程
     */
    private int reviewId;
    /**
     * 批准类型
     */
    private String approvalType;
    /**
     * 批准级别
     */
    private String approvalLevel;
    /**
     * 结果
     */
    private long subNo;
    /**
     * 结果
     */
    private String result;
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 时间
     */
    private String dateTime;
    /**
     * 评论
     */
    private String comments;

    /**
     * 审核状态
     */
    private int auditStatus;

    /**
     * 类型名字
     */
    private String typeName;

    /**
     * 发起日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
}
