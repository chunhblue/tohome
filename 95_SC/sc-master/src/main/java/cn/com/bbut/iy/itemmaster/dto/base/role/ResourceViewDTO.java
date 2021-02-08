package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceViewDTO implements Serializable {

	private String depCd;

	private String depName;

	private String pmaCd;
	private String pmaName;
	private String categoryCd;
	private String categoryName;
	private String subCategoryCd;
	private String subCategoryName;

}
