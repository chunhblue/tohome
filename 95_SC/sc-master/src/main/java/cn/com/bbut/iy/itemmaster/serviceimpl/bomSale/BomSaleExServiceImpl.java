package cn.com.bbut.iy.itemmaster.serviceimpl.bomSale;

import cn.com.bbut.iy.itemmaster.dao.BomSaleMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;
import static cn.com.bbut.iy.itemmaster.util.CommonUtils.formatNum;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "bomSaleExService")
public class BomSaleExServiceImpl implements ExService {

    @Autowired
    private BomSaleMapper bomSaleMapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        BomSaleParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), BomSaleParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        /*int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setSaleStartDate(startDate);
        }*/
        // 获取业务日期
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        // 生成文件标题信息对象
        session.setHeaderListener(new BomSaleExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, BomSaleParamDTO jsonParam) {
        // 查询数据
        List<BomSaleDTO> _list = bomSaleMapper.selectListByCondition(jsonParam);
        // 遍历数据
        BigDecimal TotalSaleQty=BigDecimal.ZERO;
        BigDecimal TotalSaleAmt=BigDecimal.ZERO;
        int no = 1;
        for (BomSaleDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getAccDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getArticleName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getSalesUnit());

            String saleQtyT = String.valueOf(ls.getSaleQtyT());
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(saleQtyT));
            BigDecimal saleQtyT1 = ls.getSaleQtyT();
            TotalSaleQty=TotalSaleQty.add(saleQtyT1);

            String saleAmtT = String.valueOf(ls.getSaleAmtT());
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, formatNum(saleAmtT));
            BigDecimal saleAmtT1 = ls.getSaleAmtT();
            TotalSaleAmt=TotalSaleAmt.add(saleAmtT1);
//            String saleTaxT = String.valueOf(ls.getSaleTaxT());
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
//            setCellValue(cell, formatNum(saleTaxT));
//
//            String returnQtyT = String.valueOf(ls.getReturnQtyT());
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
//            setCellValue(cell, formatNum(returnQtyT));
//
//            String returnAmtT = String.valueOf(ls.getReturnAmtT());
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
//            setCellValue(cell, formatNum(returnAmtT));
//
//            String returnTaxT = String.valueOf(ls.getReturnTaxT());
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
//            setCellValue(cell, formatNum(returnTaxT));

           /* String grossMargin = String.valueOf(ls.getGrossMargin());
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, formatNum(grossMargin));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_6));
            setCellValue(cell, ls.getGrossMarginRate());

            String avgCostNoTax = String.valueOf(ls.getAvgCostNoTax());
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, formatNum(avgCostNoTax));*/

            curRow++;
        }
        Row row = sheet.createRow(curRow);
        int curCol = 0;
        Cell cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValueNo(cell, no++);

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, " ");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Total:");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, " ");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, " ");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, " ");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, " ");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, " ");


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(TotalSaleQty.toString()));


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
        setCellValue(cell, formatNum(TotalSaleAmt.toString()));



        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 14 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 19 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        /*sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);*/
    }

}
