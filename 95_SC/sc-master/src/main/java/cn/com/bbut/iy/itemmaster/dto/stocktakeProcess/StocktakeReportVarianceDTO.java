package cn.com.bbut.iy.itemmaster.dto.stocktakeProcess;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StocktakeReportVarianceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 分类编号
    private String categoryCd;
    // 分类名称
    private String categoryName;
    // 商品种类
    private BigDecimal sku;
    // 库存数量
    private BigDecimal inventoryQty;
    // 差异数量
    private BigDecimal varianceQty;
    // 差异金额
    private BigDecimal varianceAmt;
    // 成本售价
    private BigDecimal baseSalePrice;
}
