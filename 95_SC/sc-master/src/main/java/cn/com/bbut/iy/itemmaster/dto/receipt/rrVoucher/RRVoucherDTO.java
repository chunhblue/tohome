package cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 进退货传票头档DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RRVoucherDTO extends GridDataDTO {

    // 店铺编号
    private String storeCd;
    private String storeName;

    // 单据编号
    private String orderId;

    // 源单据编号
    private String orgOrderId;

    // 仓库编号
    private String warehouseCd;

    // 仓库名称
    private String warehouseName;

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
    private String orderMethodName;

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

    // 共通DTO
    private CommonDTO commonDTO;

    // 审核流程编号
    private Integer reviewId;

    // 审核状态
    private Integer reviewSts;
    private String reviewStsText;
    // 实际退货审核状态
    private int status;

    // 原订单日期
    private String orgOrderDate;
    private String oc;
    private String ofc;
    private String om;
    private String ocName;
    private String ofcName;
    private String omName;
}
