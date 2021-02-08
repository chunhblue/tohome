package cn.com.bbut.iy.itemmaster.dto.receipt.rrModify;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 进退货传票修正查询DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RRModifyParamDTO extends GridParamDTO {

    // 分页
    private int limitStart;

    // 业务日期
    private String businessDate;

    // 修正收货日期
    private String modifyStartDate;
    private String modifyEndDate;
    private String orderNochargeQty;
    private String receiveNochargeQty;

    // 订单编号
    private String orderId;

    // 原订单编号
    private String orgOrderId;

    // 修正单类型
    private String orderType;

    // 供应商编号
    private String vendorId;

    // 商品编号
    private String articleId;

    // 序号
    private String serialNo;

    // 审核状态
    private String reviewStatus;

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

    private String isFreeItem;
    private String isFreeItemText;

    // 退货类型: 10 直接退货   20 原订单退货
    private String returnType;
    private String om;
    private String am;
    private String oc;

}
