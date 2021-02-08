package cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily;

import lombok.Data;

@Data
public class VendorReceiptDailyDTO {

    private String storeCd;
    private String storeName;
    private String accDate; // 订货日期
    private String pmaCd;
    private String pmaName;
    private String categoryCd;
    private String categoryName;
    private String subCategoryCd;
    private String subCategoryName;
    private String amCd;
    private String omCd;
    private String amName;
    private String omName;
    private String vendorId;
    private String vendorName;
    private String articleId;
    private String articleName;
    private String barcode;
    private String dc;

    // 收货单号
    private String receiveId;
    private String receivedRemark;
    private String userName;
    private String receiveQty; // 收货数量
    private String receiveTotalQty; // 收货数量
    private String receiveAmt; // 收货金额
    private String receiveDate; // 收货金额

}
