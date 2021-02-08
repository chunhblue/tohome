package cn.com.bbut.iy.itemmaster.dto.bm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;

/**
 * Ajax返回结果
 * 
 * @author HanHaiyun
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class BmItemResultDto extends AjaxResultDto {

    /** Item Barcode **/
    private String item;
    /** 商品名称 **/
    private String name;
    /** 商品系统码 **/
    private String itemSystem;
}
