package cn.com.bbut.iy.itemmaster.dto.approval;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  审核流程头档 param
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewParamDto extends GridParamDTO {
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
}
