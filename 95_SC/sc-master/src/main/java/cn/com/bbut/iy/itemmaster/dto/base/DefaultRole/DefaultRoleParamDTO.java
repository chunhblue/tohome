package cn.com.bbut.iy.itemmaster.dto.base.DefaultRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;

/**
 * 默认角色授权 参数dto
 * 
 * @author songxz
 * @date: 2019年10月8日 - 下午3:23:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRoleParamDTO extends GridParamDTO {
    private Integer roleId;
    private String dpt;
    private String store;
    private String postCode;
    private String occupCode;
}
