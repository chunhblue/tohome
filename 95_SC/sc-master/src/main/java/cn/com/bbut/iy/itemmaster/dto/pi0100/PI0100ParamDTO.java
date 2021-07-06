package cn.com.bbut.iy.itemmaster.dto.pi0100;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.util.Collection;

@Data
public class PI0100ParamDTO extends GridParamDTO {
    private String piCd;
    private String piDate;
    private String piStartDate;
    private String piEndDate;
    private String piStatus;
    private String reviewStatus;
    // 分页
    private int limitStart;

    private String startTime;

    // 是否分页
    private boolean flg = true;

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

}
