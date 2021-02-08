package cn.com.bbut.iy.itemmaster.dto.bm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.entity.IyBmItemCk;

/**
 * order 编辑单品明细提交对象 没用了 等着删除
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IyBmItemCkAndStore extends IyBmItemCk {
    private String store;
}
