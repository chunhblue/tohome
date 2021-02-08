package cn.com.bbut.iy.itemmaster.dto.orderFailed;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订货失败数据DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailedGridDTO extends GridDataDTO {

    // 商品编号
    private String articleId;

    // 商品名称
    private String articleName;

    // 店铺编号
    private String storeCd;

    private String storeName;

    // 订货日期
    private String orderDate;

    // 收货日期
    private String deliveryDate;

    // 便？
    private String shipmentCd;

    // 订货种别
    private String orderMethod;

    // 订货录入方式
    private String orderInputType;

    // ？
    private String orderInputId;

    // 【物流】保质温度带(保管区分)
    private String preserveType;

    // 订货单位
    private String receiveUnitId;

    // 订货数量
    private BigDecimal orderQty;

    // 订货搭赠量
    private BigDecimal orderNoChargeQty;

    // 订货价格
    private BigDecimal orderPrice;

    // 包装内数量
    private BigDecimal orderUnitQty;

    // 订货批量
    private BigDecimal orderBatchQty;

    // 最大订货数量
    private BigDecimal maxOrderQty;

    // 最小订货数量
    private BigDecimal minOrderQty;

    // 商品条码
    private String barcode;

    // 部门CD
    private String depCd;

    // 大分类CD
    private String pmaCd;

    // 中分类CD
    private String categoryCd;

    // 小分类CD
    private String subCategoryCd;

    // 税率CD
    private String purchaseVatCd;

    // 税率
    private BigDecimal purchaseTaxRate;

    // 税额
    private BigDecimal orderTax;

    // 订货金额(含税)
    private BigDecimal orderAmt;

    // 订货金额(未税)
    private BigDecimal orderAmtNoTax;

    // MA
    private String maCd;

    // MA名称
    private String maName;

    // 配送类型
    private String deliveryTypeCd;

    // 配送类型名称
    private String deliveryTypeName;

    // 配送区域
    private String deliveryAreaId;

    // 配送区域名称
    private String deliveryAreaName;

    // 物流中心
    private String deliveryCenterId;

    // 物流中心名称
    private String deliveryCenterName;

    // 供应商ID
    private String vendorId;

    // 供应商名称
    private String vendorName;

    // 配送顺序
    private BigDecimal deliveryOrder;

    // 传票编号
    private String invoiceNo;

    // 行NO
    private String invoiceLineNo;

    // 订货错误号
    private String orderErrorCd;

    // 订单类型名称
    private String methodName;

    // 失败原因名称
    private String failedReason;

    // 订货单位
    private String orderUom;

    // 共通
    private CommonDTO commonDTO;

    // 与最低订货数量差距数量
    private BigDecimal orderGapQuantity;

    // 与最低订货金额差距金额
    private BigDecimal orderGapAmount;

    private BigDecimal minOrderAmt;

    // 供应商订单合计订货数量
    private BigDecimal totalOrderQty;

    private BigDecimal totalOrderAmt;
    // 订单号
    private String orderId;

    private String createUserId;

    private String unitName;
}
