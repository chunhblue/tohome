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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
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
        /*int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setEffectiveStartDate(startDate);
        }*/

        // 生成文件标题信息对象
        session.setHeaderListener(new DaySaleExHeaderListener(jsonParam));

        session.createWorkBook();
        Workbook wb = session.getWb();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 3;
            // 生产excel内容
            createExcelBody(sheet, curRow, jsonParam,wb);
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
    private void createExcelBody(Sheet sheet, int curRow, DaySaleReportParamDTO jsonParam, Workbook wb) {
        List<CellRangeAddress> regionList = new ArrayList<CellRangeAddress>();
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
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getStoreCd());
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getStoreName());
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getSaleDate()));
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getAvgCustomerNo());


                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime68());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime810());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1012());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1214());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getShift1());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1416());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1618());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1820());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime2022());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getShift2());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime2224());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime02());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime24());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime46());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getShift3());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTotalAmt());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getAmName());
                curRow++;


                curCol = 4;
                row = sheet.createRow(curRow);
                /*cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, "");*/

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, "Count per hour:");


                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour6_8());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour8_10());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour10_12());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour12_14());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, setAmountValue(ls.getHour6_8()).add(setAmountValue(ls.getHour8_10())).add(setAmountValue(ls.getHour10_12())).add(setAmountValue(ls.getHour12_14())));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour14_16());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour16_18());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour18_20());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour20_22());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, setAmountValue(ls.getHour14_16()).add(setAmountValue(ls.getHour16_18())).add(setAmountValue(ls.getHour18_20())).add(setAmountValue(ls.getHour20_22())));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour22_24());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour0_2());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour2_4());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour4_6());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, setAmountValue(ls.getHour22_24()).add(setAmountValue(ls.getHour0_2())).add(setAmountValue(ls.getHour2_4())).add(setAmountValue(ls.getHour4_6())));

                /*cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, "");*/
                curRow++;
            }

            // 合并单元格
            for (CellRangeAddress region : regionList) {
                if(region.getLastRow()>region.getFirstRow()+1 || region.getLastColumn()>region.getFirstColumn()){
                    sheet.addMergedRegion(region);
                    setRegionUtil(region, sheet, wb);
                }
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
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getStoreCd());
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getStoreName());
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateToStr(ls.getSaleDate()));
                // 保存region
                setRegionList(regionList,1,curRow ,(curCol-1));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getAvgCustomerNo());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime68());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime810());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1012());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1214());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getShift1());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1416());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1618());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime1820());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime2022());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getShift2());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime2224());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime02());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime24());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTime46());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getShift3());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getTotalAmt());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getAmName());

                curRow++;


                curCol = 4;
                row = sheet.createRow(curRow);
                /*cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, "");*/

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, "Count per hour:");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour6_8());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour8_10());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour10_12());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour12_14());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, setAmountValue(ls.getHour6_8()).add(setAmountValue(ls.getHour8_10())).add(setAmountValue(ls.getHour10_12())).add(setAmountValue(ls.getHour12_14())));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour14_16());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour16_18());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour18_20());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour20_22());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, setAmountValue(ls.getHour14_16()).add(setAmountValue(ls.getHour16_18())).add(setAmountValue(ls.getHour18_20())).add(setAmountValue(ls.getHour20_22())));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour22_24());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour0_2());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour2_4());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, ls.getHour4_6());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, setAmountValue(ls.getHour22_24()).add(setAmountValue(ls.getHour0_2())).add(setAmountValue(ls.getHour2_4())).add(setAmountValue(ls.getHour4_6())));

                /*cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                setCellValue(cell, "");

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, "");*/
                curRow++;
            }

            // 合并单元格
            for (CellRangeAddress region : regionList) {
                if(region.getLastRow()>region.getFirstRow()+1 || region.getLastColumn()>region.getFirstColumn()){
                    sheet.addMergedRegion(region);
                    setRegionUtil(region, sheet, wb);
                }
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
    // 保存合并单元格的 region
    private void setRegionList(List<CellRangeAddress> regionList, int maxSize, int curRow, int curCol) {
        if (maxSize > 0) {
            // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
            regionList.add(new CellRangeAddress(curRow, curRow + maxSize, curCol, curCol));
        }
    }
    // 合并单元格的边框
    private void setRegionUtil(CellRangeAddress region, Sheet sheet, Workbook wb) {
        // 以下设置合并单元格的边框，避免边框不齐
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, wb);
    }
}
