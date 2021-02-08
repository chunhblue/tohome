package cn.com.bbut.iy.itemmaster.dto.ma4110;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 *  促销信息 param
 *
 * @author zcz
 * @date:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4110ParamDto extends GridParamDTO {
    private String promotionCd;

    private String promotionName;

    private String itemCode;

    private String itemName;

    private String createUserId;

    private String createYmd;

    private String createEndYmd;

    private String createHms;

    private String updateUserId;

    private String updateYmd;

    private String depCd;

    private String customerStartDatefrom;

    private String promotionStartDate;

    private String promotionEndDate;

    private String customerStartDateto;

    private String ckStartDatefrom;

    private String ckStartDate;

    private String ckEndDate;

    private String ckStartDateto;

    private String businessDate;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /**
     * 店铺权限列表
     */
    private Collection<String> storeList;

    /**
     * 通知类型
     */
    private String informType;

    private String userId;

    private int limitStart;
}
