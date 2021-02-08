package cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MMPromotionItemsDTO {
    private String barcode;
    private String articleId;
    private String articleName;
    // 商品 售价, 分摊后
    private BigDecimal sellingPrice;
    // 商品销售数量
    private BigDecimal saleQty;
    // 商品销售金额
    private BigDecimal saleAmt;
    // 手动分摊值
    private BigDecimal promotionAllotValue;
}
