package cn.com.bbut.iy.itemmaster.serviceimpl.ma1105;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1105;
import cn.com.bbut.iy.itemmaster.excel.ExEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

/**
 * Excel标题设置
 *
 */
@Slf4j
public class POGExHeaderListener implements ExEventListener {

    Ma1105 param;

    public POGExHeaderListener(Ma1105 param) {
        this.param = param;
    }

    @Override
    public void action(Workbook wb) {
        Sheet sheet = wb.getSheet(Constants.EXCEL_SHEET_DEF_NAME);
        // 大标题
        String row0 = "POG Query List";

        // 二号标题
        String row1 = "";

        // 列标题
        String row2 = "NO.,Store No.,Store Name,Store Cluster,Store Group,Shelf,Sub-Shelf,Product Type,Location,Item Code,Product Name,Category,Sub Category," +
                "V. Facing,H. Facing,D. Facing,Total Facing,POG Name,Created Date";
        String[] titles = row2.split(",");

        // 开始拼接excel头信息
        int index = 0;
        CellRangeAddress region = null;

        Row row = sheet.createRow(index++);
        Cell cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
        region = new CellRangeAddress(0, 0, 0, (titles.length - 1));
        cell.setCellStyle(titleStyle0(wb));
        cell.setCellValue(row0);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        row = sheet.createRow(index++);
        cell = row.createCell(0, Cell.CELL_TYPE_STRING);
        // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
        region = new CellRangeAddress(1, 1, 0, (titles.length - 1));
        cell.setCellStyle(titleStyle1(wb));
        cell.setCellValue(row1);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        row = sheet.createRow(index++);
        int curCol = 0;
        for (String t : titles) {
            if (!t.equals("")) {
                setHeaderCell(wb, sheet, row, curCol, titleStyle2(wb), t);
                curCol++;
            }
        }
    }

    // 设置标题
    private void setHeaderCell(Workbook wb, Sheet sheet, Row row, int curCol, XSSFCellStyle style,
            String cellString) {
        Cell cell = row.createCell(curCol, Cell.CELL_TYPE_STRING);
        cell.setCellStyle(style);
        cell.setCellValue(cellString);
    }

    // 合并单元格的边框
    private void setRegionUtil(CellRangeAddress region, Sheet sheet, Workbook wb) {
        // 以下设置合并单元格的边框，避免边框不齐
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, wb);
    }

    /** 大标题样式 */
    private XSSFCellStyle titleStyle0(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);// 字号
        font.setFontName(Constants.DEFAULT_FONT);// 字体设置（宋体）
        font.setColor(IndexedColors.BLACK.index);// 字体颜色
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体

        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);// 设置字体样式
        style.setAlignment(HorizontalAlignment.CENTER);// 单元格内容左右对其方式
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        style.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格背景颜色
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);// 配合FillForegroundColor一起使用
        style.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setWrapText(false);// 文本是否自动换行
        return style;
    }

    /** 二号标题样式 */
    private XSSFCellStyle titleStyle1(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12);// 字号
        font.setFontName(Constants.DEFAULT_FONT);// 字体设置（宋体）
        font.setColor(IndexedColors.BLACK.index);// 字体颜色

        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);// 设置字体样式
        style.setAlignment(HorizontalAlignment.LEFT);// 单元格内容左右对其方式
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        style.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格背景颜色
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);// 配合FillForegroundColor一起使用
        style.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setWrapText(false);// 文本是否自动换行
        return style;
    }

    /** 三号标题样式 */
    private XSSFCellStyle titleStyle2(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11);// 字号
        font.setFontName(Constants.DEFAULT_FONT);// 字体设置（宋体）
        font.setColor(IndexedColors.BLACK.index);// 字体颜色

        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);// 设置字体样式
        style.setAlignment(HorizontalAlignment.CENTER);// 单元格内容左右对其方式
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        style.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格背景颜色
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);// 配合FillForegroundColor一起使用
        style.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setWrapText(false);// 文本是否自动换行
        return style;
    }
}
