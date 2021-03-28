package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceDailyDto;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffAttendanceDailyMapper {

    List<StaffAttendanceDailyDto> search(StaffAttendanceParamDailyDto classParamdto);

    int searchCount(StaffAttendanceParamDailyDto param);
}
