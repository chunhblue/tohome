package cn.com.bbut.iy.itemmaster.dto.stocktakeQuery;

import lombok.Data;

/**
 * @author lz
 *
 */
@Data
public class StocktakeQueryItemsParam {
    // 条件查询
    private String accDate;//盘点日期
    private String storeCd;
    private String areaCd;//货位编号
    private String depCd;//部门编号
    private String pmaCd;//大分类编号
    private String categoryCd;//中
    private String subCategoryCd;//小


    private int limitStart;
    private int limitEnd;
    private String orderByClause;
}
