package cn.com.bbut.iy.itemmaster.dto.ma4110;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 *  促销信息 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4110GridDto extends GridDataDTO {
    private String promotionCd;

    private String promotionName;

    private String itemCode;

    private String pma;

    private String dep;

    private String createUserId;

    private String createUser;

    private String createYmd;

    private String createEndYmd;

    private String createHms;

    private String updateUserId;

    private String updateYmd;

    private String updateHms;

    private String updateScreenId;

    private String updateIpAddress;

    private Integer reviewStatus;

    private long processSelection;

    private String depCd;

    private String depName;

    private String subCategoryName;

    private String customerStartDatefrom;

    private String promotionStartDate;

    private String promotionEndDate;

    private String customerStartDateto;

    private String displaySeq;

}
