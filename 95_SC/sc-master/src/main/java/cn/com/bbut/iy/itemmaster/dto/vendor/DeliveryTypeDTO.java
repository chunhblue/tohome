package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商主档配送范围对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryTypeDTO extends GridDataDTO {

    private String vendorId;

    private String maCd;

    private String deliveryTypeCd;

    private String effectiveStartDate;

    private String effectiveEndDate;

    private String vendorTelNo;

    private String vendorFaxNo;

    private String districtCd;

    private String vendorAddress1;

    private String vendorAddress2;

    private String adminName;

    private BigDecimal minOrderAmtTax;

    private String orderSendMethod;

    private BigDecimal supplyPurchaseRate;

    private BigDecimal badDiscountRate;

    private String weeklyDeliveryDay;

    private String deliveryDay1;

    private String deliveryDay2;

    private String deliveryDay3;

    private String deliveryDay4;

    private String deliveryDay5;

    private String deliveryDay6;

    private String deliveryDay7;

    private String shipmentFlg;

    private String structureCd;

    private String structureName;

    private String regionCd;

    private String regionName;

    private String orderType;

    private String orderTypeName;

}
