package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商主档最小订货数量/金额表对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma2002DTO extends GridDataDTO {

    private String vendorId;

    private String effectiveStartDate;

    private String structureCd;
    private String structureName;

    private BigDecimal minOrderQty;

    private BigDecimal minOrderAmtTax;

}
