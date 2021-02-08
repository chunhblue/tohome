package cn.com.bbut.iy.itemmaster.dto.stocktakeQuery;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author lz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StocktakeQueryParamDTO extends GridParamDTO {
    /**
     * 检索参数json格式，前端传过来后，使用 StocktakeQueryParamDTO 进行反射解析
     */
    private String searchJson;

}
