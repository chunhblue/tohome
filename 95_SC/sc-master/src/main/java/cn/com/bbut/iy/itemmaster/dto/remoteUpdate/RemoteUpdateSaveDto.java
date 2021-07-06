package cn.com.bbut.iy.itemmaster.dto.remoteUpdate;

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
public class RemoteUpdateSaveDto {
    private Integer id;
    private String startDate;
    private String groupName;
    private String groupCode;
    private String updateType;
    private String informCd;
    private List<String> storeList;
    private String fileDetailJson;
    private String createUserId;
    private String createYmd;
    private String createHms;
    private String updateUserId;
    private String updateYmd;
    private String updateHms;
    private String oldFtpFilePath;
    private String ftpFilePath;
}
