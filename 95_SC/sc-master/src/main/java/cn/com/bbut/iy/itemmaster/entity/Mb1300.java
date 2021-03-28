package cn.com.bbut.iy.itemmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Mb1300 {

    private String storeCd;

    private String businessDate;

    private String itemCode;

    private String barcode;

    private String itemName;

    private BigDecimal payooQty;

    private BigDecimal pucharseAmt;

    private BigDecimal commission;

    private BigDecimal commissionVat;

    private BigDecimal payooAmount;

    private BigDecimal ckQty;

    private BigDecimal ckAmount;

    private BigDecimal varyQty;
    private BigDecimal varyAmount;

    private String createUserId;

}
