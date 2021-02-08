package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor;

import lombok.Data;

/**
 * Created by lz on 2020/1/14.
 */
@Data
public class RVHeadResult {
    private String orderId;//单据编号
    private String orgOrderId;//原单据号
    private String orderDate;//退货时间
    private String vendorId;//仓库编号
    private String vendorName;//仓库名称
    private String storeCd;//店铺编号
    private String storeName;//店铺名称
    private String updateDate;//修改日期
    private String totalQty;//总退货数
    private String totalAmt;//总退货金额
    private String orderRemark;//订单备注
    // 后面追加
    private String createBy;
    private String modifiedBy;
    private String createDate;
}
