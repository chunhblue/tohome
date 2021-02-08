package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MM促销记录对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO extends GridDataDTO {

    private String promotionCd;

    private String promotionName;

    private String promotionStartDate;

    private String promotionEndDate;

    private String promotionStartTime;

    private String promotionEndTime;

    private String promotionWeekCycle;

    private String promotionPromptFlg;

    private String promotionPromptName;

    private String depCd;

    private String depName;

    private String regionCd;

    private String regionName;

    private String effectiveDate;

    private String promotionDiff;

    private String promotionType;

    private String promotionAllotType;

    private String effectiveSts;

    private CommonDTO commonDTO;

}
