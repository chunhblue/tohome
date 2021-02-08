
package cn.com.bbut.iy.itemmaster.dto.priceLabel;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class PriceLabelParamDTO extends GridParamDTO {
    // 店铺cd
    private String storeCd;

    // 店铺名称
    private String storeName;

    // 商品id
    private String articleId;

    // 商品条形码
    private String barcode;

    // 英文价签名称
    private String priceLabelEnName;

    // 越南文价签名称
    private String priceLabelVnName;

    // 商品名称
    private String articleName;

    // 商品生效日期
    private String effectiveStartDate;

    // 商品旧价格
    private String oldPrice;

    // 商品新价格
    private String newPrice;

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

    // 变更类型 商品变价/新商品
    private String type;



    //	查询开始日期
    private String startYmd;

    //	查询结束日期
    private String endYmd;

    // 业务日期
    private String businessDate;

    private int limitStart;

    private boolean flg = true;



    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;

}
