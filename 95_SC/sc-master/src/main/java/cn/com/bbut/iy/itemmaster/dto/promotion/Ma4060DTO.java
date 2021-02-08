package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MM促销条件设定对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4060DTO extends GridDataDTO {

    private String promotionCd;

    private String promotionTermGroup;

    private BigDecimal promotionTermQty;

    private BigDecimal promotionTermAmt;

    private BigDecimal promotionValue;

    private BigDecimal promotionAllotValue;

    private String effectiveSts;

}
