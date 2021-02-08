package cn.com.bbut.iy.itemmaster.dto.bomSale;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * BOM销售报表对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BomSaleDTO{

    private String storeCd;
    private String storeName;

    private String accDate;

    private String articleId;
    private String barcode;
    private String articleName;

    private String saleStatus;

    private String promotionId;

    private BigDecimal dmSaleQty;

    private BigDecimal dmAmtNoTax;

    private BigDecimal dmAmt;

    private BigDecimal dmSaleCost;

    private BigDecimal instoreSaleQty;

    private BigDecimal instoreSaleAmtNoTax;

    private BigDecimal instoreSaleAmt;

    private BigDecimal instoreSaleCost;

    private BigDecimal normalSaleQty;

    private BigDecimal normalSaleAmtNoTax;

    private BigDecimal normalSaleAmt;

    private BigDecimal normalSaleCost;

    private BigDecimal dzSaleQty;

    private BigDecimal dzSaleAmtNoTax;

    private BigDecimal dzSaleAmt;

    private BigDecimal dzSaleCost;

    private BigDecimal saleReturnQty;

    private BigDecimal saleReturnAmtNoTax;

    private BigDecimal saleReturnAmt;

    private BigDecimal salesReturnCost;

    private BigDecimal saleCostT;

    private BigDecimal saleQtyT;

    private BigDecimal saleAmtNoTaxT;

    private BigDecimal saleAmtT;

    private BigDecimal receiveQtyT;

    private BigDecimal receiveAmtNoTaxT;

    private BigDecimal receiveAmtT;

    private BigDecimal receiveInQty;

    private BigDecimal receiveInAmtNoTax;

    private BigDecimal receiveInAmt;

    private BigDecimal transferInQty;

    private BigDecimal transferInAmtNoTax;

    private BigDecimal transferInAmt;

    private BigDecimal receiveMQty;

    private BigDecimal receiveMAmtNoTax;

    private BigDecimal receiveMAmt;

    private BigDecimal transferInMQty;

    private BigDecimal transferInMAmtNoTax;

    private BigDecimal transferInMAmt;

    private BigDecimal costDeductAmtNoTax;

    private BigDecimal costDeductAmt;

    private BigDecimal returnQtyT;

    private BigDecimal returnAmtNoTaxT;

    private BigDecimal returnAmtT;

    private BigDecimal returnQty;

    private BigDecimal returnAmtNoTax;

    private BigDecimal returnAmt;

    private BigDecimal transferOutQty;

    private BigDecimal transferOutAmtNoTax;

    private BigDecimal transferOutTax;

    private BigDecimal returnMQty;

    private BigDecimal returnMAmtNoTax;

    private BigDecimal returnMAmt;

    private BigDecimal transferOutMQty;

    private BigDecimal transferOutMAmtNoTax;

    private BigDecimal transferOutMAmt;

    private BigDecimal receiveQtyTotal;

    private BigDecimal receiveAmtAmtNoTaxTotal;

    private BigDecimal receiveAmtTotal;

    private BigDecimal inventoryAdjustQty;

    private BigDecimal inventoryAdjustAmtNoTax;

    private BigDecimal inventoryAdjustAmt;

    private BigDecimal clearQty;

    private BigDecimal clearAmtNoTax;

    private BigDecimal clearAmt;

    private BigDecimal disuseQty;

    private BigDecimal disuseAmtNoTax;

    private BigDecimal disuseAmt;

    private BigDecimal piQty;

    private BigDecimal piAmtNoTax;

    private BigDecimal piAmt;

    private BigDecimal otherAdjustQty;

    private BigDecimal otherAdjustAmtNoTax;

    private BigDecimal otherAdjustAmt;

    private BigDecimal instoreTransferQty;

    private BigDecimal instoreTransferAmtNoTax;

    private BigDecimal instoreTransferAmt;

    private BigDecimal instoreTransferInQty;

    private BigDecimal instoreTransferInAmtNoTax;

    private BigDecimal instoreTransferInAmt;

    private BigDecimal instoreTransferOutQty;

    private BigDecimal instoreTransferOutAmtNoTax;

    private BigDecimal instoreTransferOutAmt;

    private BigDecimal costAdjustAmtNoTaxT;

    private BigDecimal costAdjustAmtT;

    private BigDecimal costDiffAmtNoTax;

    private BigDecimal costDiffAmt;

    private BigDecimal returnAdjustAmtNoTax;

    private BigDecimal returnAdjustAmt;

    private BigDecimal transerAdjustAmtNoTax;

    private BigDecimal transerAdjustAmt;

    private BigDecimal instoreTransferAdjAmtNoTax;

    private BigDecimal instoreTransferAdjAmt;

    private BigDecimal avgCostNoTax;

    private BigDecimal inventoryBeginBalance;

    private BigDecimal beginCostNoTax;

    private BigDecimal inventoryEndBalance;

    private BigDecimal inventoryEndTax;

    private String depCd;

    private String pmaCd;

    private String categoryCd;

    private String subCategoryCd;

    private String costFormula;

    private String abnormalReason;

    private BigDecimal inventoryQty;

    private BigDecimal inventoryAmtNoTax;

    private BigDecimal lastReceivePriceNoTax;

    private BigDecimal masterPriceNoTax;

    private String isTransaction;

    private String isSaleTransaction;

    private Integer againCalcTimes;

    private String againCalcDate;

    private String articleType;

    private String corpCd;

    private String zoCd;

    private String doCd;

    private String ofc;

    private String maCd;

    private String storeTypeCd;

    private String isNewInsert;

    private String isCostCalcFlg;

    private CommonDTO commonDTO;

    private BigDecimal saleTaxT;

    private BigDecimal returnTaxT;

    private BigDecimal grossMargin;

    private BigDecimal grossMarginRate;

    private String spec;

    private String salesUnit;

}
