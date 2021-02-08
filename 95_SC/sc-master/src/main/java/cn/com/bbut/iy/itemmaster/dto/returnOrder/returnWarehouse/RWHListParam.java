package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import lombok.Data;

import java.util.Collection;

/**
 * @author lz
 *
 */
@Data
public class RWHListParam {
    private String orderDate;
    private String orderStartDate;
    private String orderEndDate;
    private String orderId;
    private String orgOrderId;
    private String deliveryCenterId;
    private String deliveryCenterName;
    private String storeName;
    private String returnSts;//退货状态  区分退货确认
    private Integer reviewStatus;//审核状态
    private String orderSts;//订单状态
    private Integer status;//退货确认审核状态

    private int limitStart;
    private int limitEnd;
    private String orderByClause;
     private String returnType;
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

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
