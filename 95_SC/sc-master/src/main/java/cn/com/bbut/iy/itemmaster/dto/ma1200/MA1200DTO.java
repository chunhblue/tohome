package cn.com.bbut.iy.itemmaster.dto.ma1200;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MA1200DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MA1200DTO {
 
    // 业务日期
    private String businessDate;


    // 母货号
    private String parentArticleId;

    private String autoRepackage;
    private String repackageStatus;

    // 子货号
    private String childArticleId;
    // 子货号与母货号倍率
    private BigDecimal childQty;




}