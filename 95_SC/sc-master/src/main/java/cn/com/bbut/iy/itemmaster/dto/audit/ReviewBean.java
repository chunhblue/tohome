package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程管理的基本信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewBean {
    /**
     * 流程ID
     */
    private long nReviewid;

    /**
     * 流程编号
     */
    private String cReviewCode;

    /**
     * 类型ID
     */
    private int nTypeid;

    /**
     * 大分类ID
     */
    private String startRole;

    private String cReviewType;

    private String cReviewName;

    private String nDistinguishTypeId;

    private String status;
}
