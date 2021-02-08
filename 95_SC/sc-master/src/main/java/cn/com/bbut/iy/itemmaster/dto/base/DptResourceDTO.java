package cn.com.bbut.iy.itemmaster.dto.base;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DptResourceDTO implements Serializable {

	/** 部门id */
	private String k;
	/** 部门名称 */
	private String v;
}
