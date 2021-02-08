package cn.com.bbut.iy.itemmaster.dto.priceByDay;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 当日适用价格一览查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PriceByDayParamDTO extends GridParamDTO {

    private int limitStart;

    private String effectiveDate;

    private String articleId;
    private String articleName;

    private String barcode;

    private String vendorId;

    private String depCd;

    private String pmaCd;

    private String categoryCd;

    private String subCategoryCd;

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

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

}
