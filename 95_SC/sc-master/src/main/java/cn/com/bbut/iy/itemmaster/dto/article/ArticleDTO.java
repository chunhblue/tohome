package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品主档对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO extends GridDataDTO {

    private String articleId;

    private String effectiveStartDate;

    private String effectiveEndDate;

    private String articleName;

    private String articleShortName;

    private String articleNameEn;

    private String articleShortNameEn;

    private String deliveryTypeCd;

    private String depCd;
    private String depName;

    private String pmaCd;
    private String pmaName;

    private String categoryCd;
    private String categoryName;

    private String subCategoryCd;
    private String subCategoryName;

    private String spec;

    private String salesUnitId;

    private String brand;
    private String brandName;

    private String grade;

    private String productionLocation;
    private String productionLocationName;

    private String pbFlg;

    private String especialFlg;

    private String articleWeighFlag;

    private String articleType;

    private String articleNewFlag;

    private String storePriceChgFlg;

    private String tagType;

    private String seasonCd;

    private String lifecycleStatus;

    private String recommendReasonCd;

    private String receiveUnitId;

    private BigDecimal packageNumber;

    private String oplMethod;

    private BigDecimal minDisplayQty;

    private String orderType;

    private String barcodeType;

    private String tenantContractNo;

    private String salesVatCd;

    private String purchaseVatCd;

    private BigDecimal referPurchasePrice;

    private BigDecimal referSalePrice;

    private String preserveType;

    private BigDecimal warrantyDays;

    private BigDecimal receiveDateLimit;

    private BigDecimal saleDateLimit;

    private BigDecimal packageOsWeight;

    private BigDecimal packageOsSizeLength;

    private BigDecimal packageOsSizeWidth;

    private BigDecimal packageOsSizeHeight;

    private BigDecimal packageIsWeight;

    private BigDecimal packageIsSizeLength;

    private BigDecimal packageIsSizeWidth;

    private BigDecimal packageIsSizeHeight;

    private String areaCd;

    private BigDecimal memberPrice;

    private BigDecimal nReviewId;

    private Integer reviewStatus;

    private String articleNameVietnamese;

    private String articleShortNameVietnamese;

    private String vietnameseShortName;

    private String manufacturer;

    private String materialType;
    private String materialTypeName;

    private String converter;

    private String purchaseItem;

    private String salesItem;

    private String inventoryItem;

    private String costItem;

    private String dcItem;

    private String isDefaultFreeItem;

    private String isFoodServiceItem;

    private String isFoodServiceStockCount;

    private String notAllowOrderToSupplier;

    private String allowOrderToDc;

    private String customer;

    private String income;

    private BigDecimal discountOnInvoice;

    private BigDecimal priceUnitVat;

    private Integer remainingShelfLife;

    private String agingStockExchangeable;

    private String agingStockReturnable;

    private String imagePath;

    private String remark;

    private String contractExpireDate;

    private String qosExpireDate;

    private String uploadFilePath;

    private String uploadFileName;

    private String updateReason;

    private String regionCd;
    private String regionName;

    private BigDecimal nTypeId;

    private String servicePartner;

    private String phoneCodeValue;

    private String productStatus;

    private String temperature;

    private CommonDTO commonDTO;

}
