package cn.com.bbut.iy.itemmaster.dto.businessDaily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentAmtDto {
    /**
     * 营业日期
     */
    private String payDate;
    /**
     * 支付类型
     */
    private String payCd;
    /**
     * 支付名称
     */
    private String payName;

    private String payNamePrint;
    /**
     * 收款金额
     */
    private BigDecimal payInAmt;
    // cash
    private BigDecimal payInAmt0;
    private BigDecimal payInAmt1;
    private BigDecimal payInAmt2;
    private BigDecimal payInAmt3;
    private BigDecimal payInAmt4;
    private BigDecimal payInAmt5;
    private BigDecimal payInAmt6;
    private BigDecimal payInAmt7;

    private Integer customerCount;
    /**
     * service 金额
     * Mo mo cash - IN : 218556
     * Payoo: 168604, 240448
     * Viettel on POS: all item code with the sub-catecode = 432
     */
    private BigDecimal momoCashInAmt;
    private BigDecimal payooAmt;  // payoo 合计金额
    private BigDecimal payooCodeAmt;
    private BigDecimal payooBillAmt;
    private BigDecimal viettelAmt;
    private BigDecimal otherServiceAmt; // 其他服务类金额

    private Integer momoCashIncount;
    private Integer payooCount;     // payoo 合计人数
    private Integer payooCodeCount;
    private Integer payooBillCount;
    private Integer viettelCount;
    private Integer OtherServiceCount;
}
