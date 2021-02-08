package cn.com.bbut.iy.itemmaster.dto.base.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleStoreDTO implements Serializable {

	private String regionCd;
	private String regionName;

	private String cityCd;
	private String cityName;

	private String districtCd;
	private String districtName;

	private String storeCd;
	private String storeName;

	private String serialNo;

}
