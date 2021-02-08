package cn.com.bbut.iy.itemmaster.dto.invoiceEntry;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Collection;

@Data
public class InvoiceEntryDTO extends GridParamDTO {

    private String startDate;
    private String endDate;
    private String storeNo;
    private String status;
    private String customerName;
    private String phone;

    // 收据单号
    private String receiptNo;
    private String date;
    private BigDecimal amt;
    private int limitStart;
    private int num;

    // 是否分页
    private boolean flg = true;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
