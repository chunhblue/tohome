package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author zcz
 * 退货信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnWarehouseDetailInfo extends GridParamDTO {
     // 门店CD
    private String storeCd;
     // 退货日
    private String orderDate;
     // 商品条码
    private String barcode;
     // 商品ID
    private String articleId;
     // 商品名称
    private String articleName;
    /// 规格
    private String spec;
    /// 单位
    private String unitName;
    /// 便
    private String shipmentCd;
     // 退货单位
    private String receiveUnitId;
    // 单位id
    private String unitId;
    // 在途数量
    private BigDecimal onOrderQty;
    // 实时库存数量
    private BigDecimal realtimeQty;
    // 收货数量
    private BigDecimal receiveQty;
    // 退货数量
    private BigDecimal orderQty;
    // 退货搭赠数量
    private BigDecimal orderNochargeQty;
    private BigDecimal orderNoChargeQty;
    private BigDecimal receiveNochargeQty;
    private BigDecimal receiveNoChargeQty;
    // 退货价格
    private BigDecimal orderPrice;
    // 税率区分
    private String orderTaxTypeCd;
    // 税率
    private BigDecimal orderTaxRate;
     // 商品类型
    private String articleType;
     // 序号
    private String displaySeq;
    // 供应商id
    private String vendorId;
    //已退货数量
    private BigDecimal returnedQty;
    // 在库量
    private BigDecimal onHandQty;
    //在途中量
    private BigDecimal onOrdeQty;
    // 是否搭赠商品（0：normal/1：free）
    private String isFreeItem;
}
