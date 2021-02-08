package cn.com.bbut.iy.itemmaster.dto.coreItem;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName coreItemParamDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/8/5 19:28
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class coreItemParamDTO extends GridParamDTO {
    private String city;
    private String coreItemType;
    private String depCd;
    private String pmaCd;
    private String CategoryCd;
    private String subCategoryCd;
    private String depName;
    private String pmaName;
    private String CategoryName;
    private String subCategoryName;
    private String startDate;
    private String endDate;
    private String amCd;
    private boolean flg = true;
    private List<ResourceViewDTO> resources;
    private int limitStart;
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

    public String getCoreItemType() {
        return coreItemType;
    }

    public void setCoreItemType(String coreItemType) {
        this.coreItemType = coreItemType;
    }
}
