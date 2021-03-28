package cn.com.bbut.iy.itemmaster.dto;

import java.util.Collection;
import java.util.List;

import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.RoleStoreDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;

/**
 * excel传参用DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ExcelParam extends BaseExcelParam {

    /**
     * 检索参数json格式，前端传过来后，使用反射进行解析
     */
    private String param;

    // 权限
    private String pCode;

    // 类型标识（部分页面可能用到）
    private String flag;

    /**
     * 角色资源
     */
    private List<ResourceViewDTO> resources;

    /**
     * 组织结构包含店铺权限
     */
    private Collection<String> stores;
    private Collection<RoleStoreDTO> stores1;

    private String userId;

}
