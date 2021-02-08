package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商主档银行信息对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma2004DTO extends GridDataDTO {

    private String vendorId;

    private String effectiveStartDate;

    private String regionCd;
    private String regionName;

    private String payBank;

    private String bankCode;

    private String payBankAccount;

    private String payAccountName;

    private String branch;

    private String bankCountry;

}
