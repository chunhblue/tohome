package cn.com.bbut.iy.itemmaster.serviceimpl.bmhis;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dto.bmhis.BmHisJsonParamDTO;
import cn.com.bbut.iy.itemmaster.excel.ExEventListener;

/**
 * BM
 * 
 * @author songxz
 */
@Slf4j
public class BmHisExcelHeaderListener implements ExEventListener {
    BmHisJsonParamDTO param;

    public BmHisExcelHeaderListener(BmHisJsonParamDTO param) {
        this.param = param;
    }

    @Override
    public void action(Workbook wb) {
        Sheet sheet = wb.getSheet(Constants.EXCEL_SHEET_DEF_NAME);
        String row0 = "BM历史记录查询结果一览";

        String row1 = "";
        // 根据检索用的bm状态类确认格列的标题等
        row1 = "NO.,发起部门DPT,审核状态,"
                + "审核区分,修改标志,操作类型,操作人,操作人名称,操作时间,B/M商品分类,BM编码,采购员编码,采购员名称,销售开始日,销售结束日,BM折扣率,BM数量,"
                + "BM销售价格,店铺号,单品DPT,单品条码,单品名称,单品售价,单品折扣售价,单品毛利率,A组数量,B组数量,单品确认状态,驳回原因";
        String[] titles = row1.split(",");
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
        int curCol = 0;
        for (String t : titles) {
            if (!t.equals("")) {
                setHeaderCell(wb, sheet, row, curCol, titleStyle2(wb), t);
                curCol++;
            }
        }

    }

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
        font.setFontHeightInPoints((short) 18);// 字号
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
        font.setFontHeightInPoints((short) 9);// 字号
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

    /** 二号标题样式 */
    private XSSFCellStyle titleStyle2(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 9);// 字号
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
