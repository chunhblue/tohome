package cn.com.bbut.iy.itemmaster.dto.rtInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对应es-api里面用的对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeDto {
    private String store_cd;// 店铺cd
    private String article_id;//商品Id
    private BigDecimal sale_qty;//销售数量
    private BigDecimal on_hand_qty;//昨日在库量
    private BigDecimal on_order_qty;//在途量
    private BigDecimal write_off_qty;//报废量
    private BigDecimal return_qty;//退货量
    private BigDecimal adjustment_qty;//库存调整量
    private BigDecimal transfer_out_qty;//转出数量
    private BigDecimal transfer_in_qty;//转入数量
    private BigDecimal receive_qty;//收货数量
    private BigDecimal receive_corr_qty;//收货更正数量
    private BigDecimal return_corr_qty;//退货更正数量
    private BigDecimal transfer_out_corr_qty;//转出更正数量
    private BigDecimal transfer_in_corr_qty;//转入更正数量
    private int in_es_time;//创建时间
}
