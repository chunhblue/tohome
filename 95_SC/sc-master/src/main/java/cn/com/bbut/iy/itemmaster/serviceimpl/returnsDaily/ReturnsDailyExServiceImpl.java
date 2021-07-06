package cn.com.bbut.iy.itemmaster.serviceimpl.returnsDaily;

import cn.com.bbut.iy.itemmaster.dao.ReturnsDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.returnsDaily.ReturnsDailyService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "customerReturnExService")
public class ReturnsDailyExServiceImpl implements ExService {

    @Autowired
    private ReturnsDailyMapper mapper;
    @Autowired
    private ReturnsDailyService service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        ReturnsDailyParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), ReturnsDailyParamDTO.class);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 生成文件标题信息对象
        session.setHeaderListener(new ReturnsDailyExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, ReturnsDailyParamDTO jsonParam) {
        // 查询数据
        Map<String,Object> map = service.getPrintDate(jsonParam);
        List<ReturnsDailyDTO> _list = new ArrayList<>();
        for(Map.Entry<String,Object> entry : map.entrySet()) {
            if ("data".equals(entry.getKey())) {
                Object list = entry.getValue();
                _list = (List<ReturnsDailyDTO>) list;
            }
        }
        BigDecimal totalQty = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        jsonParam.setFlg(false);
        ReturnsDailyDTO getItemTotal = mapper.getItemTotal(jsonParam);
        if(getItemTotal!=null){
            totalQty = getItemTotal.getTotalQty();
            totalAmount = getItemTotal.getTotalAmt();
        }

        // 遍历数据
        int no = 1;
        for (ReturnsDailyDTO ls : _list) {

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
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getArticleName());

            /*cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getBarcode());*/

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateAndTimeToStr19(ls.getTranDate()));


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getPosId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getTranSerialNo());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getNonSaleType());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, Integer.parseInt(ls.getOrderQty()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getTotalAmt());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getCashierId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getCashierName());

//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
//            setCellValue(cell, ls.getAmCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getAmName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getMode());

            curRow++;
        }
        for (int i=0;i<_list.size();i++){

            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, "");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,"");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, "");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
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

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, "");

            /*cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, "");*/

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, "Total:");


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,  totalQty);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,  totalAmount);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,   "");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,   "");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,  "");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell,  "");

        }

        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
//        sheet.setColumnWidth(columnIndex++, 45 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
    }

}
