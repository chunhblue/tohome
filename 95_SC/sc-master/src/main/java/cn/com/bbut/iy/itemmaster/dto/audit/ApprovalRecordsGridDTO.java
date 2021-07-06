package cn.com.bbut.iy.itemmaster.dto.audit;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 审核记录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalRecordsGridDTO extends GridDataDTO {
    /**
     * 批准类型
     */
    private String approvalType;
    /**
     * 批准级别
     */
    private String approvalLevel;
    /**
     * 结果
     */
    private long subNo;
    /**
     * 用户编码
     */
    private String userCode;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 时间
     */
//    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT+8")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date dateTime;
    /**
     * 评论
     */
    private String comments;

    /**
     * 审核状态
     */
    private int auditStatus;

    /**
     * 类型名字
     */
    private String typeName;


    public String getResult() {
        if(this.subNo == 0) {
            return "Submitted";
        }
        if(this.auditStatus%10 == 5) {
            return "Rejected";
        }else if(this.auditStatus%10 == 6) {
            return "Withdrawn";
        }else if(this.auditStatus%10 == 7) {
            return "Expired";
        }else {
            return "Approved";
        }
    }
}
