package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;

import java.util.Map;

public interface StaffAttendanceDailySerivce {
    Map<String,Object> search(StaffAttendanceParamDailyDto param);
}
