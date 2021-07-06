package cn.com.bbut.iy.itemmaster.serviceimpl.difference;

import cn.com.bbut.iy.itemmaster.dao.OD0010Mapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListParam;
import cn.com.bbut.iy.itemmaster.dto.difference.DifferenceListResult;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Number;
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

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "diffExService")
public class DiffExServiceImpl implements ExService {

    @Autowired
    private OD0010Mapper mapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        DifferenceListParam jsonParam = gson.fromJson(paramDTO.getParam(), DifferenceListParam.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 获取当前角色店铺权限
        jsonParam.setStores(paramDTO.getStores());
        // 获取业务日期
        if(StringUtils.isBlank(jsonParam.getBusinessDate())){
            jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        }
        // 生成文件标题信息对象
        session.setHeaderListener(new DiffExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, DifferenceListParam jsonParam) {
        // 查询数据
        List<DifferenceListResult> _list = mapper.selectByConditionPlus(jsonParam);
        // 遍历数据
        int no = 1;
        for (DifferenceListResult ls : _list) {
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
            setCellValue(cell, fmtDateToStr(ls.getOrderDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getReceiveDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, ls.getReceiveId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            String temp1 = "";
            String temp2 = "";

            if ("0".equals(jsonParam.getOrderDifferentiate())) {
                temp1 = ls.getVendorId();
                temp2 = ls.getVendorName();
            } else {
                temp1 = ls.getDeliveryCenterId();
                temp2 = ls.getDeliveryCenterName();
            }

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, temp1);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, temp2);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));

            String str = String.valueOf(ls.getTotalAmt());
            Double dou=Double.valueOf(str);
            setCellValue(cell, String.format("%,d",dou.intValue()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getUpdateYmd()));

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
    }


}
