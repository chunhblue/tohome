package cn.com.bbut.iy.itemmaster.dto.clearInventory;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClearInventoryParamDTO extends GridParamDTO {

    private String accDate;

    private String articleId;

    private String articleName;

    private String depCd;

    private String pmaCd;

    private String categoryCd;

    private String subCategoryCd;

    private boolean flg = true;

    // 分页
    private int limitStart;
}
