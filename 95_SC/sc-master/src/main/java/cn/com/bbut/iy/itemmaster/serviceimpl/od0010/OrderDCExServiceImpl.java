package cn.com.bbut.iy.itemmaster.serviceimpl.od0010;

import cn.com.bbut.iy.itemmaster.dao.OD0010CMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010.od0010ParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * @ClassName directSupplierExServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/4/24 17:27
 * @Version 1.0
 */
@Slf4j
@Service(value = "orderDCEx")
public class OrderDCExServiceImpl implements ExService {
    @Autowired
    private OD0010CMapper od0010CMapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        od0010ParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), od0010ParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源、权限参数
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 生成文件标题信息对象
        session.setHeaderListener(new OrderDCExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, od0010ParamDTO jsonParam) {
        // 查询数据
        List<od0010DTO> _list = od0010CMapper.getcdOrderInformation(jsonParam);
        //System.out.println(_list+"12");
        // 遍历数据
        int no = 1;
        for (od0010DTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOrderId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtIntDate(ls.getOrderDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getVendorId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getWarehouseName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, getReviewStatus(ls.getReviewStatus()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            System.out.println(ls.getOrderMethod());
            setCellValue(cell, getMenthod(ls.getOrderMethod()));

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
    }

    private String getReviewStatus(String value) {
        String str = " ";
        switch (value) {
            case "1":
                str = "Pending";
                break;
            case "5":
                str = "Rejected";
                break;
            case "6":
                str = "Withdrawn";
                break;
            case "7":
                str = "Expired";
                break;
            case "10":
                str = "Approved";
                break;
            case "15":
                str = "Receiving Pending";
                break;
            case "20":
                str = "Received";
                break;
            case "30":
                str = "Paid";
                break;

        }
        return str;
    }
    // 格式化数字类型的日期：yyyymmdd → dd/mm/yyyy
    private String fmtIntDate(String date){
        if(date==null){
            return "";
        }
        String res = "";
        res = date.substring(6,8)+"/"+date.substring(4,6)+"/"+date.substring(0,4);
        return res;
    }

    private String getMenthod(String value){
        String str = " ";
        switch (value) {
            case "40":
                str = "DC Store Order";
                break;
            case "30":
                str="DC Allocation Order";
                break;
            case "20":
                str = "Direct Store Purchase Order";
                break;
            case "10":
                str="Direct Store Purchase Allocation";
                break;
        }
        return str;
    }
}
