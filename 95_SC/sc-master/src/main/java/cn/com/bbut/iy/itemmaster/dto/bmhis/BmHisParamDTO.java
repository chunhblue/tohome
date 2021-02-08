package cn.com.bbut.iy.itemmaster.dto.bmhis;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;

/**
 * BM 历史查询dto
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmHisParamDTO extends GridParamDTO {

    /**
     * 检索参数json格式，前端传过来后，使用 BmJsonParamDTO 进行反射解析
     */
    private String searchJson;

    private Collection<Integer> roleIds;

    private String pCode;
}
