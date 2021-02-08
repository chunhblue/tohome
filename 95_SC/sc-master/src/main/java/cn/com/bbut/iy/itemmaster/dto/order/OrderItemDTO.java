package cn.com.bbut.iy.itemmaster.dto.order;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * order 商品
 * 
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO  extends GridDataDTO {
    /**
     * 店铺编号
     */
    private String storeCd;
    /**
     * 部门编号
     */
    private String depCd;
    /**
     * 部门名称
     */
    private String depName;
    /**
     * 大分类编号
     */
    private String pmaCd;
    /**
     * 大分类名称
     */
    private String pmaName;
    /**
     * 中分类编号
     */
    private String categoryCd;
    /**
     * 中分类名称
     */
    private String categoryName;
    /**
     * 小分类编号
     */
    private String subCategoryCd;
    /**
     * 小分类名称
     */
    private String subCategoryName;
    /**
     * 货架号
     */
    private String shelf;
    /**
     * 货架层号
     */
    private String subShelf;
    /**
     * 商品件数
     */
    private String articleCount;
    /**
     * 已订件数
     */
    private String doOrderCount;
    /**
     * 可订件数
     */
    private String canOrderCount;

    private String orderStartDate;

    private String orderEndDate;


    // 店铺职位
    private String position;
}
