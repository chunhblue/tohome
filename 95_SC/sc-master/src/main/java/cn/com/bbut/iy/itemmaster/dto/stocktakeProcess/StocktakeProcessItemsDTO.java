package cn.com.bbut.iy.itemmaster.dto.stocktakeProcess;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 盘点认列表,一览
 */
@Data
public class StocktakeProcessItemsDTO implements Serializable {

    private String barcode;
    private String articleId;
    private String articleName;
    private String spec;
    private String uom;
    private String onHandQty;
    private String badQty;
    private String firstQty;
    private String secondQty;
    private String variance;
    private String changeQty;
    private BigDecimal baseSalePrice; // 售价
    private String varianceAmt; // 总差异金额

    private String region; // 商品存放区域
    private String converted; // 商品拆包装转换比例
}
