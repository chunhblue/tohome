package cn.com.bbut.iy.itemmaster.dto.priceChange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品列表
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    /**
     * 紧急变价单号
     */
    private String changeId;
    /**
     * 变价业务日期
     */
    private String accDate;
    /**
     * 商品名称
     */
    private String articleName;
    private String articleId;
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
     * 门店号
     */
    private String storeCd;
    /**
     * 变价商品序号
     */
    private String serialNo;
    /**
     * 变价商品货号
     */
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
}
