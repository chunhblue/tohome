package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 供应商主档 vendor email
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma2008DTO extends GridDataDTO {

    /**
     * 供应商ID
     */
    private String vendorId;
    /**
     * 供应商生效开始日
     */
    private String effectiveStartDate;

    private String mailTitle;
    private String productGroupCd;
    private String email;
    private String ccEmail;
    private String regionCd;
    private String regionName;
    private String productGroupName;
    private String attn;
    private String poEmail;
    private String orderAddress;
    private String orderTelNo;
    private String orderFaxNo;
    private String orderAdminName;
    private String orderType;
    private String orderTypeName;
    private String structureCd;
    private String structureName;
}
