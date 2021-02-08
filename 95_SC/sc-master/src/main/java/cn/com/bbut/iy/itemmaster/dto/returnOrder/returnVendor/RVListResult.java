package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor;

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
public class RVListResult extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段
    private String orderDate;//退货时间
    private String orderId;//单据编号
    private String orgOrderId;//源单据号
    private String vendorId;//供应商编号
    private String vendorName;//供应商名称
    private String storeCd;//店铺编号
    private String storeName;//店铺名称
    private String totalQty;//总退货数
    private String totalAmt;//总退货金额
    private String orderQty;//总退货数
    private String receiveQty;//总实际退货数
    private String orderSts;//退货单状态
    private String reviewStatus;//审核状态
    private String status;//审核状态
    private String orderType;//审核状态
    private String returnType;//退货类型

}
