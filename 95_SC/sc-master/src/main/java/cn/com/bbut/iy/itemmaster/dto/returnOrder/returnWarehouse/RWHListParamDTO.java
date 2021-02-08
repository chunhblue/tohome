package cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @author lz
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class RWHListParamDTO extends GridParamDTO {
    /**
     * 检索参数json格式，前端传过来后，使用 RWHListParamDTO 进行反射解析
     */
    private String searchJson;

    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;

}
