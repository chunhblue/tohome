package cn.com.bbut.iy.itemmaster.dto.receipt.rrModify;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 进退货传票修正明细DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RRModifyDetailsDTO extends GridDataDTO {

    private String storeCd;

    private String orderId;

    private String serialNo;

    private String articleId;
    private String articleName;
    private String articleNameEn;

    private String barcode;

    private String vendorId;

    private String promotionId;

    private String orderUnit;

    private BigDecimal orderQty;

    private BigDecimal orderNochargeQty;

    private BigDecimal orderPrice;

    private BigDecimal orderAmt;

    private BigDecimal orderAmtNotax;

    private BigDecimal orderTax;

    private BigDecimal receivePrice;

    private BigDecimal receiveQty;

    private BigDecimal receiveNochargeQty;

    private BigDecimal receiveTotalQty;

    private BigDecimal receiveTotalAmt;

    private BigDecimal receiveTotalAmtNotax;

    private BigDecimal receiveTax;

    private String reasonId;

    private BigDecimal adjustAmt;

    private BigDecimal adjustQty;

    private String spec;

    private BigDecimal realtimeQty; // 实时库存数量

    /* 收货/退货数据 */
    private BigDecimal rrOrderPrice;
    private BigDecimal rrOrderQty;
    private BigDecimal rrOrderAmt;

    private BigDecimal modifyQty;  // 修正数量
    private BigDecimal modifyNochargeQty;
    private String isFreeItem;
    private String isFreeItemText;
}
