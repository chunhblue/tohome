package cn.com.bbut.iy.itemmaster.dto.bm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;

/**
 * Bm Ajax返回结果
 * 
 * @author songxz
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class AjaxResultBmDto extends AjaxResultDto {
    private Object data;
}
