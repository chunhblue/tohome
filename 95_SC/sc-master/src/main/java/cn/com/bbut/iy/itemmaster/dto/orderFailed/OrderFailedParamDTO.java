package cn.com.bbut.iy.itemmaster.dto.orderFailed;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订货失败查询DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailedParamDTO extends GridParamDTO {
    // 业务日期
    private String businessDate;

    // 订货日期
    private String orderStartDate;
    private String orderEndDate;

    private String orderDate;

    // 失败原因
    private String failedReason;

    // 商品条码
    private String barcode;
    // 供应商ID
    private String vendorId;
    private String vendorName;
    // 商品ID
    private String articleId;

    private String articleName;

    private String orderId;

    private String vendorInfo;
    private String ItemInfo;

    // 共通
    private CommonDTO commonDTO;

    private String createUserId;

    // 分页
    private int limitStart;

    // 是否分页
    private boolean flg = true;
}
