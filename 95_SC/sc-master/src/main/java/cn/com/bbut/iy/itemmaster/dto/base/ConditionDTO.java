package cn.com.bbut.iy.itemmaster.dto.base;

import cn.com.bbut.iy.itemmaster.dto.base.role.IyResourceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionDTO implements Serializable {

    private String condition;

    private String conditionDpt;

    private String conditionStore;

    private List<IyResourceDTO> resources;
}
