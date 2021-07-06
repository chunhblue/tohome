package cn.com.bbut.iy.itemmaster.dto.cash;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 金种一览
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CashDetailParam extends GridParamDTO {

    private String storeName;
    private String ofc;
    private String amName;
    private String payAmtFlg;

    private String vStartDate;

    private String vEndDate;

    private String userId;

    private String payAmtDiff;

    private String shift;

    private String posId;
    private String am;
    private String amCd;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    // 业务日期
    private String businessDate;

    private int limitStart;

    private boolean flg = true;
}
