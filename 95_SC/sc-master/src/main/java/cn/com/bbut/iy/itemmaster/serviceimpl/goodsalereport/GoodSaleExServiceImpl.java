package cn.com.bbut.iy.itemmaster.serviceimpl.goodsalereport;

import cn.com.bbut.iy.itemmaster.dao.goodsalereport.GoodsSaleReportMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportParamDTO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "saleDailyExService")
public class GoodSaleExServiceImpl implements ExService {

    @Autowired
    private GoodsSaleReportMapper mapper;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        goodSaleReportParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), goodSaleReportParamDTO.class);
        // 导出不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setStartDate(startDate);
        }
        // 生成文件标题信息对象
        session.setHeaderListener(new GoodSaleExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, goodSaleReportParamDTO jsonParam) {
        // 查询数据
        jsonParam.setFlg(false);
        List<goodSaleReportDTO> _list = mapper.search(jsonParam);
        int itemSKU=mapper.getArticleCount(jsonParam);
        BigDecimal sumSaleAmount=BigDecimal.ZERO;
        BigDecimal sumSaleQty=BigDecimal.ZERO;
        // 遍历数据
        int no = 1;
        for (goodSaleReportDTO ls : _list) {
            sumSaleAmount=sumSaleAmount.add(new BigDecimal(ls.getSaleAmount()));
            sumSaleQty=sumSaleQty.add(new BigDecimal(ls.getSaleQty()));
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

            String saleDate = ls.getSaleDate();
            if (!StringUtils.isEmpty(saleDate)) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(saleDate);
                    saleDate=new SimpleDateFormat("dd/MM/yyyy").format(date);
                } catch (ParseException e) {
                    saleDate = "";
                    e.printStackTrace();
                }
            }
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, saleDate);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getDepName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPmaName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getCategoryName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getSubCategoryName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getArticleName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getSaleQty()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPriceActual()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getAmCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getAmName());

            curRow++;
        }
        int curCol = 0;
        Row row = sheet.createRow(curRow);
        Cell cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
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
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, "Total item Qty:");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, itemSKU+"");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, "Total Sale Amount:");


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(sumSaleAmount.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, "Total Sale Qty:");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(sumSaleQty.toString()));

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
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 40 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
    }

}
