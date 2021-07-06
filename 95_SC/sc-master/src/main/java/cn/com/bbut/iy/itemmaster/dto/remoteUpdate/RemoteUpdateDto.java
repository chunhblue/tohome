package cn.com.bbut.iy.itemmaster.dto.remoteUpdate;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteUpdateDto extends GridParamDTO {
    private Integer id;
    private String storeCd;
    private String storeName;
    private String startDate;
    private String informCd;
    private String groupName;
    private String updateType;
    private String updateTypeName;
    private String createUserId;
    private String createYmd;
    private String createHms;
    private String updateUserId;
    private String updateYmd;
    private String updateHms;
}
