package cn.com.bbut.iy.itemmaster.dto.daysalereport;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName DaySaleReportParamDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/25 13:17
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DaySaleReportParamDTO extends GridParamDTO {
    private int limitStart;
    private String City;
    private String storeName;
    private String avgCustomerNo;
    private String saleAmt;
    private String psd;
    private String  ofc;
    private String am;

    // 是否分页
    private boolean flg = true;

    private  String effectiveStartDate;
    private String  effectiveEndDate;

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
