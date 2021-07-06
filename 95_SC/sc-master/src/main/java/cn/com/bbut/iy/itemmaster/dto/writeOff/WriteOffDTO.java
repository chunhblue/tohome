package cn.com.bbut.iy.itemmaster.dto.writeOff;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 门店报废日报对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffDTO extends GridDataDTO{

    private String storeCd;
    private String storeName;

    private String articleId;
    private String articleName;

    private String uom;

    private String writeOffDate;

    private String subCategoryCd;
    private String subCategoryName;
    private String pmaCd;
    private String pmaName;

    private String depCd;
    private String depName;
    private String ofc;
     private String ofcName;
    private String barcode;
    private String accDate;


    private String categoryCd;

    private String categoryName;

    private BigDecimal writeOffQty;

    private BigDecimal writeOffAmt;

    private String adjustReason;

    private BigDecimal saleQty; // 单品销售合计数量

}
