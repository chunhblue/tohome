package cn.com.bbut.iy.itemmaster.dto.cardPaymentReport;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName cardPaymentParamDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/19 18:09
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class cardPaymentParamDto  extends GridParamDTO {

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
