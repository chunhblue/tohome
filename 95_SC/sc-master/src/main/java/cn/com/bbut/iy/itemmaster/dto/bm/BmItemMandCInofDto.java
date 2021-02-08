package cn.com.bbut.iy.itemmaster.dto.bm;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * order 使用，取得单品的 基本信息，和店铺控制表的信息 及（M表和C表数据）
 * 
 * @author HanHaiyun
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BmItemMandCInofDto {

    /** Item Barcode **/
    private String item;
    /** 商品名称 **/
    private String name;
    /** 商品系统码 **/
    private String itemSystem;

    private String stroe;

    private String dpt;
    // 销售单价
    private BigDecimal priceTax;
    // 进货单价
    private BigDecimal costTax;
}
