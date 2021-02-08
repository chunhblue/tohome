package cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MMPromotionDataDTO {
    // 订单编号
    private Integer tranSerialNo;

    private String storeCd;
    private String storeName;
    private String accDate;
    private String promotionCd;
    // 促销主题
    private String promotionTheme;
    // 促销模式
    private String promotionPattern;
    // 促销类型
    private String promotionType;
    // 分摊类型
    private String distributionType;
    // 分摊类型 cd 1:手动比例分摊    2:自动比例分摊    3:不分摊
    private String promotionAllotCd;
    // 总售价
    private BigDecimal totalSellingPrice;
    // 总销售数量
    private BigDecimal totalSaleQty;
    // 总销售金额
    private BigDecimal totalSaleAmt;
    private BigDecimal discountAmt;

    // 商品明细数据
    private List<MMPromotionItemsDTO> list;
}
