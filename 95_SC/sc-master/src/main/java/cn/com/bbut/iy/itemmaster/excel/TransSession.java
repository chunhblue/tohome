/**
 * ClassName  TransSession
 *
 * History
 * Create User: Shiy
 * Create Date: 2013-2-4
 * Update User:
 * Update Date:
 */
package cn.com.bbut.iy.itemmaster.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

/**
 * 用于文档转换控制，每次转换使用独立的instance <br>
 * 生命周期：
 * <ol>
 * <li>createWorkBook</li>
 * <li>如果存在initListener，执行</li>
 * <li>createSheet</li>
 * <li>如果存在headerListener，执行，此时可设置header行、设置格式</li>
 * <li>setCellVaule</li>
 * <li>saveTo 包含dispose</li>
 * <li>dispose</li>
 * <ol>
 * 
 * @author Shiy
 */
@Component
public class TransSession {
    private static final int DEFAULT_WINDOWSIZE = 100;

    @Getter
    private Workbook wb;

    @Getter
    private Sheet curSheet;

    @Setter
    @Getter
    private ExEventListener initListener;

    @Setter
    @Getter
    private ExEventListener headerListener;

    @Setter
    @Getter
    private ExEventListener endListener;

    /**
     * 创建workbook
     * 
     * @param rowAccessWindowSize
     *            缓存在内存中的行数，达到此行数后会将数据写到临时文件
     */
    public void createWorkBook(int rowAccessWindowSize) {
        createSSWorkBook(rowAccessWindowSize);
    }

    /**
     * 创建workbook 默认内存保存数据行数100，超过此行数数据写到临时文件
     */
    public void createWorkBook() {
        createSSWorkBook(DEFAULT_WINDOWSIZE);
    }

    private void createSSWorkBook(int rowAccessWindowSize) {
        wb = new SXSSFWorkbook(rowAccessWindowSize);
        if (initListener != null) {
            initListener.action(wb);
        }
    }

    /**
     * 创建sheet，name为默认值
     */
    public void createSheet() {
        curSheet = wb.createSheet();
        onHeader();
    }

    /**
     * 创建sheet
     * 
     * @param sheetName
     *            指定的sheet名
     */
    public void createSheet(String sheetName) {
        curSheet = wb.createSheet(sheetName);
        onHeader();
    }

    private void onHeader() {
        if (this.headerListener != null) {
            headerListener.action(wb);
        }
    }

    public void setCellVaule(int rownum, int colnum, String value) {
        Row row = curSheet.getRow(rownum);
        if (curSheet.getRow(rownum) == null) {
            row = curSheet.createRow(rownum);
        }
        Cell cell = row.getCell(colnum);
        if (cell == null) {
            cell = row.createCell(colnum);
        }
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
    }

    public void setCellVaule(int rownum, int colnum, Date value) {
        Row row = curSheet.getRow(rownum);
        if (curSheet.getRow(rownum) == null) {
            row = curSheet.createRow(rownum);
        }
        Cell cell = row.getCell(colnum);
        if (cell == null) {
            cell = row.createCell(colnum);
        }
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        CellStyle style = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        style.setDataFormat(df.getFormat("yyyy/MM/dd"));
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public void setCellVaule(int rownum, int colnum, Double value) {
        Row row = curSheet.getRow(rownum);
        if (curSheet.getRow(rownum) == null) {
            row = curSheet.createRow(rownum);
        }
        Cell cell = row.getCell(colnum);
        if (cell == null) {
            cell = row.createCell(colnum);
        }
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        cell.setCellValue(value);
    }

    public void setCellVaule(int rownum, int colnum, Calendar value) {
        Row row = curSheet.getRow(rownum);
        if (curSheet.getRow(rownum) == null) {
            row = curSheet.createRow(rownum);
        }
        Cell cell = row.getCell(colnum);
        if (cell == null) {
            cell = row.createCell(colnum);
        }
        CellStyle style = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        style.setDataFormat(df.getFormat("yyyy/MM/dd"));
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    public void saveTo(OutputStream out) throws IOException {
        if (endListener != null) {
            endListener.action(wb);
        }
        wb.write(out);
        dispose();
    }

    public void dispose() {
        if (wb instanceof SXSSFWorkbook) {
            ((SXSSFWorkbook) wb).dispose();
        }
    }

}
