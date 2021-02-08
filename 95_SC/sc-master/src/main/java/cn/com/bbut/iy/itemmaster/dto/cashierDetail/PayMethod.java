package cn.com.bbut.iy.itemmaster.dto.cashierDetail;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 支付方式
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayMethod  extends GridDataDTO {
    /**
     * 支付方式cd
     */
    private String payCd;
    /**
     * 支付方式名称
     */
    private String payNamePrint;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
}
