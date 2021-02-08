package cn.com.bbut.iy.itemmaster.dto.coreItem;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.dc.pr.PRError;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName coreItemDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/8/5 19:04
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class coreItemDTO extends GridDataDTO {
    private String articleId;
    private String articleName;
    private String depCd;
    private String pmaCd;
    private String ofc;
    private String CategoryCd;
    private String subCategoryCd;
    private String depName;
    private String pmaName;
    private String amName;
    private String CategoryName;
    private String subCategoryName;
    private BigDecimal saleQty;
    private Integer seqNo;
    private BigDecimal saleAmt;
    private String structureCd;
    private String storeCd;
    private String storeName;
    private String accDate;
    private String barcode;
    private String regionCd;
    /** 权限包含的所有店铺编号 */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /**
     * 角色资源
     */
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    private List<ResourceViewDTO> resources;
}
