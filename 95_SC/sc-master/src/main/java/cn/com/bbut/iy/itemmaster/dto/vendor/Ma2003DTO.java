package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 供应商主档大分类信息对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma2003DTO extends GridDataDTO {

    private String vendorId;

    private String effectiveStartDate;

    private String depCd;
    private String depName;

    private String pmaCd;
    private String pmaName;

}
