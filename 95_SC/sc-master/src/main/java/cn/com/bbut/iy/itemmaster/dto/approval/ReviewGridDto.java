package cn.com.bbut.iy.itemmaster.dto.approval;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  审核流程头档 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewGridDto extends GridDataDTO {
    /**
     * 流程编号
     */
    private String nReviewid;
    /**
     * 流程code
     */
    private String cReviewcode;

    /**
     * 流程名称
     */
    private String cReviewname;

    /**
     * 流程类型编号
     */
    private Integer nTypeid;

    /**
     * 流程类型名称
     */
    private String cTypeName;
}
