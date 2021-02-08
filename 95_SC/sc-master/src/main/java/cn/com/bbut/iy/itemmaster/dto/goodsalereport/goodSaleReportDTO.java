package cn.com.bbut.iy.itemmaster.dto.goodsalereport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName goodSaleReportDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/30 14:18
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class goodSaleReportDTO  {
    private String articleId;
    private String articleName;
    private String barcode;
    private String saleDate;
    private String storeCd;
    private  String storeName;
    private String city;
    private String ofc;
    private String depCd;
    private String depName;
    private String pmaCd;
    private String pmaName;
    private String categoryCd;
    private String categoryName;
    private String subCategoryCd;
    private String subCategoryName;
    private String saleAmount;
    private String priceActual; // 实际售价
    private String accDate; // 营业日期
    private String saleQty;
    private String amCd;
    private String amName;


}
