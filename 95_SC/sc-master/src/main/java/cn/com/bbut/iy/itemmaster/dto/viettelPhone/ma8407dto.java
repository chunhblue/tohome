package cn.com.bbut.iy.itemmaster.dto.viettelPhone;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName ma8407dto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/18 18:49
 * @Version 1.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ma8407dto extends GridDataDTO {
    private int limitStart;



    private String requestDate;
    private String requestId;
    private String orderId;
    private String receiptNo;
    private BigDecimal transAmt;
    private String status;
    private String message;


}
