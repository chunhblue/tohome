package cn.com.bbut.iy.itemmaster.serviceimpl.orderreport;

import cn.com.bbut.iy.itemmaster.dao.OrderReportMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportDTO;
import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.orderreport.OrderReportService;
import cn.com.bbut.iy.itemmaster.serviceimpl.goodsalereport.GoodSaleExHeaderListener;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * @ClassName OrderReportExServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/8/4 13:52
 * @Version 1.0
 */
@Slf4j
@Service(value = "orderReportExservice")
public class OrderReportExServiceImpl  implements ExService {
    @Autowired
    private OrderReportMapper orderReportMapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        OrderReportParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), OrderReportParamDTO.class);
        // 导出不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 生成文件标题信息对象
        session.setHeaderListener(new OrderReportExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, OrderReportParamDTO param) {
        if (param == null) {
            param = new OrderReportParamDTO();
        }
        List<OrderReportDTO> addItemList = new ArrayList<>();
        // 查询数据
        List<OrderReportDTO> items = orderReportMapper.getOrderInfo(param);
        for (OrderReportDTO item : items) {
            item.setArticleId(param.getArticleId());
            item.setArticleName(param.getArticleName());
            item.setDepCd(param.getDepCd());
            item.setCategoryCd(param.getCategoryCd());
            item.setSubCategoryCd(param.getSubCategoryCd());
            item.setAm(param.getAm());
            List<OrderReportDTO> addItem = orderReportMapper.getItemInfo(item);
            for (OrderReportDTO dtoam : addItem) {
                dtoam.setOrderDate(formatDate(item.getOrderDate()));
                addItemList.add(dtoam);
            }
        }
        for (OrderReportDTO dto : addItemList) {
            if (!dto.getAutoOrderQty().equals(new BigDecimal(0)) && !dto.getVarianceQty().equals(new BigDecimal(0))) {
                BigDecimal autoOrderQty = dto.getAutoOrderQty();
                BigDecimal varianceQty = dto.getVarianceQty();
                BigDecimal divide = autoOrderQty.divide(varianceQty);
                dto.setVariance(divide);
            } else {
                dto.setVariance(new BigDecimal(0));
            }
        }
        // 遍历数据
        int no = 1;
        for (OrderReportDTO ls : addItemList) {
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
            setCellValue(cell, ls.getOrderDate());


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
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getUom());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getAutoOrderQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, ls.getRealQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            if (!ls.getAutoOrderQty().equals(new BigDecimal(0)) && !ls.getVarianceQty().equals(new BigDecimal(0))) {
                BigDecimal autoOrderQty = ls.getAutoOrderQty();
                BigDecimal varianceQty = ls.getVarianceQty();
                BigDecimal divide = autoOrderQty.divide(varianceQty);
                ls.setVariance(divide);
            } else {
                ls.setVariance(new BigDecimal(0));
            }
            setCellValue(cell, Math.round(new Double(String.valueOf(ls.getVariance()))) + "%");

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, ls.getPsd());

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 40 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 10 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
//        sheet.setColumnWidth(columnIndex++, 15 * 256);
//        sheet.setColumnWidth(columnIndex++, 15 * 256);
//        sheet.setColumnWidth(columnIndex++, 15 * 256);
//        sheet.setColumnWidth(columnIndex++, 15 * 256);
//        sheet.setColumnWidth(columnIndex++, 15 * 256);
//        sheet.setColumnWidth(columnIndex++, 15 * 256);
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
}
