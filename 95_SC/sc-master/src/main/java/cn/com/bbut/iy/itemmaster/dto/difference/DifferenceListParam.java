package cn.com.bbut.iy.itemmaster.dto.difference;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author lz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class DifferenceListParam  extends GridParamDTO {
    private String orderId;
    private String orgOrderId;
    private String orderStartDate;
    private String orderEndDate;
    private String receiveStartDate;
    private String receiveEndDate;
    private String createStartDate;
    private String createEndDate;
    private String deliveryCenterId;

    private int limitStart;
    private int limitEnd;
    private String orderByClause;

    private String businessDate;
    private String orderDifferentiate;
    private String vendorId;
    private String dc;


    private String deliveryOrderId; // 发货单编号
    private String deliveryStartDate; // 发货单开始日期
    private String deliveryEndDate; // 发货单结束日期

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
}
