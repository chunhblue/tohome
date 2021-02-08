package cn.com.bbut.iy.itemmaster.dto.base.actorAssignment;

import java.io.Serializable;

import lombok.Data;

@Data
public class SubmitDataDTO implements Serializable {

    private Integer id;

    private Integer roleId;

    private String userId;

    private Integer assType;

    private String startDate;

    private String endDate;

    private String assUserId;
}
