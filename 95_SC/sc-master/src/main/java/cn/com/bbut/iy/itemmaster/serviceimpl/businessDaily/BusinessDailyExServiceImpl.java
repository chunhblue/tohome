package cn.com.bbut.iy.itemmaster.serviceimpl.businessDaily;

import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.BusinessDailyDto;
import cn.com.bbut.iy.itemmaster.dto.businessDaily.ExpenditureAmtDto;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.businessDaily.BusinessDailyService;
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
@Service(value = "businessDailyExService")
public class BusinessDailyExServiceImpl implements ExService {

    @Autowired
    private BusinessDailyService businessDailyService;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        Map<String, Object> jsonParam = gson.fromJson(paramDTO.getParam(), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        jsonParam.put("flg", false);
        // 资源权限参数设置
        jsonParam.put("resources", paramDTO.getResources());
        // 生成文件标题信息对象
        session.setHeaderListener(new BusinessDailyExHeaderListener(jsonParam));
        session.createWorkBook();
        Workbook wb = session.getWb();
        // 创建excel工作表，调用标题信息对象执行标题添加
        session.createSheet("Data");
        try {
            Sheet sheet = session.getCurSheet();
            createExcelStyleToMap(session.getWb());
            // 内容起始下标
            int curRow = 5;
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
    private void createExcelBody(Sheet sheet, int curRow, Map<String, Object> jsonParam, Workbook wb) {
        // 取出检索参数
        String storeCd = (String) jsonParam.get("storeCd");
        String businessDate = (String) jsonParam.get("businessDate");

        // 查询数据
        Map<String, Object> resultMap = businessDailyService.getData(storeCd, businessDate);
        Integer lastMonthSalesAmount = (Integer) resultMap.get("lastMonthSalesAmount");
        Integer countCustomer = (Integer) resultMap.get("countCustomer");


        // 绑定数据
        // 销售数据行
        BusinessDailyDto saleData = (BusinessDailyDto) resultMap.get("saleData");
        Row row = sheet.createRow(curRow++);

        // 销售毛额
        Cell cell = row.createCell(0);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getGrossSaleAmount());
        // 折扣金额
        cell = row.createCell(2);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getDiscountAmount());
        // 实际销售额
        cell = row.createCell(4);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getSaleAmount());
        // 溢收金额
        cell = row.createCell(6);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getSpillAmount());
        // 服务费
        cell = row.createCell(8);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getServiceAmount());
        // 充值费用
        cell = row.createCell(10);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getChargeAmount());
        // 充值退款费用
        cell = row.createCell(12);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, saleData.getChargeRefundAmount());

        cell = row.createCell(14);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
         if (lastMonthSalesAmount==null){
             lastMonthSalesAmount=0;
         }
        setCellValue(cell, formatNum(String.valueOf(lastMonthSalesAmount/4)));

        cell = row.createCell(16);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, countCustomer);


        // 合并单元格
        CellRangeAddress region = null;
              for (int i = 0; i < 17; i += 2) {
            region = new CellRangeAddress(curRow - 1, curRow - 1, i, i + 1);
            sheet.addMergedRegion(region);
            setRegionUtil(region, sheet, wb);
        }



        // 跳过一行
        curRow++;

        // 三个表格  支付方式, 经费, 银行缴款
        // 支付方式(后面会动态设置, 暂时写死)
        BusinessDailyDto payAmt = (BusinessDailyDto) resultMap.get("payAmt");
        // 经费
        List<ExpenditureAmtDto> expenditureAmt = (List<ExpenditureAmtDto>) resultMap.get("expenditureAmt");
        // 银行缴款
        BusinessDailyDto bankDeposit = (BusinessDailyDto) resultMap.get("bankDeposit");

        row = sheet.createRow(curRow++);
        // 三个表格的标题
        cell = row.createCell(0);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Payment Type");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 0, 3);

        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(7);

        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Expense");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 7, 10);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(14);


        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Bank Deposit");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 14, 17);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        // 遍历内容
        // 获取最大值, 最小值 6
        int rowHeight = 6;
        if (rowHeight < expenditureAmt.size()) {
            rowHeight = expenditureAmt.size();
        }

        // 遍历数据
        for (int i = 0; i <= rowHeight; i++) {
            row = sheet.createRow(curRow++);
            // 设置支付方式
            setPayAmt(i, curRow,rowHeight,payAmt, row, sheet, wb);
            // 设置经费
            setExpenditureAmt(i,curRow,rowHeight,expenditureAmt, row, sheet, wb);
            // 设置银行缴款
            setBankDeposit(i, curRow,rowHeight,bankDeposit, row, sheet, wb);
        }

        // 跳过一行
        curRow++;

        // 设置 4 个表格的标题
        row = sheet.createRow(curRow++);
        cell = row.createCell(0);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Category Sales");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 0, 1);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(5);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Category Sales(Continued)");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 5, 7);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(10);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Category Sales(Continued)");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 10, 12);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);
        cell = row.createCell(15);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, "Category Sales(Continued)");
        region = new CellRangeAddress(curRow - 1, curRow - 1, 15, 17);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        // 获取分类销售数据
        List<BusinessDailyDto> saleAmountByPma = (List<BusinessDailyDto>) resultMap.get("saleAmountByPma");
        // 设置 4 个 表格, 一个表格 20 行
        if (saleAmountByPma==null) {
            saleAmountByPma = new ArrayList<BusinessDailyDto>();
        }

        List<BusinessDailyDto> listOne = new ArrayList<BusinessDailyDto>();
        List<BusinessDailyDto> listTwo = new ArrayList<BusinessDailyDto>();
        List<BusinessDailyDto> listThree = new ArrayList<BusinessDailyDto>();
        List<BusinessDailyDto> listFour = new ArrayList<BusinessDailyDto>();
        List<BusinessDailyDto> listOther = new ArrayList<BusinessDailyDto>();
        // 将一个 list 分成四份
        for (int i = 0; i < saleAmountByPma.size(); i++) {
            if (i < 20) {
                listOne.add(saleAmountByPma.get(i));
            } else if (i >= 20 && i < 40) {
                listTwo.add(saleAmountByPma.get(i));
            } else if (i >= 40 && i < 60) {
                listThree.add(saleAmountByPma.get(i));
            } else if (i >= 60 && i < 80) {
                listFour.add(saleAmountByPma.get(i));
            } else {
                listOther.add(saleAmountByPma.get(i));
            }
        }

        BigDecimal totalOne = new BigDecimal("0");
        BigDecimal totalTwo = new BigDecimal("0");
        BigDecimal totalThree = new BigDecimal("0");
        BigDecimal totalFour = new BigDecimal("0");
        // 绑定数据
        for (int i = 0; i < 20; i++) {
            row = sheet.createRow(curRow++);
            // 第一个表格
            Cell nameCell = row.createCell(0);
            nameCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            Cell valCell = row.createCell(1);
            valCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            if (i < listOne.size()) {
                BusinessDailyDto dto = listOne.get(i);
                setCellValue(nameCell, dto.getCategoryName());
                setCellValue(valCell, dto.getSaleAmount());
                if (dto.getSaleAmount()!=null) {
                    totalOne = totalOne.add(dto.getSaleAmount());
                }
            }

            // 第二个表格
            nameCell = row.createCell(5);
            nameCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            Cell  valCell1 = row.createCell(6);
            valCell1.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
             Cell  valCell2 = row.createCell(7);
            valCell2.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            region = new CellRangeAddress(curRow - 1, curRow - 1, 6, 7);
            sheet.addMergedRegion(region);
            setRegionUtil(region, sheet, wb);
            if (i < listTwo.size()) {
                BusinessDailyDto dto = listTwo.get(i);
                setCellValue(nameCell, dto.getCategoryName());
                setCellValue(valCell1, dto.getSaleAmount());
                if (dto.getSaleAmount()!=null) {
                    totalTwo = totalTwo.add(dto.getSaleAmount());
                }
            }

            // 第三个表格
            nameCell = row.createCell(10);
            nameCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            valCell = row.createCell(11);
            region = new CellRangeAddress(curRow - 1, curRow - 1, 10, 11);
            sheet.addMergedRegion(region);
            valCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
           Cell valCell12 = row.createCell(12);
            valCell12.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            valCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            if (i < listThree.size()) {
                BusinessDailyDto dto = listThree.get(i);
                setCellValue(nameCell, dto.getCategoryName());
                setCellValue(valCell12, dto.getSaleAmount());
                if (dto.getSaleAmount()!=null) {
                    totalThree = totalThree.add(dto.getSaleAmount());
                }
            }

            // 第四个表格
            nameCell = row.createCell(15);
            nameCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            valCell = row.createCell(16);
            valCell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
           Cell valCell17 = row.createCell(17);
            valCell17.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            region = new CellRangeAddress(curRow - 1, curRow - 1, 16, 17);
            sheet.addMergedRegion(region);
            if (i < listFour.size()) {
                BusinessDailyDto dto = listFour.get(i);
                setCellValue(nameCell, dto.getCategoryName());
                setCellValue(valCell, dto.getSaleAmount());
                if (dto.getSaleAmount()!=null) {
                    totalFour = totalFour.add(dto.getSaleAmount());
                }
            }
        }

        // 设置四个表格的total
        row = sheet.createRow(curRow++);
        // 第一个表格
        cell = row.createCell(0);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell,"Total");
        cell = row.createCell(1);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell,totalOne);

        // 第二个表格

       Cell cell1 = row.createCell(5);
        cell1.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));

        Cell  cell2 = row.createCell(6);
        cell2.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        Cell cell15 = row.createCell(7);
        cell15.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        region = new CellRangeAddress(curRow - 1, curRow - 1, 6, 7);
        setCellValue(cell1,"Total");
        sheet.addMergedRegion(region);
        setCellValue(cell2,totalTwo);
        //12 18

        // 第三个表格
        cell = row.createCell(10);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
         Cell cell3 = row.createCell(11);
        cell3.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        cell2 = row.createCell(12);
        cell2.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell2,"Total");

        region = new CellRangeAddress(curRow - 1, curRow - 1, 10, 11);
        sheet.addMergedRegion(region);
        setCellValue(cell2,totalThree);

        // 第四个表格
        cell = row.createCell(15);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        Cell cell6 = row.createCell(16);
        cell6.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        Cell cell5 = row.createCell(17);
        cell5.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell,"Total");
        //12 18
        region = new CellRangeAddress(curRow - 1, curRow - 1, 16, 17);
        sheet.addMergedRegion(region);
        setCellValue(cell5,totalFour);

        // 跳过一行
        curRow++;

        // 设置总的分类销售额
        row = sheet.createRow(curRow++);
        // 第一个表格
        cell = row.createCell(7);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell,"Total Category Sales");

        region = new CellRangeAddress(curRow - 1, curRow - 1, 7, 8);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(9);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell,totalOne.add(totalTwo).add(totalThree).add(totalFour));
        region = new CellRangeAddress(curRow - 1, curRow - 1, 9, 10);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);


        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 13 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
    }

    // 设置银行缴款
    private void setBankDeposit(int rowNum, int curRow, int rowHeight, BusinessDailyDto bankDeposit, Row row, Sheet sheet, Workbook wb) {
        String name = "";
        BigDecimal amt = null;
        if (rowNum == 0) {
            name = "Working capital retained yesterday";
            amt = bankDeposit.getRetentionAmount();
        } else if (rowNum == 1) {
            name = "Subtotal due today";
            amt = bankDeposit.getCashAmount();
        } else if (rowNum == 2) {
            name = "Subtotal of accumulated cash";
            amt = bankDeposit.getReceivablesAmount();
        } else if (rowNum == rowHeight) {
            name = "Total";
            if (bankDeposit.getRetentionAmount()==null) {
                bankDeposit.setRetentionAmount(new BigDecimal("0"));
            }
            if (bankDeposit.getCashAmount()==null) {
                bankDeposit.setCashAmount(new BigDecimal("0"));
            }
            if (bankDeposit.getReceivablesAmount()==null) {
                bankDeposit.setReceivablesAmount(new BigDecimal("0"));
            }
            amt = bankDeposit.getRetentionAmount().add(bankDeposit.getCashAmount()).add(bankDeposit.getReceivablesAmount());
        }
        Cell cell = row.createCell(14);

        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, name);
        CellRangeAddress region = new CellRangeAddress(curRow - 1, curRow - 1, 14, 15);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(16);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, amt);
        region = new CellRangeAddress(curRow - 1, curRow - 1, 16, 17);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);
    }

    // 设置经费
    private void setExpenditureAmt(int i, int curRow, int rowHeight, List<ExpenditureAmtDto> expenditureAmt, Row row, Sheet sheet, Workbook wb) {

        ExpenditureAmtDto expenditureAmtDto = null;
        if (i < expenditureAmt.size()) {
            expenditureAmtDto = expenditureAmt.get(i);
        }

        if (expenditureAmtDto == null) {
            expenditureAmtDto = new ExpenditureAmtDto();
        }

        // 设置total
        if (i == rowHeight) {
            BigDecimal totalAmt = new BigDecimal("0");
            for (ExpenditureAmtDto dto : expenditureAmt) {
                totalAmt = dto.getExpenseAmt().add(totalAmt);
            }
            expenditureAmtDto.setItemName("Total");
            expenditureAmtDto.setExpenseAmt(totalAmt);
        }

//        Cell cell = row.createCell(5);
        Cell cell = row.createCell(7);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, expenditureAmtDto.getItemName());
//        CellRangeAddress region = new CellRangeAddress(curRow - 1, curRow - 1, 5, 6);
        CellRangeAddress region = new CellRangeAddress(curRow - 1, curRow - 1, 7, 8);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

//        cell = row.createCell(7);
        cell = row.createCell(9);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, expenditureAmtDto.getExpenseAmt());
//        region = new CellRangeAddress(curRow - 1, curRow - 1, 7, 8);
        region = new CellRangeAddress(curRow - 1, curRow - 1, 9, 10);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);
    }

    // 设置支付方式
    private void setPayAmt(int rowNum, int curRow, int rowHeight, BusinessDailyDto payAmtDTO, Row row, Sheet sheet, Workbook wb) {
        String payName = "";
        BigDecimal payAmt = null;
        if (rowNum == 0) {
            payName = "Cash";
            payAmt = payAmtDTO.getPayAmt0();
        } else if (rowNum == 1) {
            payName = "Card Payment";
            payAmt = payAmtDTO.getPayAmt1();
        } else if (rowNum == 2) {
            payName = "E-Voucher";
            payAmt = payAmtDTO.getPayAmt2();
        } else if (rowNum == 3) {
            payName = "Momo";
            payAmt = payAmtDTO.getPayAmt3();
        } else if (rowNum == 4) {
            payName = "Payoo";
            payAmt = payAmtDTO.getPayAmt4();
        } else if (rowNum == 5) {
            payName = "Viettel";
            payAmt = payAmtDTO.getPayAmt5();
        } else if (rowNum == rowHeight) {
            payName = "Total";
            payAmt = payAmtDTO.getPayAmt();
        }

        Cell cell = row.createCell(0);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
        setCellValue(cell, payName);
        CellRangeAddress region = new CellRangeAddress(curRow - 1, curRow - 1, 0, 1);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);

        cell = row.createCell(2);
        cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
        setCellValue(cell, payAmt);
        region = new CellRangeAddress(curRow - 1, curRow - 1, 2, 3);
        sheet.addMergedRegion(region);
        setRegionUtil(region, sheet, wb);
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
