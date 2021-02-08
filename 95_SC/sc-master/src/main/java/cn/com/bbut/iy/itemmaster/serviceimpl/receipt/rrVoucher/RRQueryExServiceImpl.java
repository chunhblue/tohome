package cn.com.bbut.iy.itemmaster.serviceimpl.receipt.rrVoucher;

import cn.com.bbut.iy.itemmaster.dao.RRVoucherMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.rrVoucher.RRVoucherParamDTO;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
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
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "rrQueryExService")
public class RRQueryExServiceImpl implements ExService {

    @Autowired
    private RRVoucherMapper mapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Override
    public File getExcel(BaseExcelParam param) throws IOException {
        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        RRVoucherParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), RRVoucherParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        jsonParam.setStores(paramDTO.getStores());
        // 获取业务日期
        if(StringUtils.isBlank(jsonParam.getBusinessDate())){
            jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        }
        // 特定审核状态分离为两步退货审核状态
        switch (jsonParam.getOrderSts()){
            case "21":
                jsonParam.setOrderSts(null);
                jsonParam.setStatus(1);
                break;
            case "5":
                jsonParam.setOrderSts("5");
                jsonParam.setStatus(5);
                break;
            case "6":
                jsonParam.setOrderSts("6");
                jsonParam.setStatus(6);
                break;
            case "7":
                jsonParam.setOrderSts("7");
                jsonParam.setStatus(7);
                break;
            default:
                jsonParam.setOrderSts(jsonParam.getOrderSts());
                jsonParam.setStatus(0);
        }
        // 生成文件标题信息对象
        session.setHeaderListener(new RRQueryExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, RRVoucherParamDTO jsonParam) {
        // 查询数据
        List<RRVoucherDTO> _list = mapper.selectListByCondition(jsonParam);
        // 遍历数据
        int no = 1;
        for (RRVoucherDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValue(cell, fmtDateToStr(ls.getOrderDate()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOrderId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOrgOrderId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, getTypeName(ls.getOrderType()));

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getStoreCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getStoreName());

            /*cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOfc());*/

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOfcName());

            /*cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOm());*/

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOmName());

            /*cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getOc());*/

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOcName());



            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getWarehouseCd());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getWarehouseName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getOrderMethodName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_6));
            setCellValue(cell, fmtRate(ls.getTaxRate()));

            if(ls.getReviewStsText() != null){
                if(ls.getReviewStsText().equals("1")){
                    ls.setReviewStsText("Return Request Pending");
                }else if(ls.getReviewStsText().equals("5")){
                    ls.setReviewStsText("Rejected");
                }else if(ls.getReviewStsText().equals("6")){
                    ls.setReviewStsText("Withdrawn");
                }else if(ls.getReviewStsText().equals("7")){
                    ls.setReviewStsText("Expired");
                }else if(ls.getReviewStsText().equals("10")){
                    // 这里考虑实际退货的时候的pending/Rejected/Withdrawn/Expired状态
                    switch (ls.getStatus()){
                        case 1:
                            ls.setReviewStsText("Return Pending");
                            break;
                        case 5:
                            ls.setReviewStsText("Return Rejected");
                            break;
                        case 6:
                            ls.setReviewStsText("Return Withdrawn");
                            break;
                        case 7:
                            ls.setReviewStsText("Return Expired");
                            break;
                        default:
                            ls.setReviewStsText("Return Request Approved");
                    }
                }else if(ls.getReviewStsText().equals("15")){
                    ls.setReviewStsText("Receiving Pending");
                }else if(ls.getReviewStsText().equals("20")){
                    if(ls.getOrderType().equals("0")){
                        ls.setReviewStsText("Returned");
                    }else{
                        ls.setReviewStsText("Received");
                    }
                }else {
                    ls.setReviewStsText("");
                }
            }
            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getReviewStsText());

            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 22 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 23 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 19 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
    }

    /**
     * 单据类型名称
     *
     * @param status
     */
    private String getTypeName(String status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case "1":
                return "[1] Receiving Document";
            case "0":
                return "[0] Return Document";
            default:
                return "";
        }
    }

    /**
     * 审核状态获取
     *
     * @param status
     */
    private String getStatus(Integer status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case 1:
                return "Pending";
            case 5:
                return "Rejected";
            case 6:
                return "Withdrawn";
            case 10:
                return "Approved";
            case 20:
                return "Returned/Received";
            case 7:
                return "Expired";
            default:
                return "";
        }
    }
    // 格式化百分比
   private String  fmtRate(BigDecimal taxRate){
        if(taxRate==null){
            return  "0%";
        }else {
          return  taxRate.multiply(new BigDecimal(100)).toString()+"%";

        }
    }
}
