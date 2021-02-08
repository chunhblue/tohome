package cn.com.bbut.iy.itemmaster.dto.orderVendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * vendor/dc order 详情信息
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVendorDetailInfo  extends GridDataDTO {
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 商品条码
     */
    private String barcode;
    /**
     * 商品编号
     */
    private String articleId;
    /**
     * 商品名称
     */
    private String articleName;
    /**
     * 厂商编号
     */
    private String vendorId;
    /**
     * 厂商名称
     */
    private String vendorName;
    /**
     * 规格
     */
    private String spec;
    /**
     * converter
     */
    private String converter;
    /**
     * 过去平均4周销售量
     */
    private String psd;
    /**
     * 最低陈列量
     */
    private BigDecimal minDisplayQty;
    /**
     * 推荐订货量
     */
    private BigDecimal autoOrderQty;
    /**
     * 实际订货数量
     */
    private BigDecimal orderQty;
    /**
     * 订货搭赠数量
     */
    private BigDecimal orderNochargeQty;
    /**
     * 订货总量
     */
    private BigDecimal orderTotalQty;
    /**
     * 销售价格
     */
    private BigDecimal salePrice;
    /**
     * 订货单位
     */
    private String unitId;
    /**
     * 订货单位
     */
    private String unitName;
    /**
     * 订货价格
     */
    private BigDecimal orderPrice;
    /**
     * 税区分
     */
    private String purchaseVatCd;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 订货批量
     */
    private BigDecimal orderBatchQty;
    /**
     * 最低订货金额
     */
    private BigDecimal minOrderAmtTax;
    /**
     * 最低订量
     */
    private BigDecimal minOrderQty;
    /**
     * 最高订量
     */
    private BigDecimal maxOrderQty;

    /**
     * dc moq
     */
    private BigDecimal dcMinOrderQty;

    /**
     * 订货总金额
     */
    private BigDecimal orderTotalAmt;

    private String totalQty;
}
