package cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class VendorReturnedDailyDTO implements Serializable {

    // 退货单号
    private String orderId;
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
    private String vendorId; // 供应商Id
    private String vendorName;
    private String articleId;
    private String articleName;
    private String barcode;
    private String dc;
    private String userName;
    private String orderRemark;
    // 仓库DC供应商Id
    private String deliveryCenterId;
    private String deliveryCenterName;

    private String returnQty; // 退货数量
    private String returnTotalQty; // 退货数量
    private String returnAmt; // 退货金额
}
