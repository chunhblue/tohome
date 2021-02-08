package cn.com.bbut.iy.itemmaster.dto.base;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签dto 适用于 名称编号的返回
 * 
 * @author songxz
 * @date: 2019年10月9日 - 下午1:30:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemStoreDTO {
    // 商品系统码
    private String itemSystem;
    // 所属店铺
    private String store;
    // 销售单价
    private BigDecimal priceTax;
    // 进货单价
    private BigDecimal costTax;
}
