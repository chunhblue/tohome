package cn.com.bbut.iy.itemmaster.dto.mySubmissions;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MySubmissionsGridDTO extends GridDataDTO {

    /**
     * 审批范围
     */
    private String approvalCategory;

    /**
     * 审批类型
     */
    private String approvalType;

    /**
     * 审批描述
     */
    private String summary;

    private String createUser;

    private String createDateTime;

    private Integer nRecordid;

    private String URL;

    private String recordCd;

    private String effectiveStartDate;
}
