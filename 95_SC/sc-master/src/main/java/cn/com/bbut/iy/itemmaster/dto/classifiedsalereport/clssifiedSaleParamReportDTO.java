package cn.com.bbut.iy.itemmaster.dto.classifiedsalereport;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName clssifiedSaleParamReportDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/27 14:37
 * @Version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class clssifiedSaleParamReportDTO extends GridParamDTO {

    private String startDate;
    private String endDate;
    private String pmaCd;
    private String articleId;
    private String am;
    private String city;
    private String depCd;
    private String depName;
    private String ofc;
     private String maCd;
     private String subCategoryCd;
     private String categoryCd;

     private boolean flg = true;
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

}
