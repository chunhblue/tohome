package cn.com.bbut.iy.itemmaster.dto.inventory;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 传票头档
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sk0010DTO{

    // 店号
    private String storeCd;

    // 传票编号
    private String voucherNo;

    // 传票类型
    private String voucherType;

    // 传票日期
    private String voucherDate;

    // 对方店号
    private String storeCd1;

    // 传票编号1(对方店订单编号)
    private String voucherNo1;

    // 传票状态
    private String voucherSts;

    // 备注
    private String remark;

    // 税率
    private BigDecimal taxRate;

    // 传票金额(金额)
    private BigDecimal voucherAmtNoTax;

    // 传票税额
    private BigDecimal voucherTax;

    // 传票金额(含税)
    private BigDecimal voucherAmt;

    //附件信息
    private String fileDetailJson;

    // 上传flg
    private String uploadFlg;

    // 上传日期
    private String uploadDate;

    // 用户信息
    private CommonDTO commonDTO;
}
