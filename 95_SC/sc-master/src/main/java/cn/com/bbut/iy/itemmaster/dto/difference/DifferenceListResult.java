package cn.com.bbut.iy.itemmaster.dto.difference;

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
public class DifferenceListResult extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    /**
     * 数据库查询出的字段
     */

    // 订单编号
    private String orderId;
    // 发货单编号
    private String deliveryOrderId;
    private String orgOrderId;
    private String orderDate;
    private String deliveryDate; // 发货单日期
    private String receiveDate;
    private String receiveId;
    private String createDate;
    private String updateYmd;
    private String storeCd;
    private String storeName;
    private String deliveryCenterId;
    private String deliveryCenterName;
    private String totalAmt;

    private String vendorId;
    private String vendorName;

}
