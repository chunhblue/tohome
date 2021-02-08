package cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 进退货传票查询DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RRVoucherParamDTO extends GridParamDTO {

    // 分页
    private int limitStart;

    // 业务日期
    private String businessDate;

    // 原订单日期
    private String orgOrderStartDate;
    private String orgOrderEndDate;

    // 收退货日期
    private String orderStartDate;
    private String orderEndDate;

    // 订单编号
    private String orderId;

    // 原订单编号
    private String orgOrderId;

    // 供应商编号
    private String vendorId;

    // 订单类型
    private String orderType;

    // 审核状态
    private String orderSts;

    // 实际退货审核状态
    private int status;

    // 订货方式
    private String orderMethod;

    // 是否分页
    private boolean flg = true;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    private String  omCode;
    private String amCode;
    private String ocCode;

}
