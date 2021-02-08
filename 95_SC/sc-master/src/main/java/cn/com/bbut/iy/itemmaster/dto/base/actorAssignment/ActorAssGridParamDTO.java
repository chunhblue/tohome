package cn.com.bbut.iy.itemmaster.dto.base.actorAssignment;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorAssGridParamDTO extends GridParamDTO {

    private Integer roleId;

	private String userId;

	private Integer assType;
}
