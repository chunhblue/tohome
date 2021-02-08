package cn.com.bbut.iy.itemmaster.dto.orderVendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * order vendor
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVendorInfo{
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
     * 供应商id
     */
    private String vendorId;
    /**
     * 供应商名称
     */
    private String vendorName;
    /**
     * 供应商邮箱
     */
    private String vendorEmail;
    /**
     * 最低订货数量
     */
    private BigDecimal minOrderQty;
    /**
     * 最低订货量
     */
    private BigDecimal minOrderAmtTax;
    /**
     * 备注
     */
    private String orderRemark;
    /**
     * 订单截止时间
     */
    private String expireDate;
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

    private String regionCd;
    private String regionName;

    private String cityCd;
    private String cityName;

    private String districtCd;
    private String districtName;

    // 订单折扣率
    private String discountRate;
}
