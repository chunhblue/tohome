package cn.com.bbut.iy.itemmaster.dto.stocktakeProcess;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class StocktakeReportByDepDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String piCd;

    private String piDate;

    private String storeCd;

    private String pmaCd;

    private String pmaName;

    private BigDecimal varianceAmt;

    private String zoCd;
}
