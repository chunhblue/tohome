package cn.com.bbut.iy.itemmaster.dto.adjustmentDaily;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 门店库存调整日报对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AdjustmentDailyDTO extends GridDataDTO {

    private String storeCd;
    private String storeName;

    private String articleId;
    private String articleName;
    private String uom;

    private String adjustmentDate;
    private String depCd;
    private String depName;
    private String pmaCd;
    private String pmaName;



    private String categoryCd;

    private String categoryName;
    private String subCategoryCd;
    private String subCategoryName;

    private String ofc;
    private String ofcName;
    private String barcode;
    private String am;


    private BigDecimal adjustmentQty;

    private BigDecimal adjustmentAmt;

    private String adjustReason;

    private String adjustReasonText;

    private String generalReason;

    private String generalReasonText;

}
