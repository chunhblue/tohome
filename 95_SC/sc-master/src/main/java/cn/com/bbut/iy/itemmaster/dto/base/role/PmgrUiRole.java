package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色，存放角色基本信息
 * 
 * @author songxz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PmgrUiRole implements Serializable {
	private static final long serialVersionUID = 2012L;
	private Integer id;// id
	private String name;// 名称
	private Integer superId;// 父级id
	private Integer rtype;// 该角色所属的资源类型
	private String rid;// 所属，该角色所属的资源小分类id，注：rtype+rid可确定所属
	private Boolean isuse;// 是否可授权
	private String ridName;// 所属名称
}
