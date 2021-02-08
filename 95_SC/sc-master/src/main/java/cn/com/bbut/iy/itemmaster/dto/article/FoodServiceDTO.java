package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 商品厨打关系对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class FoodServiceDTO {

    private String articleId;

    private String effectiveStartDate;

    private String kitchenChoose;

    private String schemeOne;

    private String schemeTwo;

    private String schemeThree;

    private String effectiveEndDate;

    private String effectiveSts;

    private CommonDTO commonDTO;

    private String schemeOneName;

    private String schemeTwoName;

    private String schemeThreeName;

}
