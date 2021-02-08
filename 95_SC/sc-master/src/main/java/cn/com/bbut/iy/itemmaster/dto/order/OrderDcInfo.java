package cn.com.bbut.iy.itemmaster.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * order dc
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDcInfo {
    /**
     * 订货时间
     */
    private String orderDate;
    /**
     * 业务时间
     */
    private String businessDate;
    /**
     * 传票号
     */
    private String orderId;
    /**
     * 传票状态
     */
    private String orderSts;
    /**
     * 传票类型
     */
    private String orderType;
    /**
     * 店铺编号
     */
    private String storeCd;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * dc id
     */
    private String dcId;
    /**
     * dc名称
     */
    private String dcName;
    /**
     * 备注
     */
    private String orderRemark;
    /**
     * 订单截止时间
     */
    private String expireDate;
    /**
     * 税率cd
     */
    private String purchaseVatCd;
    /**
     * 修改人id
     */
    private String createUserId;
    /**
     * 修改人名称
     */
    private String createUserName;
    /**
     * 修改时间
     */
    private String createYmd;
    /**
     * 修改人id
     */
    private String updateUserId;
    /**
     * 修改人名称
     */
    private String updateUserName;
    /**
     * 修改时间
     */
    private String updateYmd;

    private String reviewStatus;
}
