package cn.com.bbut.iy.itemmaster.dto.stocktakeProcess;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StocktakeReportItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 店铺cd
    private String storeCd;
    // 店铺名称
    private String storeName;
    // 商品id
    private String articleId;
    // 商品名称
    private String articleName;
    // 分类编号
    private String categoryCd;
    // 分类名称
    private String categoryName;
    // 商品材料类型
    private String materialName;
    // 单位
    private String uom;
    // 库存数量
    private BigDecimal inventoryQty;
    // 盘点数量
    private BigDecimal piQty;
    // 成本售价
    private BigDecimal baseSalePrice;
    // 差异数量
    private BigDecimal varianceQty;
    // 差异金额
    private BigDecimal varianceAmt;
    // 不良数量
    private BigDecimal badQty;
}
