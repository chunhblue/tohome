package cn.com.bbut.iy.itemmaster.dto.goodsalereport;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName goodSaleReportParamDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/30 14:29
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class goodSaleReportParamDTO  extends GridParamDTO {
    private String articleId;
    private String articleName;

    private String barcode;
    private String saleDate;
    private  String storeName;
    private String city;
    private String ofc;
    private String depCd;
    private String pmaCd;
    private String categoryCd;
    private String subCategoryCd;
    private String pmaName;
    private String saleAmount;
    private String accDate; // 营业日期
    private String saleQty;
    private String startDate;
    private String endDate;
    private String ma;
    private String maCd;
    private String depName;

    private String am;

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
