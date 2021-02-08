package cn.com.bbut.iy.itemmaster.dto.receipt.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 供应商自送验收头档DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VendorReceiptParamDTO extends GridParamDTO {
    // 数据来源类型
    private String type;

    // 业务日期
    private String businessDate;

    // 收货日期
    private String receivingStartDate;
    private String receivingEndDate;

    // 订货日期
    private String orderStartDate;
    private String orderEndDate;

    // 出库日期
    private String deliveryStartDate;
    private String deliveryEndDate;

    // 收货单编号
    private String receiveId;

    // 订单编号
    private String orderId;

    // 商品编号
    private String itemId;

    // 供应商编号
    private String vendorId;

    // 免费商品编号
    private String freeOrderId;

    // 订单状态
    private String voucherStatus;

    // 共通
    private CommonDTO commonDTO;

    // 分页
    private int limitStart;

    // 是否分页
    private boolean flg = true;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;

}
