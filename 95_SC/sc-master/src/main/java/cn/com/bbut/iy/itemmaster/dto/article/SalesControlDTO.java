package cn.com.bbut.iy.itemmaster.dto.article;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 商品销售控制对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SalesControlDTO extends GridDataDTO {

    private String articleId;

    private String structureCd;
    private String structureName;

    private String storeTypeCd;

    private String maCd;
    private String maName;

    private String effectiveStartDate;
    
    private String effectiveEndDate;

    private String saleFlg;

    private BigDecimal baseSalePrice;

    private BigDecimal recommendQty;

    private String salePauseFlg;

    private BigDecimal memberPrice;

    private String saleTaxTypeCd;
    private String saleTaxTypeName;

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

    private String saleInactive;

}
