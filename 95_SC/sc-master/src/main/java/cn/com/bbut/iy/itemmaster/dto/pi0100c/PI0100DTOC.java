package cn.com.bbut.iy.itemmaster.dto.pi0100c;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PI0100DTOC extends GridDataDTO {

    private String piCd;
    private String piDate;
    private String storeCd;
    private String piType;
    private String piMethod;
    private String piStatus;
    private String piStartTime;
    private String piEndTime;
    private String remarks;
    private String createYmd;
    private String createHms;
    private String createUserId;
    private String flag;
    private String storeName;
    private String updateUserId;
    private String updateYmd;
    private String updateHms;
    private String createUserName;
    private String articleId;
    private String barcode;
    private String uploadFlg;
    // 明细
    private List<PI0110DTOC> details;

    // 商品明细
    private List<StocktakeItemDTOC> itemList;



    // 经费
    private BigDecimal expenseAmt;
    // 审核状态
    private String reviewSts;
    private String reason;
    private String reasonCode;
}
