package cn.com.bbut.iy.itemmaster.dto.od0000_t;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * od0000_t
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OD0000TDTO extends GridDataDTO {

    // 业务日期
    private String businessDate;

    // 数据来源类型区分
    private String type;

    // 收货编号
    private String receiveId;

    // 店铺编号
    private String storeCd;

    // 店铺编号
    private String storeName;

    // 单据编号
    private String orderId;

    // 源单据编号
    private String orgOrderId;

    // 供应商编号
    private String vendorId;

    // 供应商编号
    private String vendorName;

    // 配送类型CD
    private String deliveryTypeCd;

    // 物流中心ID
    private String deliveryCenterId;

    // 配送区域CD
    private String deliveryAreaCd;

    // 车次
    private String shipment;

    // 单据类型
    private String orderType;

    // 单据类型
    private String orderTypeName;

    // 订货方式
    private String orderMethod;

    // 结算方式
    private String paymentType;

    // 促销期号
    private String promotionId;

    // 订单日期
    private String orderDate;

    // 预定交货日
    private String deliveryDate;

    // 订单终止日
    private String expireDate;

    // 收货日期
    private String receiveDate;

    // 物流中心地址
    private String vendorAddress;

    // 门店地址
    private String storeAddress;

    // 订单状态
    private String orderSts;

    // 订单状态名称
    private String stsName;

    // 日结标识
    private String nrFlg;

    // 订单备注
    private String orderRemark;

    // 税区分CD
    private String purchaseVatCd;

    // 税率
    private BigDecimal taxRate;

    // 订单金额(含税)
    private BigDecimal orderAmt;

    // 订单税额
    private BigDecimal orderTax;

    // 收货金额(含税)
    private BigDecimal receiveAmt;

    // 收货税额
    private BigDecimal receiveTax;

    // 上传flg
    private String uploadFlg;

    // 上传日期
    private String uploadDate;

    // 订货区分
    private String orderDifferentiate;

    // 订货方式
    private String orderMethodName;

    // 审核状态
    private Integer reviewSts;

    // 是否允许多次收货
    private String multipleFlg;

    // 收货备注
    private String receivedRemark;

    /* 子表求和统计字段 */
    // 订单订购总数量
    private BigDecimal orderTotalQty;
    // 订单订购总金额（含税）
    private BigDecimal orderTotalAmt;
    // 订单订购总金额
    private BigDecimal orderTotalAmtNoTax;
    // 订单收货总数量
    private BigDecimal receiveTotalQty;
    // 订单收货总金额（含税）
    private BigDecimal receiveTotalAmt;
    // 订单收货总金额
    private BigDecimal receiveTotalAmtNoTax;

    //附件信息
    private String fileDetailJson;

    // 共通
    private CommonDTO commonDTO;
    //physical_receiving_date
    private String physicalReceivingDate;

    private String freeOrderId;
}
