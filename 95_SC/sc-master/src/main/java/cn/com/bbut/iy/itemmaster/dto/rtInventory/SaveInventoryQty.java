package cn.com.bbut.iy.itemmaster.dto.rtInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 连接ES,与其字段对应
 * lch
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SaveInventoryQty {
    private String storeCd;// 店铺cd
    private String articleId;//商品Id
    private String detailType;//库存类型
    private Timestamp inEsTime;

    private Float saleQty;//销售数量
    private Float onHandQty;//昨日在库量
    private Float onOrderQty;//在途量
    private Float writeOffQty;//报废量
    private Float returnQty;//退货量
    private Float adjustmentQty;//库存调整量
    private Float transferOutQty;//转出数量
    private Float transferInQty;//转入数量
    private Float receiveQty;//收货数量
    private Float receiveCorrQty;//收货更正数量
    private Float returnCorrQty;//退货更正数量
    private Float transferOutCorrQty;//转出更正数量
    private Float transferInCorrQty;//转入更正数量

    private String depCd;   // 部门编号
    private String pmaCd;   // 大分类编号
    private String categoryCd;   // 中分类编号
    private String subCategoryCd;   // 小分类编号

    private String warehousDate;   // BI库存日期
    private BigDecimal bIStockQty;   // BI库存数量
    private BigDecimal bIStockAmt;   // BI库存金额
    private BigDecimal avgCostNotax = BigDecimal.ZERO;    // sk0000_today 商品平均价格

    private Float realtimeQty; // 实时库存数量
    private Float inventoryQty; // 通用qty
}
