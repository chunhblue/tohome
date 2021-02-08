package cn.com.bbut.iy.itemmaster.dto.adjustmentDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 门店库存调整日报查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AdjustmentDailyParamDTO extends GridParamDTO {

    private String businessDate;

    private String adjustmentStartDate;
    private String adjustmentEndDate;

    private String articleId;

    private String articleName;



    private String am;


    private String generalReason;
    private String adjustReason;
    private String depCd;
    private String depName;
    private String pmaCd;
    private String pmaName;
    private String categoryCd;
    private String categoryName;
    private String subCategoryCd;
    private String subCategoryName;


    private String ofc;
    private String ofcName;
    private String barcode;

    private int limitStart;
    private boolean flg = true;

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

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
