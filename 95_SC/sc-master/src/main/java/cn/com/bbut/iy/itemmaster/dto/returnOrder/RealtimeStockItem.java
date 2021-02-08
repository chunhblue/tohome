package cn.com.bbut.iy.itemmaster.dto.returnOrder;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zcz
 * 商品实时库存
 */
@Data
public class RealtimeStockItem {
    /**
    * 商品编号
    */
    private String articleId;
   /**
    * 商品条码
    */
    private String barcode;
   /**
    * 商品名称
    */
    private String articleName;
   /**
    * 规格
    */
    private String spec;
    /**
    * 单位
    */
    private String salesUnitId;
    /**
    * 昨日库存数量
    */
    private BigDecimal onHandQty;
   /**
    * 当日销售数量
    */
    private BigDecimal saleQty;
   /**
    * 当日收货数量
    */
    private BigDecimal receiveQty;
   /**
    * 当日库存调整数量
    */
    private BigDecimal adjustQty;
   /**
    * 在途数量
    */
    private BigDecimal onOrderQty;
    /**
     * 实时库存数量
     */
    private BigDecimal realtimeQty;
        
}
