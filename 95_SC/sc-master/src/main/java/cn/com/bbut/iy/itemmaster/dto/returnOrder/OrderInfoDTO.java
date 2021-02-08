package cn.com.bbut.iy.itemmaster.dto.returnOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDTO{
    /**
     * 订货区分
     */
    private String orderDifferentiate;
    /**
     * 订货时间
     */
    private String orderDate;
    /**
     * 单据号
     */
    private String orderId;
    /**
     * 原单据号
     */
    private String orgOrderId;
    /**
     * 店铺号
     */
    private String storeCd;
    /**
     * 店铺号
     */
    private String storeName;
    /**
     * 仓库号
     */
    private String vendorId;
    /**
     * 仓库
     */
    private String vendorName;
    /**
     * 配送中心id
     */
    private String dcId;
    /**
     * 配送中心
     */
    private String dcName;

    /**
     * 正在审核的退货单
     */
    private int auditNum;

    //退货合同信息
    /**
     * 退货标识 1为可退货  0不可退货
     */
    private String thisReturn;
    /**
     * 退货保质期
     */
    private String thisRemainReturn;
    /**
     * 退货保质期
     */
    private String thisExchangeReturn;
}
