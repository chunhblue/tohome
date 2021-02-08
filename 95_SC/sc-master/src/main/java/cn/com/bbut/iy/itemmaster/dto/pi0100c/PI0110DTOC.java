package cn.com.bbut.iy.itemmaster.dto.pi0100c;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PI0110DTOC extends GridDataDTO {

    private String piCd;
    private String piDate;
//    private String piTypeCode;
//    private String piTypeName;
    private String pmaCd;
    private String pmaName;
    private String createYmd;
    private String createHms;
    private String createUserId;
    // 审核状态
    private String reviewSts;

}
