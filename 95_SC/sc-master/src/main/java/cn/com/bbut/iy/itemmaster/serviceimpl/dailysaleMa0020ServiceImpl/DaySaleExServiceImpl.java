package cn.com.bbut.iy.itemmaster.serviceimpl.dailysaleMa0020ServiceImpl;

import cn.com.bbut.iy.itemmaster.dao.DaSaleReportMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "sellDayExService")
public class DaySaleExServiceImpl implements ExService {

    @Autowired
    private DaSaleReportMapper mapper;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        DaySaleReportParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), DaySaleReportParamDTO.class);
        // 导出不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setEffectiveStartDate(startDate);
        }

        // 生成文件标题信息对象
        session.setHeaderListener(new DaySaleExHeaderListener(jsonParam));

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
    private void createExcelBody(Sheet sheet, int curRow, DaySaleReportParamDTO jsonParam) {
        List<DaySaleReportDTO> _list=new ArrayList<>();
        if (jsonParam.getTypeDate().equals("1")){
            _list = mapper.selectDaySaleReport(jsonParam);
            int no = 1;
            for (DaySaleReportDTO ls : _list) {
                int curCol = 0;
                Row row = sheet.createRow(curRow);
                Cell cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValueNo(cell, no++);

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getStoreCd());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getStoreName());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getSaleDate()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getAvgCustomerNo()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime1214()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime1416()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime1618()+""));



                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime1820()+""));
                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getShift1()+""));


                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime2022()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime2224()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime02()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime24()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getShift1()+""));
                cell = row.createCell(curCol++);

                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime46()+""));


                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime68()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime810()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTime1012()+""));



                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getShift3()+""));
                System.out.println(ls.getShift3());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, formatNum(ls.getTotalAmt()+""));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getAmName());


























                curRow++;
            }
            // 设置列宽
            int columnIndex = 0;
            sheet.setColumnWidth(columnIndex++, 5 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 22 * 256);
            sheet.setColumnWidth(columnIndex++, 18 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 25 * 256);
        }
        if (jsonParam.getTypeDate().equals("2")){
            _list = mapper.selectDayPosSaleReport(jsonParam);
            int no = 1;
            for (DaySaleReportDTO ls : _list) {

                int curCol = 0;
                Row row = sheet.createRow(curRow);
                Cell cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValueNo(cell, no++);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getSaleDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getAvgCustomerNo()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime68()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime810()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime1012()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime1214()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getShift1()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime1416()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime1618()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime1820()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime2022()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getShift2()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime2224()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime02()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime24()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTime46()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getShift3()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getTotalAmt()+""));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getAmName());


            curRow++;
            }
            // 设置列宽
            int columnIndex = 0;
            sheet.setColumnWidth(columnIndex++, 5 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 22 * 256);
            sheet.setColumnWidth(columnIndex++, 18 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 25 * 256);
        }


    }

}
