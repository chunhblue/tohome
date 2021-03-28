package cn.com.bbut.iy.itemmaster.serviceimpl.viettelPhoneServiceImpl;

import cn.com.bbut.iy.itemmaster.dao.SA0050Mapper;
import cn.com.bbut.iy.itemmaster.dao.viettelPhoneMapper.ViettelPhoneMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050GridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050ParamDto;
import cn.com.bbut.iy.itemmaster.dto.viettelParamPhone.ma8407Paramdto;
import cn.com.bbut.iy.itemmaster.dto.viettelPhone.ma8407dto;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.serviceimpl.CashierExHeaderListener;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValue;

/**
 * @ClassName viettelPhoneExServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/19 15:03
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service(value = "viettelPhoneExService")
public class viettelPhoneExServiceImpl implements ExService {

    @Autowired
    private ViettelPhoneMapper viettelPhoneMapper;
    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        ma8407Paramdto jsonParam = gson.fromJson(paramDTO.getParam(), ma8407Paramdto.class);
        // 导出不需要分页
        jsonParam.setFlg(false);
        jsonParam.setStores(paramDTO.getStores1());
        // 生成文件标题信息对象
        session.setHeaderListener(new  viettelPhoneExHeaderListener(jsonParam));
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

    private void createExcelBody(Sheet sheet, int curRow,ma8407Paramdto param) {
        // 查询数据

        List<ma8407dto> ma8407dtos = viettelPhoneMapper.searchDataExport(param);
        // 遍历数据
        int no = 1;
        for (ma8407dto ls : ma8407dtos) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, fmtDateAndTimeToStr(ls.getRequestDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getRequestId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOrderId());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getReceiptNo());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, formatNum(ls.getTransAmt().toString()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, isSuccess(ls.getStatus()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getMessage());


            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
//        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
    }
    public static String isSuccess(String status) {
        if (StringUtils.isBlank(status)) {
            return "";
        }
       if (status.equals("10")){
           return "Success";
       }else {
           return "Fail";
       }
    }
}
