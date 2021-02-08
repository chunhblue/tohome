package cn.com.bbut.iy.itemmaster.dto.mRoleStore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 角色&店铺关系查询参数DTO
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MRoleStoreParam {

	// 角色信息
	private Collection<Integer> roleIds;

	// 角色信息
	private Collection<String> storeCds;

	// 大区编号
	private String regionCd;

	// 城市编号
	private String cityCd;

	// 区域编号
	private String districtCd;

	// 店铺编号
	private String storeCd;

	// 检索框输入值
	private String v;

}
