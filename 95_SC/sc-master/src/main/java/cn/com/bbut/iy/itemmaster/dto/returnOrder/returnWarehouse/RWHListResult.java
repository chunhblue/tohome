package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * @author lz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RWHListResult extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段
    private String orderDate;//退货时间
    private String orderId;//单据编号
    private String orgOrderId;//源单据号
    private String deliveryCenterId;//仓库编号
    private String deliveryCenterName;//仓库名称
    private String storeCd;//店铺编号
    private String storeName;//店铺名称
    private String orderSts;//订单状态
    private String orderQty;//总退货数
    private String receiveQty;//总实际退货数
    private String orderAmt;//总退货金额
    private String reviewStatus;//审核状态
    private String status;//审核状态
    private String orderType;
    private String returnType;
    private String barcode;
    private String depName;//退货类型
    private String articleName;//退货类型
    private String articleId;//退货类型

}
