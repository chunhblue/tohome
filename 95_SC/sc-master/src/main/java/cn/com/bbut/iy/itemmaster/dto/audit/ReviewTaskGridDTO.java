package cn.com.bbut.iy.itemmaster.dto.audit;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 审核任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewTaskGridDTO extends GridDataDTO {
    /**
     * 数据cd
     */
    private String recordCd;
    /**
     * 审核状态
     */
    private String status;
    /**
     * 审核状态名称
     */
    private String statusName;

    public String getStatusName() {
        if("1".equals(this.status)){
            return "Pending";
        }else if ("5".equals(this.status)){
            return "Rejected";
        }else if ("6".equals(this.status)){
            return "Withdrawn";
        }else if ("7".equals(this.status)){
            return "Expired";
        }else if ("10".equals(this.status)){
            return "Approved";
        }else{
            return "";
        }
    }

    /**
     * 类型编号
     */
    private String typeId;
    /**
     * 类型名称
     */
    private String typeName;
    /**
     * 发起用户id
     */
    private String createUserId;
    /**
     * 发起用户名称
     */
    private String createUserName;
    /**
     * 发起时间
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date auditTime;
    /**
     * 地址
     */
    private String url;

    /**
     * 审核log id
     */
    private Integer id;

}
