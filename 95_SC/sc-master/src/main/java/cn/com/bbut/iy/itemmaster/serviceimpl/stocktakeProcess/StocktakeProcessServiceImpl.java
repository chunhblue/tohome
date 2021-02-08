package cn.com.bbut.iy.itemmaster.serviceimpl.stocktakeProcess;

import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakeProcessMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.*;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MRoleStoreService;
import cn.com.bbut.iy.itemmaster.service.stocktakeProcess.StocktakeProcessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class StocktakeProcessServiceImpl implements StocktakeProcessService {

    @Autowired
    private StocktakeProcessMapper stocktakeProcessMapper;
    @Autowired
    private MRoleStoreService mRoleStoreService;
    @Autowired
    private StocktakePlanMapper stocktakePlanMapper;
    @Autowired
    private CM9060Service cm9060Service;

    /**
     * 格式化日期
     * @param piDate
     * @return
     */
    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

    /**
     * 格式化时间 2020 07 04 17 05 16
     *
     * @param piTime
     * @return
     */
    private String formatTime(String piTime) {
        if (StringUtils.isEmpty(piTime)) {
            return "";
        }
        String hh = piTime.substring(0, 2);
        String mm = piTime.substring(2, 4);
        String ss = piTime.substring(4, 6);
        return hh + ":" + mm + ":" + ss;
    }

    // 查询数据
    @Override
    public GridDataDTO<StocktakeProcessDTO> search(PI0100ParamDTO pi0100) {
        int count = stocktakeProcessMapper.searchCount(pi0100);
        List<StocktakeProcessDTO> result = stocktakeProcessMapper.search(pi0100);
        for (StocktakeProcessDTO stock : result) {
            stock.setPiDate(formatDate(stock.getPiDate()));
            stock.setPiStartTime(formatTime(stock.getPiStartTime()));
            stock.setPiEndTime(formatTime(stock.getPiEndTime()));
        }
        return new GridDataDTO<>(result,pi0100.getPage() , count,pi0100.getRows());
    }

    // 获取头档信息
    @Override
    public PI0100DTO getData(String piCd, String piDate) {
        if (piCd==null || StringUtils.isEmpty(piCd) ||
                piDate==null || StringUtils.isEmpty(piDate)) {
            return null;
        }
        PI0100DTO result = stocktakePlanMapper.getPI0100ByPrimary(piCd, piDate);
        if (result!=null&&result.getPiDate()!=null) {
            result.setPiDate(formatDate(result.getPiDate()));
            result.setPiStartTime(formatTime(result.getPiStartTime()));
            result.setPiEndTime(formatTime(result.getPiEndTime()));
        }
        return result;
    }

    /**
     * 获得正常盘点的商品
     */
    @Override
    public GridDataDTO<StocktakeProcessItemsDTO> getTableData1(String piCd, String piDate,
                                                               String storeCd, String searchVal,
                                                               Integer startQty, Integer endQty,
                                                               Integer startAmt, Integer endAmt,
                                                               String sidx, String sord,
                                                               int page, int rows) {
        String businessDate = cm9060Service.getValByKey("0000");
        int count = stocktakeProcessMapper.getTableData1Count(piCd,piDate,storeCd,searchVal,businessDate,startQty,endQty,startAmt,endAmt);
        int limit = (page - 1)*rows;
        List<StocktakeProcessItemsDTO> result = stocktakeProcessMapper.getTableData1(piCd,piDate,storeCd,searchVal,businessDate,startQty,endQty,startAmt,endAmt,sidx,sord,page,rows,limit);
        for (StocktakeProcessItemsDTO item : result) {
            if (item.getBaseSalePrice()!=null&& !StringUtils.isEmpty(item.getVariance())) {
                BigDecimal varianceAmt = item.getBaseSalePrice().multiply(new BigDecimal(item.getVariance()));
                item.setVarianceAmt(String.valueOf(varianceAmt));
            }
        }
        return new GridDataDTO<StocktakeProcessItemsDTO>(result,page , count,rows);
    }

    /**
     *  获得未盘到的商品
     */
    @Override
    public GridDataDTO<StocktakeProcessItemsDTO> getTableData2(String piCd, String piDate,
                                                               String storeCd, String searchVal,
                                                               Integer startQty, Integer endQty,
                                                               Integer startAmt, Integer endAmt,
                                                               String sidx, String sord,
                                                               int page, int rows) {
        String businessDate = cm9060Service.getValByKey("0000");
        int count = stocktakeProcessMapper.getTableData2Count(piCd,piDate,storeCd,searchVal,businessDate,startQty,endQty,startAmt,endAmt);
        int limit = (page - 1)*rows;
        List<StocktakeProcessItemsDTO> result = stocktakeProcessMapper.getTableData2(piCd,piDate,storeCd,searchVal,businessDate,startQty,endQty,startAmt,endAmt,sidx,sord,page,rows,limit);
        for (StocktakeProcessItemsDTO item : result) {
            if (item.getBaseSalePrice()!=null&& !StringUtils.isEmpty(item.getVariance())) {
                BigDecimal varianceAmt = item.getBaseSalePrice().multiply(new BigDecimal(item.getVariance()));
                item.setVarianceAmt(String.valueOf(varianceAmt));
            }
        }
        return new GridDataDTO<StocktakeProcessItemsDTO>(result,page , count,rows);
    }

    /**
     *  获得账面上无库存的商品
     */
    @Override
    public GridDataDTO<StocktakeProcessItemsDTO> getTableData3(String piCd, String piDate,
                                                               String storeCd, String searchVal,
                                                               Integer startQty, Integer endQty,
                                                               Integer startAmt, Integer endAmt,
                                                               String sidx, String sord,
                                                               int page, int rows) {
        String businessDate = cm9060Service.getValByKey("0000");
        int count = stocktakeProcessMapper.getTableData3Count(piCd,piDate,storeCd,searchVal,businessDate,startQty,endQty,startAmt,endAmt);
        int limit = (page - 1)*rows;
        List<StocktakeProcessItemsDTO> result = stocktakeProcessMapper.getTableData3(piCd,piDate,storeCd,searchVal,businessDate,startQty,endQty,startAmt,endAmt,sidx,sord,page,rows,limit);
        for (StocktakeProcessItemsDTO item : result) {
            if (item.getBaseSalePrice()!=null&& !StringUtils.isEmpty(item.getVariance())) {
                BigDecimal varianceAmt = item.getBaseSalePrice().multiply(new BigDecimal(item.getVariance()));
                item.setVarianceAmt(String.valueOf(varianceAmt));
            }
        }
        return new GridDataDTO<>(result,page , count,rows);
    }

    @Override
    public List<StocktakeProcessDTO> getPrintData(PI0100ParamDTO pi0100) {
        List<StocktakeProcessDTO> result = stocktakeProcessMapper.search(pi0100);
        for (StocktakeProcessDTO stock : result) {
            stock.setPiDate(formatDate(stock.getPiDate()));
            stock.setPiStartTime(formatTime(stock.getPiStartTime()));
            stock.setPiEndTime(formatTime(stock.getPiEndTime()));
        }
        return result;
    }

    /**
     * 生成excel
     */
    @Override
    public SXSSFWorkbook getExportHSSFWorkbook(String piCd, String piDate, String storeCd) {
        // 声明一个工作簿
        SXSSFWorkbook wb = new SXSSFWorkbook();
        // 生成一个表格
        SXSSFSheet sheet1 = wb.createSheet("Physical Inventory Result (P1)");
        // 创建第一个sheet
        this.createSheet1(piCd,piDate,storeCd,sheet1,wb);
        // 生成一个表格
        SXSSFSheet sheet2 = wb.createSheet("Physical Inventory Result (P2)");
        // 创建第二个sheet
        this.createSheet2(piCd,piDate,storeCd,sheet2,wb);
        // 生成一个表格
        SXSSFSheet sheet3 = wb.createSheet("IV Analysis");
        // 创建第三个sheet
        this.createSheet3(piCd,piDate,storeCd,sheet3,wb);
        // 生成一个表格
        SXSSFSheet sheet4 = wb.createSheet("IV by Department");
        // 创建第四个sheet
        try {
            this.createSheet4(piCd,piDate,storeCd,sheet4,wb);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 生成一个表格
        SXSSFSheet sheet5 = wb.createSheet("Bad Merchandise Report");
        // 创建第三个sheet
        this.createSheet5(piCd,piDate,storeCd,sheet5,wb);

        return wb;
    }

    private void createSheet5(String piCd, String piDate, String storeCd, SXSSFSheet sheet, SXSSFWorkbook wb) {
        // 缩放 70%
        sheet.setZoom(70);

        // 生成并设置另一个样式
        CellStyle style0 = wb.createCellStyle();
        style0.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style0.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style0.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style0.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        sheet.setDefaultColumnStyle(7,style0);

        // 生成并设置另一个样式
        CellStyle style1 = wb.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font font1 = wb.createFont();
        // 设置字体
        font1.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font1.setFontHeightInPoints((short) 10);
        // 在样式4中引用这种字体
        style1.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style2.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style3 = wb.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.RED.getIndex());
        style3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style3.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style3.setFont(font1);

        // 生成并设置另一个样式
        DataFormat format = wb.createDataFormat();
        CellStyle style4 = wb.createCellStyle();
        style4.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style4.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style4.setDataFormat(format.getFormat("dd/MM/yyyy"));

        // 在样式4中引用这种字体
        style4.setFont(font1);

        // 设置列宽
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 50 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 3 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 15 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 15 * 256);
        sheet.setColumnWidth(14, 30 * 256);

        // 行号
        int rowNum = 0;

        // title 行
        SXSSFRow row = sheet.createRow(rowNum++);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 14));
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue("Picture of Bad Merchandise");
        cell.setCellStyle(style1);

        // 第二行结果总数
        row = sheet.createRow(rowNum++);

        cell = row.createCell(0);
        cell.setCellValue("Bad Merchandise Result");
        cell.setCellStyle(style3);

        cell = row.createCell(1);
        cell.setCellValue("StoreID");

        cell = row.createCell(2);
        cell.setCellValue(storeCd);

        cell = row.createCell(3);
        cell.setCellValue("Count date");

        cell = row.createCell(4);
        cell.setCellStyle(style4);
        cell.setCellValue(getDateEmpty(piDate));

        // 第三行商品 参数
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("No.");

        cell = row.createCell(1);
        cell.setCellValue("ItemCode");

        cell = row.createCell(2);
        cell.setCellValue("BarCode");

        cell = row.createCell(3);
        cell.setCellValue("ItemName");

        cell = row.createCell(4);
        cell.setCellValue("UOM");

        cell = row.createCell(5);
        cell.setCellValue("Quantity");

        // 绑定不良商品信息
        String businessDate = cm9060Service.getValByKey("0000");
        List<StocktakeProcessItemsDTO> list = stocktakeProcessMapper.getBadMerchandisingList(piCd,piDate,storeCd,businessDate);
        for (int i = 0; i < list.size(); i++) {

            StocktakeProcessItemsDTO item = list.get(i);
            // 第四行商品假数据
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellValue(i+1);

            cell = row.createCell(1);
            cell.setCellValue(item.getArticleId());

            cell = row.createCell(2);
            cell.setCellValue(item.getBarcode());

            cell = row.createCell(3);
            cell.setCellValue(item.getArticleName());

            cell = row.createCell(4);
            cell.setCellValue(item.getUom());

            cell = row.createCell(5);
            cell.setCellValue(item.getBadQty());
        }
    }

    private void createSheet4(String piCd, String piDate, String storeCd,SXSSFSheet sheet, SXSSFWorkbook wb) throws ParseException {
        // 行数
        int rowNum = 0;

        DataFormat format = wb.createDataFormat();
        // 生成并设置另一个样式
        CellStyle style1 = wb.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font font1 = wb.createFont();
        // 设置字体
        font1.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font1.setFontHeightInPoints((short) 10);
        // 在样式4中引用这种字体
        style1.setFont(font1);

        // 生成并设置另一个样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderTop(CellStyle.BORDER_THIN);
        titleStyle.setBorderRight(CellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setDataFormat(format.getFormat("MMM-yy"));
        // 生成另一种字体4
        Font titleFont = wb.createFont();
        // 设置字体
        titleFont.setFontName("Microsoft JhengHei");
        // 设置字体大小
        titleFont.setFontHeightInPoints((short) 10);
        titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        titleStyle.setFont(titleFont);

        // 生成并设置另一个样式
        CellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderTop(CellStyle.BORDER_THIN);
        style2.setBorderRight(CellStyle.BORDER_THIN);
        style2.setBorderLeft(CellStyle.BORDER_THIN);
        style2.setBorderBottom(CellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style2.setDataFormat(format.getFormat("#,##0"));
        // 生成另一种字体4
        Font font2 = wb.createFont();
        // 设置字体
        font2.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font2.setFontHeightInPoints((short) 10);
        // 在样式4中引用这种字体
        style2.setFont(font2);

        // 生成并设置另一个样式
        CellStyle style3 = wb.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderTop(CellStyle.BORDER_THIN);
        style3.setBorderRight(CellStyle.BORDER_THIN);
        style3.setBorderLeft(CellStyle.BORDER_THIN);
        style3.setBorderBottom(CellStyle.BORDER_THIN);
        style3.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style3.setDataFormat(format.getFormat("#,##0"));
        // 生成另一种字体4
        Font font3 = wb.createFont();
        // 设置字体
        font3.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font3.setFontHeightInPoints((short) 10);
        // 粗体
        font3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style3.setFont(font3);

        // 设置列宽
        sheet.setColumnWidth(0, 15 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 15 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 15 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 15 * 256);

        // 第一行, store code
        SXSSFRow row = sheet.createRow(rowNum++);
        SXSSFCell cell = row.createCell(1);
        cell.setCellValue(storeCd);

        // 跳过一行
        rowNum++;

        // title 行
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellStyle(titleStyle);
        cell.setCellValue("Row Labels");

        // 获取 过去六次的盘点结果, by Department
        List<List<StocktakeReportByDepDTO>> result = this.getVarianceByDep(piCd,piDate,storeCd);

        // 遍历标题
        for (int i = 0; i < result.size(); i++) {
            // 遍历标题 获得每组的第一个, 取得日期就可以了
            StocktakeReportByDepDTO item = result.get(i).get(0);
            cell = row.createCell(i+1);
            cell.setCellStyle(titleStyle);
            cell.setCellValue(getDateEmpty(item.getPiDate()));
        }

        List<String> pmaNames = new ArrayList<String>();
        for (int i = 0; i < result.size(); i++) {
            for (int j = 0; j < result.get(i).size(); j++) {
                StocktakeReportByDepDTO item = result.get(i).get(j);
                if(!pmaNames.contains(item.getPmaName())){
                    pmaNames.add(item.getPmaName());
                }
            }
        }

        for (int i = 0; i < pmaNames.size(); i++) {
            row = sheet.createRow(rowNum++);
            String pmaName = pmaNames.get(i);
            cell = row.createCell(0);
            cell.setCellStyle(style2);
            cell.setCellValue(pmaName);
            for (int j = 0; j < result.size(); j++){
                cell = row.createCell(j+1);
                cell.setCellStyle(style2);
                for (int k = 0; k < result.get(j).size(); k++) {
                    StocktakeReportByDepDTO item = result.get(j).get(k);
                    if(item.getPmaName().equals(pmaName)){
                        cell.setCellValue(getNumber0(item.getVarianceAmt()));
                    }
                }
            }
        }

        List<Long> amts = new ArrayList<Long>();
        // 遍历获得 total 数据
        for (int i = 0; i < result.size(); i++) {
            Long totalVarianceAmt = 0L;
            List<StocktakeReportByDepDTO> item = result.get(i);
            for (int j = 0; j < item.size(); j++) {
                totalVarianceAmt += getNumber0(item.get(j).getVarianceAmt());
            }
            amts.add(totalVarianceAmt);
        }

        // total 行数据
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellStyle(style3);
        cell.setCellValue("Grand Total");
        // 遍历设置total 行
        for (int i = 0; i < amts.size(); i++) {
            cell = row.createCell(i+1);
            cell.setCellStyle(style3);
            cell.setCellValue(amts.get(i));
        }
    }

    private void createSheet3(String piCd, String piDate, String storeCd,SXSSFSheet sheet, SXSSFWorkbook wb) {
        // 去除网格线
        sheet.setDisplayGridlines(false);
        // 缩放比例
        sheet.setZoom(85);
        DataFormat format = wb.createDataFormat();
        // 生成并设置另一个样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font titleFont = wb.createFont();
        // 设置字体
        titleFont.setFontName("Microsoft JhengHei");
        // 设置字体大小
        titleFont.setFontHeightInPoints((short) 20);
        // 设置字体颜色
        titleFont.setColor(IndexedColors.RED.getIndex());
        // 在样式4中引用这种字体
        titleStyle.setFont(titleFont);

        // 生成并设置另一个样式
        CellStyle style1 = wb.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        style1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style1.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font font1 = wb.createFont();
        // 设置字体
        font1.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font1.setFontHeightInPoints((short) 10);
        // 在样式4中引用这种字体
        style1.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style2.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style3 = wb.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.DARK_YELLOW.getIndex());
        style3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style3.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style4 = wb.createCellStyle();
        style4.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style4.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style4.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style4.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style4.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style5 = wb.createCellStyle();
        style5.setFillForegroundColor(IndexedColors.TAN.getIndex());
        style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style5.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style5.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style5.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style5.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style5.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style6 = wb.createCellStyle();
        style6.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        style6.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style6.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style6.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style6.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style6.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style6.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style6.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font font6 = wb.createFont();
        // 设置字体
        font6.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font6.setFontHeightInPoints((short) 15);
        // 粗体
        font6.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

        // 在样式4中引用这种字体
        style6.setFont(font6);

        // 生成并设置另一个样式
        CellStyle style7 = wb.createCellStyle();
        style7.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style7.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style7.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style7.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        style7.setWrapText(true);
        // 生成另一种字体4
        Font font7 = wb.createFont();
        // 设置字体
        font7.setFontName("宋体");
        // 设置字体大小
        font7.setFontHeightInPoints((short) 11);
        // 粗体
        font7.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

        // 在样式4中引用这种字体
        style7.setFont(font7);

        // 生成并设置另一个样式
        CellStyle style8 = wb.createCellStyle();
        style8.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style8.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style8.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style8.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        // style8.setWrapText(true);
        // 生成另一种字体4
        Font font8 = wb.createFont();
        // 设置字体
        font8.setFontName("宋体");
        // 设置字体大小
        font8.setFontHeightInPoints((short) 10);
        // 在样式4中引用这种字体
        style8.setFont(font8);

        // 生成并设置另一个样式
        CellStyle style9 = wb.createCellStyle();
        style9.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style9.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style9.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style9.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style9.setDataFormat(format.getFormat("#,##0"));
        // 在样式4中引用这种字体
        style9.setFont(font8);

        // 设置列宽
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 15 * 256);
        sheet.setColumnWidth(2, 20 * 256);
        sheet.setColumnWidth(3, 15 * 256);
        sheet.setColumnWidth(4, 15 * 256);
        sheet.setColumnWidth(5, 5 * 256);
        sheet.setColumnWidth(6, 15 * 256);
        sheet.setColumnWidth(7, 15 * 256);
        sheet.setColumnWidth(8, 15 * 256);
        sheet.setColumnWidth(9, 5 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 5 * 256);
        sheet.setColumnWidth(14, 30 * 256);

        // 行号
        int rowNum = 0;

        // title 行
        SXSSFRow row = sheet.createRow(rowNum++);
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue("GENERAL ANALYSIS MAP");
        cell.setCellStyle(titleStyle);

        // 获得 盘点 头档信息
        StocktakeReportDTO head = stocktakeProcessMapper.getStocktakingHead(piCd, piDate, storeCd);

        // store 信息
        SXSSFCell cell1 = row.createCell(3);
        cell1.setCellValue(getValEmpty(head.getStoreName()));
        cell1.setCellStyle(style1);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 3, 7));
        setRegionUtil(new CellRangeAddress(rowNum-1, rowNum-1, 3, 7),sheet,wb);

        // 跳过一行空白
        rowNum++;

        // 第二行 第一个 title 单元格
        row = sheet.createRow(rowNum++);
        row.setHeight((short) 600);
        SXSSFCell row2Cell = row.createCell(3);
        row2Cell.setCellValue("Goods Structure");
        row2Cell.setCellStyle(style2);

        // 第二行 第二个 title 单元格
        row2Cell = row.createCell(6);
        row2Cell.setCellValue("Inventory Variance Map (Qtt) UNIT");
        row2Cell.setCellStyle(style3);

        // 第二行 第三个 title 单元格
        row2Cell = row.createCell(10);
        row2Cell.setCellValue("Inventory Variance Map (Amt) 'VND");
        row2Cell.setCellStyle(style4);

        // 第二行 第四个 title 单元格
        row2Cell = row.createCell(14);
        row2Cell.setCellValue("Average COGs 'VND");
        row2Cell.setCellStyle(style5);

        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 3, 4));
        setRegionUtil(new CellRangeAddress(rowNum-1, rowNum-1, 3, 4),sheet,wb);
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 6, 8));
        setRegionUtil(new CellRangeAddress(rowNum-1, rowNum-1, 6, 8),sheet,wb);
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 10, 12));
        setRegionUtil(new CellRangeAddress(rowNum-1, rowNum-1, 10, 12),sheet,wb);

        // 第三行行 第一个 栏位 category name
        row = sheet.createRow(rowNum++);
        // row.setHeight((short) 600);
        SXSSFCell row3Cell = row.createCell(0);
        row3Cell.setCellValue("Category Name");
        row3Cell.setCellStyle(style6);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(rowNum-1, rowNum-1, 0, 1));
        setRegionUtil(new CellRangeAddress(rowNum-1, rowNum-1, 0, 1),sheet,wb);

        row3Cell = row.createCell(3);
        row3Cell.setCellValue("Category Allocation");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(4);
        row3Cell.setCellValue("Category Quantity");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(6);
        row3Cell.setCellValue("Overage");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(7);
        row3Cell.setCellValue("Shortage");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(8);
        row3Cell.setCellValue("Total IV");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(10);
        row3Cell.setCellValue("Overage");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(11);
        row3Cell.setCellValue("Shortage");
        row3Cell.setCellStyle(style7);

        row3Cell = row.createCell(12);
        row3Cell.setCellValue("Total IV");
        row3Cell.setCellStyle(style7);

        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellStyle(style8);
        cell.setCellValue("Row Labels");

        String businessDate = cm9060Service.getValByKey("0000");
        // 根据分类获得 商品分类下的 种类数量 和库存数量
        List<StocktakeReportVarianceDTO> totalList = stocktakeProcessMapper.getVarianceByCategory(piCd,piDate,storeCd,businessDate,null);
        // 根据分类获得 缺少商品的差异数据
        List<StocktakeReportVarianceDTO> ltList = stocktakeProcessMapper.getVarianceByCategory(piCd,piDate,storeCd,businessDate,"LT");
        // 根据分类获得 多出商品的差异数据
        List<StocktakeReportVarianceDTO> gtList = stocktakeProcessMapper.getVarianceByCategory(piCd, piDate, storeCd, businessDate, "GT");

        // 记录有差异的 category
        List<String> categorys = new ArrayList<String>();
        // 遍历缺少的 category有哪些
        for (int i = 0; i < ltList.size(); i++) {
            String categoryName = ltList.get(i).getCategoryName();
            if (!categorys.contains(categoryName)) {
                categorys.add(categoryName);
            }
        }
        // 遍历多出的category有哪些
        for (int i = 0; i < gtList.size(); i++) {
            String categoryName = gtList.get(i).getCategoryName();
            if (!categorys.contains(categoryName)) {
                categorys.add(categoryName);
            }
        }

        // 绑定数据
        for (int i = 0; i < categorys.size(); i++) {
            String categoryName = categorys.get(i);
            // 第三行行 第一个 栏位 category name
            row = sheet.createRow(rowNum++);
            cell = row.createCell(0);
            cell.setCellStyle(style8);
            cell.setCellValue(categoryName);

            BigDecimal sku = new BigDecimal("0");
            BigDecimal inventoryQty = new BigDecimal("0");
            BigDecimal baseSalePrice = new BigDecimal("0");
            // 获得category 下的商品种类和 库存
            for (int j = 0; j < totalList.size(); j++) {
                StocktakeReportVarianceDTO item = totalList.get(j);
                if (categoryName.equals(item.getCategoryName())) {
                    sku = item.getSku();
                    inventoryQty = item.getInventoryQty();
                    baseSalePrice = item.getBaseSalePrice();
                }
            }

            BigDecimal ltVarianceQty = new BigDecimal("0");
            BigDecimal ltVarianceAmt = new BigDecimal("0");
            // 获得缺少的数据
            for (int j = 0; j < ltList.size(); j++) {
                StocktakeReportVarianceDTO item = ltList.get(j);
                if (categoryName.equals(item.getCategoryName())) {
                    ltVarianceQty = item.getVarianceQty();
                    ltVarianceAmt = item.getVarianceAmt();
                }
            }

            BigDecimal gtVarianceQty = new BigDecimal("0");
            BigDecimal gtVarianceAmt = new BigDecimal("0");
            // 获得多出的数据
            for (int j = 0; j < gtList.size(); j++) {
                StocktakeReportVarianceDTO item = gtList.get(j);
                if (categoryName.equals(item.getCategoryName())) {
                    gtVarianceQty = item.getVarianceQty();
                    gtVarianceAmt = item.getVarianceAmt();
                }
            }

            // 商品种类数量
            row3Cell = row.createCell(3);
            row3Cell.setCellValue(getNumber0(sku));
            row3Cell.setCellStyle(style9);

            // 商品库存数量
            row3Cell = row.createCell(4);
            row3Cell.setCellValue(getNumber0(inventoryQty));
            row3Cell.setCellStyle(style9);

            // 差异数量
            cell = row.createCell(6);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(gtVarianceQty));

            cell = row.createCell(7);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(ltVarianceQty));

            // 差异数量总和
            cell = row.createCell(8);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(ltVarianceQty.add(gtVarianceQty)));

            // 差异金额
            cell = row.createCell(10);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(gtVarianceAmt));

            cell = row.createCell(11);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(ltVarianceAmt));

            // 获得所有的差异金额综合
            cell = row.createCell(12);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(ltVarianceAmt.add(gtVarianceAmt)));

            // 获得分类 平均 成本售价
            BigDecimal avgSalePrice = baseSalePrice.divide(sku, 2, RoundingMode.HALF_UP);

            cell = row.createCell(14);
            cell.setCellStyle(style9);
            cell.setCellValue(getNumber0(avgSalePrice));
        }

    }

    // 创建第二个sheet
    private void createSheet2(String piCd,String piDate,String storeCd,SXSSFSheet sheet, SXSSFWorkbook wb) {
        // 去除网格线
        sheet.setDisplayGridlines(false);
        // 缩放比例
        sheet.setZoom(85);
        /**
         * 冻结列
         * [[零表示不设置冻结]]
         * 	第一个参数表示要冻结的列数;从零开始;
         * 	第二个参数表示要冻结的行数;从零开始;
         * 	第三个参数表示在右侧窗格中可以看见的冻结列数。从1开始；
         * 	第四个参数表示在底部窗格中可以看见的冻结行数。从1开始；
         */
        sheet.createFreezePane(6,12,7,13);
        DataFormat format = wb.createDataFormat();
        // 生成并设置另一个样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font titleFont = wb.createFont();
        // 设置字体
        titleFont.setFontName("Microsoft JhengHei");
        // 设置字体大小
        titleFont.setFontHeightInPoints((short) 30);
        // 字体加粗
        titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        titleStyle.setFont(titleFont);

        // 生成并设置另一个样式
        CellStyle style1 = wb.createCellStyle();
        style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font font1 = wb.createFont();
        // 设置字体
        font1.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font1.setFontHeightInPoints((short) 10);
        // 字体加粗
        // font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style1.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style2.setDataFormat(format.getFormat("dd-MMM-yy"));

        // 生成另一种字体4
        Font font2 = wb.createFont();
        // 设置字体
        font2.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font2.setFontHeightInPoints((short) 10);
        // 字体加粗
        // font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style2.setFont(font2);

        // 生成并设置另一个样式
        CellStyle style3 = wb.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style3.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style3.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        style3.setWrapText(true);
        // 生成另一种字体4
        Font font3 = wb.createFont();
        // 设置字体
        font3.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font3.setFontHeightInPoints((short) 10);
        // 粗体
        font3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);

        // 在样式4中引用这种字体
        style3.setFont(font3);

        // 生成并设置另一个样式, 蓝色背景
        CellStyle style4 = wb.createCellStyle();
        style4.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style4.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style4.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style4.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style4.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        style4.setWrapText(true);
        style4.setFont(font3);

        // 生成并设置另一个样式,红色背景
        CellStyle style5 = wb.createCellStyle();
        style5.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style5.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style5.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style5.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style5.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        style5.setWrapText(true);
        style5.setFont(font3);

        CellStyle style6 = wb.createCellStyle();
        style6.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        style6.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style6.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style6.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style6.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style6.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style6.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style6.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        style6.setWrapText(true);
        style6.setFont(font3);

        CellStyle style7 = wb.createCellStyle();
        style7.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style7.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style7.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style7.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style7.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style7.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style7.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style7.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        // 自动换行
        style7.setWrapText(true);
        // 生成另一种字体4
        Font font7 = wb.createFont();
        // 设置字体
        font7.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font7.setFontHeightInPoints((short) 9);
        font7.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 设置斜体
        font7.setItalic(true);
        style7.setFont(font7);

        CellStyle style8 = wb.createCellStyle();
        style8.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style8.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style8.setBorderTop(CellStyle.BORDER_DOTTED);
        style8.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        style8.setBorderRight(CellStyle.SOLID_FOREGROUND);
        style8.setBorderBottom(CellStyle.BORDER_DOTTED);
        style8.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style8.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style8.setDataFormat(format.getFormat("#,##0"));
        // 自动换行
        style8.setWrapText(true);
        // 生成另一种字体4
        Font font8 = wb.createFont();
        // 设置字体
        font8.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font8.setFontHeightInPoints((short) 10);

        style8.setFont(font8);

        // 生成并设置另一个样式, 蓝色背景
        CellStyle style9 = wb.createCellStyle();
        style9.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style9.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style9.setBorderTop(CellStyle.BORDER_DOTTED);
        style9.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        style9.setBorderRight(CellStyle.SOLID_FOREGROUND);
        style9.setBorderBottom(CellStyle.BORDER_DOTTED);
        style9.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style9.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style9.setDataFormat(format.getFormat("#,##0"));
        // 自动换行
        style9.setWrapText(true);
        style9.setFont(font8);

        // 生成并设置另一个样式, 红色背景
        CellStyle style10 = wb.createCellStyle();
        style10.setFillForegroundColor(IndexedColors.CORAL.getIndex());
        style10.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style10.setBorderTop(CellStyle.BORDER_DOTTED);
        style10.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        style10.setBorderRight(CellStyle.SOLID_FOREGROUND);
        style10.setBorderBottom(CellStyle.BORDER_DOTTED);
        style10.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style10.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style10.setDataFormat(format.getFormat("#,##0"));
        // 自动换行
        style10.setWrapText(true);
        style10.setFont(font8);

        // 生成并设置另一个样式, 绿色背景
        CellStyle style11 = wb.createCellStyle();
        style11.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        style11.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style11.setBorderTop(CellStyle.BORDER_DOTTED);
        style11.setBorderLeft(CellStyle.SOLID_FOREGROUND);
        style11.setBorderRight(CellStyle.SOLID_FOREGROUND);
        style11.setBorderBottom(CellStyle.BORDER_DOTTED);
        style11.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style11.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style11.setDataFormat(format.getFormat("#,##0"));
        // 自动换行
        style11.setWrapText(true);
        style11.setFont(font8);

        // 设置列宽
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 10 * 256);
        sheet.setColumnWidth(2, 70 * 256);
        sheet.setColumnWidth(3, 20 * 256);
        sheet.setColumnWidth(4, 20 * 256);
        sheet.setColumnWidth(5, 20 * 256);
        sheet.setColumnWidth(6, 20 * 256);
        sheet.setColumnWidth(7, 20 * 256);
        sheet.setColumnWidth(8, 20 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(10, 20 * 256);
        sheet.setColumnWidth(11, 20 * 256);
        sheet.setColumnWidth(12, 20 * 256);
        sheet.setColumnWidth(13, 20 * 256);
        sheet.setColumnWidth(14, 20 * 256);
        sheet.setColumnWidth(15, 20 * 256);
        sheet.setColumnWidth(16, 20 * 256);

        /**
         * 这里是创建标题
         */
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        SXSSFRow row = sheet.createRow(1);
        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        SXSSFCell titilecell = row.createCell(0);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 16));

        // 设置单元格内容
        titilecell.setCellValue("PHYSICAL INVENTORY RESULT (Page 2)");
        titilecell.setCellStyle(titleStyle);

        // 获得头部信息
        StocktakeReportItemDTO grandTotal = stocktakeProcessMapper.getGrandTotal(piCd,piDate,storeCd);

        if (grandTotal == null) {
            grandTotal = new StocktakeReportItemDTO();
        }

        // 创建第三行, store 信息
        row = sheet.createRow(3);
        SXSSFCell row3Cell0 = row.createCell(0);
        row3Cell0.setCellValue("STORE CODE:");
        row3Cell0.setCellStyle(style1);

        SXSSFCell row3Cell2 = row.createCell(2);
        row3Cell2.setCellValue(getValEmpty(grandTotal.getStoreCd()));
        row3Cell2.setCellStyle(style2);

        SXSSFCell row3Cell3 = row.createCell(14);
        row3Cell3.setCellValue("COUNTED DATE:");
        row3Cell3.setCellStyle(style1);

        SXSSFCell row3Cell4 = row.createCell(15);
        Date countedDate = getDateEmpty(piDate);
        if (countedDate != null) {
            row3Cell4.setCellValue(countedDate);
        }
        row3Cell4.setCellStyle(style2);
        // 合并单元格
        CellRangeAddress countedDateRegion = new CellRangeAddress(3, 3, 15, 16);
        sheet.addMergedRegion(countedDateRegion);
        setRegionUtil(countedDateRegion,sheet,wb);

        // 创建第五行, store 信息
        row = sheet.createRow(5);
        SXSSFCell row5Cell0 = row.createCell(0);
        row5Cell0.setCellValue("STORE NAME:");
        row5Cell0.setCellStyle(style1);

        SXSSFCell row5Cell2 = row.createCell(2);
        row5Cell2.setCellValue(getValEmpty(grandTotal.getStoreName()));
        row5Cell2.setCellStyle(style2);

        SXSSFCell row5Cell4 = row.createCell(15);
        row5Cell4.setCellValue("");
        row5Cell4.setCellStyle(style2);
        // 合并单元格
        CellRangeAddress brandRegion = new CellRangeAddress(5, 5, 15, 16);
        sheet.addMergedRegion(brandRegion);
        setRegionUtil(brandRegion,sheet,wb);

        // 创建第7行, store 信息
        row = sheet.createRow(7);
        SXSSFCell row7Cell0 = row.createCell(0);
        row7Cell0.setCellValue("II. Detail.");
        row7Cell0.setCellStyle(style1);

        // 创建第8行, table列表信息
        row = sheet.createRow(8);

        SXSSFCell row8Cell0 = row.createCell(0);
        row8Cell0.setCellValue("#");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(1);
        row8Cell0.setCellValue("Item Code");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(2);
        row8Cell0.setCellValue("Item Name");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(3);
        row8Cell0.setCellValue("Category Name");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(4);
        row8Cell0.setCellValue("Type");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(5);
        row8Cell0.setCellValue("UOM");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(6);
        row8Cell0.setCellValue("Stock on SAP");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(7);
        row8Cell0.setCellValue("Upload Pcount on SAP");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(8);
        row8Cell0.setCellValue("Unit Cost (VND)");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(9);
        row8Cell0.setCellValue("Before Adjust");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(11);
        row8Cell0.setCellValue("Adjust");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(14);
        row8Cell0.setCellValue("After Adjust");
        row8Cell0.setCellStyle(style3);

        row8Cell0 = row.createCell(16);
        row8Cell0.setCellValue("Bad Merchandise");
        row8Cell0.setCellStyle(style3);

        // 创建第9行, table列表信息
        row = sheet.createRow(9);
        // 设置行高
        row.setHeight((short) 1000);
        SXSSFCell row9Cell9 = row.createCell(9);
        row9Cell9.setCellValue("Variance Qtt");
        row9Cell9.setCellStyle(style4);

        SXSSFCell row9Cell10 = row.createCell(10);
        row9Cell10.setCellValue("Variance Amt");
        row9Cell10.setCellStyle(style4);

        SXSSFCell row9Cell11 = row.createCell(11);
        row9Cell11.setCellValue("Human Error");
        row9Cell11.setCellStyle(style5);

        SXSSFCell row9Cell12 = row.createCell(12);
        row9Cell12.setCellValue("System");
        row9Cell12.setCellStyle(style5);

        SXSSFCell row9Cell13 = row.createCell(13);
        row9Cell13.setCellValue("IV Amount allocated from DC to stores");
        row9Cell13.setCellStyle(style5);

        SXSSFCell row9Cell14 = row.createCell(14);
        row9Cell14.setCellValue("Variance Qtt");
        row9Cell14.setCellStyle(style6);

        SXSSFCell row9Cell15 = row.createCell(15);
        row9Cell15.setCellValue("Variance Amt");
        row9Cell15.setCellStyle(style6);

        // 合并单元格 0 - 8
        for (int i = 0; i <= 8; i++) {
            CellRangeAddress tableTitleRegion = new CellRangeAddress(8, 9, i, i);
            sheet.addMergedRegion(tableTitleRegion);
            setRegionUtil(tableTitleRegion,sheet,wb);
        }
        // 最后一行合并
        CellRangeAddress lastTitleRegion = new CellRangeAddress(8, 9, 16, 16);
        sheet.addMergedRegion(lastTitleRegion);
        setRegionUtil(lastTitleRegion,sheet,wb);
        // 差异栏位合并
        CellRangeAddress beforeAdj = new CellRangeAddress(8, 8, 9, 10);
        sheet.addMergedRegion(beforeAdj);
        setRegionUtil(beforeAdj,sheet,wb);

        CellRangeAddress adj = new CellRangeAddress(8, 8, 11, 13);
        sheet.addMergedRegion(adj);
        setRegionUtil(adj,sheet,wb);

        CellRangeAddress afterAdj = new CellRangeAddress(8, 8, 14, 15);
        sheet.addMergedRegion(afterAdj);
        setRegionUtil(afterAdj,sheet,wb);

        // 创建第9行, table列表信息
        row = sheet.createRow(10);
        row.createCell(0).setCellStyle(style7);
        row.createCell(1).setCellStyle(style7);
        row.createCell(2).setCellStyle(style7);
        row.createCell(3).setCellStyle(style7);
        row.createCell(4).setCellStyle(style7);
        row.createCell(5).setCellStyle(style7);

        SXSSFCell row10Cell6 = row.createCell(6);
        row10Cell6.setCellValue("(1)");
        row10Cell6.setCellStyle(style7);

        SXSSFCell row10Cell7 = row.createCell(7);
        row10Cell7.setCellValue("(2)");
        row10Cell7.setCellStyle(style7);

        SXSSFCell row10Cell8 = row.createCell(8);
        row10Cell8.setCellValue("(3)");
        row10Cell8.setCellStyle(style7);

        SXSSFCell row10Cell9 = row.createCell(9);
        row10Cell9.setCellValue("(4)=(2)-(1)");
        row10Cell9.setCellStyle(style4);

        SXSSFCell row10Cell10 = row.createCell(10);
        row10Cell10.setCellValue("(5)=(4)*(3)");
        row10Cell10.setCellStyle(style4);

        SXSSFCell row10Cell11 = row.createCell(11);
        row10Cell11.setCellValue("(6)");
        row10Cell11.setCellStyle(style5);

        SXSSFCell row10Cell12 = row.createCell(12);
        row10Cell12.setCellValue("(7)");
        row10Cell12.setCellStyle(style5);

        SXSSFCell row10Cell13 = row.createCell(13);
        row10Cell13.setCellValue("(8)");
        row10Cell13.setCellStyle(style5);

        SXSSFCell row10Cell14 = row.createCell(14);
        row10Cell14.setCellValue("(9)=(4)-(6)-(7)-(8)");
        row10Cell14.setCellStyle(style6);

        SXSSFCell row10Cell15 = row.createCell(15);
        row10Cell15.setCellValue("(10)=(9)*(3)");
        row10Cell15.setCellStyle(style6);

        SXSSFCell row10Cell16 = row.createCell(16);
        row10Cell16.setCellValue("(11)");
        row10Cell16.setCellStyle(style7);

        // 创建第11行, total行
        row = sheet.createRow(11);

        // 空行设置边框
        row.createCell(0).setCellStyle(style7);
        row.createCell(1).setCellStyle(style7);
        row.createCell(3).setCellStyle(style7);
        row.createCell(4).setCellStyle(style7);
        row.createCell(5).setCellStyle(style7);



        SXSSFCell row11Cell = row.createCell(2);
        row11Cell.setCellValue("Grand Total:");
        row11Cell.setCellStyle(style7);

        row11Cell = row.createCell(6);
        row11Cell.setCellValue(getNumber0(grandTotal.getInventoryQty()));
        row11Cell.setCellStyle(style8);

        row11Cell = row.createCell(7);
        row11Cell.setCellValue(getNumber0(grandTotal.getPiQty()));
        row11Cell.setCellStyle(style8);

        row11Cell = row.createCell(8);
        row11Cell.setCellValue(getNumber0(grandTotal.getBaseSalePrice()));
        row11Cell.setCellStyle(style8);

        row11Cell = row.createCell(9);
        row11Cell.setCellValue(getNumber0(grandTotal.getVarianceQty()));
        row11Cell.setCellStyle(style9);

        row11Cell = row.createCell(10);
        row11Cell.setCellValue(getNumber0(grandTotal.getVarianceAmt()));
        row11Cell.setCellStyle(style9);

        row11Cell = row.createCell(11);
        row11Cell.setCellValue(0);
        row11Cell.setCellStyle(style10);

        row11Cell = row.createCell(12);
        row11Cell.setCellValue(0);
        row11Cell.setCellStyle(style10);

        row11Cell = row.createCell(13);
        row11Cell.setCellValue(0);
        row11Cell.setCellStyle(style10);

        row11Cell = row.createCell(14);
        row11Cell.setCellValue(0);
        row11Cell.setCellStyle(style11);

        row11Cell = row.createCell(15);
        row11Cell.setCellValue(0);
        row11Cell.setCellStyle(style11);

        row11Cell = row.createCell(16);
        row11Cell.setCellValue(getNumber0(grandTotal.getBadQty()));
        row11Cell.setCellStyle(style8);

        // 获取商品数据
        List<StocktakeReportItemDTO> itemList = stocktakeProcessMapper.getStocktakeReportItems(piCd,piDate,storeCd);

        // 遍历商品数据
        for (int i = 0; i < itemList.size(); i++) {

            StocktakeReportItemDTO item = itemList.get(i);

            // 创建第11行, total行
            row = sheet.createRow((12+i));
            SXSSFCell cell = row.createCell(0);
            cell.setCellValue(i+1);
            cell.setCellStyle(style8);

            cell = row.createCell(1);
            cell.setCellValue(item.getArticleId());
            cell.setCellStyle(style8);

            cell = row.createCell(2);
            cell.setCellValue(item.getArticleName());
            cell.setCellStyle(style8);

            cell = row.createCell(3);
            cell.setCellValue(item.getCategoryName());
            cell.setCellStyle(style8);

            cell = row.createCell(4);
            cell.setCellValue(item.getMaterialName());
            cell.setCellStyle(style8);

            cell = row.createCell(5);
            cell.setCellValue(item.getUom());
            cell.setCellStyle(style8);

            cell = row.createCell(6);
            cell.setCellValue(item.getInventoryQty().longValue());
            cell.setCellStyle(style8);

            cell = row.createCell(7);
            cell.setCellValue(item.getPiQty().longValue());
            cell.setCellStyle(style8);

            cell = row.createCell(8);
            cell.setCellValue(item.getBaseSalePrice().longValue());
            cell.setCellStyle(style8);

            cell = row.createCell(9);
            cell.setCellValue(item.getVarianceQty().longValue());
            cell.setCellStyle(style9);

            cell = row.createCell(10);
            cell.setCellValue(item.getVarianceAmt().longValue());
            cell.setCellStyle(style9);

            // 暂时空着 start
            cell = row.createCell(11);
            cell.setCellStyle(style10);

            cell = row.createCell(12);
            cell.setCellStyle(style10);

            cell = row.createCell(13);
            cell.setCellStyle(style10);

            cell = row.createCell(14);
            cell.setCellStyle(style11);

            cell = row.createCell(15);
            cell.setCellStyle(style11);
            // 暂时空着 end

            // Bad Merchandise 暂时空着
            cell = row.createCell(16);
            cell.setCellStyle(style8);
            cell.setCellValue(getNumber0(item.getBadQty()));
        }
    }

    // 创建第一个sheet
    private void createSheet1(String piCd,String piDate,String storeCd,SXSSFSheet sheet, SXSSFWorkbook wb) {
        // 去除网格线
        sheet.setDisplayGridlines(false);
        // 缩放比例
        sheet.setZoom(85);

        // 生成并设置另一个样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setBorderBottom(XSSFCellStyle.BORDER_NONE);
        titleStyle.setBorderLeft(XSSFCellStyle.BORDER_NONE);
        titleStyle.setBorderRight(XSSFCellStyle.BORDER_NONE);
        titleStyle.setBorderTop(XSSFCellStyle.BORDER_NONE);
        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font titleFont = wb.createFont();
        // 设置字体
        titleFont.setFontName("Microsoft JhengHei");
        // 设置字体大小
        titleFont.setFontHeightInPoints((short) 20);
        // 字体加粗
        titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        titleStyle.setFont(titleFont);

        // 生成并设置另一个样式
        CellStyle style1 = wb.createCellStyle();
        short dateFormat = wb.createDataFormat().getFormat("dd-MMM-yy");
        style1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style1.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style1.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style1.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style1.setDataFormat(dateFormat);

        // 生成另一种字体4
        Font font1 = wb.createFont();
        // 设置字体
        font1.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font1.setFontHeightInPoints((short) 10);
        // 字体加粗
        // font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style1.setFont(font1);

        // 生成并设置另一个样式
        CellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style2.setDataFormat(dateFormat);

        // 生成另一种字体4
        Font font2 = wb.createFont();
        // 设置字体
        font2.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font2.setFontHeightInPoints((short) 10);
        // 字体加粗
        // font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style2.setFont(font2);

        // 生成并设置另一个样式
        CellStyle style3 = wb.createCellStyle();
        style3.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style3.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style3.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        Font font3 = wb.createFont();
        // 设置字体
        font3.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font3.setFontHeightInPoints((short) 10);
        // 字体加粗
        font3.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        font3.setUnderline(HSSFFont.U_SINGLE);
        // 在样式4中引用这种字体
        style3.setFont(font3);

        // 生成并设置另一个样式
        CellStyle style4 = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        style4.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style4.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style4.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style4.setDataFormat(format.getFormat("#,##0"));
        // 生成另一种字体4
        Font font4 = wb.createFont();
        // 设置字体
        font4.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font4.setFontHeightInPoints((short) 10);
        // 字体下划线
        // font4.setUnderline(HSSFFont.U_SINGLE);
        // 在样式4中引用这种字体
        style4.setFont(font4);

        // 生成并设置另一个样式
        CellStyle style5 = wb.createCellStyle();
        style5.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style5.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style5.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style5.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style5.setDataFormat(format.getFormat("#,##0"));

        // 生成另一种字体4
        Font font5 = wb.createFont();
        // 设置字体
        font5.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font5.setFontHeightInPoints((short) 10);
        // 字体加粗
        // font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style5.setFont(font5);

        // 生成并设置另一个样式
        CellStyle style6 = wb.createCellStyle();
        style6.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style6.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style6.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style6.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style6.setDataFormat(dateFormat);

        // 生成另一种字体4
        Font font6 = wb.createFont();
        // 设置字体
        font6.setFontName("PMingLiU-ExtB");
        // 设置字体大小
        font6.setFontHeightInPoints((short) 10);
        // 字体加粗
        // font1.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style6.setFont(font5);

        // 生成并设置另一个样式
        CellStyle style7 = wb.createCellStyle();
        style7.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
        style7.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style7.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style7.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style7.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style7.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style7.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style7.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style2.setFont(font2);

        // 生成并设置另一个样式
        CellStyle style8 = wb.createCellStyle();
        style8.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style8.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style8.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style8.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style8.setDataFormat(format.getFormat("#,##0"));

        // 在样式4中引用这种字体
        style8.setFont(font5);

        // 生成并设置另一个样式
        CellStyle style9 = wb.createCellStyle();
        style9.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style9.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style9.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style9.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style9.setDataFormat(format.getFormat("0.00%"));

        style9.setFont(font5);

        // 生成并设置另一个样式
        CellStyle style10 = wb.createCellStyle();
        style10.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style10.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style10.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style10.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style10.setBorderRight(XSSFCellStyle.BORDER_THIN);
        style10.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style10.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style10.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

        // 在样式4中引用这种字体
        style10.setFont(font2);

        // 设置列宽
        sheet.setColumnWidth(0, 5 * 256);
        sheet.setColumnWidth(1, 8 * 256);
        sheet.setColumnWidth(2, 10 * 256);
        sheet.setColumnWidth(3, 10 * 256);
        sheet.setColumnWidth(4, 10 * 256);
        sheet.setColumnWidth(5, 10 * 256);
        sheet.setColumnWidth(6, 10 * 256);
        sheet.setColumnWidth(7, 10 * 256);
        sheet.setColumnWidth(8, 10 * 256);
        sheet.setColumnWidth(9, 5 * 256);
        sheet.setColumnWidth(10, 15 * 256);
        sheet.setColumnWidth(11, 15 * 256);
        sheet.setColumnWidth(12, 15 * 256);
        sheet.setColumnWidth(13, 5 * 256);
        sheet.setColumnWidth(14, 15 * 256);
        sheet.setColumnWidth(15, 15 * 256);
        sheet.setColumnWidth(16, 15 * 256);

        /**
         * 这里是创建标题
         */
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        SXSSFRow row = sheet.createRow(0);
        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        SXSSFCell titilecell = row.createCell(0);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));

        // 设置单元格内容
        titilecell.setCellValue("PHYSICAL INVENTORY RESULT (Page 1)");
        titilecell.setCellStyle(titleStyle);

        // 第一行数据
        // 获取store name/code 当前盘点的开始时间及结束时间
        StocktakeReportDTO headInfo = stocktakeProcessMapper.getStocktakingHead(piCd,piDate,storeCd);
        if (headInfo==null) {
            headInfo = new StocktakeReportDTO();
        }

        row = sheet.createRow(2);
        // 合并单元格, 第 一列 到第二列,
        CellRangeAddress region1 = new CellRangeAddress(2, 2, 2, 6);
        CellRangeAddress region2 = new CellRangeAddress(2, 2, 13, 16);
        sheet.addMergedRegion(region1);
        sheet.addMergedRegion(region2);
        setRegionUtil(region1,sheet,wb);
        setRegionUtil(region2,sheet,wb);

        SXSSFCell row1Cell1 = row.createCell(0);
        row1Cell1.setCellStyle(style1);
        row1Cell1.setCellValue("STORE NAME");

        SXSSFCell row1Cell3 = row.createCell(2);
        row1Cell3.setCellStyle(style2);
        row1Cell3.setCellValue(getValEmpty(headInfo.getStoreName()));

        SXSSFCell row1Cell4 = row.createCell(11);
        row1Cell4.setCellValue("Counted Date");
        row1Cell4.setCellStyle(style1);

        SXSSFCell row1Cell5 = row.createCell(13);
        Date thisStocktakeDate = getDateEmpty(headInfo.getThisStocktakeDate());
        if (thisStocktakeDate == null) {
            row1Cell5.setCellValue("");
        } else {
            row1Cell5.setCellValue(thisStocktakeDate);
        }
        row1Cell5.setCellStyle(style2);

        // 第三行数据
        row = sheet.createRow(4);
        // 合并单元格, 第 一列 到第二列,
        CellRangeAddress region3 = new CellRangeAddress(4, 4, 2, 6);
        CellRangeAddress region4 = new CellRangeAddress(4,4,13,16);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        setRegionUtil(region3,sheet,wb);
        setRegionUtil(region4,sheet,wb);

        SXSSFCell row2Cell1 = row.createCell(0);
        row2Cell1.setCellStyle(style1);
        row2Cell1.setCellValue("STORE CODE");

        SXSSFCell row2Cell3 = row.createCell(2);
        row2Cell3.setCellStyle(style2);
        row2Cell3.setCellValue(getValEmpty(headInfo.getStoreCd()));

        SXSSFCell row2Cell4 = row.createCell(11);
        row2Cell4.setCellValue("Started at");
        row2Cell4.setCellStyle(style1);

        SXSSFCell row2Cell5 = row.createCell(13);
        row2Cell5.setCellValue(getTimeEmpty(headInfo.getThisStartTime()));
        row2Cell5.setCellStyle(style2);

        // 第四行行数据
        row = sheet.createRow(6);
        // 合并单元格, 第 一列 到第二列,
        CellRangeAddress region5 = new CellRangeAddress(6,6,13,16);
        sheet.addMergedRegion(region5);
        setRegionUtil(region5,sheet,wb);

        SXSSFCell row4Cell4 = row.createCell(11);
        row4Cell4.setCellValue("Finished at");
        row4Cell4.setCellStyle(style1);

        SXSSFCell row4Cell5 = row.createCell(13);
        row4Cell5.setCellValue(getTimeEmpty(headInfo.getThisEndTime()));
        row4Cell5.setCellStyle(style2);

        // 第十行SUMMARY 数据 title
        row = sheet.createRow(10);
        SXSSFCell row10Cell0 = row.createCell(0);
        row10Cell0.setCellValue("I. SUMMARY");
        row10Cell0.setCellStyle(style3);

        // 第11行 列表
        row = sheet.createRow(11);
        SXSSFCell row11Cell0 = row.createCell(8);
        row11Cell0.setCellValue("Variance");
        row11Cell0.setCellStyle(style4);

        SXSSFCell row11Cell1 = row.createCell(10);
        row11Cell1.setCellValue("SKU");
        row11Cell1.setCellStyle(style4);

        SXSSFCell row11Cell2 = row.createCell(11);
        row11Cell2.setCellValue("Qtt");
        row11Cell2.setCellStyle(style4);

        SXSSFCell row11Cell3 = row.createCell(12);
        row11Cell3.setCellValue("Amount");
        row11Cell3.setCellStyle(style4);

        SXSSFCell row11Cell4 = row.createCell(14);
        row11Cell4.setCellValue("SKU");
        row11Cell4.setCellStyle(style4);

        SXSSFCell row11Cell5 = row.createCell(15);
        row11Cell5.setCellValue("Qtt");
        row11Cell5.setCellStyle(style4);

        SXSSFCell row11Cell6 = row.createCell(16);
        row11Cell6.setCellValue("Amount");
        row11Cell6.setCellStyle(style4);

        // 第12行 列表
        row = sheet.createRow(12);
        SXSSFCell row12Cell0 = row.createCell(1);
        row12Cell0.setCellValue("Opening Date");
        row12Cell0.setCellStyle(style1);

        // 开店日期
        SXSSFCell row12Cell1 = row.createCell(4);
        Date openDate = getDateEmpty(headInfo.getOpenDate());
        if (openDate!=null) {
            row12Cell1.setCellValue(openDate);
        }
        row12Cell1.setCellStyle(style1);

        SXSSFCell row12Cell2 = row.createCell(10);
        row12Cell2.setCellValue("This time");
        row12Cell2.setCellStyle(style6);

        SXSSFCell row12Cell3 = row.createCell(12);
        // Date thisStocktakeDate = getDateEmpty(headInfo.getThisStocktakeDate());
        if (thisStocktakeDate!=null) {
            row12Cell3.setCellValue(thisStocktakeDate);
        }
        row12Cell3.setCellStyle(style6);

        SXSSFCell row12Cell4 = row.createCell(14);
        row12Cell4.setCellValue("Last time");
        row12Cell4.setCellStyle(style6);

        SXSSFCell row12Cell5 = row.createCell(16);
        Date lastStocktakeDate = getDateEmpty(headInfo.getLastStocktakeDate());
        if (lastStocktakeDate!=null) {
            row12Cell5.setCellValue(lastStocktakeDate);
        }
        row12Cell5.setCellStyle(style6);

        // 第13行 列表 Shortage 部分
        row = sheet.createRow(13);
        SXSSFCell row13Cell0 = row.createCell(1);
        row13Cell0.setCellValue("NRI Store Sytsem Application Date");
        row13Cell0.setCellStyle(style1);

        SXSSFCell row13Cell1 = row.createCell(4);
        Date effectiveDate = getDateEmpty(headInfo.getEffectiveStartDate());
        if (effectiveDate==null) {
            row13Cell1.setCellValue("");
        } else {
            row13Cell1.setCellValue(effectiveDate);
        }
        row13Cell1.setCellStyle(style1);

        SXSSFCell row13Cell2 = row.createCell(8);
        row13Cell2.setCellValue("Shortage");
        row13Cell2.setCellStyle(style6);

        // 第14行 列表
        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        StocktakeReportDTO thisShortageVarianceLT = stocktakeProcessMapper.getStocktakeVariance(headInfo.getThisStocktakeCd(),
                headInfo.getThisStocktakeDate(),headInfo.getStoreCd(),"LT","01");

        if (thisShortageVarianceLT==null) {
            thisShortageVarianceLT = new StocktakeReportDTO();
        }

        row = sheet.createRow(14);
        SXSSFCell row14Cell0 = row.createCell(1);
        row14Cell0.setCellValue("Last Stock take Date");
        row14Cell0.setCellStyle(style1);

        SXSSFCell row14Cell1 = row.createCell(4);
        if (lastStocktakeDate==null) {
            row14Cell1.setCellValue("");
        } else {
            row14Cell1.setCellValue(lastStocktakeDate);
        }
        row14Cell1.setCellStyle(style1);

        SXSSFCell row14Cell2 = row.createCell(8);
        row14Cell2.setCellValue("General Merchandise");
        row14Cell2.setCellStyle(style6);

        SXSSFCell row14Cell3 = row.createCell(10);
        row14Cell3.setCellValue(getNumber0(thisShortageVarianceLT.getSku()));
        row14Cell3.setCellStyle(style8);

        SXSSFCell row14Cell4 = row.createCell(11);
        row14Cell4.setCellValue(getNumber0(thisShortageVarianceLT.getQty()));
        row14Cell4.setCellStyle(style8);

        SXSSFCell row14Cell5 = row.createCell(12);
        row14Cell5.setCellValue(getNumber0(thisShortageVarianceLT.getAmt()));
        row14Cell5.setCellStyle(style8);

        StocktakeReportDTO lastShortageVarianceLT = null;
        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        // 上一次盘点的数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastShortageVarianceLT = stocktakeProcessMapper.getStocktakeVariance(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(),headInfo.getStoreCd(),"LT","01");

            if (lastShortageVarianceLT==null) {
                lastShortageVarianceLT = new StocktakeReportDTO();
            }

            SXSSFCell row14Cell6 = row.createCell(14);
            row14Cell6.setCellValue(getNumber0(lastShortageVarianceLT.getSku()));
            row14Cell6.setCellStyle(style8);

            SXSSFCell row14Cell7 = row.createCell(15);
            row14Cell7.setCellValue(getNumber0(lastShortageVarianceLT.getQty()));
            row14Cell7.setCellStyle(style8);

            SXSSFCell row14Cell8 = row.createCell(16);
            row14Cell8.setCellValue(getNumber0(lastShortageVarianceLT.getAmt()));
            row14Cell8.setCellStyle(style8);
        } else {
            SXSSFCell row14Cell6 = row.createCell(14);
            row14Cell6.setCellStyle(style8);

            SXSSFCell row14Cell7 = row.createCell(15);
            row14Cell7.setCellStyle(style8);

            SXSSFCell row14Cell8 = row.createCell(16);
            row14Cell8.setCellStyle(style8);
        }


        // 第15行 列表
        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        StocktakeReportDTO thisShortageVarianceLT2 = stocktakeProcessMapper.getStocktakeVariance(headInfo.getThisStocktakeCd(),
                headInfo.getThisStocktakeDate(),headInfo.getStoreCd(),"LT","02");

        if (thisShortageVarianceLT2==null) {
            thisShortageVarianceLT2 = new StocktakeReportDTO();
        }

        row = sheet.createRow(15);
        SXSSFCell row15Cell0 = row.createCell(1);
        row15Cell0.setCellValue("Last Stock take (Cleared) Date");
        row15Cell0.setCellStyle(style1);

        SXSSFCell row15Cell1 = row.createCell(4);
        if (lastStocktakeDate == null) {
            row15Cell1.setCellValue("");
        } else {
            row15Cell1.setCellValue(lastStocktakeDate);
        }
        row15Cell1.setCellStyle(style1);

        SXSSFCell row15Cell2 = row.createCell(8);
        row15Cell2.setCellValue("Food Service");
        row15Cell2.setCellStyle(style6);

        SXSSFCell row15Cell3 = row.createCell(10);
        row15Cell3.setCellValue(getNumber0(thisShortageVarianceLT2.getSku()));
        row15Cell3.setCellStyle(style8);

        SXSSFCell row15Cell4 = row.createCell(11);
        row15Cell4.setCellValue(getNumber0(thisShortageVarianceLT2.getQty()));
        row15Cell4.setCellStyle(style8);

        SXSSFCell row15Cell5 = row.createCell(12);
        row15Cell5.setCellValue(getNumber0(thisShortageVarianceLT2.getAmt()));
        row15Cell5.setCellStyle(style8);


        StocktakeReportDTO lastShortageVarianceLT2 = null;
        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        // 上一次盘点的数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastShortageVarianceLT2 = stocktakeProcessMapper.getStocktakeVariance(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(),headInfo.getStoreCd(),"LT","02");

            if (lastShortageVarianceLT2==null) {
                lastShortageVarianceLT2 = new StocktakeReportDTO();
            }

            SXSSFCell row15Cell6 = row.createCell(14);
            row15Cell6.setCellValue(getNumber0(lastShortageVarianceLT2.getSku()));
            row15Cell6.setCellStyle(style8);

            SXSSFCell row15Cell7 = row.createCell(15);
            row15Cell7.setCellValue(getNumber0(lastShortageVarianceLT2.getQty()));
            row15Cell7.setCellStyle(style8);

            SXSSFCell row15Cell8 = row.createCell(16);
            row15Cell8.setCellValue(getNumber0(lastShortageVarianceLT2.getAmt()));
            row15Cell8.setCellStyle(style8);
        } else {
            SXSSFCell row15Cell6 = row.createCell(14);
            row15Cell6.setCellStyle(style8);

            SXSSFCell row15Cell7 = row.createCell(15);
            row15Cell7.setCellStyle(style8);

            SXSSFCell row15Cell8 = row.createCell(16);
            row15Cell8.setCellStyle(style8);
        }


        // 第16行 列表
        row = sheet.createRow(16);
        SXSSFCell row16Cell0 = row.createCell(1);
        row16Cell0.setCellValue("Days");
        row16Cell0.setCellStyle(style1);

        String days = "";
        if (!StringUtils.isEmpty(getValEmpty(headInfo.getDay()))) {
            days = headInfo.getDay()+" days";
        }
        SXSSFCell row16Cell1 = row.createCell(4);
        row16Cell1.setCellValue(days);
        row16Cell1.setCellStyle(style1);

        SXSSFCell row16Cell2 = row.createCell(8);
        row16Cell2.setCellValue("Total Shortage");
        row16Cell2.setCellStyle(style6);

        SXSSFCell row16Cell3 = row.createCell(10);
        long thisTotalSku = getNumber0(thisShortageVarianceLT2.getSku())+getNumber0(thisShortageVarianceLT.getSku());
        row16Cell3.setCellValue(thisTotalSku);
        row16Cell3.setCellStyle(style5);

        SXSSFCell row16Cell4 = row.createCell(11);
        long thisTotalQty = getNumber0(thisShortageVarianceLT2.getQty())+getNumber0(thisShortageVarianceLT.getQty());
        row16Cell4.setCellValue(thisTotalQty);
        row16Cell4.setCellStyle(style5);

        SXSSFCell row16Cell5 = row.createCell(12);
        long thisTotalAmt = getNumber0(thisShortageVarianceLT2.getAmt())+getNumber0(thisShortageVarianceLT.getAmt());
        row16Cell5.setCellValue(thisTotalAmt);
        row16Cell5.setCellStyle(style5);

        long lastTotalSku = 0L;
        long lastTotalQty = 0L;
        long lastTotalAmt = 0L;
        // 上一次盘点的数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastTotalSku = getNumber0(lastShortageVarianceLT2.getSku())+getNumber0(lastShortageVarianceLT.getSku());
            SXSSFCell row16Cell6 = row.createCell(14);
            row16Cell6.setCellValue(lastTotalSku);
            row16Cell6.setCellStyle(style5);

            lastTotalQty = getNumber0(lastShortageVarianceLT2.getQty())+getNumber0(lastShortageVarianceLT.getQty());
            SXSSFCell row16Cell7 = row.createCell(15);
            row16Cell7.setCellValue(lastTotalQty);
            row16Cell7.setCellStyle(style5);

            lastTotalAmt = getNumber0(lastShortageVarianceLT2.getAmt())+getNumber0(lastShortageVarianceLT.getAmt());
            SXSSFCell row16Cell8 = row.createCell(16);
            row16Cell8.setCellValue(lastTotalAmt);
            row16Cell8.setCellStyle(style5);
        } else {
            SXSSFCell row16Cell6 = row.createCell(14);
            row16Cell6.setCellStyle(style5);

            SXSSFCell row16Cell7 = row.createCell(15);
            row16Cell7.setCellStyle(style5);

            SXSSFCell row16Cell8 = row.createCell(16);
            row16Cell8.setCellStyle(style5);
        }


        // 设置区域, 加边框
        CellRangeAddress region6 = new CellRangeAddress(12,16,1,4);
        setRegionUtil(region6,sheet,wb);

        // 获得账面上总量(本次盘点)
        StocktakeReportDTO thisBookValue = stocktakeProcessMapper.getStocktakeVariance(headInfo.getThisStocktakeCd(),
                headInfo.getThisStocktakeDate(), headInfo.getStoreCd(), null, null);

        if (thisBookValue == null) {
            thisBookValue = new StocktakeReportDTO();
        }

        // 第17行 列表
        row = sheet.createRow(17);
        SXSSFCell row17Cell2 = row.createCell(8);
        row17Cell2.setCellValue("% Shortage/Book Value");
        row17Cell2.setCellStyle(style6);

        // 两个数相除
        double thisBookValSku = divisionFun(thisTotalSku,getNumber0(thisBookValue.getSku()));
        SXSSFCell row17Cell3 = row.createCell(10);
        row17Cell3.setCellValue(thisBookValSku);
        row17Cell3.setCellStyle(style9);

        double thisBookValQty = divisionFun(thisTotalQty,getNumber0(thisBookValue.getBookValQty()));
        SXSSFCell row17Cell4 = row.createCell(11);
        row17Cell4.setCellValue(thisBookValQty);
        row17Cell4.setCellStyle(style9);

        double thisBookValAmt = divisionFun(thisTotalAmt,getNumber0(thisBookValue.getBookValAmt()));
        SXSSFCell row17Cell5 = row.createCell(12);
        row17Cell5.setCellValue(thisBookValAmt);
        row17Cell5.setCellStyle(style9);


        // 获得账面上总量(上次次盘点)
        StocktakeReportDTO lastBookValue = null;
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastBookValue = stocktakeProcessMapper.getStocktakeVariance(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(), headInfo.getStoreCd(), null, null);
            if (lastBookValue == null) {
                lastBookValue = new StocktakeReportDTO();
            }
            double lastBookValSku = divisionFun(lastTotalSku,getNumber0(lastBookValue.getSku()));
            SXSSFCell row17Cell6 = row.createCell(14);
            row17Cell6.setCellValue(lastBookValSku);
            row17Cell6.setCellStyle(style9);

            double lastBookValQty = divisionFun(lastTotalQty,getNumber0(lastBookValue.getBookValQty()));
            SXSSFCell row17Cell7 = row.createCell(15);
            row17Cell7.setCellValue(lastBookValQty);
            row17Cell7.setCellStyle(style9);

            double lastBookValAmt = divisionFun(lastTotalAmt,getNumber0(lastBookValue.getBookValAmt()));
            SXSSFCell row17Cell8 = row.createCell(16);
            row17Cell8.setCellValue(lastBookValAmt);
            row17Cell8.setCellStyle(style9);
        } else {
            SXSSFCell row17Cell6 = row.createCell(14);
            row17Cell6.setCellStyle(style9);

            SXSSFCell row17Cell7 = row.createCell(15);
            row17Cell7.setCellStyle(style9);

            SXSSFCell row17Cell8 = row.createCell(16);
            row17Cell8.setCellStyle(style9);
        }


        // 设置区域, 加边框
        CellRangeAddress region7 = new CellRangeAddress(16,17,10,12);
        CellRangeAddress region8 = new CellRangeAddress(16,17,14,16);
        setRegionUtil(region7,sheet,wb);
        setRegionUtil(region8,sheet,wb);

        // 第19行 列表 Overage 部分
        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        StocktakeReportDTO thisOverageVarianceGT = stocktakeProcessMapper.getStocktakeVariance(headInfo.getThisStocktakeCd(),
                headInfo.getThisStocktakeDate(),headInfo.getStoreCd(),"GT","01");

        if (thisOverageVarianceGT==null) {
            thisOverageVarianceGT = new StocktakeReportDTO();
        }

        row = sheet.createRow(19);
        SXSSFCell row19Cell2 = row.createCell(8);
        row19Cell2.setCellValue("Overage");
        row19Cell2.setCellStyle(style6);

        // 第20行 列表
        row = sheet.createRow(20);
        SXSSFCell row20Cell2 = row.createCell(8);
        row20Cell2.setCellValue("General Merchandise");
        row20Cell2.setCellStyle(style6);

        SXSSFCell row20Cell3 = row.createCell(10);
        row20Cell3.setCellValue(getNumber0(thisOverageVarianceGT.getSku()));
        row20Cell3.setCellStyle(style8);

        SXSSFCell row20Cell4 = row.createCell(11);
        row20Cell4.setCellValue(getNumber0(thisOverageVarianceGT.getQty()));
        row20Cell4.setCellStyle(style8);

        SXSSFCell row20Cell5 = row.createCell(12);
        row20Cell5.setCellValue(getNumber0(thisOverageVarianceGT.getAmt()));
        row20Cell5.setCellStyle(style8);

        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        StocktakeReportDTO lastOverageVarianceGT = null;
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastOverageVarianceGT = stocktakeProcessMapper.getStocktakeVariance(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(),headInfo.getStoreCd(),"GT","01");

            if (lastOverageVarianceGT==null) {
                lastOverageVarianceGT = new StocktakeReportDTO();
            }

            SXSSFCell row20Cell6 = row.createCell(14);
            row20Cell6.setCellValue(getNumber0(lastOverageVarianceGT.getSku()));
            row20Cell6.setCellStyle(style8);

            SXSSFCell row20Cell7 = row.createCell(15);
            row20Cell7.setCellValue(getNumber0(lastOverageVarianceGT.getQty()));
            row20Cell7.setCellStyle(style8);

            SXSSFCell row20Cell8 = row.createCell(16);
            row20Cell8.setCellValue(getNumber0(lastOverageVarianceGT.getAmt()));
            row20Cell8.setCellStyle(style8);
        } else {
            SXSSFCell row20Cell6 = row.createCell(14);
            row20Cell6.setCellStyle(style8);

            SXSSFCell row20Cell7 = row.createCell(15);
            row20Cell7.setCellStyle(style8);

            SXSSFCell row20Cell8 = row.createCell(16);
            row20Cell8.setCellStyle(style8);
        }


        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        StocktakeReportDTO thisOverageVarianceGT2 = stocktakeProcessMapper.getStocktakeVariance(headInfo.getThisStocktakeCd(),
                headInfo.getThisStocktakeDate(),headInfo.getStoreCd(),"GT","02");

        if (thisOverageVarianceGT2==null) {
            thisOverageVarianceGT2 = new StocktakeReportDTO();
        }

        // 第21行 列表
        row = sheet.createRow(21);
        SXSSFCell row21Cell2 = row.createCell(8);
        row21Cell2.setCellValue("Food Service");
        row21Cell2.setCellStyle(style6);

        SXSSFCell row21Cell3 = row.createCell(10);
        row21Cell3.setCellValue(getNumber0(thisOverageVarianceGT2.getSku()));
        row21Cell3.setCellStyle(style8);

        SXSSFCell row21Cell4 = row.createCell(11);
        row21Cell4.setCellValue(getNumber0(thisOverageVarianceGT2.getQty()));
        row21Cell4.setCellStyle(style8);

        SXSSFCell row21Cell5 = row.createCell(12);
        row21Cell5.setCellValue(getNumber0(thisOverageVarianceGT2.getAmt()));
        row21Cell5.setCellStyle(style8);

        /**
         * 获取盘点的差异商品, 多出或者缺少的
         * GT: 多出的
         * LT: 缺少的
         */
        StocktakeReportDTO lastOverageVarianceGT2 = null;
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastOverageVarianceGT2 = stocktakeProcessMapper.getStocktakeVariance(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(),headInfo.getStoreCd(),"GT","02");

            if (lastOverageVarianceGT2==null) {
                lastOverageVarianceGT2 = new StocktakeReportDTO();
            }

            SXSSFCell row21Cell6 = row.createCell(14);
            row21Cell6.setCellValue(getNumber0(lastOverageVarianceGT2.getSku()));
            row21Cell6.setCellStyle(style8);

            SXSSFCell row21Cell7 = row.createCell(15);
            row21Cell7.setCellValue(getNumber0(lastOverageVarianceGT2.getQty()));
            row21Cell7.setCellStyle(style8);

            SXSSFCell row21Cell8 = row.createCell(16);
            row21Cell8.setCellValue(getNumber0(lastOverageVarianceGT2.getAmt()));
            row21Cell8.setCellStyle(style8);
        } else {
            SXSSFCell row21Cell6 = row.createCell(14);
            row21Cell6.setCellStyle(style8);

            SXSSFCell row21Cell7 = row.createCell(15);
            row21Cell7.setCellStyle(style8);

            SXSSFCell row21Cell8 = row.createCell(16);
            row21Cell8.setCellStyle(style8);
        }


        // 第22行 列表
        row = sheet.createRow(22);
        SXSSFCell row22Cell2 = row.createCell(8);
        row22Cell2.setCellValue("Total Overage");
        row22Cell2.setCellStyle(style6);

        // 本次盘点多出的数据
        long thisOverageTotalSku = getNumber0(thisOverageVarianceGT.getSku())+getNumber0(thisOverageVarianceGT2.getSku());
        SXSSFCell row22Cell3 = row.createCell(10);
        row22Cell3.setCellValue(thisOverageTotalSku);
        row22Cell3.setCellStyle(style5);

        long thisOverageTotalQty = getNumber0(thisOverageVarianceGT.getQty())+getNumber0(thisOverageVarianceGT2.getQty());
        SXSSFCell row22Cell4 = row.createCell(11);
        row22Cell4.setCellValue(thisOverageTotalQty);
        row22Cell4.setCellStyle(style5);

        long thisOverageTotalAmt = getNumber0(thisOverageVarianceGT.getAmt())+getNumber0(thisOverageVarianceGT2.getAmt());
        SXSSFCell row22Cell5 = row.createCell(12);
        row22Cell5.setCellValue(thisOverageTotalAmt);
        row22Cell5.setCellStyle(style5);

        // 上一次盘点的所有多出的商品
        long lastOverageTotalSku = 0L;
        long lastOverageTotalQty = 0L;
        long lastOverageTotalAmt = 0L;
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastOverageTotalSku = getNumber0(lastOverageVarianceGT.getSku())+getNumber0(lastOverageVarianceGT2.getSku());
            SXSSFCell row22Cell6 = row.createCell(14);
            row22Cell6.setCellValue(lastOverageTotalSku);
            row22Cell6.setCellStyle(style5);

            lastOverageTotalQty = getNumber0(lastOverageVarianceGT.getQty())+getNumber0(lastOverageVarianceGT2.getQty());
            SXSSFCell row22Cell7 = row.createCell(15);
            row22Cell7.setCellValue(lastOverageTotalQty);
            row22Cell7.setCellStyle(style5);

            lastOverageTotalAmt = getNumber0(lastOverageVarianceGT.getAmt())+getNumber0(lastOverageVarianceGT2.getAmt());
            SXSSFCell row22Cell8 = row.createCell(16);
            row22Cell8.setCellValue(lastOverageTotalAmt);
            row22Cell8.setCellStyle(style5);
        } else {
            SXSSFCell row22Cell6 = row.createCell(14);
            row22Cell6.setCellStyle(style5);

            SXSSFCell row22Cell7 = row.createCell(15);
            row22Cell7.setCellStyle(style5);

            SXSSFCell row22Cell8 = row.createCell(16);
            row22Cell8.setCellStyle(style5);
        }



        // 第23行 列表
        row = sheet.createRow(23);
        SXSSFCell row23Cell2 = row.createCell(8);
        row23Cell2.setCellValue("% Overage/Book Value");
        row23Cell2.setCellStyle(style6);

        // 两个数相除
        double thisOverageBookValSku = divisionFun(thisOverageTotalSku,getNumber0(thisBookValue.getSku()));
        SXSSFCell row23Cell3 = row.createCell(10);
        row23Cell3.setCellValue(thisOverageBookValSku);
        row23Cell3.setCellStyle(style9);

        // 两个数相除
        double thisOverageBookValQty = divisionFun(thisOverageTotalQty,getNumber0(thisBookValue.getBookValQty()));
        SXSSFCell row23Cell4 = row.createCell(11);
        row23Cell4.setCellValue(thisOverageBookValQty);
        row23Cell4.setCellStyle(style9);

        // 两个数相除
        double thisOverageBookValAmt = divisionFun(thisOverageTotalAmt,getNumber0(thisBookValue.getBookValAmt()));
        SXSSFCell row23Cell5 = row.createCell(12);
        row23Cell5.setCellValue(thisOverageBookValAmt);
        row23Cell5.setCellStyle(style9);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 两个数相除
            double lastOverageBookValSku = divisionFun(lastOverageTotalSku,getNumber0(lastBookValue.getSku()));
            SXSSFCell row23Cell6 = row.createCell(14);
            row23Cell6.setCellValue(lastOverageBookValSku);
            row23Cell6.setCellStyle(style9);

            // 两个数相除
            double lastOverageBookValQty = divisionFun(lastOverageTotalQty,getNumber0(lastBookValue.getBookValQty()));
            SXSSFCell row23Cell7 = row.createCell(15);
            row23Cell7.setCellValue(lastOverageBookValQty);
            row23Cell7.setCellStyle(style9);

            // 两个数相除
            double lastOverageBookValAmt = divisionFun(lastOverageTotalAmt,getNumber0(lastBookValue.getBookValAmt()));
            SXSSFCell row23Cell8 = row.createCell(16);
            row23Cell8.setCellValue(lastOverageBookValAmt);
            row23Cell8.setCellStyle(style9);
        }  else {
            // 两个数相除
            SXSSFCell row23Cell6 = row.createCell(14);
            row23Cell6.setCellStyle(style9);

            // 两个数相除
            SXSSFCell row23Cell7 = row.createCell(15);
            row23Cell7.setCellStyle(style9);

            // 两个数相除
            SXSSFCell row23Cell8 = row.createCell(16);
            row23Cell8.setCellStyle(style9);
        }


        // 设置区域, 加边框
        CellRangeAddress region9 = new CellRangeAddress(22,23,10,12);
        CellRangeAddress region10 = new CellRangeAddress(22,23,14,16);
        setRegionUtil(region9,sheet,wb);
        setRegionUtil(region10,sheet,wb);

        // 第25行 列表 Overage 部分
        row = sheet.createRow(25);
        SXSSFCell row25Cell2 = row.createCell(8);
        row25Cell2.setCellValue("Total Variance");
        row25Cell2.setCellStyle(style6);

        // 第26行 列表
        row = sheet.createRow(26);
        SXSSFCell row26Cell2 = row.createCell(8);
        row26Cell2.setCellValue("General Merchandise");
        row26Cell2.setCellStyle(style6);

        // 两个数相加
        long thisMerchandiseVarianceTotalSku = additionFun(thisShortageVarianceLT.getSku(),thisOverageVarianceGT.getSku());
        SXSSFCell row26Cell3 = row.createCell(10);
        row26Cell3.setCellValue(thisMerchandiseVarianceTotalSku);
        row26Cell3.setCellStyle(style8);

        // 两个数相加
        long thisMerchandiseVarianceTotalQty = additionFun(thisShortageVarianceLT.getQty(),thisOverageVarianceGT.getQty());
        SXSSFCell row26Cell4 = row.createCell(11);
        row26Cell4.setCellValue(thisMerchandiseVarianceTotalQty);
        row26Cell4.setCellStyle(style8);

        // 两个数相加
        long thisMerchandiseVarianceTotalAmt = additionFun(thisShortageVarianceLT.getAmt(),thisOverageVarianceGT.getAmt());
        SXSSFCell row26Cell5 = row.createCell(12);
        row26Cell5.setCellValue(thisMerchandiseVarianceTotalAmt);
        row26Cell5.setCellStyle(style8);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 两个数相加
            long lastMerchandiseVarianceTotalSku = additionFun(lastShortageVarianceLT.getSku(),lastOverageVarianceGT.getSku());
            SXSSFCell row26Cell6 = row.createCell(14);
            row26Cell6.setCellValue(lastMerchandiseVarianceTotalSku);
            row26Cell6.setCellStyle(style8);

            // 两个数相加
            long lastMerchandiseVarianceTotalQty = additionFun(lastShortageVarianceLT.getQty(),lastOverageVarianceGT.getQty());
            SXSSFCell row26Cell7 = row.createCell(15);
            row26Cell7.setCellValue(lastMerchandiseVarianceTotalQty);
            row26Cell7.setCellStyle(style8);

            // 两个数相加
            long lastMerchandiseVarianceTotalAmt = additionFun(lastShortageVarianceLT.getAmt(),lastOverageVarianceGT.getAmt());
            SXSSFCell row26Cell8 = row.createCell(16);
            row26Cell8.setCellValue(lastMerchandiseVarianceTotalAmt);
            row26Cell8.setCellStyle(style8);
        } else {
            SXSSFCell row26Cell6 = row.createCell(14);
            row26Cell6.setCellStyle(style8);

            SXSSFCell row26Cell7 = row.createCell(15);
            row26Cell7.setCellStyle(style8);

            SXSSFCell row26Cell8 = row.createCell(16);
            row26Cell8.setCellStyle(style8);
        }

        // 第27行 列表
        row = sheet.createRow(27);
        SXSSFCell row27Cell2 = row.createCell(8);
        row27Cell2.setCellValue("Food Service");
        row27Cell2.setCellStyle(style6);

        // 两个数相加
        long thisServiceVarianceTotalSku = additionFun(thisShortageVarianceLT2.getSku(),thisOverageVarianceGT2.getSku());
        SXSSFCell row27Cell3 = row.createCell(10);
        row27Cell3.setCellValue(thisServiceVarianceTotalSku);
        row27Cell3.setCellStyle(style8);

        // 两个数相加
        long thisServiceVarianceTotalQty = additionFun(thisShortageVarianceLT2.getQty(),thisOverageVarianceGT2.getQty());
        SXSSFCell row27Cell4 = row.createCell(11);
        row27Cell4.setCellValue(thisServiceVarianceTotalQty);
        row27Cell4.setCellStyle(style8);

        // 两个数相加
        long thisServiceVarianceTotalAmt = additionFun(thisShortageVarianceLT2.getAmt(),thisOverageVarianceGT2.getAmt());
        SXSSFCell row27Cell5 = row.createCell(12);
        row27Cell5.setCellValue(thisServiceVarianceTotalAmt);
        row27Cell5.setCellStyle(style8);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 两个数相加
            long lastServiceVarianceTotalSku = additionFun(lastShortageVarianceLT2.getSku(),lastOverageVarianceGT2.getSku());
            SXSSFCell row27Cell6 = row.createCell(14);
            row27Cell6.setCellValue(lastServiceVarianceTotalSku);
            row27Cell6.setCellStyle(style8);

            // 两个数相加
            long lastServiceVarianceTotalQty = additionFun(lastShortageVarianceLT2.getQty(),lastOverageVarianceGT2.getQty());
            SXSSFCell row27Cell7 = row.createCell(15);
            row27Cell7.setCellValue(lastServiceVarianceTotalQty);
            row27Cell7.setCellStyle(style8);

            // 两个数相加
            long lastServiceVarianceTotalAmt = additionFun(lastShortageVarianceLT2.getAmt(),lastOverageVarianceGT2.getAmt());
            SXSSFCell row27Cell8 = row.createCell(16);
            row27Cell8.setCellValue(lastServiceVarianceTotalAmt);
            row27Cell8.setCellStyle(style8);
        } else {
            SXSSFCell row27Cell6 = row.createCell(14);
            row27Cell6.setCellStyle(style8);

            SXSSFCell row27Cell7 = row.createCell(15);
            row27Cell7.setCellStyle(style8);

            SXSSFCell row27Cell8 = row.createCell(16);
            row27Cell8.setCellStyle(style8);
        }

        // 第28行 列表
        row = sheet.createRow(28);
        SXSSFCell row28Cell2 = row.createCell(8);
        row28Cell2.setCellValue("Total IV");
        row28Cell2.setCellStyle(style6);

        long thisTotalVarianceSku = thisTotalSku+thisOverageTotalSku;
        SXSSFCell row28Cell3 = row.createCell(10);
        row28Cell3.setCellValue(thisTotalVarianceSku);
        row28Cell3.setCellStyle(style5);

        long thisTotalVarianceQty = thisTotalQty+thisOverageTotalQty;
        SXSSFCell row28Cell4 = row.createCell(11);
        row28Cell4.setCellValue(thisTotalVarianceQty);
        row28Cell4.setCellStyle(style5);

        long thisTotalVarianceAmt = thisTotalAmt+thisOverageTotalAmt;
        SXSSFCell row28Cell5 = row.createCell(12);
        row28Cell5.setCellValue(thisTotalVarianceAmt);
        row28Cell5.setCellStyle(style5);

        long lastTotalVarianceSku = 0L;
        long lastTotalVarianceQty = 0L;
        long lastTotalVarianceAmt = 0L;
        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastTotalVarianceSku = lastTotalSku+lastOverageTotalSku;
            SXSSFCell row28Cell6 = row.createCell(14);
            row28Cell6.setCellValue(lastTotalVarianceSku);
            row28Cell6.setCellStyle(style5);

            lastTotalVarianceQty = lastTotalQty+lastOverageTotalQty;
            SXSSFCell row28Cell7 = row.createCell(15);
            row28Cell7.setCellValue(lastTotalVarianceQty);
            row28Cell7.setCellStyle(style5);

            lastTotalVarianceAmt = lastTotalAmt+lastOverageTotalAmt;
            SXSSFCell row28Cell8 = row.createCell(16);
            row28Cell8.setCellValue(lastTotalVarianceAmt);
            row28Cell8.setCellStyle(style5);
        } else {
            SXSSFCell row28Cell6 = row.createCell(14);
            row28Cell6.setCellStyle(style5);

            SXSSFCell row28Cell7 = row.createCell(15);
            row28Cell7.setCellStyle(style5);

            SXSSFCell row28Cell8 = row.createCell(16);
            row28Cell8.setCellStyle(style5);
        }



        // 第29行 列表
        row = sheet.createRow(29);
        SXSSFCell row29Cell2 = row.createCell(8);
        row29Cell2.setCellValue("Average IV  per Day");
        row29Cell2.setCellStyle(style6);

        long thisAvgSku = 0L;
        long thisAvgQty = 0L;
        long thisAvgAmt = 0L;
        long lastAvgSku = 0L;
        long lastAvgQty = 0L;
        long lastAvgAmt = 0L;

        // 计算上一次盘点到这一次盘点的平均量
        if (!StringUtils.isEmpty(headInfo.getDay()) && !"0".equals(headInfo.getDay())) {
            long day = Long.valueOf(headInfo.getDay());
            thisAvgSku = thisTotalVarianceSku/day;
            thisAvgQty = thisTotalVarianceQty/day;
            thisAvgAmt = thisTotalVarianceAmt/day;
        }

        SXSSFCell row29Cell3 = row.createCell(10);
        row29Cell3.setCellStyle(style5);
        row29Cell3.setCellValue(thisAvgSku);

        SXSSFCell row29Cell4 = row.createCell(11);
        row29Cell4.setCellValue(thisAvgQty);
        row29Cell4.setCellStyle(style5);

        SXSSFCell row29Cell5 = row.createCell(12);
        row29Cell5.setCellValue(thisAvgAmt);
        row29Cell5.setCellStyle(style5);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 获取上一次盘点 距离 上上一次盘点间隔多少天
            StocktakeReportDTO lastHeadInfo = stocktakeProcessMapper.getStocktakingHead(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(), headInfo.getStoreCd());
            if (lastHeadInfo != null) {
                // 计算上一次盘点到上上一次盘点的平均量
                if (!StringUtils.isEmpty(lastHeadInfo.getDay()) && !"0".equals(lastHeadInfo.getDay())) {
                    long day = Long.valueOf(lastHeadInfo.getDay());
                    lastAvgSku = thisTotalVarianceSku/day;
                    lastAvgQty = thisTotalVarianceQty/day;
                    lastAvgAmt = thisTotalVarianceAmt/day;
                }
            }

            SXSSFCell row29Cell6 = row.createCell(14);
            row29Cell6.setCellValue(lastAvgSku);
            row29Cell6.setCellStyle(style5);

            SXSSFCell row29Cell7 = row.createCell(15);
            row29Cell7.setCellValue(lastAvgQty);
            row29Cell7.setCellStyle(style5);

            SXSSFCell row29Cell8 = row.createCell(16);
            row29Cell8.setCellValue(lastAvgAmt);
            row29Cell8.setCellStyle(style5);
        } else {
            SXSSFCell row29Cell6 = row.createCell(14);
            row29Cell6.setCellStyle(style5);

            SXSSFCell row29Cell7 = row.createCell(15);
            row29Cell7.setCellStyle(style5);

            SXSSFCell row29Cell8 = row.createCell(16);
            row29Cell8.setCellStyle(style5);
        }

        // 设置区域, 加边框
        CellRangeAddress region11 = new CellRangeAddress(28,29,10,12);
        CellRangeAddress region12 = new CellRangeAddress(28,29,14,16);
        setRegionUtil(region11,sheet,wb);
        setRegionUtil(region12,sheet,wb);

        // 第30行 列表
        row = sheet.createRow(30);
        SXSSFCell row30Cell2 = row.createCell(8);
        row30Cell2.setCellValue("% IV/Book Value");
        row30Cell2.setCellStyle(style6);

        if (thisBookValue.getSku() == null) {
            thisBookValue.setSku(new BigDecimal("0"));
        }
        double thisTotalVarianceSkuPer = divisionFun(thisTotalVarianceSku, thisBookValue.getSku().longValue());
        SXSSFCell row30Cell3 = row.createCell(10);
        row30Cell3.setCellValue(thisTotalVarianceSkuPer);
        row30Cell3.setCellStyle(style9);

        if (thisBookValue.getQty() == null) {
            thisBookValue.setQty(new BigDecimal("0"));
        }
        double thisTotalVarianceQtyPer = divisionFun(thisTotalVarianceQty, thisBookValue.getQty().longValue());
        SXSSFCell row30Cell4 = row.createCell(11);
        row30Cell4.setCellValue(thisTotalVarianceQtyPer);
        row30Cell4.setCellStyle(style9);

        if (thisBookValue.getQty() == null) {
            thisBookValue.setQty(new BigDecimal("0"));
        }
        double thisTotalVarianceAmtPer = divisionFun(thisTotalVarianceAmt, thisBookValue.getAmt().longValue());
        SXSSFCell row30Cell5 = row.createCell(12);
        row30Cell5.setCellValue(thisTotalVarianceAmtPer);
        row30Cell5.setCellStyle(style9);

        // 上一次盘点数据
        double lastTotalVarianceSkuPer = 0;
        double lastTotalVarianceQtyPer = 0;
        double lastTotalVarianceAmtPer = 0;
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            if (lastBookValue.getSku() == null) {
                lastBookValue.setSku(new BigDecimal("0"));
            }
            lastTotalVarianceSkuPer = divisionFun(lastTotalVarianceSku, lastBookValue.getSku().longValue());
            SXSSFCell row30Cell6 = row.createCell(14);
            row30Cell6.setCellValue(lastTotalVarianceSkuPer);
            row30Cell6.setCellStyle(style9);

            if (lastBookValue.getQty() == null) {
                lastBookValue.setQty(new BigDecimal("0"));
            }
            lastTotalVarianceQtyPer = divisionFun(lastTotalVarianceQty, lastBookValue.getQty().longValue());
            SXSSFCell row30Cell7 = row.createCell(15);
            row30Cell7.setCellValue(lastTotalVarianceQtyPer);
            row30Cell7.setCellStyle(style9);

            if (lastBookValue.getAmt() == null) {
                lastBookValue.setAmt(new BigDecimal("0"));
            }
            lastTotalVarianceAmtPer = divisionFun(lastTotalVarianceAmt, lastBookValue.getAmt().longValue());
            SXSSFCell row30Cell8 = row.createCell(16);
            row30Cell8.setCellValue(lastTotalVarianceAmtPer);
            row30Cell8.setCellStyle(style9);
        } else {
            SXSSFCell row30Cell6 = row.createCell(14);
            row30Cell6.setCellStyle(style9);

            SXSSFCell row30Cell7 = row.createCell(15);
            row30Cell7.setCellStyle(style9);

            SXSSFCell row30Cell8 = row.createCell(16);
            row30Cell8.setCellStyle(style9);
        }



        // 第31行 列表
        row = sheet.createRow(31);
        SXSSFCell row31Cell2 = row.createCell(8);
        row31Cell2.setCellValue("% Without IV");
        row31Cell2.setCellStyle(style6);

        // 公式 100 - % IV/Book Value 例如: 100% - 14.06% = 85.94%
        // 因为 poi 设置 百分比格式会直接把数值 * 100 来显示, 取得的原数据时 是没有 * 100 的
        // 所以 变成 1 - % IV/Book Value  例如 1 - 0.1406 = 0.8594, poi 的百分比格式会自动 *100 显示 85.94%
        double thisWithoutIvSku = (1 - Math.abs(thisTotalVarianceSkuPer));
        SXSSFCell row31Cell3 = row.createCell(10);
        row31Cell3.setCellValue(thisWithoutIvSku);
        row31Cell3.setCellStyle(style9);

        double thisWithoutIvQty = (1 - Math.abs(thisTotalVarianceQtyPer));
        SXSSFCell row31Cell4 = row.createCell(11);
        row31Cell4.setCellValue(thisWithoutIvQty);
        row31Cell4.setCellStyle(style9);

        double thisWithoutIvAmt = (1 - Math.abs(thisTotalVarianceAmtPer));
        SXSSFCell row31Cell5 = row.createCell(12);
        row31Cell5.setCellValue(thisWithoutIvAmt);
        row31Cell5.setCellStyle(style9);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 公式 100 - % IV/Book Value 例如: 100% - 14.06% = 85.94%
            // 因为 poi 设置 百分比格式会直接把数值 * 100 来显示, 取得的原数据时 是没有 * 100 的
            // 所以 变成 1 - % IV/Book Value  例如 1 - 0.1406 = 0.8594, poi 的百分比格式会自动 *100 显示 85.94%
            double lastWithoutIvSku = (1 - Math.abs(lastTotalVarianceSkuPer));
            SXSSFCell row31Cell6 = row.createCell(14);
            row31Cell6.setCellValue(lastWithoutIvSku);
            row31Cell6.setCellStyle(style9);

            double lastWithoutIvQty = (1 - Math.abs(lastTotalVarianceQtyPer));
            SXSSFCell row31Cell7 = row.createCell(15);
            row31Cell7.setCellValue(lastWithoutIvQty);
            row31Cell7.setCellStyle(style9);

            double lastWithoutIvAmt = (1 - Math.abs(lastTotalVarianceAmtPer));
            SXSSFCell row31Cell8 = row.createCell(16);
            row31Cell8.setCellValue(lastWithoutIvAmt);
            row31Cell8.setCellStyle(style9);
        } else {
            SXSSFCell row31Cell6 = row.createCell(14);
            row31Cell6.setCellValue("");
            row31Cell6.setCellStyle(style9);

            SXSSFCell row31Cell7 = row.createCell(15);
            row31Cell7.setCellValue("");
            row31Cell7.setCellStyle(style9);

            SXSSFCell row31Cell8 = row.createCell(16);
            row31Cell8.setCellValue("");
            row31Cell8.setCellStyle(style9);
        }

        // 第32行 列表
        row = sheet.createRow(32);
        SXSSFCell row32Cell2 = row.createCell(8);
        row32Cell2.setCellValue("Book Value");
        row32Cell2.setCellStyle(style6);

        SXSSFCell row32Cell3 = row.createCell(10);
        row32Cell3.setCellValue(getNumber0(thisBookValue.getSku()));
        row32Cell3.setCellStyle(style5);

        SXSSFCell row32Cell4 = row.createCell(11);
        row32Cell4.setCellValue(getNumber0(thisBookValue.getBookValQty()));
        row32Cell4.setCellStyle(style5);

        SXSSFCell row32Cell5 = row.createCell(12);
        row32Cell5.setCellValue(getNumber0(thisBookValue.getBookValAmt()));
        row32Cell5.setCellStyle(style5);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            SXSSFCell row32Cell6 = row.createCell(14);
            row32Cell6.setCellValue(getNumber0(lastBookValue.getSku()));
            row32Cell6.setCellStyle(style5);

            SXSSFCell row32Cell7 = row.createCell(15);
            row32Cell7.setCellValue(getNumber0(lastBookValue.getBookValQty()));
            row32Cell7.setCellStyle(style5);

            SXSSFCell row32Cell8 = row.createCell(16);
            row32Cell8.setCellValue(getNumber0(lastBookValue.getBookValAmt()));
            row32Cell8.setCellStyle(style5);
        } else {
            SXSSFCell row32Cell6 = row.createCell(14);
            row32Cell6.setCellStyle(style5);

            SXSSFCell row32Cell7 = row.createCell(15);
            row32Cell7.setCellStyle(style5);

            SXSSFCell row32Cell8 = row.createCell(16);
            row32Cell8.setCellStyle(style5);
        }

        // 设置区域, 加边框
        CellRangeAddress region13 = new CellRangeAddress(30,32,10,12);
        CellRangeAddress region14 = new CellRangeAddress(30,32,14,16);
        setRegionUtil(region13,sheet,wb);
        setRegionUtil(region14,sheet,wb);

        // 第33行 列表
        row = sheet.createRow(33);
        SXSSFCell row33Cell2 = row.createCell(8);

        row33Cell2.setCellValue("1/3 Sale during the stock-take");
        row33Cell2.setCellStyle(style6);

        // 获取盘点期间 1/3 的销售额
        Integer saleAmt = stocktakeProcessMapper._getStocktakePeriodSaleAmt(
                headInfo.getThisStocktakeDate(),
                headInfo.getStoreCd(),
                headInfo.getThisStartTime(),
                headInfo.getThisEndTime());
        if (saleAmt == null) {
            saleAmt = 0;
        }
        double _thisSaleQty = saleAmt/3;
        SXSSFCell row33Cell5 = row.createCell(10);
        row33Cell5.setCellValue(_thisSaleQty);
        row33Cell5.setCellStyle(style5);

        // 上一次盘点数据
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 获取上一次盘点期间 1/3 的销售额
            Integer lastSaleAmt = stocktakeProcessMapper._getStocktakePeriodSaleAmt(
                    headInfo.getLastStocktakeDate(),
                    headInfo.getStoreCd(),
                    headInfo.getLastStartTime(),
                    headInfo.getLastEndTime());
            if (lastSaleAmt == null) {
                lastSaleAmt = 0;
            }
            double _lastSaleQty = lastSaleAmt/3;
            SXSSFCell row33Cell8 = row.createCell(14);
            row33Cell8.setCellValue(_lastSaleQty);
            row33Cell8.setCellStyle(style5);
        } else {
            SXSSFCell row33Cell8 = row.createCell(14);
            row33Cell8.setCellStyle(style5);
        }

        // 设置区域, 加边框
        CellRangeAddress region15 = new CellRangeAddress(33,33,10,12);
        CellRangeAddress region16 = new CellRangeAddress(33,33,14,16);
        sheet.addMergedRegion(region15);
        sheet.addMergedRegion(region16);
        setRegionUtil(region15,sheet,wb);
        setRegionUtil(region16,sheet,wb);

        // 第34行 列表
        row = sheet.createRow(34);
        SXSSFCell row34Cell2 = row.createCell(8);
        row34Cell2.setCellValue("Sales from last counting to this counting");
        row34Cell2.setCellStyle(style6);

        // 获取上次盘点到这次盘点的总销售额
        Integer thisSaleAmt = 0;
        Integer _lastToThisSaleAmt = 0;
        if (StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            // 没有上次盘点数据就为 0
            SXSSFCell row34Cell5 = row.createCell(10);
            row34Cell5.setCellValue(0);
            row34Cell5.setCellStyle(style5);

            SXSSFCell row34Cell8 = row.createCell(14);
            row34Cell8.setCellStyle(style5);
        } else {
            thisSaleAmt = stocktakeProcessMapper.getLastToThisSaleAmt(headInfo);
            SXSSFCell row34Cell5 = row.createCell(10);
            if (thisSaleAmt==null) {
                thisSaleAmt = 0;
            }
            row34Cell5.setCellValue(thisSaleAmt);
            row34Cell5.setCellStyle(style5);

            // 获取上一次盘点的数据
            StocktakeReportDTO _lastHead = stocktakeProcessMapper.getStocktakingHead(headInfo.getLastStocktakeCd(),
                    headInfo.getLastStocktakeDate(), headInfo.getStoreCd());
            if (_lastHead == null || StringUtils.isEmpty(_lastHead.getLastStocktakeCd())) {
                SXSSFCell row34Cell8 = row.createCell(14);
                row34Cell8.setCellValue(0);
                row34Cell8.setCellStyle(style5);
            } else {
                // 获取上一次到上上一次的销售数据
                _lastToThisSaleAmt = stocktakeProcessMapper.getLastToThisSaleAmt(_lastHead);
                if (_lastToThisSaleAmt==null) {
                    _lastToThisSaleAmt = 0;
                }
                SXSSFCell row34Cell8 = row.createCell(14);
                row34Cell8.setCellValue(_lastToThisSaleAmt);
                row34Cell8.setCellStyle(style5);
            }
        }


        // 设置区域, 加边框
        CellRangeAddress region17 = new CellRangeAddress(34,34,10,12);
        CellRangeAddress region18 = new CellRangeAddress(34,34,14,16);
        sheet.addMergedRegion(region17);
        sheet.addMergedRegion(region18);
        setRegionUtil(region17,sheet,wb);
        setRegionUtil(region18,sheet,wb);

        // 第35行 列表
        row = sheet.createRow(35);
        SXSSFCell row35Cell2 = row.createCell(8);
        row35Cell2.setCellValue("% Variance");
        row35Cell2.setCellStyle(style6);

        BigDecimal thisVarianceAmt = new BigDecimal("0");
        if (thisSaleAmt!=0) {
            thisVarianceAmt = new BigDecimal(String.valueOf(thisTotalVarianceAmt)).
                    divide(new BigDecimal(String.valueOf(thisSaleAmt)), 3, BigDecimal.ROUND_HALF_UP);
            // thisVarianceAmt = (thisTotalVarianceAmt/thisSaleAmt);
        }
        SXSSFCell row35Cell5 = row.createCell(10);
        row35Cell5.setCellValue(thisVarianceAmt.doubleValue());
        row35Cell5.setCellStyle(style9);

        BigDecimal lastVarianceAmt = new BigDecimal("0");
        SXSSFCell row35Cell8 = row.createCell(14);
        if (StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            row35Cell8.setCellValue("");
            row35Cell8.setCellStyle(style9);
        } else {
            if (_lastToThisSaleAmt!=0) {
                lastVarianceAmt = new BigDecimal(String.valueOf(lastTotalVarianceAmt)).
                        divide(new BigDecimal(String.valueOf(_lastToThisSaleAmt)), 3, BigDecimal.ROUND_HALF_UP);
                // lastVarianceAmt = (lastTotalVarianceAmt/_lastToThisSaleAmt);
            }
            row35Cell8.setCellValue(lastVarianceAmt.doubleValue());
            row35Cell8.setCellStyle(style9);
        }

        // 设置区域, 加边框
        CellRangeAddress region20 = new CellRangeAddress(35,35,10,12);
        CellRangeAddress region19 = new CellRangeAddress(35,35,14,16);
        sheet.addMergedRegion(region20);
        sheet.addMergedRegion(region19);
        setRegionUtil(region20,sheet,wb);
        setRegionUtil(region19,sheet,wb);

        // 第36行 列表
        row = sheet.createRow(36);
        SXSSFCell row36Cell2 = row.createCell(8);
        row36Cell2.setCellValue("Bad Merchandize");
        row36Cell2.setCellStyle(style6);

        // 本次盘点
        int thisBadQty = stocktakeProcessMapper.getBadQty(headInfo.getThisStocktakeCd(),headInfo.getThisStocktakeDate(),storeCd);

        SXSSFCell row36Cell5 = row.createCell(10);
        row36Cell5.setCellValue(thisBadQty);
        row36Cell5.setCellStyle(style5);

        // 上一次盘点
        int lastBadQty = 0;
        if (!StringUtils.isEmpty(headInfo.getLastStocktakeCd())) {
            lastBadQty = stocktakeProcessMapper.getBadQty(headInfo.getLastStocktakeCd(),headInfo.getLastStocktakeDate(),storeCd);
        }

        SXSSFCell row36Cell8 = row.createCell(14);
        row36Cell8.setCellValue(lastBadQty);
        row36Cell8.setCellStyle(style5);
        /*后续添加 Bad Merchandize数据, 现在画面没有处理 end*/

        // 设置区域, 加边框
        CellRangeAddress region21 = new CellRangeAddress(36,36,10,12);
        CellRangeAddress region22 = new CellRangeAddress(36,36,14,16);
        sheet.addMergedRegion(region21);
        sheet.addMergedRegion(region22);
        setRegionUtil(region21,sheet,wb);
        setRegionUtil(region22,sheet,wb);

        // 第十行SUMMARY 数据 SUMMARY NOTES 部分
        row = sheet.createRow(38);
        SXSSFCell row38Cell0 = row.createCell(1);
        row38Cell0.setCellValue("SUMMARY NOTES");
        row38Cell0.setCellStyle(style3);

        // 设置区域, 加边框
        CellRangeAddress region23 = new CellRangeAddress(40,64,1,16);
        setRegionUtil(region23,sheet,wb);

        // row66Cell0 部分
        row = sheet.createRow(66);
        SXSSFCell row66Cell0 = row.createCell(1);
        row66Cell0.setCellValue("FINDINGS AND ISSUES");
        row66Cell0.setCellStyle(style3);

        // row66Cell0 部分
        row = sheet.createRow(68);
        SXSSFCell row68Cell0 = row.createCell(1);
        row68Cell0.setCellValue("Findings/Issues");
        row68Cell0.setCellStyle(style3);

        // 画一个表格区域
        int i = 10;
        CellRangeAddress region24 = new CellRangeAddress(69,(i+69)-1,1,16);
        setRegionUtil(region24,sheet,wb);
        // 遍历 10 行列表
        for (int i1 = 0; i1 < i; i1++) {
            row = sheet.createRow((69+i1));
            CellRangeAddress region25 = new CellRangeAddress((i1+69),(i1+69),2,4);
            CellRangeAddress region26 = new CellRangeAddress((i1+69),(i1+69),5,8);
            CellRangeAddress region27 = new CellRangeAddress((i1+69),(i1+69),9,12);
            CellRangeAddress region28 = new CellRangeAddress((i1+69),(i1+69),15,16);
            CellRangeAddress region29 = new CellRangeAddress((i1+69),(i1+69),1,1);
            CellRangeAddress region30 = new CellRangeAddress((i1+69),(i1+69),13,14);
            sheet.addMergedRegion(region25);
            sheet.addMergedRegion(region26);
            sheet.addMergedRegion(region27);
            sheet.addMergedRegion(region28);
            sheet.addMergedRegion(region30);
            setRegionUtil(region25,sheet,wb);
            setRegionUtil(region26,sheet,wb);
            setRegionUtil(region27,sheet,wb);
            setRegionUtil(region28,sheet,wb);
            setRegionUtil(region29,sheet,wb);
            setRegionUtil(region30,sheet,wb);

            // 标题行
            if (i1 == 0) {
                SXSSFCell cell1 = row.createCell(1);
                cell1.setCellValue("No.");
                cell1.setCellStyle(style7);

                SXSSFCell cell2 = row.createCell(2);
                cell2.setCellValue("Details");
                cell2.setCellStyle(style7);

                SXSSFCell cell3 = row.createCell(5);
                cell3.setCellValue("Recommendation");
                cell3.setCellStyle(style7);

                SXSSFCell cell4 = row.createCell(9);
                cell4.setCellValue("Person In Charge");
                cell4.setCellStyle(style7);

                SXSSFCell cell5 = row.createCell(13);
                cell5.setCellValue("Deadline");
                cell5.setCellStyle(style7);

                SXSSFCell cell6 = row.createCell(15);
                cell6.setCellValue("Status");
                cell6.setCellStyle(style7);
            } else {
                SXSSFCell cell1 = row.createCell(1);
                cell1.setCellStyle(style10);

                SXSSFCell cell2 = row.createCell(2);
                cell2.setCellStyle(style10);

                SXSSFCell cell3 = row.createCell(5);
                cell3.setCellStyle(style10);

                SXSSFCell cell4 = row.createCell(9);
                cell4.setCellStyle(style10);

                SXSSFCell cell5 = row.createCell(13);
                cell5.setCellStyle(style10);

                SXSSFCell cell6 = row.createCell(15);
                cell6.setCellStyle(style10);
            }
        }
    }


    // 获取过去 六次 的盘点数据
    private List<List<StocktakeReportByDepDTO>> getVarianceByDep(String piCd, String piDate, String storeCd) {
        // 过去六次的 盘点信息
        List<StocktakeProcessDTO> list = stocktakeProcessMapper.getPastPiCd(piCd,piDate,storeCd);

        if (list == null) {
            return new ArrayList<List<StocktakeReportByDepDTO>>();
        }

        // 用于接收 分组好后的 多个list
        List<List<StocktakeReportByDepDTO>> newList = new ArrayList<List<StocktakeReportByDepDTO>>();
        for (int i = 0; i < list.size(); i++) {
            StocktakeProcessDTO item = list.get(i);
            // 过去的盘点结果 by Department
            List<StocktakeReportByDepDTO> result = stocktakeProcessMapper.getVarianceByDep(
                    item.getPiCd(),item.getPiDate(),item.getStoreCd());
            newList.add(result);
        }

        return newList;
    }


    private long additionFun(BigDecimal arg1, BigDecimal arg2) {
        if (arg1 == null) {
            arg1 = new BigDecimal("0");
        }
        if (arg2 == null) {
            arg2 = new BigDecimal("0");
        }

        return arg1.add(arg2).longValue();
    }

    // 两个数相除
    private double divisionFun(long arg1, long arg2) {
        if (arg1 == 0L || arg2 == 0L) {
            return 0;
        }
        try {
            BigDecimal divide = new BigDecimal(arg1 + "").divide(new BigDecimal(arg2 + ""),3, BigDecimal.ROUND_HALF_UP);
            // return (arg1 / arg2) * 100;
            return divide.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 100059 -> 10:00 AM
    private long getNumber0(BigDecimal num) {
        if (num==null) {
            return 0L;
        }
        return num.longValue();
    }

    // 100059 -> 10:00 AM
    private String getTimeEmpty(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        String hour = str.substring(0, 2);
        String minute = str.substring(2, 4);
        String second = str.substring(4, 6);
        String timeStr = hour+":"+minute+":"+second;
        try {
            Date parse = new SimpleDateFormat("HH:mm:ss").parse(timeStr);
            str = new SimpleDateFormat("HH:mm aa",Locale.ENGLISH).format(parse);
        } catch (ParseException e) {
            str = "";
        }
        return str;
    }

    // 是为空 返回 ""
    private String getValEmpty (String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }
    // 20200810 -> 10-Aug-20
    private Date getDateEmpty (String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        String year = str.substring(0, 4);
        String month = str.substring(4, 6);
        String day = str.substring(6, 8);
        String dateStr = day+"-"+month+"-"+year;
        try {
            Date parse = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
            return parse;
            // str = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH).format(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 合并单元格的边框
    private void setRegionUtil(CellRangeAddress region, Sheet sheet, Workbook wb) {
        // 以下设置合并单元格的边框，避免边框不齐
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, wb);
    }
}
