package cn.com.bbut.iy.itemmaster.serviceimpl.mmPromotionSaleDaily;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dao.VendorReceiptDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionItemsDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionSaleDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.base.DefaultRoleService;
import cn.com.bbut.iy.itemmaster.service.mmPromotionSaleDaily.MMPromotionSaleDailyService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 */
@Slf4j
@Service(value = "mmPromotionSaleDailyExService")
public class MMPromotionSaleDailyExServiceImpl implements ExService {

    @Autowired
    private MMPromotionSaleDailyService mmPromotionSaleDailyService;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private DefaultRoleService defaultRoleService;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        MMPromotionSaleDailyParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), MMPromotionSaleDailyParamDTO.class);
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        /*int i = defaultRoleService.getMaxPosition(paramDTO.getUserId());
        if(i >= 4){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            String startDate = sdf.format(calendar.getTime());
            jsonParam.setStartDate(startDate);
        }*/
        // 生成文件标题信息对象
        session.setHeaderListener(new MMPromotionSaleDailyExHeaderListener(jsonParam));
        session.createWorkBook();
        Workbook wb = session.getWb();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 3;
            // 生产excel内容
            createExcelBody(sheet, curRow, jsonParam, wb);
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
     * @param wb
     */
    private void createExcelBody(Sheet sheet, int curRow, MMPromotionSaleDailyParamDTO jsonParam, Workbook wb) {
        // 查询数据
        Map<String, Object> map = mmPromotionSaleDailyService.search(jsonParam);

        List<MMPromotionDataDTO> _list = (List<MMPromotionDataDTO>) map.get("data");
        // 遍历数据
        int no = 1;
        List<CellRangeAddress> regionList = new ArrayList<CellRangeAddress>();
        for (MMPromotionDataDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);

            setRegionList(regionList,ls.getList(),curRow,curCol);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreCd());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getAccDate()));

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPromotionCd());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPromotionTheme());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPromotionPattern());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPromotionType());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getDistributionType());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getTotalSellingPrice());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getTotalSaleQty());

            setRegionList(regionList,ls.getList(),curRow,curCol);
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getTotalSaleAmt());

            boolean flag = false;
            for (MMPromotionItemsDTO item : ls.getList()) {
                // 记录列
                int col = curCol;
                if (flag) {
                    row = sheet.createRow(curRow);
                }

                cell = row.createCell(col++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, item.getBarcode());

                cell = row.createCell(col++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, item.getArticleId());

                cell = row.createCell(col++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, item.getArticleName());

                cell = row.createCell(col++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, item.getSellingPrice());

                cell = row.createCell(col++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, item.getSaleQty());

                cell = row.createCell(col++);
                cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                setCellValue(cell, item.getSaleAmt());

                flag = true;
                curRow++;
            }

            // curRow++;
        }

        // 循环设置边框
        for (int i = 0; i < regionList.size(); i++) {
            CellRangeAddress _region = regionList.get(i);
            sheet.addMergedRegion(_region);
            setRegionUtil(_region, sheet, wb);
        }

        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
    }

    // 保存合并单元格的 region
    private void setRegionList(List<CellRangeAddress> regionList, List<MMPromotionItemsDTO> list, int curRow, int curCol) {
        if (list != null && list.size() > 1) {
            // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
            regionList.add(new CellRangeAddress(curRow, curRow + list.size() - 1, curCol, curCol));
        }
    }

    // 合并单元格的边框
    private void setRegionUtil(CellRangeAddress region, Sheet sheet, Workbook wb) {
        // 以下设置合并单元格的边框，避免边框不齐
        RegionUtil.setBorderBottom(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderTop(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderLeft(CellStyle.BORDER_THIN, region, sheet, wb);
        RegionUtil.setBorderRight(CellStyle.BORDER_THIN, region, sheet, wb);
    }
}
