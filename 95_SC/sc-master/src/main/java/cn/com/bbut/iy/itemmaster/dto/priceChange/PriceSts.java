package cn.com.bbut.iy.itemmaster.dto.priceChange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 变价状态
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceSts {
    /**
     * 生命周期状态
     */
    private String lifecycleStatus;
    /**
     * 商品形态
     */
    private String articleType;
    /**
     * 门店可否紧急变价标志
     */
    private String storePriceChgFlg;
}
