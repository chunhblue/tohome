package cn.com.bbut.iy.itemmaster.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;

/**
 * 功能描述:拆分excel的多个sheet为多个独立的excel文件
 *
 * @author lch
 * @since 2020年9月2日
 */
public class SpiltExcel {
    private static final Logger logger = LoggerFactory.getLogger(SpiltExcel.class);

    private final static String EXCEL_2003 = ".xls"; // 2003- 版本的excel
    private final static String EXCEL_2007 = ".xlsx"; // 2007+ 版本的excel

    /**
     * XSSFWorkbook 导出数据时，数据量过大，会报内存溢出，
     * SXSSFWorkbook不能读取文件，需要将XSSFWorkbook与SXSSFWorkbook结合起来使用
     * @param filePath
     * @param file
     * @throws Exception
     */
    public static void splitFile(String filePath,MultipartFile file) throws Exception {
        Workbook workbook = null;
        String filePathSpilt = null; // 文件路径的前缀
        String fileName = file.getOriginalFilename();
        // 文件格式错误
        if(!isExcel(file)){
            logger.error("file is not excel!");
             return;
        }
        String fileType = getFileType(file);
        // 第一步，创建一个webbook，对应一个Excel文件
        if (EXCEL_2003.equals(fileType)) {
            workbook = new HSSFWorkbook(new FileInputStream(new File(filePath + File.separator)));
            filePathSpilt = filePath.replace(EXCEL_2003, "");
        }else if (EXCEL_2007.equals(fileType)) {
            File file1 = new File(filePath + File.separator);
            FileInputStream fileInputStream = new FileInputStream(file1);
            // 内存中保持100行，超过便放在磁盘中
            SXSSFWorkbook swb = new SXSSFWorkbook(new XSSFWorkbook(fileInputStream),100);
            workbook = swb.getXSSFWorkbook();
            filePathSpilt = filePath.replace(EXCEL_2007, "");
        }else {
            logger.error("file is not excel!");
            return;
        }
        int total = workbook.getNumberOfSheets();
        workbook.close();
        System.out.println(total);
        SXSSFWorkbook wb = null;
        if (EXCEL_2007.equals(fileType)) {
            wb = new SXSSFWorkbook(100);
        }
        for (int i = 0; i < total; i++) {// 获取每个Sheet表
            String filePath2 = filePathSpilt + i + fileType;
            copyFile(filePath, filePath2);
            File newFile = new File(filePath2 + File.separator);
            Workbook workbook2 = null;
            if (EXCEL_2003.equals(fileType)) {
                workbook2 = new HSSFWorkbook(new FileInputStream(newFile));
            }else if (EXCEL_2007.equals(fileType)) {
                workbook2 = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(newFile)),100);
            }
            int total2 = workbook2.getNumberOfSheets();
            for (int j = total2-1; j >= 0 ; j--) {
                if (i == j) {
                    continue;
                }
                workbook2.removeSheetAt(j);
            }
            String filePath3 = filePathSpilt +"_" + i+ fileType;
            FileOutputStream fout = new FileOutputStream(filePath3 + File.separator);
            workbook2.write(fout);
            workbook2.close();
            fout.close();
            newFile.delete();//删除文件
        }
        if (EXCEL_2007.equals(fileType)) {
            FileOutputStream  fis = new FileOutputStream (filePath + File.separator);
            wb.write(fis);
            fis.close();
        }
        System.out.println("ok");
    }

    private static void copyFile(String srcPathStr, String desPathStr) {
        try {
//            SXSSFWorkbook wb = new SXSSFWorkbook(100);

            FileInputStream fis = new FileInputStream(srcPathStr + File.separator);
            FileOutputStream fos = new FileOutputStream(desPathStr + File.separator);
            byte datas[] = new byte[1024 * 8];
            int len = 0;
            while ((len = fis.read(datas)) != -1) {
                fos.write(datas, 0, len);
//                wb.write(fos);       // 数据量很大的时候，这样读取，文件打不开
            }
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能描述:判断是不是excel文件
     *
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
}
