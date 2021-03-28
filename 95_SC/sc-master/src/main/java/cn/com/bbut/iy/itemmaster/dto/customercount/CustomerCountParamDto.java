package cn.com.bbut.iy.itemmaster.dto.customercount;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName CustomerCountDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/20 16:37
 * @Version 1.0
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerCountParamDto  extends GridParamDTO {
    private String startDate;
    private String endDate;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    private int limitStart;
    private String regionCd;
    private String cityCd;
    private String storeCd;
    private String districtCd;
    private String bussinessDate;
    private boolean flg=true;
}
