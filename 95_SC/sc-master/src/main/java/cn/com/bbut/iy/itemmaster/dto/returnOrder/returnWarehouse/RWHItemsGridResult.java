package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author lz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RWHItemsGridResult extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段
    private String barcode;//商品条码
    private String articleId;//商品编号
    private String articleName;//商品名称
    private String unitId;//单位编号
    private String unitName;//单位
    private String vendorId;//供应商id
    private String spec;//规格
    private String orderQty;//退货数量
    private String orderActualQty;//退货实际数量
    private String orderPrice;//价格
    private String orderAmount;//退货总金额
    private String reasonId;//退货理由id
    private String reasonName;//退货理由名称
    private BigDecimal receiveQty;//实际退货量
    private BigDecimal orderNochargeQty;
    private BigDecimal receiveNoChargeQty;
    private BigDecimal getReceiveNochargeQty;
    private String isFreeItem; // 是否搭赠商品
    private String isFreeItemText;

}
