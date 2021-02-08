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
     * 收款金额
     */
    private BigDecimal payInAmt;
}
