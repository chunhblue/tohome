package cn.com.bbut.iy.itemmaster.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.com.bbut.iy.itemmaster.entity.base.IyStore;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExController;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.shiy.common.baseutil.Container;

/**
 * @author Shiy
 */
@Slf4j
public class ExportUtil {

    public static final String KEY = "expKey";

    public static final String DATETIMEPATTERN = "yyyyMMddHHmmssSSS";
    public static final DateTimeFormatter DATETIMEPATTERN_FORMATTER = DateTimeFormatter
            .ofPattern(DATETIMEPATTERN);

    /**
     * EXPORT统一的转换处理，不重复生成
     *
     * @return
     */
    public static String export(String prefix, HttpServletRequest req, ExService service,
            BaseExcelParam param) {
        ExController controller = Container.getBean(ExController.class);
        String key = getExpKey(param, prefix);
        log.debug("export for key : {}", key);
        controller.checkAndRun(key, service, param);
        req.setAttribute(KEY, key);
        return "/common/export";
    }

    /**
     * EXPORT统一的转换处理，不重复生成(没有req)
     *
     * @return
     */
    public static String export(String prefix, ExService service, BaseExcelParam param) {
        ExController controller = Container.getBean(ExController.class);
        // String key = getExpKey(param, prefix);
        String key = getNewExpKey(prefix);
        log.debug("export for key : {} is not req", key);
        controller.checkAndRun(key, service, param);
        return key;
    }

    /**
     * EXPORT统一的转换处理，不重复生成
     *
     * @return
     */
    public static String export(String prefix, HttpServletRequest req, ExService service,
            BaseExcelParam param, boolean isReCreate) {
        ExController controller = Container.getBean(ExController.class);
        String key = null;
        if (isReCreate) {
            key = getNewExpKey(prefix);
        } else {
            key = getExpKey(param, prefix);
        }
        log.debug("export for key : {}", key);
        controller.checkAndRun(key, service, param);
        req.setAttribute(KEY, key);
        return "/common/export";
    }

    /**
     * 唯一区分的key,规则：prefix + "-" + BaseParam.hashcode
     *
     * @param param
     * @param prefix
     * @return
     */
    public static String getExpKey(BaseExcelParam param, String prefix) {
        StringBuffer sb = new StringBuffer(30);
        sb.append(prefix).append("_");
        sb.append(param.hashCode());
        return sb.toString();
    }

    /**
     * 生成唯一的key,用于重新生成 唯一区分的key,规则：prefix + "-" + BaseParam.hashcode
     *
     * @param prefix
     * @return
     */
    public static String getNewExpKey(String prefix) {
        StringBuffer sb = new StringBuffer(30);
        sb.append(prefix).append("_NEW_");
        sb.append(System.currentTimeMillis());
        return sb.toString();
    }

    /**
     * 生成文件名，规则：prefix + "_" + yyyyMMddHHmmssSSS + ".xlsx"
     *
     * @param prefix
     * @return
     */
    public static String getFilename(String prefix) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix).append("_");
        LocalDateTime today = LocalDateTime.now();
        sb.append(today.format(DATETIMEPATTERN_FORMATTER));
        sb.append(".xlsx");
        return sb.toString();
    }

    /**
     * 分析excel
     *
     * @param filePath
     *            文件路径
     * @param stores
     *            有效店铺集合
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
    public static List<String> analyze(String filePath, List<IyStore> stores)
            throws FileNotFoundException, IOException {
        List<String> rest = new ArrayList<>();
        Workbook book = null;
        try {
            book = new XSSFWorkbook(filePath);
        } catch (Exception e) {
            book = new HSSFWorkbook(new FileInputStream(filePath));
        }
        Sheet sheet = book.getSheet("Data");
        Map<String, Integer> storeIndex = new LinkedHashMap<>();
        // 促销分类与商品编号坐标
        // excel标题位，用来确定取得对应促销分类与商品编号的列坐标
        Row rowTopTitle = sheet.getRow(1);
        // 如果标题头不为空
        if (rowTopTitle != null) {
            for (int i = 0; i <= 1; i++) {
                Cell cell = rowTopTitle.getCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                // 装载
                storeIndex.put(cell.getStringCellValue(), i);
            }
        } else {
            // 如果标题行为空则返回空对象
            return null;
        }
        for (IyStore st : stores) {
            // 加载有效店铺集合并设定对应默认坐标均为0
            storeIndex.put(st.getStore(), -1);
        }
        // excel标题位，用来确定取得对应有效店铺的列坐标
        Row rowTitle = sheet.getRow(2);
        if (rowTitle != null) {// 如果标题头不为空
            for (int i = 2; i <= stores.size() + 1; i++) {
                Cell cell = rowTitle.getCell(i);
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                // 装载
                storeIndex.put(cell.getStringCellValue(), i);
            }
        } else {
            // 如果标题行为空则返回空对象
            return null;
        }
        // 获取到Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();
        // 遍历数据 0为起始坐标。
        for (int i = 3; i < rows; i++) {
            Iterator iter = storeIndex.entrySet().iterator();
            Row row = sheet.getRow(i);
            if (row != null) {
                // 实际值
                String value = "";
                // 验证是否存在值
                String valueVerify = "";
                // 根据有效坐标进行数据获取（实际排序按照系统Map顺序进行）
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    Object val = entry.getValue();
                    Cell cell = row.getCell((Integer) val);
                    if (cell != null) {
                        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                        if (value != "") {
                            value += "▇";
                        }
                        value += cell.getStringCellValue();
                        valueVerify += cell.getStringCellValue();
                    } else {
                        value += "▇";
                    }
                }
                if ("".equals(valueVerify)) {
                    break;
                }
                rest.add(value);
            }
        }
        return rest;
    }
}
