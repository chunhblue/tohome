package cn.com.bbut.iy.itemmaster.dto.pi0100c;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 盘点查询商品信息
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StocktakeItemDTOC {
    // 商品条码
    private String barcode;

    private String articleId;
    private String articleName;
    private String storeCd;
    private String piCd;
    private String piDate;
    private String piStatus;
    // 生效开始日期
    private String effectiveStartDate;

    // 生效结束日期
    private String effectiveEndDate;
    // 单位
    private String uomCd;
    private String uom;

    // 规格
    private String spec;

    //初盘品项数
    private BigDecimal firstQty;

    //复盘品项数
    private BigDecimal secondQty;

    //盘点儿商品价格
   private BigDecimal expensePrice;

   //库存数
   private BigDecimal qty;

   // 实时库存
   private String stockQty;

   private String updateYmd;
   private String updateHms;

   // 上次修改时间
   private String lastUpdateTime;

   // 描述
   private String note;
}
