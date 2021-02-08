package cn.com.bbut.iy.itemmaster.dto.bomSale;

import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * BOM销售报表查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BomSaleParamDTO {

    private String businessDate;

    private String saleStartDate;
    private String saleEndDate;

    private String articleId;

    // 是否分页
    private boolean flg = true;

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

    /** 大区名称 */
    private String regionName;
    /** 城市名称 */
    private String cityName;
    /** 区域名称 */
    private String districtName;
    /** 店铺名称 */
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

}
