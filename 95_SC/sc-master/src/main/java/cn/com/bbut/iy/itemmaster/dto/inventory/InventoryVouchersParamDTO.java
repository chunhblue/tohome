package cn.com.bbut.iy.itemmaster.dto.inventory;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 供应商自送验收明细DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class InventoryVouchersParamDTO extends GridParamDTO {

    // 业务日期
    private String businessDate;

    // 分页
    private int limitStart;

    // 传票编号
    private String voucherNo;
    private String voucherNo1;

    // 传票日期
    private String voucherStartDate;
    private String voucherEndDate;

    // 转入店铺编号
    private String storeCdTo;

    // 传票状态
    private String voucherSts;

    // 传票类型
    private String voucherType;

    // 传票类型集合
    private List<String> types;

    // 是否上传
    private String upload;

    // 商品信息(商品编号、条码)
    private String itemInfo;

    // 一般原因
    private String generalReason;
    //原因
    private String reason;

    private Integer startQty;

    private Integer endQty;

    // 审核状态
    private Integer reviewSts;

    // 分页
    private boolean flg = true;

    private String am;

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
    private String  omCode;
    private String amCode;
    private String ocCode;



}
