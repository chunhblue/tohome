package cn.com.bbut.iy.itemmaster.dto.writeOff;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 门店报废日报查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class WriteOffParamDTO extends GridParamDTO {
    private String businessDate;



    private String writeOffStartDate;
    private String writeOffEndDate;

    private String articleId;



    private String articleName;

    private String writeOffDate;


    private String ofcName;

     private String ofc;


    private String am;
    private String barcord;
    private String depCd;
    private String depName;

    private String pmaCd;
    private String pmaName;
    private String categoryCd;

    private String categoryName;
    private String subCategoryCd;
    private String subCategoryName;
    private String barcode;
    private String adjustReason;

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
