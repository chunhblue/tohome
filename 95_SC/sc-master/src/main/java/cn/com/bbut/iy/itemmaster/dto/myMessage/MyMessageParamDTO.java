package cn.com.bbut.iy.itemmaster.dto.myMessage;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MyMessageParamDTO extends GridParamDTO {

    private String searchJson;

    private int limitStart;

    private String approvalCategory;

    private String approvalType;

    private String approvalRecords;

    private String submissionsDate;

    private String submitterName;

    private String submitterCode;

    private String status;
}
