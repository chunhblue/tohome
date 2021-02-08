package cn.com.bbut.iy.itemmaster.dto.order;

import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * order Ajax返回结果
 * 
 * @author zcz
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ResultDto extends AjaxResultDto {
    private Object data;
}
