package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.vendor;

import cn.com.bbut.iy.itemmaster.dao.receipt.vendor.VendorMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorReceiptParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "receiptFromVExService")
public class VendorExServiceImpl implements ExService {

    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        VendorReceiptParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), VendorReceiptParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
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
            createExcelBody(sheet, curRow, jsonParam);
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
    private void createExcelBody(Sheet sheet, int curRow, VendorReceiptParamDTO jsonParam) {
        // 查询数据
        List<VendorReceiptGridDTO> _list = vendorMapper.selectReceipt(jsonParam);
        // 遍历数据
        int no = 1;
        for (VendorReceiptGridDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getReceiveId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getReceiveDateT()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOrderId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getOrderDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, getReviewSts(ls.getReviewSts()));

            /*cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, ls.getOrderAmt());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
            setCellValue(cell, ls.getReceiveAmt());*/

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 26 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 23 * 256);
//        sheet.setColumnWidth(columnIndex++, 18 * 256);
//        sheet.setColumnWidth(columnIndex++, 18 * 256);
    }

    /**
     * 审核状态
     *
     * @param status
     */
    private String getReviewSts(String status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case "1":
                return "Pending";
            case "5":
                return "Rejected";
            case "6":
                return "Withdrawn";
            case "7":
                return "Expired";
            case "10":
                return "Approved";
            case "15":
                return "Receiving Pending";
            case "20":
                return "Received";
            case "30":
                return "Paid";
            default:
                return "";
        }
    }

}
