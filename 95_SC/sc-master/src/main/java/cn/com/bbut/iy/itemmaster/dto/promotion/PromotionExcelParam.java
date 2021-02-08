package cn.com.bbut.iy.itemmaster.dto.promotion;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;

/**
 * MM促销一览画面 excel传参用DTO
 * 
 * @author songxz
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PromotionExcelParam extends BaseExcelParam {

    /**
     * 检索参数json格式，前端传过来后，使用反射进行解析
     */
    private String param;

    // 权限
    private String pCode;

}
