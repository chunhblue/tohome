package cn.com.bbut.iy.itemmaster.dto.pi0100c;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PI0100ParamDTOC extends GridParamDTO {
    private String piCd;
    private String piStartDate;
    private String piEndDate;
    private String piStatus;
    // 分页
    private int limitStart;

    // 是否分页
    private boolean flg = true;
    /** 大区名称 */
    private String regionName;
    /** 城市名称 */
    private String cityName;
    /** 区域名称 */
    private String districtName;
    /** 店铺CD */
    private String storeName;

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
    // 审核状态
    private String reviewSts;

}
