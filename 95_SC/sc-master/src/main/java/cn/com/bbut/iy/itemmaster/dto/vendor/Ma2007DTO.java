package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 供应商主档 vendor email
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma2007DTO extends GridDataDTO {

    /**
     * 供应商ID
     */
    private String vendorId;
    /**
     * 供应商生效开始日
     */
    private String effectiveStartDate;
    /**
     * Trading Term No
     */
    private String tradingTermNo;
    /**
     * Trading Term生效开始日
     */
    private String tradingTermStartDate;
    /**
     * 是否是默认Trading Term
     */
    private String isDefault;
}
