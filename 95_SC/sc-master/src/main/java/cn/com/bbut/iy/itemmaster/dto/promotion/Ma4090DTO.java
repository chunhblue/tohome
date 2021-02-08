package cn.com.bbut.iy.itemmaster.dto.promotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * MM促销区域设定对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4090DTO extends GridDataDTO {

    private String serial;

    private String promotionCd;

    private String maCd;
    private String maName;

    private String storeTypeCd;
    private String storeTypeName;

    private BigDecimal displaySeq;

    private String effectiveSts;

}
