package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MM促销品牌设定对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4085DTO extends GridDataDTO {

    private String promotionCd;

    private String promotionTermGroup;

    private String brandCd;
    private String brandName;

    private BigDecimal displaySeq;

    private String effectiveSts;

}
