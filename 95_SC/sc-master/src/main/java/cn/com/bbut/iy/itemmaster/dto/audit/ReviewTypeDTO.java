package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewTypeDTO {
    /**
     * 类型ID
     */
    private int nTypeid;

    /**
     * 类型名称
     */
    private String cTypeName;
}
