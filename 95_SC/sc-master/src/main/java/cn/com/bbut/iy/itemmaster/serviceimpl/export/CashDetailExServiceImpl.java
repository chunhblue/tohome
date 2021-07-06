package cn.com.bbut.iy.itemmaster.serviceimpl.export;

import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetail;
import cn.com.bbut.iy.itemmaster.dto.cash.CashDetailParam;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.CashService;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "slsfExService")
public class CashDetailExServiceImpl implements ExService {

    @Autowired
    private CashService cashService;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        CashDetailParam jsonParam = gson.fromJson(paramDTO.getParam(), CashDetailParam.class);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        /*int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setVStartDate(startDate);
        }*/
        // 生成文件标题信息对象
        session.setHeaderListener(new CashDetailExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, CashDetailParam jsonParam) {
        // 不分页
        jsonParam.setFlg(false);
        // 查询数据
        GridDataDTO<CashDetail> data = cashService.getCashDetailyList(jsonParam);
        List<CashDetail> _list = data.getRows();
        BigDecimal payInAmt0 = BigDecimal.ZERO,payInAmt1 = BigDecimal.ZERO,payInAmt2 = BigDecimal.ZERO,
                additional=BigDecimal.ZERO,offsetClaim=BigDecimal.ZERO;
        BigDecimal payAmt0 = BigDecimal.ZERO,payAmt1 = BigDecimal.ZERO,payAmt2 = BigDecimal.ZERO,payInAmt = BigDecimal.ZERO,
                payAmt = BigDecimal.ZERO,payAmtDiff = BigDecimal.ZERO;
        BigDecimal customerQty=BigDecimal.ZERO;
        // 遍历数据
        int no = 1;
        for (CashDetail ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell,ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOfc());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell,ls.getAmName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getPayDate()));


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getCustomerQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayInAmt0().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayInAmt1().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayInAmt2().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getAdditional().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getOffsetClaim().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayAmt().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayAmt0().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayAmt1().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayAmt2().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayAmtDiff().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, formatNum(ls.getPayInAmt().toString()));

            if(ls.getCustomerQty() != null){
                customerQty = customerQty.add(ls.getCustomerQty()) ;
            }

            if(ls.getPayInAmt0() != null){
                payInAmt0 = payInAmt0.add(ls.getPayInAmt0()) ;
            }
            if(ls.getPayInAmt1() != null){
                payInAmt1 = payInAmt1.add(ls.getPayInAmt1()) ;
            }
            if(ls.getPayInAmt2() != null){
                payInAmt2 = payInAmt2.add(ls.getPayInAmt2()) ;
            }
            if(ls.getAdditional() != null){
                additional = additional.add(ls.getAdditional()) ;
            }
            if(ls.getAdditional() != null){
                offsetClaim = offsetClaim.add(ls.getOffsetClaim()) ;
            }
            if(ls.getPayAmt() != null){
                payAmt = payAmt.add(ls.getPayAmt()) ;
            }
            if(ls.getPayAmt0() != null){
                payAmt0 = payAmt0.add(ls.getPayAmt0()) ;
            }
            if(ls.getPayAmt1() != null){
                payAmt1 = payAmt1.add(ls.getPayAmt1()) ;
            }
            if(ls.getPayAmt2() != null){
                payAmt2 = payAmt2.add(ls.getPayAmt2()) ;
            }
            if(ls.getPayAmtDiff() != null){
                payAmtDiff = payAmtDiff.add(ls.getPayAmtDiff()) ;
            }
            if(ls.getPayInAmt() != null){
                payInAmt = payInAmt.add(ls.getPayInAmt()) ;
            }
            curRow++;
        }
        int curCol = 0;
        Row row = sheet.createRow(curRow);
        Cell cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell,"");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell,"");


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, "Total:");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(customerQty.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payInAmt0.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payInAmt1.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payInAmt2.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(additional.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(offsetClaim.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payAmt.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payAmt0.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payAmt1.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payAmt2.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payAmtDiff.toString()));

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, formatNum(payInAmt.toString()));

        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
    }


}
