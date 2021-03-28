package cn.com.bbut.iy.itemmaster.dto.hhtReport;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName hhtReportParamDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/17 17:46
 * @Version 1.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class hhtReportParamDto extends GridParamDTO {
    private String startDate;
    private String hhstartDate;
    private String endDate;
    private String hhendDate;
    private String storeCd;
    private String businessDate;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String aStoreCd;
    private boolean flg = true;
    private int limitStart;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
