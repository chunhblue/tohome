package cn.com.bbut.iy.itemmaster.dto.viettelParamPhone;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName ma8407dto
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/18 18:47
 * @Version 1.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ma8407Paramdto extends GridParamDTO {
    private int limitStart;
    private String requestDate;
    private String startDateTime;
    private String endDateTime;
    /** 权限包含的所有店铺编号 */
    private Collection<RoleStoreDTO> stores;
    // 是否分页
    private boolean flg = true;
}
