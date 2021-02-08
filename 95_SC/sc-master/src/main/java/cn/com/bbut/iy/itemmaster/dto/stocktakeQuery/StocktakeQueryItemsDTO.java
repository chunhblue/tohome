package cn.com.bbut.iy.itemmaster.dto.stocktakeQuery;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Created by lz on 2020/1/19.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StocktakeQueryItemsDTO extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段

    private String piCd; // 盘点单号
    private String accDate;//盘点日期
    private String barcode;
    private String baseSalePrice; // 售价
    private String articleId;
    private String articleName;
    private String partCd;
    private String partName;
    private String depCd;
    private String depName;
    private String pmaCd;
    private String categoryCd;
    private String subCategoryCd;
    private String subCategoryName;
    private String spec;
    private String uom;
    private String areaCd;
    private String firstQty;
    private String secondQty;
}
