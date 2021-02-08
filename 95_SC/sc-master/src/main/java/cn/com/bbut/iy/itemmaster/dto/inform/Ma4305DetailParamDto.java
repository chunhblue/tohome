package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 通报消息 角色范围 param
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4305DetailParamDto extends GridParamDTO {
    /**
     *通报编号
     */
    private String informCd;

}
