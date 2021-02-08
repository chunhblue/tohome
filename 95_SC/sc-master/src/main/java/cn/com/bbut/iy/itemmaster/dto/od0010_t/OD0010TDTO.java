package cn.com.bbut.iy.itemmaster.dto.od0010_t;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * od0010_t
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OD0010TDTO extends GridDataDTO {

    // 收货编号
    private String receiveId;

    // 店铺编号
    private String storeCd;

    // 订单号
    private String orderId;

    // 序号
    private String serialNo;

    // 商品货号
    private String articleId;

    // 商品货号
    private String articleName;
    private String articleNameEn;

    // 商品条码
    private String barcode;

    // 厂商编号
    private String vendorId;

    // 促销期号
    private String promotionId;

    // 规格
    private String spec;

    // 订货单位
    private String orderUnit;

    // 订货单位
    private String uom;

    // 税率
    private String vat;

    // 订购数量
    private BigDecimal orderQty;

    // 搭赠数量
    private BigDecimal orderNoChargeQty;

    // 订货总数量
    private BigDecimal orderTotalQty;

    // 订购单价
    private BigDecimal orderPrice;

    // 订购含税
    private BigDecimal orderAmt;

    // 订购未税
    private BigDecimal orderAmtNoTax;

    // 订购税额
    private BigDecimal orderTax;

    // 税率
    private BigDecimal taxRate;

    // 收货单价
    private BigDecimal receivePrice;

    // 实收数量
    private BigDecimal receiveQty;

    // 实收搭赠数
    private BigDecimal receiveNoChargeQty;

    // 总实收数量
    private BigDecimal receiveTotalQty;

    // 总收货含税
    private BigDecimal receiveTotalAmt;

    // 总收货未税
    private BigDecimal receiveTotalAmtNoTax;

    // 总收货税额
    private BigDecimal receiveTax;

    // 共通DTO
    private CommonDTO commonDTO;

    private String differenceReason;

    private String differenceReasonText;
    // 收货日期
    private String receivingDate;

    private String isFreeItem;

    private String isFreeItemText;

    // 实退数量
    private BigDecimal returnQty;
    // 修正数量
    private BigDecimal correctionDifference;
    // 上次修正数量
    private BigDecimal lastCorrectionDifference;
}
