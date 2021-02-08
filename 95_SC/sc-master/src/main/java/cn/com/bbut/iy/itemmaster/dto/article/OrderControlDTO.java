package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品进货控制对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderControlDTO extends GridDataDTO {

    private String articleId;

    private String structureCd;
    private String structureName;

    private String maCd;

    private String vendorId;
    private String vendorShortName;

    private String effectiveStartDate;
    
    private String effectiveEndDate;

    private String saleTaxTypeCd;

    private String orderTaxTypeCd;
    private String orderTaxTypeName;

    private BigDecimal baseOrderPrice;

    private BigDecimal adviseSalePrice;

    private String purchaseUnitId;

    private BigDecimal orderUnitQty;

    private BigDecimal minOrderQty;

    private BigDecimal maxOrderQty;

    private BigDecimal orderBatchQty;

    private String orderPauseFlg;

    private String returnPauseFlg;

    private String receivePriceChgFlg;

    private String vendorArticleId;

    private String shipmentFlg;

    private String majorVendorFlg;

    private String isDefault;

    private BigDecimal dcMOrderQty;

    private String orderPauseDcFlg;

    private BigDecimal remainingShelfLife;

    private String exchangablePauseFlg;

    private BigDecimal exchangableShelfLife;

    private BigDecimal notificationShelfLife;

    private BigDecimal orderDiscountOnInvoice;

    private BigDecimal minOrderAmtTax;

    private String productGroupCd;
    private String productGroupName;

    private String vendorEffDate;

    private String defaultBarcode;

    private String orderTerminated;

    private String dcItem;

    private String createUserId;

    private String createYmd;

    private String createHms;

    private String updateUserId;

    private String updateYmd;

    private String updateHms;

    private String updateScreenId;

    private String updateIpAddress;

    private String nrUpdateFlg;

    private String nrYmd;

    private String nrHms;

}
