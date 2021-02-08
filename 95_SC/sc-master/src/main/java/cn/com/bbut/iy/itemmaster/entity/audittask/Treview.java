package cn.com.bbut.iy.itemmaster.entity.audittask;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 流程管理的基本信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Treview extends GridParamDTO {

    /*
     * 流程id
     */
    private Integer nReviewid;

    // 流程编码
    private String cReviewCode;

    // 主档类型id
    private String nTypeid;

    // 过程事件
    private String cReviewName;

    // 角色
    private String startRole;

    // 待办id
    private Integer nRecordid;

    // 窗口URL
    private String URL;

    // 提交编码
    private String submitterCode;

    // 提交名称
    private String submitterName;

    // 主档id
    private String recordCd;

    // 实际开始日期
    private String effectiveStartDate;

    /**
     * 操作员姓名
     */
    private String operatorname;
}
