package cn.com.bbut.iy.itemmaster.serviceimpl.export;

import cn.com.bbut.iy.itemmaster.dao.PogShelfMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfDto;
import cn.com.bbut.iy.itemmaster.dto.pogShelf.PogShelfParamDto;
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
import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValue;

@Slf4j
@Service(value = "shelfMaintenanceExService")
public class ShelfMaintenanceExServiceImpl implements ExService {
        @Autowired
        private PogShelfMapper mapper;

        @Override
        public File getExcel(BaseExcelParam param) throws IOException {
            TransSession session = Container.getBean(TransSession.class);
            // 查询参数转换
            ExcelParam paramDTO = (ExcelParam) param;
            Gson gson = new Gson();
            PogShelfParamDto jsonParam = gson.fromJson(paramDTO.getParam(), PogShelfParamDto.class);
            // 导出数据不需要分页
            jsonParam.setFlg(false);
            // 生成文件标题信息对象
            session.setHeaderListener(new ShelfMaintenanceExHeaderListener(jsonParam));
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
        private void createExcelBody(Sheet sheet, int curRow, PogShelfParamDto jsonParam) {
            // 查询数据
            List<PogShelfDto> _list = mapper.getPogShelfList(jsonParam);
            // 遍历数据
            int no = 1;
            for (PogShelfDto ls : _list) {
                int curCol = 0;
                Row row = sheet.createRow(curRow);
                Cell cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValueNo(cell, no++);

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                setCellValue(cell, fmtDateAndTimeToStr(ls.getCreateTime()));

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
                setCellValue(cell, ls.getStoreCd());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getStoreName());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getPogName());

                cell = row.createCell(curCol++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, ls.getCreateUserName());

                curRow++;
            }
            // 设置列宽
            int columnIndex = 0;
            sheet.setColumnWidth(columnIndex++, 5 * 256);
            sheet.setColumnWidth(columnIndex++, 25 * 256);
            sheet.setColumnWidth(columnIndex++, 15 * 256);
            sheet.setColumnWidth(columnIndex++, 20 * 256);
            sheet.setColumnWidth(columnIndex++, 25 * 256);
            sheet.setColumnWidth(columnIndex++, 18 * 256);
        }

}
