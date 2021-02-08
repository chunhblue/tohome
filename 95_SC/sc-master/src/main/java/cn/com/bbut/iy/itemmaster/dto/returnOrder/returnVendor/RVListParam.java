package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor;

import lombok.Data;

import java.util.Collection;

/**
 * @author lz
 *
 */
@Data
public class RVListParam  {
    //追加
    private String orderStartDate;
    private String orderEndDate;
    private String orderDate;
    private String orderId;
    private String orgOrderId;
    private String vendorId;
    private String vendorName;
    private String storeName;
    private String returnSts;//退货状态  区分退货确认
    private String orderSts;//订单状态
    private String returnType;
    private int limitStart;
    private int limitEnd;
    private String orderByClause;

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
    /** 审核状态值 */
    private Integer reviewStatus;
    /** 审核状态值 */
    private Integer status;
}
