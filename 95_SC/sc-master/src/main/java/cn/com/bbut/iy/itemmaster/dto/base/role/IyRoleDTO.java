package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;
import java.util.List;

import cn.shiy.common.pmgr.entity.RoleResources;
import lombok.Data;

@Data
public class IyRoleDTO implements Serializable {

	private Integer id;

	private String name;

	private String remark;

	private List<RoleResources> resources;

	private String permissionsStr;

	private String menusStr;

	private String storesStr;

}
