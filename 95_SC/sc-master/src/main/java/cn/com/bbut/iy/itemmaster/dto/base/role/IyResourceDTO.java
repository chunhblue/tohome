package cn.com.bbut.iy.itemmaster.dto.base.role;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IyResourceDTO implements Serializable {

	private String dpt;

	private String store;
}
