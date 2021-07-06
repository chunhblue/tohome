package cn.com.bbut.iy.itemmaster.dto.base.actorAssignment;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorAssGridDataDTO implements Serializable {

    private Integer id;

    private Integer roleId;

    private String roleName;

    private String remark;

    private String userId;

    private String userName;

//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
}
