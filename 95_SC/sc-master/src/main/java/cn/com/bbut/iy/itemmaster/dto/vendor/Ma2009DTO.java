package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 供应商主档 vendor email
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma2009DTO extends GridDataDTO {
    /**
     * 状态
     */
    private String status;

    /**
     * 供应商ID
     */
    private String vendorId;
    /**
     * 供应商生效开始日
     */
    private String effectiveStartDate;

    private String regionCd;

    private String regionName;

    private String payable;

    private String receivable;

    private String sts;
}
