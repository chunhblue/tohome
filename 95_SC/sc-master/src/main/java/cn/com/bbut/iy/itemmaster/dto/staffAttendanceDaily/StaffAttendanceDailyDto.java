package cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffAttendanceDailyDto  extends GridDataDTO {

    private String storeCd;
    private String dateIn;

    /**
     * 员工编号
     */
    private String staffCode;

    private String staffName;

    private String dateIn1;

    private String timeIn;

    private String dateOut;

    private String timeOut;

    private String hourWorked;

    private String shift;
}
