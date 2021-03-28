package cn.com.bbut.iy.itemmaster.serviceimpl.export;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.RealTimeInventoryQueryMapper;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryParamDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RealTimeDto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.excel.BaseExcelParam;
import cn.com.bbut.iy.itemmaster.excel.ExService;
import cn.com.bbut.iy.itemmaster.excel.TransSession;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.serviceimpl.CM9060ServiceImpl;
import cn.com.bbut.iy.itemmaster.serviceimpl.RealTimeInventoryQueryServiceImpl;
import cn.com.bbut.iy.itemmaster.util.Utils;
import cn.shiy.common.baseutil.Container;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.*;

/**
 * Excel生成
 *
 */
@Slf4j
@Service(value = "realTimeExService")
public class RealtimeExServiceImpl implements ExService {

    @Autowired
    private RealTimeInventoryQueryMapper mapper;
    @Autowired
    private CM9060ServiceImpl cm9060Service;

    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    RealTimeInventoryQueryMapper realTimeInventoryQueryMapper;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    RealTimeInventoryQueryService realTimeInventoryQueryService;
    @Override
    public File getExcel(BaseExcelParam param) throws IOException {

        TransSession session = Container.getBean(TransSession.class);
        // 查询参数转换
        ExcelParam paramDTO = (ExcelParam) param;
        Gson gson = new Gson();
        RTInventoryQueryParamDTO jsonParam = gson.fromJson(paramDTO.getParam(), RTInventoryQueryParamDTO.class);
        // 导出数据不需要分页
        jsonParam.setFlg(false);
        // 资源权限参数设置
        jsonParam.setStores(paramDTO.getStores());
        jsonParam.setResources(paramDTO.getResources());
        // 获取业务日期
        jsonParam.setBusinessDate(cm9060Service.getValByKey("0000"));
        // 生成文件标题信息对象
        session.setHeaderListener(new RealtimeExHeaderListener(jsonParam));
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
    private void createExcelBody(Sheet sheet, int curRow, RTInventoryQueryParamDTO rTParamDTO) {
        String inEsTime = cm9060Service.getValByKey("1206");
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");

        rTParamDTO.setBusinessDate(dto.getSpValue());
        List<String> articleIdList = new ArrayList<>();
        List<RTInventoryQueryDTO> _list = realTimeInventoryQueryMapper.InventoryQueryBy(rTParamDTO);
        try {
            // List转jackJosn字符串
            String articleIdListJson = new ObjectMapper().writeValueAsString(articleIdList);

            String connUrl = inventoryUrl + "GetRelTimeInventory/"+"/"+rTParamDTO.getStoreCd()
                    +"/*/*/*/*/*/" + inEsTime+"/*/*";
            String urlData = RealTimeInventoryQueryServiceImpl.RequestPost(articleIdListJson,connUrl);
            if(urlData == null || "".equals(urlData)){
                log.info("Failed to connect to live inventory data！");
                return ;
            }
            Gson gson = new Gson();
            // 获取第一层的信息
            ArrayList<RtInvContent> rtInvContent2 = gson.fromJson(urlData,new TypeToken<List<RtInvContent>>() {}.getType());

            RtInvContent rtInvContent = rtInvContent2.get(0);
            if(rtInvContent == null){
                rtInvContent = new RtInvContent();
            }
            String content = rtInvContent.getContent();
            // 获取第二层的信息
            ArrayList<RealTimeDto> realTimeDto2 = gson.fromJson(content,new TypeToken<List<RealTimeDto>>() {}.getType());
            if(realTimeDto2.size()>0) {
                for (RTInventoryQueryDTO rtDto : _list) {
                    for (RealTimeDto realTimeDto : realTimeDto2) {
                        if (realTimeDto.getArticle_id().equals(rtDto.getItemCode())) {
                            rtDto.setAdjustQty(realTimeDto.getAdjustment_qty()); // 当日库存调整数量
                            rtDto.setTransferOutQty(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()));//调拨--调出数量+调出修正
                            rtDto.setOnHandQty(realTimeDto.getOn_hand_qty());// 昨日库存数量
                            rtDto.setSaleQty(realTimeDto.getSale_qty());// 当日销售数量
                            rtDto.setScrapQty(realTimeDto.getWrite_off_qty());//报废数量
                            rtDto.setTransferInQty(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()));//调拨--调入数量+调入修正
                            rtDto.setStoreReturnQty(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));//退货数量 + 退货更正数量
                            rtDto.setOnOrderQty(realTimeDto.getOn_order_qty());// 在途数量
                            rtDto.setReceiveQty(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()));// 当日收货数量 + 收货更正数量
                            // 计算实时库存数量
                            BigDecimal rTimeQty = realTimeDto.getOn_hand_qty().add(realTimeDto.getReceive_qty().add(realTimeDto.getReceive_corr_qty()))
                                    .add(realTimeDto.getAdjustment_qty()).subtract(realTimeDto.getTransfer_out_qty().add(realTimeDto.getTransfer_out_corr_qty()))
                                    .subtract(realTimeDto.getSale_qty()).subtract(realTimeDto.getWrite_off_qty()).add(realTimeDto.getTransfer_in_qty().add(realTimeDto.getTransfer_in_corr_qty()))
                                    .subtract(realTimeDto.getReturn_qty().add(realTimeDto.getReturn_corr_qty()));
                            rtDto.setRealtimeQty(rTimeQty);
                        }
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 查询数据
       // List<RTInventoryQueryDTO> _list = mapper.InventoryQueryBy(jsonParam);
        // 遍历数据
        int no = 1;
        for (RTInventoryQueryDTO ls : _list) {
            int curCol = 0;
            Row row = sheet.createRow(curRow);
            Cell cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_1));
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getItemBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getItemCode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getItemName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getSpecification());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getUom());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getPmaName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getCategoryName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_2));
            setCellValue(cell, ls.getSubCategoryName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getVendorId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_3));
            setCellValue(cell, ls.getVendorName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getRealtimeQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOnHandQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getSaleQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getReceiveQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getStoreReturnQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getAdjustQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOnOrderQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getScrapQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getTransferInQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getTransferOutQty());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOfc());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOfcName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOc());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOcName());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOm());

            cell = row.createCell(curCol++);
            cell.setCellStyle(MAP_STYLE.get(STYPE_KEY_4));
            setCellValue(cell, ls.getOmName());
            curRow++;
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 18 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 15 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 17 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 35 * 256);
        sheet.setColumnWidth(columnIndex++, 33 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 38 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 27 * 256);
        sheet.setColumnWidth(columnIndex++, 27 * 256);
        sheet.setColumnWidth(columnIndex++, 27 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 38 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 27 * 256);
        sheet.setColumnWidth(columnIndex++, 27 * 256);
        sheet.setColumnWidth(columnIndex++, 27 * 256);;
    }

    /**
     * 获取传票类型名称
     *
     * @param status
     */   private String getTypeName(String status) {
        if(status == null){
            return "";
        }
        switch (status) {
            case "601":
                return "In-store Transfer In";
            case "602":
                return "In-store Transfer Out";
            case "603":
                return "Inventory Write-off";
            case "604":
                return "Stock Adjustment";
            case "605":
                return "Take Stock";
            case "500":
                return "Transfer Instructions";
            case "501":
                return "Store Transfer In";
            case "502":
                return "Store Transfer Out";
            case "506":
                return "Transfer";
            default:
                return "";
        }

    }


}
