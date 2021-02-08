package cn.com.bbut.iy.itemmaster.dto.mySubmissions;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor // 有参构造
@NoArgsConstructor
public class MySubbmitterParamDTO extends GridParamDTO {

    private int limitStart;

    private Integer userId;

    private String userName;

    private String userRole;

}
