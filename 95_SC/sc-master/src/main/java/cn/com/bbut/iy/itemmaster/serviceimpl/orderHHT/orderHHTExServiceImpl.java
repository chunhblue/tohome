package cn.com.bbut.iy.itemmaster.serviceimpl.orderHHT;

import cn.com.bbut.iy.itemmaster.dao.OrderFailedMapper;
import cn.com.bbut.iy.itemmaster.dao.orderHHT.OrderHHTMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedParamDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTParamDto;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.serviceimpl.order.FailedExHeaderListener;
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
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * @ClassName orderHHTExServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/5/27 10:44
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service(value = "orderHHTExService")
public class orderHHTExServiceImpl  implements ExService {
    @Autowired
    private OrderHHTMapper mapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        OrderHHTParamDto jsonParam = gson.fromJson(paramDTO.getParam(), OrderHHTParamDto.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        // 生成文件标题信息对象
        session.setHeaderListener(new OrderHHTHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, OrderHHTParamDto jsonParam) {
        // 查询数据
        jsonParam.setFlg(false);
        List<OrderHHTGridDTO> _list = mapper.getDataList(jsonParam);
        // 遍历数据
        int no = 1;
        for (OrderHHTGridDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, formatDate(ls.getOrdDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getArticleName());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getQty());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getVendorId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getVendorName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, fmtStringIntDate(ls.getUploadTime()));


            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
    }
    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

    private String fmtStringIntDate(String date) {
        String year=date.substring(0,4);
        String month=date.substring(4,6);
        String day=date.substring(6,8);
        String hour=date.substring(8,10);
        String min=date.substring(10,12);
        String sec=date.substring(12,14);
        return day+"/"+month+"/"+year+" "+hour+":"+min+":"+sec;
    }
}
