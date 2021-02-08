package cn.com.bbut.iy.itemmaster.dto.receipt.rrModify;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 进退货传票修正头档DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RRModifyDTO extends GridDataDTO {

    private String storeCd;
    private String storeName;

    private String orderId;

    private String orgOrderId;

    private String vendorId;
    private String vendorName;

    private String deliveryTypeCd;

    private String deliveryCenterId;

    private String deliveryAreaCd;

    private String shipment;

    private String orderType;

    private String orderMethod;

    private String paymentType;

    private String promotionId;

    private String orderDate;

    private String deliveryDate;

    private String expireDate;

    private String receiveDate;

    private String vendorAddress;

    private String storeAddress;

    private String orderSts;

    private String nrFlg;

    private String orderRemark;

    private String purchaseVatCd;

    private BigDecimal taxRate;

    private BigDecimal orderAmt;

    private BigDecimal orderTax;

    private BigDecimal receiveAmt;

    private BigDecimal receiveTax;

    private String orderDifferentiate;

    private String uploadFlg;

    private String uploadDate;

    // 审核状态
    private String reviewStatus;

    // 退货类型: 10 直接退货 20 原订单退货
    private String returnType;

    // 共通
    private CommonDTO commonDTO;

    // 昨日库存数量
    private BigDecimal onHandQty;
    // 在途数量
    private BigDecimal onOrderQty;

    private String am;
    private String oc;
    private String om;

}
