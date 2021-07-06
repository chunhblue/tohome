package cn.com.bbut.iy.itemmaster.dto.remoteUpdate;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteUpdateCheckStoresDto extends GridParamDTO {
    private String[] storeCds;
    private String startDate;
    private String updateType;
    private String updateTypeName;
    private Integer id;
}
