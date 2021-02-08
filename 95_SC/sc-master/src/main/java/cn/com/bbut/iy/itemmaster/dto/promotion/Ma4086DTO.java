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
public class Ma4086DTO extends GridDataDTO {

    private String promotionCd;

    private String promotionTermGroup;

    private String brandCd;
    private String brandName;

    private String articleId;
    private String articleName;

    private String subCategoryCd;
    private String subCategoryName;

    private String depCd;
    private String depName;

    private String pmaCd;
    private String pmaName;

    private String categoryCd;
    private String categoryName;

    private BigDecimal displaySeq;

    private String effectiveSts;

}
