package cn.com.bbut.iy.itemmaster.dto.yearendpromotion;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName yearEndPromotionParamDto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/2 16:15
 * @Version 1.0
 * @Description
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class yearEndPromotionParamDto  extends GridParamDTO {
    private String startDate;
    private String endDate;

    private String businessDate;
    private int limitStart;
    private Boolean flg=true;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;
}
