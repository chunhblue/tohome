package cn.com.bbut.iy.itemmaster.dto.base.DefaultRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认角色授权编辑用dto 参数dto
 * 
 * @author songxz
 * @date: 2019年10月8日 - 下午3:23:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DefaultRoleEditDTO {
    // 为空则视为Add，不为空则视为修改
    private Integer id;
    private Integer roleId;
    private String dpt;
    private String postCode;
    private String occupCode;
    // 逗号“,”分割多个店铺,需要按照店铺数量进行批量插入，
    private String store;
    private String userid;
}
