package cn.com.bbut.iy.itemmaster.dto.base.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesDataDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String groupCode;// 组标记
	private Integer rtype;// 资源类型
	private String rid;// 资源id
}
