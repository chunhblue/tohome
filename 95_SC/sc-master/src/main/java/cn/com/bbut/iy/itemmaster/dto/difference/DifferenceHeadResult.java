package cn.com.bbut.iy.itemmaster.dto.difference;

import lombok.Data;

/**
 * Created by lz on 2020/1/15.
 */
@Data
public class DifferenceHeadResult {
    private String orderId;//单据编号
    private String orgOrderId;//原单据号
    private String orderDate;//订货日期
    private String receiveDate;//收货日期
    private String createDate;//创建日期
    private String deliveryCenterId;//仓库编号
    private String deliveryCenterName;//仓库名称
    private String storeCd;//店铺编号
    private String storeName;//店铺名称
    private String totalQty;//总调整数
    private String totalAmt;//总调整金额
    private String orderRemark;//订单备注

    private String totaldeliveryQty; // 总发货数量
    private String totalVarianceQty; // 总差异数量
    private String totalVarianceAmt; // 总差异金额

    private String deliveryOrderId; // 发货单编号
    private String deliveryDate;//发货日期
}
