package cn.com.bbut.iy.itemmaster.dto.pogShelf;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PogShelfParamDto  extends GridParamDTO {
    private String pogCd;

    private String pogName;

    private String storeCd;

    private String startDate;

    private String endDate;

    private String reviewStatus;

    private int limitStart;

    // 分页
    private boolean flg = true;
}
