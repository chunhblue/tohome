package cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 进退货传票明细DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RRVoucherDetailsDTO extends GridDataDTO {

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

    // 订购单价
    private BigDecimal orderPrice;

    // 订购含税
    private BigDecimal orderAmt;

    // 订购未税
    private BigDecimal orderAmtNoTax;

    // 订购税额
    private BigDecimal orderTax;

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

}
