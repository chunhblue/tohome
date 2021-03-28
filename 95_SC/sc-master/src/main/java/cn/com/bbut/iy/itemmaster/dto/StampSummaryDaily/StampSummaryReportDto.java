package cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily;

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
public class StampSummaryReportDto extends GridDataDTO {

    private String businessDate;

    private String storeCd;

    private String storeName;

    private String articleId;

    private String articleName;

    private BigDecimal totalIssuedQty;

    private BigDecimal bonusItemsQty;

    private BigDecimal amountQty;

    private String createYmdHms;


}
