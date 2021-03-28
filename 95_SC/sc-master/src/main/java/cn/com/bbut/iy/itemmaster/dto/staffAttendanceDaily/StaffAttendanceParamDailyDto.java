package cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAttendanceParamDailyDto extends GridParamDTO {

    private String startDate;

    private String endDate;

    /**
     * 业务日期
     */
    private String businessDate;

    /** 店铺CD */
    private String storeCd;

    private boolean flg = true;
    private int limitStart;

}
