package cn.com.bbut.iy.itemmaster.dto.bm;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Item Barcode和对应的店铺控制记录信息，Ajax返回结果
 * 
 * @author songxz
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ItmeForStoreResultDto implements Serializable {

    private static final long serialVersionUID = 2012L;
    // 序号No
    private Integer no;
    // bm编码
    private String bmCode;
    // bm类型
    private String bmtype;
    // 店铺号
    private String store;
    // 商品系统码
    private String itemSystem;
    // Item Barcode
    private String item;
    // 商品名称
    private String itemName;
    // 商品DPT
    private String dpt;
    // bm销售价格
    private BigDecimal bmPrice;
    // 销售单价
    private BigDecimal priceTax;
    // 进货单价
    private BigDecimal costTax;
    // 折扣销售单价
    private BigDecimal disPriceTax;
    // 毛利率
    private BigDecimal profitRate;
    // 确认状态 0：未确认 1：已确认 2：驳回
    private Integer status;
}
