package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionToMenuDTO implements Serializable {

	private Integer permId;

	private String permName;

	private Integer flg;// 是否被选中
}
