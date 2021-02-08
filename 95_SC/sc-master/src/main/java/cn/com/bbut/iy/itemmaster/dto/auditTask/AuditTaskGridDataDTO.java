package cn.com.bbut.iy.itemmaster.dto.auditTask;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class AuditTaskGridDataDTO extends GridDataDTO {


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
