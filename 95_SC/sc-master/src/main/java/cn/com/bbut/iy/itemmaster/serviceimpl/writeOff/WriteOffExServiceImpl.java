package cn.com.bbut.iy.itemmaster.serviceimpl.writeOff;

import cn.com.bbut.iy.itemmaster.dao.WriteOffMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.writeOff.WriteOffService;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "writeOffDailyExService")
public class WriteOffExServiceImpl implements ExService {

    @Autowired
    private WriteOffMapper mapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Autowired
    private WriteOffService service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        WriteOffParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), WriteOffParamDTO.class);
        // 导出不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 获取业务日期
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        // 生成文件标题信息对象
        session.setHeaderListener(new WriteOffExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, WriteOffParamDTO jsonParam) {
        // 查询数据
        List<WriteOffDTO> _list = mapper.selectListByCondition(jsonParam);
        WriteOffDTO offDto = service.deleteGetOffQty(jsonParam);
        // 遍历数据
        int no = 1;
        for (WriteOffDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreCd());


            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getWriteOffDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, setStrValue(ls.getDepCd())+" "+setStrValue(ls.getDepName()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, setStrValue(ls.getPmaCd())+" "+setStrValue(ls.getPmaName()));



            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, setStrValue(ls.getCategoryCd())+" "+setStrValue(ls.getCategoryName()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, setStrValue(ls.getSubCategoryCd())+" "+setStrValue(ls.getSubCategoryName()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, setStrValue(ls.getArticleName()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getUom());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getWriteOffQty());

//            cell = row.createCell(curCol++);
//            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_5));
//            setCellValue(cell, ls.getWriteOffAmt());
//SALE Qty 占时隐藏
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getSaleQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getAdjustReason());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOfc());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOfcName());
            curRow++;
        }

        int curCol = 0;
        Row row = sheet.createRow(curRow);
        Cell cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");


        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "");



        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "Total:");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, "total Item");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
        setCellValue(cell, (int) offDto.getRecords());

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "total qty");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, offDto.getWriteOffQty());

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, offDto.getSaleQty());

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");

        cell = row.createCell(curCol++);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
        setCellValue(cell, "");
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
    }
    public static String formatNum(String num) {
        if (StringUtils.isBlank(num)) {
            return "0";
        }
        int i = new BigDecimal(num).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        DecimalFormat df = new DecimalFormat("###,###");
        String result = df.format(i);
        return result;
    }
}
