package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.warehouse;

import cn.com.bbut.iy.itemmaster.dao.receipt.warehouse.WarehouseMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptGridDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.warehouse.WarehouseReceiptParamDTO;
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
@Service(value = "receiptFromDCExService")
public class WarehouseExServiceImpl implements ExService {

    @Autowired
    private WarehouseMapper warehouseMapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        WarehouseReceiptParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), WarehouseReceiptParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        // 获取业务日期
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        // 生成文件标题信息对象
        session.setHeaderListener(new WarehouseExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, WarehouseReceiptParamDTO jsonParam) {
        // 查询数据
        List<WarehouseReceiptGridDTO> _list = warehouseMapper.selectReceipt(jsonParam);
        // 遍历数据
        int no = 1;
        for (WarehouseReceiptGridDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getReceiveId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getReceiveDateT()));

//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
//            setCellValue(cell, fmtDateToStr(ls.getDeliveryDate()));

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
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 23 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
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
                return  "Receiving Pending";
            case "20":
                return "Received";
            case "30":
                return "Paid";
            default:
                return "";
        }
    }
    // 获取审核状态
//    private String getReviewSts(String status){
//        if(status == null){
//            return "";
//        }
//        switch (status) {
//            case "1":
//                value = "Pending";
//                break;
//            case "5":
//                value = "Rejected";
//                break;
//            case "6":
//                value = "Withdrawn";
//                break;
//            case "7":
//                value = "Expired";
//                break;
//            case "10":
//                value = "Approved";
//                break;
//            case "15":
//                value = "Receiving Pending";
//                break;
//            case "20":
//                value = "Received";
//                break;
//            case "30":
//                value = "Paid";
//                break;
//            default:
//                value = "";
//        }
//        return $(tdObj).text(value);
//    }

}
