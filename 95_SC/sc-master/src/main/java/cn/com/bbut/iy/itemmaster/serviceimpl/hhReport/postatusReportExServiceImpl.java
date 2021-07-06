package cn.com.bbut.iy.itemmaster.serviceimpl.hhReport;

import cn.com.bbut.iy.itemmaster.dao.hhtReport.hhtReportMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportDto;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportParamDto;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;
import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValue;

/**
 * @ClassName postatusReportExServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/6/8 13:40
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service(value = "postatusReportExService")
public class postatusReportExServiceImpl implements ExService {
    @Autowired
    hhtReportMapper mapper;
    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        hhtReportParamDto hhtReportParamDto = gson.fromJson(paramDTO.getParam(), hhtReportParamDto.class);
        hhtReportParamDto.setFlg(false);
        hhtReportParamDto.setStores(paramDTO.getStores());
       session.setHeaderListener(new postatusReportHeaderListener(hhtReportParamDto));
        session.createWorkBook();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 3;
            // 生产excel内容
            createExcelBody(sheet, curRow, hhtReportParamDto);
            File outfile = new File(Utils.getFullRandomFileName());
            session.saveTo(new FileOutputStream(outfile));
            return outfile;
        } finally {
            session.dispose();
        }
    }
    private void createExcelBody(Sheet sheet, int curRow, hhtReportParamDto param) {
        param.setFlg(false);

        List<hhtReportDto> reportDTOList1 = mapper.selectDataToal(param);
        List<hhtReportDto> reportDTOList2 = mapper.selectReceivingPend(param);
        List<hhtReportDto> reportDTOList3 = mapper.selectApproved(param);
        List<hhtReportDto> reportDTOList4 = mapper.selectReceived(param);

        int no = 1;
        for (hhtReportDto dto : reportDTOList1) {
            for (hhtReportDto dto1 : reportDTOList2) {
//                if (dto1.getStoreCd().equals(dto.getStoreCd()) && dto1.getOrderDate().equals(dto.getOrderDate())) {
                if (dto1.getStoreCd().equals(dto.getStoreCd())) {
                    if (dto.getNoOfPendingGrpo() == null  || dto.getNoOfPendingGrpo()==0 ){
                        dto.setNoOfPendingGrpo(dto1.getNoOfPendingGrpo());
                    }
                } else {
                    dto.setNoOfPendingGrpo(0);
                }
            }
            for (hhtReportDto dto1 : reportDTOList3) {
//                if (dto1.getStoreCd().equals(dto.getStoreCd()) && dto1.getOrderDate().equals(dto.getOrderDate())) {
                if (dto1.getStoreCd().equals(dto.getStoreCd())) {
                    if (dto.getGrpoOfQty() == null  || dto.getGrpoOfQty()==0 ){
                        dto.setGrpoOfQty(dto1.getGrpoOfQty());
                    }
                } else {
                    if (dto.getGrpoOfQty() == null  || dto.getGrpoOfQty()==0 ){
                        dto.setGrpoOfQty(0);
                    }else {
                        dto.setGrpoOfQty(dto.getGrpoOfQty());
                    }

                }
            }
            for (hhtReportDto dto1 : reportDTOList4) {
//                if (dto1.getStoreCd().equals(dto.getStoreCd()) && dto1.getOrderDate().equals(dto.getOrderDate())) {
                if (dto1.getStoreCd().equals(dto.getStoreCd())) {
                    if (dto.getNoOfConfiPo() == null  || dto.getNoOfConfiPo()==0 ){
                        dto.setNoOfConfiPo(dto1.getNoOfConfiPo());
                    }
                } else {
                    if (dto.getNoOfConfiPo() == null  || dto.getNoOfConfiPo()==0 ){
                        dto.setNoOfConfiPo(dto1.getNoOfConfiPo());
                    }else {
                        dto.setNoOfConfiPo(dto.getNoOfConfiPo());
                    }
                }
            }
            int curCol = 0;

            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, dto.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, dto.getStoreName());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, isNull(dto.getTotalOpenPo()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, isNull(dto.getGrpoOfQty()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, isNull(dto.getNoOfConfiPo()));


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, isNull(dto.getNoOfPendingGrpo()));
            curRow++;
        }
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);

    }
    private  Integer isNull(Integer data) {
        if (data==null){
            return 0;
        }else {
            return data;
        }
    }
}
