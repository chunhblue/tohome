package cn.com.bbut.iy.itemmaster.dto.base;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * menu 菜单
 * 
 * @author bxg
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuParamDTO extends GridParamDTO {

    /** 菜单name */
    private String name;
    // 分页
    private int limitStart;
}
