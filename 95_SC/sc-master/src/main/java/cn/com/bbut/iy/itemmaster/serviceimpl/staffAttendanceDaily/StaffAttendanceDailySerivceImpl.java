package cn.com.bbut.iy.itemmaster.serviceimpl.staffAttendanceDaily;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.StaffAttendanceDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceDailyDto;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.StaffAttendanceDailySerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StaffAttendanceDailySerivceImpl implements StaffAttendanceDailySerivce {
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private StaffAttendanceDailyMapper mapper;

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    @Override
    public Map<String, Object> search(StaffAttendanceParamDailyDto param) {
        // 获取业务日期
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        // 总条数
        int count = mapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<StaffAttendanceDailyDto> reportDTOList = mapper.search(param);
        for(StaffAttendanceDailyDto dto:reportDTOList){
            String timeIn = dto.getTimeIn().substring(0,4)+"00";
            String timeOut = dto.getTimeOut().substring(0,4)+"00";
            String startTime = formatDate(dto.getDateIn1(),timeIn);
            String endTime = formatDate(dto.getDateOut(),timeOut);
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long nd = 1000*60*60*24; // 一天的毫秒数
            long nh = 1000*60*60;    // 一小时的毫秒数
            long nm = 1000*60;       // 一分钟的毫秒数
            long diff = 0;
            try {
                diff = sd.parse(endTime).getTime()-sd.parse(startTime).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long day = diff/nd;
            long hour = diff%nd/nh;
            long min = diff%nd%nh/nm;
            if(day == 0){
                dto.setHourWorked(hour+":"+min);
            }else {
                dto.setHourWorked(day+":"+hour+":"+min);
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",reportDTOList);
        return map;
    }

    /**
     * yyyyMMdd hhmmss -> yyyy-MM-dd hh:mm:ss
     * @param date
     * @return
     */
    public String formatDate(String date,String time){
        String newDate = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        String newTime = time.substring(0,2)+":"+time.substring(2,4)+":"+time.substring(4,6);
        return newDate + " " + newTime;
    }
}
