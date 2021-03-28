package cn.com.bbut.iy.itemmaster.util;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 转换工具
 */
@Slf4j
public class CommonUtils {

    private static final BigDecimal HUNDRED = new BigDecimal(100);

    /** excel样式 **/
    public static Map<String, CellStyle> MAP_STYLE = new HashMap<>();
    /** 文本居中 */
    public static final String STYPE_KEY_1 = "style_1";
    /** 文本左对齐 */
    public static final String STYPE_KEY_2 = "style_2";
    /** 文本右对齐 */
    public static final String STYPE_KEY_3 = "style_3";
    /** 数字右对齐，无小数 */
    public static final String STYPE_KEY_4 = "style_4";
    /** 金额右对齐，两位小数 */
    public static final String STYPE_KEY_5 = "style_5";
    /** 率，右对齐，两位小数，含%号 */
    public static final String STYPE_KEY_6 = "style_6";


    /**
     * 字符串截取为时间样式
     *
     * @param date String yyyyMMdd
     * @return String dd/MM/yyyy
     */
    public static String fmtDateToStr(String date) {
        if (StringUtils.isBlank(date) || date.length()!=8) {
            return "";
        }
        return date.substring(6, 8) + "/" + date.substring(4, 6) + "/"
                + date.substring(0, 4);
    }

    /**
     * 字符串截取为时间样式
     *
     * @param date String yyyyMMddhhmmss
     * @return String dd/MM/yyyy hh:mm:ss
     */
    public static String fmtDateAndTimeToStr(String date) {
        if (StringUtils.isBlank(date) || date.length()!=14) {
            return "";
        }
        return date.substring(6, 8) + "/" + date.substring(4, 6) + "/"
                + date.substring(0, 4)+" "+date.substring(8,10)+":"+date.substring(10,12)+":"+date.substring(12,14);
    }

    //  String 2021-03-20 15:50:07  --> String dd/MM/yyyy hh:mm:ss
    public static String fmtDateAndTimeToStr19(String date) {
        if (StringUtils.isBlank(date)) {
            return "";
        }
        return date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4)  +" "+date.substring(10,19);
    }


    /**
     * 获取审核状态
     * @param status
     * @return
     */
    public static String getAuditStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return "";
        }
        if("1".equals(status)){
            return "Pending";
        }else if ("5".equals(status)){
            return "Rejected";
        }else if ("6".equals(status)){
            return "Withdrawn";
        }else if ("7".equals(status)){
            return "Expired";
        }else if ("10".equals(status)){
            return "Approved";
        }else if ("20".equals(status)){
            return "Returned";
        }
        return "";
    }

    /**
     * 获取传票类型名称
     *
     * @param status
     */
    public static String getTypeName(String status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case "601":
                return "In-store Transfer In";
            case "602":
                return "In-store Transfer Out";
            case "603":
                return "Inventory Write-off";
            case "604":
                return "Stock Adjustment";
            case "605":
                return "Take Stock";
            case "500":
                return "Transfer Instructions";
            case "501":
                return "Store Transfer In";
            case "502":
                return "Store Transfer Out";
            case "506":
                return "Transfer";
            default:
                return "";
        }
    }

    /**
     * 时间戳转换字符串
     *
     * @param date String yyyyMMdd
     * @return String dd/MM/yyyy HH:mm:ss
     */
    public static String dateToString(Date date) {
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String _date = "";
        try{
            _date = df1.format(date);
        }catch (Exception e){
            _date = "";
            log.error(e.getMessage(), e);
        }
        return _date;
    }
    /**
     * String dd/MM/yyyy 转换成yyyyMMdd
     *
     */
    public static String dateToNewFormat(String date){
        String newpayDate = date.replaceAll("/", "");
        String payDays = newpayDate.substring(0, 2);
        String payMonth = newpayDate.substring(2, 4);
        String payYear = newpayDate.substring(4, 8);
        String newDateFomat=payYear+payMonth+payDays;
        return newDateFomat;

    }


    /**
     * 四舍五入,不保留小数,千分位
     */
    public static String formatNum(String num) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        int i = new BigDecimal(num).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        DecimalFormat df = new DecimalFormat("###,###");
        String result = df.format(i);
        return result;
    }

       /**
     * 字符串时间格式化
     *
     * @param date String yyyyMMdd
     * @return String dd/MM/yyyy
     */
    public static String dateConvert(String date) {
        DateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        try{
            Date _date = df1.parse(date);
            df1 = new SimpleDateFormat("dd/MM/yyyy");
            date = df1.format(_date);
        }catch (ParseException e){
            date = "";
            log.error(e.getMessage(), e);
        }
        return date;
    }
    /**
     * 字符串赋值
     *
     * @param cell 单元格对象
     * @param value Integer
     */
    public static void setCellValueNo(Cell cell, Integer value) {
        if (value != null) {
            cell.setCellValue(value.intValue());
        } else {
            cell.setCellValue("");
        }
    }

    /**
     * 字符串赋值
     *
     * @param cell 单元格对象
     * @param value String
     */
    public static void setCellValue(Cell cell, String value) {
        if (value != null) {
            cell.setCellValue(value.trim());
        } else {
            cell.setCellValue("");
        }
    }

    /**
     * 数字赋值
     *
     * @param cell 单元格对象
     * @param value Integer
     */
    public static void setCellValue(Cell cell, Integer value) {
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
    }

    /**
     * 金额赋值
     *
     * @param cell 单元格对象
     * @param value BigDecimal
     */
    public static void setCellValue(Cell cell, BigDecimal value) {
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
    }

    /**
     * 数值为空，赋值为0
     * @param value
     */
    public static BigDecimal setAmountValue(BigDecimal value) {
        if (value != null) {
           return value;
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 率 格式化 乘100，保留2位小数 四舍五入
     *
     * @param value BigDecimal
     */
    public static BigDecimal rateFmt(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.multiply(HUNDRED).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 率 格式化 除100，保留2位小数 四舍五入
     *
     * @param value BigDecimal
     */
    public static BigDecimal moneyFmt(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    /**
     * 指定精度除法运算
     *
     * @param val BigDecimal
     * @param divisor BigDecimal
     * @param scale int
     */
    public static BigDecimal division(BigDecimal val, BigDecimal divisor, int scale) {
        if (val == null) {
            return null;
        }
        if (divisor.doubleValue() == 0) {
            return BigDecimal.ZERO;
        }
        return val.divide(divisor, scale, RoundingMode.HALF_UP);
    }

    /**
     * 生成报表样式
     *
     * @param wb
     */
    public static void createExcelStyleToMap(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName(Constants.DEFAULT_FONT);
        DataFormat df = wb.createDataFormat();// 格式化使用
        MAP_STYLE.clear();

        // 文本居中+边线
        XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("@"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        MAP_STYLE.put(STYPE_KEY_1, style);

        // 文本左对齐+边线
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("@"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_2, style);

        // 文本右对齐+边线
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("@"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_3, style);

        // 数字右对齐，无小数
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("#,###,##0"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_4, style);

        // 价格右对齐，有2位小数
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("#,###,##0.00"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_5, style);

        // 率 右对齐，有2位小数，和%号
        style = (XSSFCellStyle) wb.createCellStyle();
        style.setFont(font);
        style.setDataFormat(df.getFormat("##0.00%"));
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 单元格内容上下对其方式
        MAP_STYLE.put(STYPE_KEY_6, style);
    }
}
