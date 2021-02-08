package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dao.PromotionMapper;
import cn.com.bbut.iy.itemmaster.dto.promotion.PromotionDTO;
import cn.com.bbut.iy.itemmaster.dto.promotion.PromotionExcelParam;
import cn.com.bbut.iy.itemmaster.dto.promotion.PromotionParamDTO;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;

import com.google.gson.Gson;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * MM Promotion List Excel生成
 *
 */
@Slf4j
@Service(value = "mmExcelService")
public class PromotionExServiceImpl implements ExService {

    @Autowired
    private PromotionMapper mapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        PromotionExcelParam paramDTO = (PromotionExcelParam) param;
        Gson gson = new Gson();
        PromotionParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), PromotionParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 生成文件标题信息对象
        session.setHeaderListener(new PromotionExHeaderListener(jsonParam));
        session.createWorkBook();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 3;
            // 生产excel内容
            createExcelBody(sheet, curRow, jsonParam, paramDTO.getPCode());
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
    private void createExcelBody(Sheet sheet, int curRow, PromotionParamDTO jsonParam, String pcode) {
        // 查询数据
        List<PromotionDTO> _list = mapper.selectListByCondition(jsonParam);
        // 遍历数据
        int no = 1;
        for (PromotionDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getPromotionCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getPromotionName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getPromotionStartDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getPromotionEndDate()));

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 40 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
    }

    /**
     * 生成报表样式
     * 
     * @param wb
     */
    private void createExcelStyleToMap(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName(Constants.DEFAULT_FONT);
        DataFormat df = wb.createDataFormat();// 格式化使用
        MAP_STYLE.clear();

        // 文本居中+边线
        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("@"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        MAP_STYLE.put(STYPE_KEY_1, style);
    }

}
