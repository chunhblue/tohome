package cn.com.bbut.iy.itemmaster.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortcutDTO {
    private String approvalCategory;

    private String approvalType;

    private String approvalStatus;

    private String summary;

    private String createUser;

    private String createDateTime;

    private Integer nRecordid;

    private String URL;

    private String recordCd;

    private String effectiveStartDate;
}
