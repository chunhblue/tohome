package cn.com.bbut.iy.itemmaster.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * order 详细商品
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDetailDTO implements Serializable {
    /**
     * 商品编号
     */
    private String articleId;
    /**
     * 商品条码
     */
    private String barcode;
    /**
     * 商品名称
     */
    private String articleName;
    /**
     * 实时在库量
     */
    private String realtimeStock;
    /**
     * 自动订货数量
     */
    private String autoOrderQty;
    /**
     * 订购数量
     */
    private BigDecimal orderQty;
    /**
     * 门店编号
     */
    private String storeCd;
    /**
     * 订货日
     */
    private String orderDate;
    /**
     * 便
     */
    private String shipmentCd;
    /**
     * 供应商ＩＤ
     */
    private String vendorId;

    private String vendorName;
    /**
     * 订货单位
     */
    private String purchaseUnitId;
    /**
     * 订货搭赠数量
     */
    private BigDecimal orderNochargeQty;
    /**
     * 上传订货量
     */
    private BigDecimal uploadOrderQty;
    /**
     * 上传订货搭赠量
     */
    private BigDecimal uploadOrderNochargeQty;
    /**
     * 订货价格
     */
    private BigDecimal baseOrderPrice;
    /**
     * 订货价格 最终
     */
    private BigDecimal orderPrice;
    /**
     * 进价促销cd
     */
    private String orderPromotionCd;

    /**
     * psd
     */
    private String psd;

    /**
     * dc商品区分
     */
    private String dcItem;

    /**
     * 订货商品是否上传
     */
    private String uploadFlg;

    /**
     * 订货状态
     */
    private String orderSts;
    /**
     * 修改前订购数量
     */
    private BigDecimal oldOrderQty;

    /**
     * 订商品标识  1:新增 2:修改
     */
    private String orderFlg;

    private String createUserId;

    private String createYmd;

    private String createHms;

    private String updateUserId;

    private String updateYmd;

    private String updateHms;

    private String totalQty;

    // 过去4周商品平均报废数量
    private String writeOffQty;
    /**
     * 促销规则描述
     */
    private String promotionDescription;
}
