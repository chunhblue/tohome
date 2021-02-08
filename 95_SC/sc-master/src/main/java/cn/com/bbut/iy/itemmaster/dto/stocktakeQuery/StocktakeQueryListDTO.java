package cn.com.bbut.iy.itemmaster.dto.stocktakeQuery;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Created by lz on 2020/1/17.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StocktakeQueryListDTO extends GridDataDTO {
    private static final long serialVersionUID = 1L;
    // 数据库查询出的字段
    private String piCd;
    private String accDate;
    private String storeCd;
    private String storeName;
    private String depCd;//部门编号
    private String depName;//部门名称
    private String pmaCd;//大分类编号
    private String pmaName;//大分类名称
    private String categoryCd;//中分类编号
    private String categoryName;//中分类名称
    private String subCategoryCd;//小分类编号
    private String subCategoryName;//小分类名称
    private String articleCount;//品项数
    private String firstQty;//初盘品项数
    private String secondQty;//复盘品项数
    private String noQty;//未盘品项数
    private String piAccountFlgName;//是否扎帐flg
    private String piFirstFinishFlgName;//是否初盘完成flg
    private String piCommitFlgName;//是否认列flg

    private String piAccountFlg; // 状态值, 隐藏域
    private String piFirstFinishFlg;
    private String piCommitFlg;
}
