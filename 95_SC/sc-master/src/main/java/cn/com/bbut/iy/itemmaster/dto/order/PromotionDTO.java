package cn.com.bbut.iy.itemmaster.dto.order;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 促销例外添加
 * 
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO{
    /**
     * 店铺编号
     */
    private String storeCd;

    /**
     * 促销例外区分 0：排除 1： 新增
     */
    private String exceptType;

    /**
     * 适用日
     */
    private String effectiveDate;

    /**
     * 促销cd
     */
    private String promotionCd;

    /**
     * 商品id
     */
    private String articleId;

    /**
     * 搭赠数量
     */
    private BigDecimal presentQty;

    /**
     * 单位
     */
    private String unitId;

    /**
     * 供应商id
     */
    private String vendorId;

    /**
     * 适用进价
     */
    private BigDecimal orderPrice;

    /**
     * 促销进价
     */
    private BigDecimal proPurchasePrice;

    private String updateYmd;

    private String updateHms;

}
