package cn.com.bbut.iy.itemmaster.dto.bm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;

/**
 * BM excel 导出
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmParamDTO extends GridParamDTO {

    /**
     * 检索参数json格式，前端传过来后，使用 BmJsonParamDTO 进行反射解析
     */
    private String searchJson;

    private List<Integer> roleIds;

}
