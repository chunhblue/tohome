package cn.com.bbut.iy.itemmaster.dto.classifiedsalereport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
/**
 * @ClassName classifiedSaleReportDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/26 17:53
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class classifiedSaleReportDTO  {
    private String articleId;
    private String saleDate;
    private String storeCd;
    private  String storeName;
    private String city;
    private String ofc;
    private String pmaCd;
    private String pmaName;
    private String saleAmount;
    private String accDate; // 营业日期
    private String saleQty;
    private String amCd;
    private String amName;
    private String merchandising; // 一般销售收入
    private String foodService; // 食物收入
    private String service; // 其他收入

/** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String aStoreCd;
	
    private String depCd;
    private String depName;
    private String maCd;
    private String subCategoryCd;
    private String categoryCd;
    private String subCategoryName;
    private String categoryName;


}
