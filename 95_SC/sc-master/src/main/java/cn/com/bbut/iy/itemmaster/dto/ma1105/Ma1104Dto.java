package cn.com.bbut.iy.itemmaster.dto.ma1105;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Ma1104Dto {
    private String pogCd;
    private String pogName;
    private String storeCd;
    private BigDecimal reviewStatus;  // 审核状态
    private String isExpired;  // 是否失效   0:生效  1:过期
    private String createUserId;
    private String createYmd;
    private String createHms;
}
