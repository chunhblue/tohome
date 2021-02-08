package cn.com.bbut.iy.itemmaster.dto.inventory;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商自送验收明细DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class InventoryVouchersGridDTO extends GridDataDTO {

    // 店铺编号
    private String storeCd;
    private String storeName;

    // 传票编号
    private String voucherNo;

    // 传票类型
    private String voucherType;
    private String typeName;

    // 传票日期
    private String voucherDate;

    // 对方店铺编号
    private String storeCd1;
    private String storeName1;

    // 传票编号1
    private String voucherNo1;

    // 传票状态
    private String voucherSts;
    private String stsName;

    // 审核状态
    private Integer reviewSts;

    //审核流程
    private Integer reviewId;

    // 传票备注
    private String remark;

    // 税率
    private BigDecimal taxRate;

    // 传票金额(未税)
    private BigDecimal voucherAmtNoTax;

    // 传票税额
    private BigDecimal voucherTax;

    // 传票金额(含税)
    private BigDecimal voucherAmt;

    // 上传flg
    private String uploadFlg;
    private String uploadName;

    // 上传日期
    private String uploadDate;

    // 创建日期
    private String createYmd;

    // 创建用户
    private String createUser;
    private String oc;
    private String ofc;
    private String om;
    private String ocName;
    private String ofcName;
    private String omName;
}
