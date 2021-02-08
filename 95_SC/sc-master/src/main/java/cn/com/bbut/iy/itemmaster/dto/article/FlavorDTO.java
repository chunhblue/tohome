package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品口味关系对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class FlavorDTO extends GridDataDTO {

    private String articleId;

    private String tastingId;

    private String effectiveStartDate;
    
    private String effectiveEndDate;

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

    private String tastingName;

    private String tastingCategoryId;

    private String tastingCategoryName;

}
