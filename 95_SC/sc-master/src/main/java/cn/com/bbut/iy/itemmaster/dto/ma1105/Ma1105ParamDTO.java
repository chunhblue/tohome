package cn.com.bbut.iy.itemmaster.dto.ma1105;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma1105ParamDTO extends GridParamDTO {
    /**
     * 检索参数json格式，前端传过来后，使用 Ma1105JsonParamDTO 进行反射解析
     */
    private String searchJson;

    /**
     * 角色资源
     */
    private List<ResourceViewDTO> resources;

    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
