package cn.com.bbut.iy.itemmaster.serviceimpl.cashierDetail;

import cn.com.bbut.iy.itemmaster.dao.CashierDetailMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.ExpenditureAmtDto;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionItemsDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.businessDaily.BusinessDailyService;
import cn.com.bbut.iy.itemmaster.serviceimpl.businessDaily.BusinessDailyExHeaderListener;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 */
@Slf4j
@Service(value = "cashierDetailExService")
public class CashierDetailExServiceImpl implements ExService {

    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private CashierDetailMapper cashierDetailMapper;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        CashierDetailParam _param = gson.fromJson(paramDTO.getParam(), new TypeToken<CashierDetailParam>() {}.getType());

        // 生成文件标题信息对象
        session.setHeaderListener(new CashierDetailExHeaderListener(_param));
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
            createExcelBody(sheet, curRow, _param, wb);
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
    private void createExcelBody(Sheet sheet, int curRow, CashierDetailParam param, Workbook wb) {

        // 查询销售主档数据
        List<SaleHead> headList = cashierDetailMapper.getSaleHead(param);

        // 获得销售明细和支付方式
        SaleHeadParam headParam = new SaleHeadParam();
        List<CellRangeAddress> regionList = new ArrayList<CellRangeAddress>();
        for (SaleHead saleHead : headList) {
            // 封装参数
            headParam.setAccDate(saleHead.getAccDate());
            headParam.setStoreCd(saleHead.getStoreCd());
            headParam.setPosId(saleHead.getPosId());
            headParam.setTranSerialNo(saleHead.getTranSerialNo());
            headParam.setFlg(false);
            List<SaleDetail> saleDetailList = cashierDetailMapper.getSaleDetail(headParam);
            List<PayMethod> payDetailList = cashierDetailMapper.getPayDetail(headParam);

            // 只取 使用的支付方式
            List<PayMethod> newPayList = new ArrayList<PayMethod>();
            for (PayMethod pay : payDetailList) {
                if (pay.getPayAmount().compareTo(BigDecimal.ZERO)!=0) {
                    newPayList.add(pay);
                }
            }

            saleHead.setPayMethodList(newPayList);
            saleHead.setSaleDetailList(saleDetailList);
        }

        Row row = null;

        Cell cell = null;
        // 绑定数据
        for (int i = 0; i < headList.size(); i++) {
            SaleHead saleHead = headList.get(i);
            // 需要合并多少行单元格
            int maxSize = 0;
            if (maxSize < saleHead.getSaleDetailList().size()) {
                maxSize = saleHead.getSaleDetailList().size()-1;
            }
            if (maxSize < saleHead.getPayMethodList().size()) {
                maxSize = saleHead.getPayMethodList().size()-1;
            }

            int curCol = 0;
            row = sheet.createRow(curRow++);

            // 业务日期
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,fmtDateToStr(saleHead.getAccDate()));
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // pos
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,saleHead.getPosId());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // 收银员
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,saleHead.getCashierId());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // Shift
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,saleHead.getShift());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // Receipt No.
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,saleHead.getTranSerialNo());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // 	Selling Time 销售时间
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,saleHead.getTranTime());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // 	Membership ID
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell,saleHead.getMemberId());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // 	Subtotal
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell,saleHead.getSaleAmount());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // 	Discount
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell,saleHead.getOverAmount());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));

            // 	Total
            cell = row.createCell(curCol++, Cell.CELL_TYPE_STRING);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell,saleHead.getPayAmount());
            // 保存region
            setRegionList(regionList,maxSize,(curRow - 1),(curCol-1));


            // 销售明细商品
            List<SaleDetail> saleDetailList = saleHead.getSaleDetailList();
            boolean rowFlg = false;
            // 支付方式, 需要判断是否需要合并单元格
            List<PayMethod> payList = saleHead.getPayMethodList();
            // 支付方式大小
            int paySize = payList.size();
            int saySize = saleDetailList.size();
            if(saySize >= paySize){
                // 记录支付方式数据真实索引
                int payNum= 0;
                // 分摊几次单元格
                int payRowDiv = saySize / paySize;
                // 取余数
                int payRowRem = saySize % paySize;
                // 记录合并单元格起始行数
                int payRow = curRow;
                // 合并多少个单元格
                int payRowMerge = 0;
                int payJ = 0;
                Row rowPay = row;
                for (int j = 0; j <= maxSize; j++) {
                    // 记录起始列号
                    int col = curCol;
                    // 新增行标记
                    if (rowFlg) {
                        row = sheet.createRow(curRow-1+j);
                        rowPay = sheet.createRow(payRow-1);
                    }
                    PayMethod pay = null;
                    if(payNum < paySize){
                        pay = payList.get(payNum);
                    }

                    if(j != payJ){
                        // 	支付方式
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));

                        // 	支付金额
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                    }


                    if(j == payJ && pay != null ){
                        // 	支付方式
                        cell = (rowPay.createCell(col++, Cell.CELL_TYPE_STRING));
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                        setCellValue(cell, pay.getPayNamePrint());

                        // 	支付金额
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell, pay.getPayAmount());

                        if(j > 0){
                            SaleDetail saleInfo = saleDetailList.get(j);
                            // barcode
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                            setCellValue(cell,saleInfo.getBarcode());

                            // 商品名称
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                            setCellValue(cell,saleInfo.getArticleShortName());

                            // spec
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                            setCellValue(cell,saleInfo.getSpec());

                            // UOM
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                            setCellValue(cell,saleInfo.getUnitName());

                            // Original Selling Price
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                            setCellValue(cell,saleInfo.getPriceOriginal());

                            // Selling Price
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                            setCellValue(cell,saleInfo.getPriceActual());

                            // Quantity
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                            setCellValue(cell,saleInfo.getSaleQty());

                            // Amount
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                            setCellValue(cell,saleInfo.getSaleAmount());

                            // Discount Amount
                            cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                            setCellValue(cell,saleInfo.getDiscountAmount());
                        }
                        // 合并多少个单元格
                        payRowMerge = payRowDiv;
                        if(payNum == paySize - 1){
                            payRowMerge = payRowDiv + payRowRem;
                        }

                        if(paySize < saySize && paySize != 0){
                            // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol, curCol));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+1, curCol+1));
                        }
                        payRow = payRow + payRowMerge;
                        payJ = payJ + payRowMerge;
                        payNum++;
                    }

                    if (j < saySize) {
                        SaleDetail saleInfo = saleDetailList.get(j);
                        // barcode
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getBarcode());

                        // 商品名称
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getArticleShortName());

                        // spec
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getSpec());

                        // UOM
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getUnitName());

                        // Original Selling Price
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getPriceOriginal());

                        // Selling Price
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getPriceActual());

                        // Quantity
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getSaleQty());

                        // Amount
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getSaleAmount());

                        // Discount Amount
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getDiscountAmount());
                    }
                    rowFlg = true;
                }

                // 加上合并后的行数
                curRow += maxSize;
            }else {
                // 记录支付方式数据真实索引
                int payNum= 0;
                // 分摊几次单元格
                int payRowDiv = paySize / saySize;
                // 取余数
                int payRowRem = paySize % saySize;
                // 记录合并单元格起始行数
                int payRow = curRow;
                // 合并多少个单元格
                int payRowMerge = 0;
                int payJ = 0;
                Row rowPay = row;
                for (int j = 0; j <= maxSize; j++) {
                    // 记录起始列号
                    int col = curCol;
                    // 新增行标记
                    if (rowFlg) {
                        row = sheet.createRow(curRow-1+j);
                        rowPay = sheet.createRow(payRow-1);
                    }
                    SaleDetail saleInfo = null;
                    if(payNum < saySize){
                        saleInfo = saleDetailList.get(payNum);
                    }


                    if(j < paySize){
                        PayMethod pay = payList.get(j);
                        // 	支付方式
                        cell = (row.createCell(col++, Cell.CELL_TYPE_STRING));
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
                        setCellValue(cell, pay.getPayNamePrint());

                        // 	支付金额
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell, pay.getPayAmount());
                    }
                    if(j != payJ){
                        // barcode
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));

                        // 商品名称
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));

                        // spec
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));

                        // UOM
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));

                        // Original Selling Price
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));

                        // Selling Price
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));

                        // Quantity
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));

                        // Amount
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));

                        // Discount Amount
                        cell = row.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                    }

                    if(j == payJ && saleInfo != null){

                        // barcode
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getBarcode());

                        // 商品名称
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getArticleShortName());

                        // spec
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getSpec());

                        // UOM
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
                        setCellValue(cell,saleInfo.getUnitName());

                        // Original Selling Price
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getPriceOriginal());

                        // Selling Price
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getPriceActual());

                        // Quantity
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getSaleQty());

                        // Amount
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getSaleAmount());

                        // Discount Amount
                        cell = rowPay.createCell(col++, Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
                        setCellValue(cell,saleInfo.getDiscountAmount());

                        // 合并多少个单元格
                        payRowMerge = payRowDiv;
                        if(payNum == paySize - 1){
                            payRowMerge = payRowDiv + payRowRem;
                        }

                        if(saySize < paySize && payRowMerge > 1){
                            // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+2, curCol+2));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+3, curCol+3));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+4, curCol+4));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+5, curCol+5));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+6, curCol+6));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+7, curCol+7));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+8, curCol+8));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+9, curCol+9));
                            regionList.add(new CellRangeAddress(payRow-1, (payRow-1)+(payRowMerge-1), curCol+10, curCol+10));
                        }
                        payRow = payRow + payRowMerge;
                        payJ = payJ + payRowMerge;
                        payNum++;
                    }

                    rowFlg = true;
                }

                // 加上合并后的行数
                curRow += maxSize;
            }

        }

        // 合并单元格
        for (CellRangeAddress region : regionList) {
            sheet.addMergedRegion(region);
            setRegionUtil(region, sheet, wb);
        }

        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
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
    private void setRegionList(List<CellRangeAddress> regionList, int maxSize, int curRow, int curCol) {
        if (maxSize > 0) {
            // 参数：起始行号，终止行号， 起始列号，终止列号 下标（0开始）
            regionList.add(new CellRangeAddress(curRow, curRow + maxSize, curCol, curCol));
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
