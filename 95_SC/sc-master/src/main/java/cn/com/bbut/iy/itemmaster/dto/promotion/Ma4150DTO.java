package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4150DTO extends GridDataDTO {
    private String promotionCd;//促销Cd
    private String articleId;//商品ID
    private String articleName;//商品名称
    private String cityCd;//城市Cd
    private String cityName;//城市名称
    private String maCd;//Tier
    private String maName;//TierName
    private BigDecimal sellingPrice;//销售价格
    private BigDecimal promotionPrice;//促销价格
    private Integer displaySeq;//排序字段
}
