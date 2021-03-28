package cn.com.bbut.iy.itemmaster.serviceimpl.staffAttendanceDaily;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.StaffAttendanceDailyMapper;
import cn.com.bbut.iy.itemmaster.dao.StampSummaryReportMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryReportDto;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceDailyDto;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.serviceimpl.stampReport.StampSummaryExHeadSerivceImpl;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

@Slf4j
@Service(value = "staffAttendanceExService")
public class StaffAttendanceExSerivceImpl  implements ExService {
    @Autowired
    private StaffAttendanceDailyMapper mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        StaffAttendanceParamDailyDto jsonParam = gson.fromJson(paramDTO.getParam(), StaffAttendanceParamDailyDto.class);
        // 导出不需要分页
        jsonParam.setFlg(false);
        // 生成文件标题信息对象
        session.setHeaderListener(new StaffAttendanceExHeadSerivceImpl(jsonParam));
        session.createWorkBook();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 3;
            // 生产excel内容
            createExcelBody(sheet, curRow, jsonParam);
            File outfile = new File(Utils.getFullRandomFileName());
            session.saveTo(new FileOutputStream(outfile));
            return outfile;
        } finally {
            session.dispose();
        }
    }

    /**
     * 生产excel内容
     *
     * @param sheet
     * @param curRow
     */
    private void createExcelBody(Sheet sheet, int curRow, StaffAttendanceParamDailyDto jsonParam) {
        // 获取业务日期
        String businessDate = getBusinessDate();
        jsonParam.setBusinessDate(businessDate);
        // 查询数据
        List<StaffAttendanceDailyDto> _list = mapper.search(jsonParam);
        for(StaffAttendanceDailyDto dto:_list){
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

        // 遍历数据
        int no = 1;
        for (StaffAttendanceDailyDto ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getDateIn()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStaffCode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStaffName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getDateIn1()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtIntTime(ls.getTimeIn()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getDateOut()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtIntTime(ls.getTimeOut()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getHourWorked());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getShift());

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
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

    public static String fmtIntTime(String date) {
        if (StringUtils.isBlank(date)) {
            return "";
        }
        return date.substring(0,2)+":"+date.substring(2,4);
    }
}
