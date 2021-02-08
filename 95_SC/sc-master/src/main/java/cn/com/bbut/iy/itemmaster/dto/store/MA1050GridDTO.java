package cn.com.bbut.iy.itemmaster.dto.store;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MA1050GridDTO extends GridDataDTO {
    private String storeCd;

    private String accountingItemCd;

    private String accountingItemName;

    private String accountCd;

    private String accountName;

    private String effectiveStartDate;

    private String effectiveEndDate;

    private String oldEffectiveStartDate;

    private String effectiveSts;

    private String createUserId;

    private String createYmd;

    private String createHms;

    private String updateUserId;

    private String updateYmd;

    private String updateHms;

    private String updateScreenId;

    private String updateIpAddress;

    private String nrUpdateFlg;

    private String nrYmd;

    private String nrHms;

}
