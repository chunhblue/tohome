package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品包装规格对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CartonSpecificationDTO {

    private String articleId;

    private String effectiveStartDate;

    private BigDecimal netWeightG;

    private BigDecimal grossWeightG;

    private String unitsPer;

    private String unitsPerCarton;

    private String cartonsPerLayer;

    private String cartonsPerPallet;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private String bundlePer;

    private CommonDTO commonDTO;

}
