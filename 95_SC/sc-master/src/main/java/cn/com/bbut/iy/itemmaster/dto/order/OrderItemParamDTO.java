package cn.com.bbut.iy.itemmaster.dto.order;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * order 商品
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemParamDTO extends GridParamDTO {
    /**
     * 订货时间
     */
    private String orderDate;
    /**
     * 订货类型
     */
    private String orderType;
    /**
     * 店铺编号
     */
    private String storeCd;
    /**
     * 店铺名称
     */
    private String storeName;
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
    /**
     * 商品id
     */
    private String articleId;
    /**
     * 商品名称
     */
    private String articleName;
    /**
     * 商品条码
     */
    private String barcode;
    /**
     * 供应商id
     */
    private String vendorId;

    /**
     * 区分dc商品 0：供应商  1：dc
     */
    private String dcItem;

    /**
     * 角色资源
     */
    private List<ResourceViewDTO> resources;

    // 上传文件Cd
    private String pogCd;
}
