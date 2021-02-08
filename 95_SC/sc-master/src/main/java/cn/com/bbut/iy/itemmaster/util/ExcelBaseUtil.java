package cn.com.bbut.iy.itemmaster.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelBaseUtil {
	private static final Logger logger = LoggerFactory.getLogger(ExcelBaseUtil.class);

	private final static String EXCEL_2003 = ".xls"; // 2003- 版本的excel
	private final static String EXCEL_2007 = ".xlsx"; // 2007+ 版本的excel
	
	/**
	 * 功能描述:创建工作簿
	 *
	 * @author grm
	 * @since 2020年1月28日
	 * @param file 文件
	 * @return workbook
	 */
	public static Workbook createWorkbook(MultipartFile file) {
		InputStream inputStream;
		Workbook workbook = null;
		try {
			if(file.isEmpty()) {
				logger.error("file is empty!");
				return null;
			}
//			// 获取文件名
//			String fileName = file.getOriginalFilename();
//			// 获取文件后缀
//			String prefix = fileName.substring(fileName.lastIndexOf("."));
//			// 创建文件
//			File excelFile = File.createTempFile(fileName, prefix);
//			// MultipartFile 转换 File：mulfile ---->File
//			file.transferTo(excelFile);
			inputStream = file.getInputStream();
			String fileType = getFileType(file);
			if (EXCEL_2003.equals(fileType)) {
				workbook = new HSSFWorkbook(inputStream);
			} else if (EXCEL_2007.equals(fileType)) {
				workbook = new XSSFWorkbook(inputStream);
			} else {
				logger.error("file is not excel!");
				return null;
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return workbook;
	}
	
	/**
	 * 功能描述:判断是否是空行
	 *
	 * @author grm
	 * @since 2020年1月28日
	 * @param row 行
	 * @return boolean
	 */
	public static boolean isEmptyRow(Row row) {
		if (row == null || row.toString().isEmpty()) {
			return true;
		} else {
			Iterator<Cell> it = row.iterator();
			boolean isEmpty = true;
			while (it.hasNext()) {
				Cell cell = it.next();
				if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					isEmpty = false;
					break;
				}
			}
			return isEmpty;
		}
	}

	/**
	 * 功能描述:判断是不是excel文件
	 *
	 * @author grm
	 * @since 2020年1月28日
	 * @param file 文件
	 * @return boolean
	 */
	public static boolean isExcel(MultipartFile file) {
		String fileType = getFileType(file);
		if ((!EXCEL_2003.equals(fileType)) && (!EXCEL_2007.equals(fileType))) {
			logger.error("file is not excel!");
			return false;
		}
		return true;
	}

	/**
	 * 功能描述:获取文件类型/后缀
	 *
	 * @author grm
	 * @since 2020年1月28日
	 * @param file 文件
	 * @return fileType
	 */
	public static String getFileType(MultipartFile file) {
		String originalFilename = file.getOriginalFilename();
		return (originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length())).toLowerCase();
	}



	/**
	 * 处理类型
	 * 
	 * @param cell
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getVal(Cell cell) {
		if (null != cell) {
			switch (cell.getCellType()) {
			case XSSFCell.CELL_TYPE_NUMERIC: // 数字
				double dou = cell.getNumericCellValue();
				NumberFormat nf = NumberFormat.getInstance();
				String str = nf.format(dou);
				if (str.indexOf(",") >= 0) {
					// 这种方法对于自动加".0"的数字可直接解决
					// 但如果是科学计数法的数字就转换成了带逗号的，例如：12345678912345的科学计数法是1.23457E+13
					// 经过这个格式化后就变成了字符串“12,345,678,912,345”，这也并不是想要的结果，所以要将逗号去掉
					str = str.replace(",", "");
				}
				return str;
			case XSSFCell.CELL_TYPE_STRING: // 字符串
				return cell.getStringCellValue() + "";
			case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
				return cell.getBooleanCellValue() + "";
			case XSSFCell.CELL_TYPE_FORMULA: // 公式
				try {
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						Date date = cell.getDateCellValue();
						return (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
					} else {
						return String.valueOf((int) cell.getNumericCellValue());
					}
				} catch (IllegalStateException e) {
					return String.valueOf(cell.getRichStringCellValue());
				}
			case XSSFCell.CELL_TYPE_BLANK: // 空值
				return "";
			case XSSFCell.CELL_TYPE_ERROR: // 故障
				return "";
			default:
				return "未知类型   ";
			}
		} else {
			return "";
		}
	}
}
