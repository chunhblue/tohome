package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MM促销商品设定对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4070DTO extends GridDataDTO {

    private String promotionCd;

    private String promotionTermGroup;

    private String articleId;
    private String articleName;

    private String barcode;

    private String subCategoryCd;
    private String subCategoryName;

    private BigDecimal articleSalePrice;

    private BigDecimal displaySeq;

    private String effectiveSts;

}
