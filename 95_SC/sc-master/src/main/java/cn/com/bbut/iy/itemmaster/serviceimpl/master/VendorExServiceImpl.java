package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dao.VendorMasterMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.vendor.VendorDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.VendorParamDTO;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;

import com.google.gson.Gson;
import org.springframework.util.StringUtils;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Vendor List Excel生成
 *
 */
@Slf4j
@Service(value = "vendorExService")
public class VendorExServiceImpl implements ExService {

    @Autowired
    private VendorMasterMapper mapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    /** excel样式 **/
    private Map<String, CellStyle> MAP_STYLE = new HashMap<>();
    private final String STYPE_KEY_1 = "style_1";

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        VendorParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), VendorParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 获取业务日期
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        // 生成文件标题信息对象
        session.setHeaderListener(new VendorExHeaderListener(jsonParam));
        session.createWorkBook();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 3;
            // 生产excel内容
            createExcelBody(sheet, curRow, jsonParam, paramDTO.getPCode());
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
    private void createExcelBody(Sheet sheet, int curRow, VendorParamDTO jsonParam, String pcode) {
        // 查询数据
        List<VendorDTO> _list = mapper.selectListByCondition(jsonParam);

        // 遍历数据
        int no = 1;
        for (VendorDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getVendorId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getVendorName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, getBusinessType(ls.getBusinessType()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getVendorAddress1());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getAdminName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getVendorTelNo());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, getOrderType(ls.getOrderSendMethod()));

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 40 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 40 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
    }

    /**
     * 生成报表样式
     * 
     * @param wb
     */
    private void createExcelStyleToMap(Workbook wb) {
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
    }

    /**
     * 获取业务区分名称
     *
     * @param status
     */
    private String getBusinessType(String status) {
        if(status == null){
            return "";
        }
        String[] arr = status.split(",");
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            if ("0".equals(s)) {
                sb.append("Distribution Center,");
            } else if ("1".equals(s)) {
                sb.append("Logistics Vendor,");
            } else if ("2".equals(s)) {
                sb.append("Self-delivering Vendor,");
            }
        }
        if (StringUtils.isEmpty(sb)) {
            return "";
        }
        return sb.substring(0,sb.lastIndexOf(","));
    }

    /**
     * 获取订单方式名称
     *
     * @param status
     */
    private String getOrderType(String status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case "1":
                return "Email";
            default:
                return "";
        }
    }

}
