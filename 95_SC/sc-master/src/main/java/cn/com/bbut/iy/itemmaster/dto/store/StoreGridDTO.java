package cn.com.bbut.iy.itemmaster.dto.store;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 店铺主档对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreGridDTO extends GridDataDTO {
    private String storeCd;

    private String effectiveStartDate;

    private String effectiveEndDate;

    private String storeName;

    private String storeType;

    private String userName;

    private String zoName;

    private String doName;

    private String maName;

    private String storeTypeName;

    private String storeGroupCd;

    private String storeGroupName;

    private String qtypeCodeName;

    private String effectiveSts;

    private String storeSizeGroupName;

    private String openDate;

    private String closeDate;

    private String currentLocation;

    private CommonDTO commonDTO;

}
