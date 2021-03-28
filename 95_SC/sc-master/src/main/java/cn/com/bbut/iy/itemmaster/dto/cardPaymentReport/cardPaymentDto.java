package cn.com.bbut.iy.itemmaster.dto.cardPaymentReport;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import com.itextpdf.text.pdf.PRAcroForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName cardPaymentDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/19 18:25
 * @Version 1.0
 * @Description
 */


@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class cardPaymentDto  extends GridDataDTO {
    private String storeCd;
    private String storeName;
    private String cardType;
    private String  transDate;
    private String  transDate1;
    private String traceNo;
    private String approveCode;
    private String cardNum;
    private BigDecimal amount;
    private String  receiptNo;
    private String  businessDate;
    private BigDecimal transcationNumber;
    private String desciption;
    private String amountCk;
    private String amountVcb;
    private BigDecimal depositAmount;
    private BigDecimal freeAmount;
    private String freePrecent;
    private String vat;
    private String depositDate;
    private String description;
}
