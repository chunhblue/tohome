package cn.com.bbut.iy.itemmaster.dto.pi0100;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.util.Collection;

/**
 * @ClassName ItemInfro
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/20 13:52
 * @Version 1.0
 * @Description
 */
@Data
public class ItemInStoreDto extends GridParamDTO {

    private String articleId;
    private String structureCd;
    private String adminStructureCd;
    private String articleName;
    private String storeCd;
    private String storeName;
    private String barcode;
    private boolean flg = true;
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    private int limitStart;

}
