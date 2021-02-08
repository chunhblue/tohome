package cn.com.bbut.iy.itemmaster.dto.auditTask;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class
AuditTaskParamDTO extends GridParamDTO {

    /**
     * 检索参数json格式，前端传过来后，使用 AuditTaskParamDTO 进行反射解析
     */
    private String searchJson;

    // 分页
    private int limitStart;


    private String approvalCategory;

    private String approvalType;

    private String approvalRecords;

    private String submissionsDate;

    private String submitterName;

    private String submitterCode;

    private String status;
}
