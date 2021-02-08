package cn.com.bbut.iy.itemmaster.dto.bmhis;

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
public class BmHisResuleDataDTO implements Serializable {
    private static final long serialVersionUID = 2019L;
    // 结果集合
    private List<BmHisListGridDataDTO> list;
    // 数据量
    private Long count;

    public BmHisResuleDataDTO(List<BmHisListGridDataDTO> list, Long count) {
        this.list = list;
        this.count = count;
    }
}
