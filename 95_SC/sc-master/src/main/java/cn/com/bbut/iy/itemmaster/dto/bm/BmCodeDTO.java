package cn.com.bbut.iy.itemmaster.dto.bm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * order 编码
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmCodeDTO {
    // 是否可用
    private boolean isUse;
    // 编码
    private String code;
    // bm类型：1 捆绑， 2 混合，3 固定组合，4 阶梯折扣， 5 AB组
    private String type;
    // 提示信息
    private String msg;

}
