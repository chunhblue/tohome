package cn.com.bbut.iy.itemmaster.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 传票查询商品信息
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoDTO{

    // 商品编号
    private String itemCode;

    // 商品名称
    private String itemName;

    // 商品条码
    private String barcode;

    // 单位
    private String uomCd;
    private String uom;

    // 规格
    private String spec;

    // 进价
    private BigDecimal orderPrice;

    // 售价
    private BigDecimal sellingPrice;

    // 成本
    private BigDecimal avgCost;
    private BigDecimal baseOrderPrice;

    // 税率
    private BigDecimal taxRate;

    // 库存量
    private BigDecimal onHandQty;
}
