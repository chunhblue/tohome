package cn.com.bbut.iy.itemmaster.dto.materialentry;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PI0100ParamDTOE extends GridParamDTO {
    private String piCd;
    private String storeCd;
    private String piStartDate;
    private String piEndDate;
    private String piStatus;
    // 分页
    private int limitStart;

}
