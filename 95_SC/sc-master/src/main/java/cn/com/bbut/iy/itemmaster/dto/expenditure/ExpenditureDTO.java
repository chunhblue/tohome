package cn.com.bbut.iy.itemmaster.dto.expenditure;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 经费登录DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureDTO extends GridDataDTO {

    private String accDate;

    private String storeCd;
    private String storeName;

    private String voucherNo;

    private String depCd;
    private String depName;

    // 经费状态
    private String recordSts;
    private String statusName;

    // 支取方式
    private String payTypeId;
    private String payName;

    // 经费科目
    private String itemId;
    private String itemName;

    private String description;

    private String remark;

    private BigDecimal expenseAmt;

    // 经办人
    private String userId;
    private String userName;

    // 审核状态
    private String status;

    //附件信息
    private String fileDetailJson;

    private String createUser;

    private String createYmd;

    private String createHms;

    private String updateUser;

    private String updateYmd;

    private String updateHms;
    /**
     * 修正录入金额
     */
    private BigDecimal additional;
    /**
     * 补偿金额(报销)
     */
    private BigDecimal offsetClaim;

    /**
     * 经费类型
     * 0 :Additional  1:Offset Claim
     */
    private String expenseType;
}
