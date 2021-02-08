package cn.com.bbut.iy.itemmaster.dto.difference;

import lombok.Data;

import java.util.Collection;

/**
 * @author lz
 *
 */
@Data
public class DifferenceListParam {
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
