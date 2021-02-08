
package cn.com.bbut.iy.itemmaster.dto.priceLabel;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceLabelDTO extends GridDataDTO {

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
	private BigDecimal oldPrice;

	// 商品新价格
	private BigDecimal newPrice;

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



}
