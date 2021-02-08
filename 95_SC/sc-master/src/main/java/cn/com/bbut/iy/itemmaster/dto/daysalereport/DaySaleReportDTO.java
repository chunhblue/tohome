package cn.com.bbut.iy.itemmaster.dto.daysalereport;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName DaySaleReport
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/25 13:07
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaySaleReportDTO  extends GridDataDTO {

    private  String storeCd;
    private String storeName;
   private String   am;
    private String avgCustomerNo;
    private String accDate;
    private String twoHours;   // 每两小时
     private  String effectiveStartDate;
    private String effectiveEndDate;
    private String saleAmt;
    private String saleDate;
    private String psd;
    private String amCd;
    private String amName;

    // 时间段的金额
    private BigDecimal time68;
    private BigDecimal time810;
    private BigDecimal time1012;
    private BigDecimal time1214;
    private BigDecimal shift1;
    private BigDecimal time1416;
    private BigDecimal time1618;
    private BigDecimal time1820;
    private BigDecimal time2022;
    private BigDecimal shift2;
    private BigDecimal time2224;
    private BigDecimal time02;
    private BigDecimal time24;
    private BigDecimal time46;
    private BigDecimal shift3;
    private BigDecimal totalAmt;
}
