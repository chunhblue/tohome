package cn.com.bbut.iy.itemmaster.dto.receipt.warehouse;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 仓库配送验收明细DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseDetailsGridDTO extends GridDataDTO {

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

    //税率
    private BigDecimal taxRate;

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

    // 总订货数量
    private BigDecimal orderTotalQty;

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
    private BigDecimal receiveQty1;
    // 实收搭赠数
    private BigDecimal receiveNoChargeQty;
    private BigDecimal receiveNoChargeQty1;

    // 总实收数量
    private BigDecimal receiveTotalQty;

    // 总收货含税
    private BigDecimal receiveTotalAmt;

    // 总收货未税
    private BigDecimal receiveTotalAmtNoTax;

    // 总收货税额
    private BigDecimal receiveTax;

    //收货单号
    private String receiveId;

    // 订货数量验收差
    private BigDecimal differenceQty;

    // 搭赠数量验收差
    private BigDecimal differenceFreeQty;

    // 上传flg
    private String uploadFlg;

    // 上传日期
    private String uploadDate;

    // 判断是否搭赠商品
    private String isFreeItem;

    private String isFreeItemText;

    // 共通
    private CommonDTO commonDTO;
}
