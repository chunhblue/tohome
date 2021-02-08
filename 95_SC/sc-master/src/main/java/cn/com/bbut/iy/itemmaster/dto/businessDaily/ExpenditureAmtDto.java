package cn.com.bbut.iy.itemmaster.dto.businessDaily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureAmtDto {
    /**
     * 经费科目id
     */
    private String itemId;
    /**
     * 经费科目名称
     */
    private String itemName;
    /**
     * 经费金额
     */
    private BigDecimal expenseAmt;
}
