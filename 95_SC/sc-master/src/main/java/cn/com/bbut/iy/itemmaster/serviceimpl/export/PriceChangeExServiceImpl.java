package cn.com.bbut.iy.itemmaster.serviceimpl.export;

import cn.com.bbut.iy.itemmaster.dao.SA0070Mapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.priceChange.PriceDetailParamDto;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "priceChangeExService")
public class PriceChangeExServiceImpl implements ExService {

    @Autowired
    private SA0070Mapper sa0070Mapper;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        PriceDetailParamDto jsonParam = gson.fromJson(paramDTO.getParam(), PriceDetailParamDto.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);

        /*int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setStartDate(startDate);
        }*/
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 生成文件标题信息对象
        session.setHeaderListener(new PriceChangeExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, PriceDetailParamDto jsonParam) {
        // 查询数据
        List<PriceDetailGridDto> _list = sa0070Mapper.getList(jsonParam);
        _list.stream().forEach(priceDetailGridDto -> {
            priceDetailGridDto.setCreateDate(priceDetailGridDto.getCreateYmd()+priceDetailGridDto.getCreateHms());
        });
        // 遍历数据
        int no = 1;
        for (PriceDetailGridDto ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getAccDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getChangeId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getArticleName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getSpec());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getUnitName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOldPrice());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getNewPrice());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateAndTimeToStr(ls.getCreateDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getCreateUserName());

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 23 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 12 * 256);
        sheet.setColumnWidth(columnIndex++, 12 * 256);
        sheet.setColumnWidth(columnIndex++, 26 * 256);
        sheet.setColumnWidth(columnIndex++, 32 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
    }

}
