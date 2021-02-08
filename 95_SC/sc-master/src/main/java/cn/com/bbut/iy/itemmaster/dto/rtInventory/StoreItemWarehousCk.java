package cn.com.bbut.iy.itemmaster.dto.rtInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class StoreItemWarehousCk {
    private String warehousDate;// 入库日期
    private String companyCd;// 公司代码
    private String storeCd;// 店铺cd
    private String articleId;//商品Id
    private BigDecimal stockQty;//库存数量
    private BigDecimal stockAmt;//库存金额
    private String warehousDays;//入库天数
    private String yearMonth;//年月
    private BigDecimal upStockQty; // 修正后库存数量
    private BigDecimal upStockAmt;// 修正后库存金额

    private BigDecimal avgCostNotax = BigDecimal.ZERO;    // sk0000_today 商品平均价格
    private String usingFlag;
}
