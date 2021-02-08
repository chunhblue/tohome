package cn.com.bbut.iy.itemmaster.dto.rtInventory;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RTInventoryQueryDTO extends GridDataDTO {
    private static final long serialVersionUID = 1L;

    // 分页
    private int limitStart;

    private String storeCd; // 店铺号
    private String itemCode; // 商品编码
    private String itemBarcode;     // 商品条码
    private String itemName;       // 商品名称
    private String specification; // 规格
    private String uom;   // 单位
    private String stockDate;   // 库存日期
    private String vendorId;   // 供应商编号
    private String orderId;   // 订单编号
    private String orderSts;
    // 传票类型
    private String voucherType;
    // 传票备注
    private String remark;
    // 传票状态
    private String voucherSts;
    private String stsName;

    private BigDecimal onHandQty;    // 昨日库存数量
    private BigDecimal saleQty;      // 当日销售数量
    private BigDecimal receiveQty; // 当日收货数量
    private BigDecimal adjustQty; // 当日库存调整数量
    private BigDecimal realtimeQty; // 实时库存数量
    private BigDecimal onOrderQty;// 在途数量
    private BigDecimal scrapQty; //报废数量
    private BigDecimal transferQty; //调拨数量
    private BigDecimal storeReturnQty;//退货数量
    private BigDecimal returnCorrQty;//退货更正数量
    private BigDecimal receiveCorrQty;//收货更正数量
    private BigDecimal transferInQty;//调拨-调入
    private BigDecimal transferOutQty;//调拨-调出
    private BigDecimal transferOutCorrQty;//调拨-调出修正
    private BigDecimal transferInCorrQty;//调拨-调入修正
    private String oc;
    private String ofc;
    private String om;
    private String ocName;
    private String ofcName;
    private String omName;
    private String depName; // 部门名称
    private String pmaName; // 大分类名称
    private String categoryName; // 中分类名称
    private String subCategoryName; // 小分类名称
    private String vendorName;
}
