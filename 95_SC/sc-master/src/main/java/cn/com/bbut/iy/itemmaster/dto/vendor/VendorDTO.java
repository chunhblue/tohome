package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商主档对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VendorDTO extends GridDataDTO {

    private String vendorId;

    private String effectiveStartDate;

    private String effectiveEndDate;

    private String vendorName;

    private String vendorNameShort;

    private String behalfVendorId;

    private String businessType;

    private String tradeStartDate;

    private String tradeEndDate;

    private String orderFlg;

    private String pmaCd;

    private String depCd;

    private String regionCd;

    private String vendorAddress1;

    private String vendorAddress2;

    private String vendorTelNo;

    private String vendorFaxNo;

    private String vendorZipCode;

    private String adminName;

    private String vendorEmail;

    private String orderTelNo;

    private String orderFaxNo;

    private String districtCd;

    private String orderAddress1;

    private String orderAddress2;

    private String orderEmail;

    private String orderAdminName;

    private String orderAdminName2;

    private String orderAdminName3;

    private BigDecimal minOrderAmtTax;

    private String minOrderQty;

    private String orderSendMethod;

    private BigDecimal supplyPurchaseRate;

    private BigDecimal badDiscountRate;

    private String shipmentFlg;

    private String payTypeCd;

    private String payPeriodCd;

    private String payBank;

    private String payBankAccount;

    private String payAccountName;

    private String branch;

    private String payTaxNo;

    private String payCurrencyType;

    private String payPeriodDay;

    private String contractSigned;

    private String liquidationAgreement;

    private String license;

    private String accountsReceivable;

    private String bankInformation;

    private String addendumSigned;

    private String trading;

    private String authorizationLetter;

    private String listingProposal;

    private String othersCheck;

    private String others;

    private BigDecimal paymentAmount;

    private BigDecimal collectionAmount;

    private BigDecimal totalAmount;

    private String dealSuspendDate;

    private String returnStartDate;

    private String returnEndDate;

    private String eliminateDate;

    private String paymentDate;

    private String processingMethod;

    private String eliminateReason;

    private String uploadFilePath;

    private String uploadFileName;

    private String tradingTermNo;

    private String tradingTermEffectiveStartDate;

    private String cutoffTime;

    private String businessRegNo;

    private String bankCountry;

    private String creditLimit;

    private String keepCreditLimit;

    private String accountPayable;

    private String accountReceivable;

    private String remarks;

    private String masterPayment;

    private String vendorCodeReceivable;

    private String vendorType;

    private CommonDTO commonDTO;

    private String regionName;

    private String topDepName;

}
