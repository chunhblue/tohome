package cn.com.bbut.iy.itemmaster.serviceimpl.stocktake;

import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "stocktakePlanExService")
public class StocktakePlanExServiceImpl implements ExService {

    @Autowired
    private StocktakePlanMapper mapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        PI0100ParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), PI0100ParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        // 生成文件标题信息对象
        session.setHeaderListener(new StocktakePlanExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, PI0100ParamDTO jsonParam) {
        // 查询数据
        List<PI0100DTO> _list = mapper.search(jsonParam);
        if(_list.size()>0){
            for(PI0100DTO piDto:_list){
                Integer dayEndOfNow = 0;
                List<Integer> dateList = mapper.getDayOfEnd(piDto.getPiDate(),piDto.getStoreCd());
                if(dateList.size()>0){
                    dayEndOfNow = dateList.get(0)-Integer.parseInt(piDto.getPiDate());
                }else {
                    int year = Integer.parseInt(piDto.getPiDate().substring(0,4));
                    String month = piDto.getPiDate().substring(4,6);
                    int day = Integer.parseInt(piDto.getPiDate().substring(6,8));
                    int nowDay = 0;int nextDay = 0;
                    switch(month){
                        case "01":
                            nowDay = 31;
                            if(year%4 == 0){
                                nextDay = 29;
                            }else {
                                nextDay = 28;
                            }
                            break;
                        case "02":
                            if(year%4 == 0){
                                nowDay = 29;
                            }else {
                                nowDay = 28;
                            }
                            nextDay = 31;
                            break;
                        case "03":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "04":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "05":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "06":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "07":
                            nowDay = 31;
                            nextDay = 31;
                            break;
                        case "08":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "09":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "10":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "11":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "12":
                            nowDay = 31;
                            nextDay = 31;
                            break;
                    }
                    dayEndOfNow = nowDay+nextDay-day;
                }
                piDto.setDayEndOfNow(dayEndOfNow);
            }
        }
        // 遍历数据
        int no = 1;
        for (PI0100DTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getPiDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, formatTime(ls.getPiStartTime()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, formatTime(ls.getPiEndTime()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getPiCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPiStatus());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPiType());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getRemarks());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getDayEndOfNow());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getCreateUserId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateAndTimeToStr(ls.getCreateYmd()));

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
    }

    /**
     * 格式化时间 2020 07 04 17 05 16
     *
     * @param piTime
     * @return
     */
    private String formatTime(String piTime) {
        if (org.springframework.util.StringUtils.isEmpty(piTime)) {
            return "";
        }
        String hh = piTime.substring(0, 2);
        String mm = piTime.substring(2, 4);
        String ss = piTime.substring(4, 6);
        return hh + ":" + mm + ":" + ss;
    }

    /**
     * 获取商品状态名称
     *
     * @param status
     */
    private String getLifecycleStatus(String status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case "00":
                return "未生效";
            case "10":
                return "正常上架";
            case "20":
                return "暂停销售";
            case "30":
                return "下架处理";
            case "90":
                return "淘汰";
            default:
                return "";
        }
    }

}
