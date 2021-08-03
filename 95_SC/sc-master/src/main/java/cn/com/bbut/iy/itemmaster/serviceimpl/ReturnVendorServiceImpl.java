package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.dao.ReturnVendorMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;

import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.*;

import cn.com.bbut.iy.itemmaster.service.ReturnVendorService;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValue;
import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValueNo;

/**
 * @author lz
 */
@Service
public class ReturnVendorServiceImpl implements ReturnVendorService {

    @Autowired
    private ReturnVendorMapper returnVendorMapper;

    @Autowired
    private OD0000Mapper od0000Mapper;

    @Override
    public GridDataDTO<RVListResult> getReturnVQueryList(RVListParamDTO param) {
        RVListParam rvListParam = new Gson().fromJson(param.getSearchJson(), RVListParam.class);
        rvListParam.setLimitStart(param.getLimitStart());
        rvListParam.setLimitEnd(param.getLimitEnd());
        rvListParam.setOrderByClause(param.getOrderByClause());
        rvListParam.setStores(param.getStores());
        List<RVListResult> list = returnVendorMapper.selectVQueryListBy(rvListParam);
        long count = returnVendorMapper.selectVQueryListCount(rvListParam);
        return new GridDataDTO<>(list, param.getPage(), count, param.getRows());
    }

    @Override
    public ReturnHeadResult getRVHeadQuery(String orderId) {
        return returnVendorMapper.selectVHeadByOrderId(orderId);
    }


    @Override
    public List<RVListResult> getReturnVPrintList(RVListParamDTO param) {
        RVListParam rvListParam = new Gson().fromJson(param.getSearchJson(), RVListParam.class);
        rvListParam.setOrderByClause(param.getOrderByClause());
        rvListParam.setStores(param.getStores());
        return od0000Mapper.selectVPrintList(rvListParam);
    }

    //    @Override
//    public SXSSFWorkbook getExcel(ReturnWarehouseParamDTO returnParamDTO, List<RWHItemsGridResult> itemList) {
//        // 声明一个工作簿
//        SXSSFWorkbook wb = new SXSSFWorkbook();
//        // 生成一个表格
//        SXSSFSheet sheet = wb.createSheet("STOCK_TAKE_RESULT");
//
//        // 生产excel内容
//        createExcelBody(sheet, wb, returnParamDTO, itemList);
//        return wb;
//    }
//
//    /**
//     * 生产excel内容
//     *
//     * @param sheet
//     */
//    private void createExcelBody(SXSSFSheet sheet, SXSSFWorkbook wb, ReturnWarehouseParamDTO returnParamDTO, List<RWHItemsGridResult> itemList) {
//
//        // 去除网格线
//        sheet.setDisplayGridlines(false);
//
//        // 大标题
//        String row0 = "GOODS RETURN NOTE";
//
//        // 二号标题
//
//        String row1 = "";
//
//        // 生成另一种字体4
//        Font font1 = wb.createFont();
//        // 设置字体
//        font1.setFontName("Microsoft JhengHei");
//        // 设置字体大小
//        font1.setFontHeightInPoints((short) 10);
//        // 生成另一种字体5  2021/07/22
//        Font font2 = wb.createFont();
//        // 设置字体
//        font2.setFontName("Arial");
//        // 设置字体大小
//        font2.setFontHeightInPoints((short) 11);
//        font2.setColor(IndexedColors.BLACK.index);
//        // 2021/7/22
//
//        Font font3= wb.createFont();
//        // 设置字体
//        font3.setFontName("Arial");
//        // 设置字体大小
//        font3.setFontHeightInPoints((short) 11);
//        font3.setColor(IndexedColors.BLACK.index);
//
//
//// 2021/7/22
//        // 生成并设置另一个样式
//        CellStyle style4 = wb.createCellStyle();
//        style4.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        style4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        style4.setAlignment(XSSFCellStyle.ALIGN_LEFT);
//        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//        style4.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
//        style4.setBorderLeft(CellStyle.BORDER_THIN);
//        style4.setBorderRight(CellStyle.BORDER_THIN);
//        style4.setBorderTop(CellStyle.BORDER_THIN);
//
//        // 在样式4中引用这种字体
//        style4.setFont(font1);
//    // 2021/7/22
//        // 生成并设置另一个样式
//        CellStyle style5= wb.createCellStyle();
//        style5.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        style5.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        style5.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//        style5.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
//        style5.setBorderLeft(CellStyle.BORDER_THIN);
//        style5.setBorderRight(CellStyle.BORDER_THIN);
//        style5.setBorderTop(CellStyle.BORDER_THIN);
//        style5.setFont(font2);
//        // 2021/7/22 Over
//        // 生成并设置另一个样式
//        CellStyle style6= wb.createCellStyle();
//        style6.setFillForegroundColor(IndexedColors.WHITE.getIndex());
//        style6.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
//        style6.setAlignment(XSSFCellStyle.ALIGN_LEFT);
//        style6.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
//        style6.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
//        style6.setBorderLeft(CellStyle.BORDER_THIN);
//        style6.setBorderRight(CellStyle.BORDER_THIN);
//        style6.setBorderTop(CellStyle.BORDER_THIN);
//        style6.setFont(font3);
//
//
//        // 2021/7/22 Over
//
//
//        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//        SXSSFRow row = sheet.createRow(0);
//        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        SXSSFCell titilecell = row.createCell(0);
//        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        CellRangeAddress cellRangeAddress1 = new CellRangeAddress(0, 0, 0, 2);
//        sheet.addMergedRegion(cellRangeAddress1);
//        // 设置单元格内容
//        titilecell.setCellValue("Store number :");
//        titilecell.setCellStyle(style6);
//        setRegionUtil(cellRangeAddress1, sheet, wb);
//
//
//        titilecell = row.createCell(3);
//        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 3, 5);
//        sheet.addMergedRegion(cellRangeAddress);
//        titilecell.setCellValue(returnParamDTO.getStoreCd());
//        titilecell.setCellStyle(style5);
//        setRegionUtil(cellRangeAddress, sheet, wb);
//
//        // 在sheet里创建第二行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//        SXSSFRow rowStoreName = sheet.createRow(1);
//        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        SXSSFCell cellStoreName = rowStoreName.createCell(0);
//        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        CellRangeAddress cellRangeAddress2 = new CellRangeAddress(1, 1, 0, 2);
//        sheet.addMergedRegion(cellRangeAddress2);
//        // 设置单元格内容
//        cellStoreName.setCellValue("Store Name :");
//
//        cellStoreName.setCellStyle(style6);
//        setRegionUtil(cellRangeAddress2, sheet, wb);
//
//        cellStoreName = rowStoreName.createCell(3);
//        CellRangeAddress cellRangeAddress3 = new CellRangeAddress(1, 1, 3, 5);
//        sheet.addMergedRegion(cellRangeAddress3);
//        cellStoreName.setCellValue(returnParamDTO.getStoreName());
//       cellStoreName.setCellStyle(style5);
//        setRegionUtil(cellRangeAddress3, sheet, wb);
//
//        // 在sheet里创建第三行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//        SXSSFRow rowDate = sheet.createRow(2);
//        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        SXSSFCell cellDate = rowDate.createCell(0);
//        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        CellRangeAddress cellRangeAddress4 = new CellRangeAddress(2, 2, 0, 2);
//        sheet.addMergedRegion(cellRangeAddress4);
//        // 设置单元格内容
//        cellDate.setCellValue("Date :");
//        cellDate.setCellStyle(style6);
//        setRegionUtil(cellRangeAddress4, sheet, wb);
//
//
//
//        cellDate = rowDate.createCell(3);
//        CellRangeAddress cellRangeAddress5 = new CellRangeAddress(2, 2, 3, 5);
//        sheet.addMergedRegion(cellRangeAddress5);
//        cellDate.setCellValue(returnParamDTO.getOrderDate());
//        cellDate.setCellStyle(style5);
//        setRegionUtil(cellRangeAddress5, sheet, wb);
//        // 在sheet里创建第四行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//
//        SXSSFRow rowDocument = sheet.createRow(3);
//        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        SXSSFCell cellDocument= rowDocument.createCell(0);
//        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        CellRangeAddress cellRangeAddress8 = new CellRangeAddress(3, 3, 0, 2);
//        sheet.addMergedRegion(cellRangeAddress8);
//        // 设置单元格内容
//        cellDocument.setCellValue("No :");
//        cellDocument.setCellStyle(style6);
//        setRegionUtil(cellRangeAddress8, sheet, wb);
//        cellDocument = rowDocument.createCell(3);
//        CellRangeAddress cellRangeAddress6 = new CellRangeAddress(3, 3, 3, 5);
//        sheet.addMergedRegion(cellRangeAddress6);
//        cellDocument.setCellValue(returnParamDTO.getOrderId());
//        cellDocument.setCellStyle(style5);
//
//        setRegionUtil(cellRangeAddress6, sheet, wb);
//        // 在sheet里创建第五行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
//
//        SXSSFRow Heightrow = sheet.createRow(4);
//        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
//        Cell Heightcell = Heightrow.createCell(0);
//        CellRangeAddress cellRangeAddress7 = new CellRangeAddress(4, 4, 0, 7);
//        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
//        sheet.addMergedRegion(cellRangeAddress7);
//        // 设置单元格内容
//        Heightcell.setCellValue(row0);
//        Heightcell.setCellStyle(titleStyle0(wb));
//        setRegionUtil(cellRangeAddress7, sheet, wb);
//        // 行号
//        int rowNum = 6;
//        // 第三行
//        row = sheet.createRow(rowNum++);
//        SXSSFCell cell = row.createCell(0);
//        row = sheet.createRow(rowNum++);
//        cell = row.createCell(0);
//        cell.setCellValue("No.");
//        cell.setCellStyle(style4);
//
//
//
//
//        cell = row.createCell(1);
//        cell.setCellValue("Item Code");
//        cell.setCellStyle(style4);
//
//
//
//        cell = row.createCell(2);
//        cell.setCellValue("Item Name");
//        cell.setCellStyle(style4);
//
//
//        cell = row.createCell(3);
//        cell.setCellValue("Standard Barcode");
//        cell.setCellStyle(style4);
//
//
//        cell = row.createCell(4);
//        cell.setCellValue("Unit");
//        cell.setCellStyle(style4);
//
//        cell = row.createCell(5);
//        cell.setCellValue("Spec");
//        cell.setCellStyle(style4);
//
//        cell = row.createCell(6);
//        cell.setCellValue("Return Qty");
//        cell.setCellStyle(style4);
//
//        cell = row.createCell(7);
//        cell.setCellValue("Return Reason");
//        cell.setCellStyle(style4);
//
//
//        int no = 1;
//        for (RWHItemsGridResult ls : itemList) {
//            int curCol = 0;
//            row = sheet.createRow(rowNum++);
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValueNo(cell, no++);
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getArticleId());
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getArticleName());
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getBarcode());
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getUnitName());
//
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getSpec());
//
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getOrderQty());
//
//
//            cell = row.createCell(curCol++);
//            cell.setCellStyle(style4);
//            setCellValue(cell, ls.getReasonName());
//        }
//        // 遍历数据
//        // 设置列宽
//        int columnIndex = 0;
//        sheet.setColumnWidth(columnIndex++, 5 * 256);
//        sheet.setColumnWidth(columnIndex++, 25 * 256);
//        sheet.setColumnWidth(columnIndex++, 20 * 256);
//        sheet.setColumnWidth(columnIndex++, 30 * 256);
//        sheet.setColumnWidth(columnIndex++, 20 * 256);
//        sheet.setColumnWidth(columnIndex++, 20 * 256);
//        sheet.setColumnWidth(columnIndex++, 20 * 256);
//        sheet.setColumnWidth(columnIndex++, 50 * 256);
//        sheet.setColumnWidth(columnIndex++, 50 * 256);
//    }
//
//    private CellStyle titleStyle0(Workbook wb) {
//        Font font = wb.createFont();
//        font.setFontHeightInPoints((short) 20);// 字号
//        font.setFontName(Constants.DEFAULT_FONT);// 字体设置（宋体）
//        font.setColor(IndexedColors.BLACK.index);// 字体颜色
//        font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体
//
//        CellStyle style = wb.createCellStyle();
//        style.setFont(font);// 设置字体样式
//        style.setAlignment((short) 2);// 单元格内容左右对其方式
//        style.setVerticalAlignment((short) 2);// 单元格内容上下对其方式
//        style.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格背景颜色
//        style.setFillPattern(CellStyle.SOLID_FOREGROUND);// 配合FillForegroundColor一起使用
//        style.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
//        style.setBorderLeft(CellStyle.BORDER_THIN);
//        style.setBorderRight(CellStyle.BORDER_THIN);
//        style.setBorderTop(CellStyle.BORDER_THIN);
//        style.setWrapText(false);// 文本是否自动换行
//        return style;
//    }
//
//    // 合并单元格的边框
//    private void setRegionUtil(CellRangeAddress region, Sheet sheet, Workbook wb) {
//        // 以下设置合并单元格的边框，避免边框不齐
//        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, wb);
//        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, wb);
//        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, wb);
//        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, wb);
//    }
}
