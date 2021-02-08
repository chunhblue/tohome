package cn.com.bbut.iy.itemmaster.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * order 商品信息
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailInfo {
    private String barcode;
    /**
     * 商品编号
     */
    private String articleId;
    /**
     * 厂商编号
     */
    private String vendorId;
    /**
     * 厂商名称
     */
    private String vendorName;
    /**
     * 配送类型
     */
    private String deliveryTypeCd;
    /**
     * 配送类型名称
     */
    private String deliveryTypeName;
    /**
     * 规格
     */
    private String spec;
    /**
     * 最低陈列量
     */
    private BigDecimal minDisplayQty;
    /**
     * 订货单位
     */
    private String unitName;
    /**
     * 箱入数
     */
    private BigDecimal orderUnitQty;
    /**
     * 最低订量
     */
    private BigDecimal minOrderQty;
    /**
     * dc商品最低订货量
     */
    private BigDecimal dcMinOrderQty;
    /**
     * 最低订货金额
     */
    private BigDecimal minOrderAmtTax;
    /**
     * 供应商最低定量
     */
    private BigDecimal vendorMinOrderQty;
    /**
     * 最高订量
     */
    private BigDecimal maxOrderQty;
    /**
     * 订货批量
     */
    private BigDecimal orderBatchQty;
    /**
     * 进价促销分类
     */
    private String orderPromotionType;
    /**
     * 进价
     */
    private BigDecimal baseOrderPrice;
    /**
     * 适用进价
     */
    private BigDecimal orderPrice;
    /**
     * 建议售价
     */
    private BigDecimal adviseSalePrice;
    /**
     * dms
     */
    private BigDecimal dms;
    /**
     * 保质天数
     */
    private BigDecimal warrantyDays;
    /**
     * 允收期限
     */
    private BigDecimal receiveDateLimit;
    /**
     * 允销期限
     */
    private BigDecimal saleDateLimit;
    /**
     * 储存条件
     */
    private String preserveTypeName;
}
