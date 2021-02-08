package cn.com.bbut.iy.itemmaster.dto.priceChange;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 * 紧急变价一览grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDetailGridDto extends GridDataDTO {
    /**
     * 紧急变价单号
     */
    private String changeId;
    /**
     * 变价业务日期
     */
    private String accDate;
    /**
     * 店铺cd
     */
    private String storeCd;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 商品名称
     */
    private String articleName;
    /**
     * 规格
     */
    private String spec;
    /**
     * 单位
     */
    private String unitName;
    /**
     * 销售状态
     */
    private String saleStatus;
    /**
     * 变价商品序号
     */
    private String serialNo;
    /**
     * 变价商品货号
     */
    private String articleId;
    /**
     * 变价商品条码
     */
    private String barcode;
    /**
     * 原价格（变价前价格）
     */
    private BigDecimal oldPrice;
    /**
     * 新价格（变价后价格）
     */
    private BigDecimal newPrice;
    /**
     * 售价（未变价价格）
     */
    private BigDecimal salePrice;
    /**
     * 变价商品delta号
     */
    private String deltaSerialNo;
    /**
     * 变价商品日期
     */
    private String createYmd;
    /**
     * 变价商品时间
     */
    private String createHms;
    /**
     * 变价时间
     */
    private String createDate;
    /**
     * 录入人
     */
    private String createUserId;
    /**
     * 录入人
     */
    private String createUserName;
}
