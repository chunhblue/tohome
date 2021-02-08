package cn.com.bbut.iy.itemmaster.dto.priceByDay;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 当日适用价格一览对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PriceByDayDTO extends GridDataDTO {

    private String effectiveDate;

    private String maCd;

    private String storeTypeCd;
    private String storeTypeName;

    private String articleId;
    private String articleName;
    private String articleNameEn;

    private String barcode;

    private String storeCd;
    private String storeName;

    private String vendorId;
    private String vendorName;

    private String depCd;
    private String depName;

    private String pmaCd;
    private String pmaName;

    private String categoryCd;
    private String categoryName;

    private String subCategoryCd;
    private String subCategoryName;

    private String lifecycleStatus;

    private String orderPromotionCd;

    private String orderSts;

    private BigDecimal orderPrice;

    private String salePromotionCd;

    private String sellingSts;

    private BigDecimal sellingPrice;

    private BigDecimal baseSalePrice;

    private BigDecimal cost;

    private CommonDTO commonDTO;

}
