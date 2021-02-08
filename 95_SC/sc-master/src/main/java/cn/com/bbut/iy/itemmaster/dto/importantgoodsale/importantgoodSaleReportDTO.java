package cn.com.bbut.iy.itemmaster.dto.importantgoodsale;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class importantgoodSaleReportDTO {
    private String articleId;
    private String articleName;
    private String barcode;
    private String saleDate;
    private String storeCd;
    private  String storeName;
    private String city;
    private String ofc;
    private String saleAmount;
    private String accDate; // 营业日期
    private String saleQty;
    private String priceActual; // 实际售价
    private String depCd;
    private String pmaCd;
    private String categoryCd;
    private String subCategoryCd;
    private String depName;
    private String pmaName;
    private String categoryName;
    private String subCategoryName;
    private String maCd;
    private String amCd;
    private String amName;
    private int seqNo;

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
}
