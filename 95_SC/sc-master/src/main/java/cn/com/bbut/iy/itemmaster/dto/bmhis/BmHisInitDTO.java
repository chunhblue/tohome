package cn.com.bbut.iy.itemmaster.dto.bmhis;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;

/**
 * order 编码
 * 
 * @author songxz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BmHisInitDTO {
    private List<AutoCompleteDTO> div;
    private List<AutoCompleteDTO> dpt;
    private List<AutoCompleteDTO> stores;
}
