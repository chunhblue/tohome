package cn.com.bbut.iy.itemmaster.dto.stocktakeProcess;

import lombok.Data;

import java.io.Serializable;

/**
 * 盘点认列表,一览
 */
@Data
public class StocktakeProcessDTO implements Serializable {

    private String piCd;
    private String piDate;
    private String piStartTime;
    private String piEndTime;
    private String storeCd;
    private String storeName;
    private String piStatusCd;
    private String piStatusName;
    private String voucherTypeName;
    private String remarks;
    private String reviewStatus;

    private String createUserId;
    private String createYmd;

}
