package cn.com.bbut.iy.itemmaster.dto.ma4200;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MA4200ParamDTO extends GridParamDTO {
    /**
     * 用户编号
     */
    private String empNumId;
    /**
     * 用户名称
     */
    private String empName;
    /**
     * 职务
     */
    private String jobTypeCd;
    /**
     * 店铺编号
     */
    private String storeCd;
    /**
     * 生效状态
     */
    private String effectiveStatus;
    /**
     * 业务时间
     */
    private String businessDate;
}