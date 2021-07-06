package cn.com.bbut.iy.itemmaster.dto.stocktakeProcess;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StocktakeReportDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 本次盘点cd;
    private String thisStocktakeCd;

    private String storeCd;

    private String storeName;
    private String effectiveStartDate;

    // 开店日期
    private String openDate;

    // 本次盘点日期;
    private String thisStocktakeDate;

    // 本次开始时间;
    private String thisStartTime;

    // 本次结束时间;
    private String thisEndTime;

    // 上次盘点日期;
    private String lastStocktakeDate;

    // 上次盘点cd;
    private String lastStocktakeCd;

    // 上次盘点开始时间;
    private String lastStartTime;

    // 上次盘点结束时间;
    private String lastEndTime;

    // 距离上次盘点相差的天数;
    private String day;

    // 商品种类数量
    private BigDecimal sku;

    // 商品总数量
    private BigDecimal qty;

    // 商品总金额
    private BigDecimal amt;

    // 账面上商品库存
    private BigDecimal bookValQty;

    // 账面上 商品库存金额
    private BigDecimal bookValAmt;

    // 本次盘点计划导出时间
    private String exportTime;

    // 本次盘点variance审核状态
    private Integer reviewStatus;
}
