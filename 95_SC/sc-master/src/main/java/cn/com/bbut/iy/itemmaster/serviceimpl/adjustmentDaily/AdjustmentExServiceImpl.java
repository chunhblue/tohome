package cn.com.bbut.iy.itemmaster.serviceimpl.adjustmentDaily;

import cn.com.bbut.iy.itemmaster.dao.AdjustmentDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.AdjustmentDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.AdjustmentDailyParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
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
@Service(value = "adjustmentDailyExService")
public class AdjustmentExServiceImpl implements ExService {

    @Autowired
    private AdjustmentDailyServiceImpl service;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        AdjustmentDailyParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), AdjustmentDailyParamDTO.class);
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 获取业务日期
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        // 生成文件标题信息对象
        session.setHeaderListener(new AdjustmentExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, AdjustmentDailyParamDTO jsonParam) {
        // 查询数据
        List<AdjustmentDailyDTO> _list = service.deleteGetList1(jsonParam);
//        AdjustmentDailyDTO adDto = service.getItemQty(jsonParam);
        AdjustmentDailyDTO adDto = service.deleteGetItemQty(jsonParam);
        // 遍历数据
        int no = 1;
        for (AdjustmentDailyDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreCd());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getAdjustmentDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getDepCd()+" "+ls.getDepName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getPmaCd()+" "+ls.getPmaName());



            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getCategoryCd()+" " +ls.getCategoryName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getSubCategoryCd()+" "+ls.getSubCategoryName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getArticleName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getUom());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getAdjustmentQty());

//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
//            setCellValue(cell, ls.getAdjustmentAmt());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getGeneralReasonText());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getAdjustReasonText());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOfc());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOfcName());

            curRow++;
        }
        int curCol = 0;
        Row row = sheet.createRow(curRow);
        Cell cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "Total:");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "total Item");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, formatNum(adDto.getRecords()+""));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "total qty");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(adDto.getAdjustmentQty().toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 40 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 23 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
    }

}
