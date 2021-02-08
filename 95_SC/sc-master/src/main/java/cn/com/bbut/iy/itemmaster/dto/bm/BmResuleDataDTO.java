package cn.com.bbut.iy.itemmaster.dto.bm;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author songxz
 *
 */
@Data
@NoArgsConstructor
public class BmResuleDataDTO implements Serializable {
    private static final long serialVersionUID = 2019L;
    // 结果集合
    private List<BmListGridDataDTO> list;
    // 数据量
    private Long count;

    public BmResuleDataDTO(List<BmListGridDataDTO> list, Long count) {
        this.list = list;
        this.count = count;
    }
}
