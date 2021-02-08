package cn.com.bbut.iy.itemmaster.dto.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签dto 适用于 名称编号的返回
 * 
 * @author songxz
 * @date: 2019年10月9日 - 下午1:30:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelDTO {
    private String code;
    private String name;
    private String codeName;
}
