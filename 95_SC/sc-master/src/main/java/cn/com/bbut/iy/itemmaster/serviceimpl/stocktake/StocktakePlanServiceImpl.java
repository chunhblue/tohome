package cn.com.bbut.iy.itemmaster.serviceimpl.stocktake;

import cn.com.bbut.iy.itemmaster.dao.StocktakeEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.service.*;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakePlanService;
import cn.com.bbut.iy.itemmaster.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Future;

@Slf4j
@Service
@EnableAsync // 2.开启多线程
public class StocktakePlanServiceImpl implements StocktakePlanService {

    @Autowired
    private StocktakePlanMapper stocktakePlanMapper;
    @Autowired
    private StocktakeEntryMapper stocktakeEntryMapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private Ma1200Service ma1200Service;
    @Autowired
    private SequenceService sequenceService;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;

    @Value("${file.fileDir}")
    private String fileDir;//target
    @Autowired
    private Ma4320Service ma4320Service;

    @Override
    @Transactional
    public String insert(PI0100DTO pi0100, List<PI0110DTO> list) {
        String piCd = sequenceService.getSequence("pi0100_isp_id_seq","ISP",pi0100.getStoreCd());
        pi0100.setPiCd(piCd);
        String piDate = pi0100.getPiDate();
        String createUserId = pi0100.getCreateUserId();
        String createYmd = pi0100.getCreateYmd();
        String createHms = pi0100.getCreateHms();

        // 补充数据
        for (PI0110DTO pi0110 : list) {
            if (pi0110 != null) {
                pi0110.setPiCd(piCd);
                pi0110.setPiDate(piDate);
                pi0110.setCreateUserId(createUserId);
                pi0110.setCreateYmd(createYmd);
                pi0110.setCreateHms(createHms);
            }
        }

        // 保存数据
        try {
            stocktakePlanMapper.savePI0100(pi0100);
            stocktakePlanMapper.savePI0110(list);
            //添加审核信息
            stocktakePlanMapper.insertAudit(piCd);
            String businessDate = cm9060Service.getValByKey("0000");
            if(!piDate.equals(businessDate)){
                // 如果不是审核当天，就默认审核通过
                pi0100.setUpdateUserId(createUserId);
                pi0100.setUpdateYmd(createYmd);
                pi0100.setUpdateHms(createHms);
                stocktakePlanMapper.updateAudit(pi0100);
            }
            return piCd;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询数据
     *
     * @param pi0100Param
     * @return
     */
    @Override
    public GridDataDTO<PI0100DTO> search(PI0100ParamDTO pi0100Param) throws ParseException {
        int count = stocktakePlanMapper.selectCountByParam(pi0100Param);

        if (count < 1) {
            return new GridDataDTO<PI0100DTO>();
        }

        List<PI0100DTO> _list = stocktakePlanMapper.search(pi0100Param);
        if(_list.size()>0){
            for(PI0100DTO piDto:_list){
                Integer dayEndOfNow = 0;
                List<Integer> dateList = stocktakePlanMapper.getDayOfEnd(piDto.getPiDate(),piDto.getStoreCd());
                if(dateList.size()>0){
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMdd");
                    Date date =sdf.parse(dateList.get(0).toString());
                    Date date1 = sdf.parse(piDto.getPiDate().toString());
                    dayEndOfNow= Math.toIntExact((date.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000));
              //      dayEndOfNow = dateList.get(0)-Integer.parseInt(piDto.getPiDate());
                }else {
                    int year = Integer.parseInt(piDto.getPiDate().substring(0,4));
                    String month = piDto.getPiDate().substring(4,6);
                    int day = Integer.parseInt(piDto.getPiDate().substring(6,8));
                    int nowDay = 0;int nextDay = 0;
                    switch(month){
                        case "01":
                             nowDay = 31;
                             if(year%4 == 0){
                                 nextDay = 29;
                             }else {
                                 nextDay = 28;
                             }
                             break;
                        case "02":
                            if(year%4 == 0){
                                nowDay = 29;
                            }else {
                                nowDay = 28;
                            }
                            nextDay = 31;
                            break;
                        case "03":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "04":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "05":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "06":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "07":
                            nowDay = 31;
                            nextDay = 31;
                            break;
                        case "08":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "09":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "10":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "11":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "12":
                            nowDay = 31;
                            nextDay = 31;
                            break;
                    }
                    dayEndOfNow = nowDay+nextDay-day;
                }
                piDto.setDayEndOfNow(dayEndOfNow);
            }
        }


        // 格式化日期
        for (PI0100DTO pi0100DTO : _list) {
            String piDate = pi0100DTO.getPiDate();
            String dateFmt = formatDate(piDate);
            pi0100DTO.setPiStartTime(formatTime(pi0100DTO.getPiStartTime()));
            pi0100DTO.setPiEndTime(formatTime(pi0100DTO.getPiEndTime()));
            pi0100DTO.setPiDate(dateFmt);
        }

        GridDataDTO<PI0100DTO> data = new GridDataDTO<PI0100DTO>(_list, pi0100Param.getPage(), count, pi0100Param.getRows());
        return data;
    }

    /**
     * 格式化日期
     *
     * @param piDate
     * @return
     */
    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day + "/" + month + "/" + year;
    }

    /**
     * 格式化时间 2020 07 04 17 05 16
     *
     * @param piTime
     * @return
     */
    private String formatTime(String piTime) {
        if (StringUtils.isEmpty(piTime)) {
            return "";
        }
        String hh = piTime.substring(0, 2);
        String mm = piTime.substring(2, 4);
        String ss = piTime.substring(4, 6);
        return hh + ":" + mm + ":" + ss;
    }

    /**
     * 获取pma List 数据
     *
     * @param page
     * @param rows
     * @param request
     * @param session
     * @return
     */
    @Override
    public GridDataDTO<PI0110DTO> getPmaList(int page, int rows, HttpServletRequest request, HttpSession session) {

        int limit = (page - 1) * rows;

        List<PI0110DTO> pmaList = stocktakePlanMapper.getPmaList(limit, page, rows);
        if (pmaList == null || pmaList.size() < 1) {
            return new GridDataDTO<PI0110DTO>();
        }
        GridDataDTO<PI0110DTO> result = new GridDataDTO<PI0110DTO>(pmaList, page, pmaList.get(0).getNum(), rows);
        return result;
    }

    /**
     * 查询头档信息和明细数据
     *
     * @param piCd
     * @param piDate
     * @return
     */
    @Override
    public PI0100DTO getData(String piCd, String piDate) {
        if (StringUtils.isEmpty(piCd) || StringUtils.isEmpty(piDate)) {
            return null;
        }

        // 获取主档信息
        PI0100DTO pi0100 = stocktakePlanMapper.getPI0100ByPrimary(piCd, piDate);

        if (pi0100 == null) {
            return null;
        }
        // 格式化日期
        String date = formatDate(pi0100.getPiDate());
        pi0100.setPiStartTime(formatTime(pi0100.getPiStartTime()));
        pi0100.setPiEndTime(formatTime(pi0100.getPiEndTime()));
        pi0100.setPiDate(date);
        // 获取明细信息
        List<PI0110DTO> pi0110List = stocktakePlanMapper.getPI0110ByPrimary(piCd, piDate);
        pi0100.setDetails(pi0110List);
        return pi0100;
    }

    /**
     * 获取下拉框参数
     *
     * @return
     */
    @Override
    public Map<String, List<Map<String, String>>> getComboxData() {
        List<Map<String, String>> piType = stocktakePlanMapper.getStocktakeParam("00510");
        List<Map<String, String>> piMethod = stocktakePlanMapper.getStocktakeParam("00520");
        List<Map<String, String>> piStatus = stocktakePlanMapper.getStocktakeParam("00530");

        Map<String, List<Map<String, String>>> map = new HashMap<String, List<Map<String, String>>>();
        map.put("piType", piType);
        map.put("piMethod", piMethod);
        map.put("piStatus", piStatus);
        return map;
    }

    /**
     * 修改
     *
     * @param pi0100
     * @param pi0110List
     * @return
     */
    @Override
    public String update(PI0100DTO pi0100, List<PI0110DTO> pi0110List) {
        String piCd = pi0100.getPiCd();
        String piDate = pi0100.getPiDate();

        // 补充数据
        for (PI0110DTO pi0110 : pi0110List) {
            if (pi0110 != null) {
                pi0110.setPiCd(piCd);
                pi0110.setPiDate(piDate);
            }
        }

        try {
            // 保存数据
            stocktakePlanMapper.deletePI0110(piCd, piDate);
            stocktakePlanMapper.updatePI0100(pi0100);
            stocktakePlanMapper.savePI0110(pi0110List);
            //添加审核信息
            stocktakePlanMapper.insertAudit(piCd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return piCd;
    }

    @Override
    public int checkPicd(String piCd) {
        return stocktakePlanMapper.checkPicd(piCd);
    }

    @Override
    public List<StocktakeItemDTO> queryExport(String piCd, String piDate, String storeCd) {
        String inEsTime = cm9060Service.getValByKey("1206");
        List<String> articles = new ArrayList<>();
        //获取需要导出的对象
        List<StocktakeItemDTO> list = stocktakePlanMapper.queryExport(piCd, piDate, storeCd);
        for(StocktakeItemDTO item:list){
            articles.add(item.getArticleId());
            item.setRealTimeQty(BigDecimal.ZERO);
        }
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" + storeCd + "/"
                + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
        if(rTdTOList.size()>0){
            for(RTInventoryQueryDTO rtDto:rTdTOList){
                for(StocktakeItemDTO stockDto:list){
                    if(stockDto.getArticleId().equals(rtDto.getItemCode())){
                        if(rtDto.getRealtimeQty() == null){
                            rtDto.setRealtimeQty(BigDecimal.ZERO);
                        }
                        if(rtDto.getSaleQty() == null){
                            rtDto.setSaleQty(BigDecimal.ZERO);
                        }
                        stockDto.setRealTimeQty(rtDto.getRealtimeQty()); // 实时库存
                        stockDto.setSaleQtyTotal(rtDto.getSaleQty());   // 当日销售数量
                    }
                }
            }
        }else {
            for(StocktakeItemDTO stockDto:list){
                stockDto.setRealTimeQty(BigDecimal.ZERO);
                stockDto.setSaleQtyTotal(BigDecimal.ZERO);
            }
        }



        // 需要验证group 母货号是否可以 参与盘点
//        List<String> articleIdArr = new ArrayList<String>();

        // 判断母货号是否允许操作, 为 '0' 可以操作, 为 '1' 不可操作
//        String _val = cm9060Service.getValByKey("0634");

        for (StocktakeItemDTO item : list) {
            // 添加商品id
//            articleIdArr.add(item.getArticleId());

            // 销售数量
            BigDecimal saleQtyTotal = item.getSaleQtyTotal();

            // 盘点异动只考虑销售数量
            item.setCQty1(saleQtyTotal);
        }

        // 不可操作, 需要过滤掉 导出对象里的 group 商品
        /*if (!"0".equals(_val)) {
             验证是否包含group sale 母货号
            List<String> groupItemList = ma1200Service.checkList(articleIdArr);
            List<StocktakeItemDTO> newItemList = new ArrayList<StocktakeItemDTO>();
            // 验证
            if (groupItemList != null && groupItemList.size() > 0) {
                for (StocktakeItemDTO item : list) {
                    for (String groupId : groupItemList) {
                        if (!groupId.equals(item.getArticleId())) {
                            newItemList.add(item);
                        }
                    }
                }
                return newItemList;
            }
        }*/

        return list;
    }

    /**
     * 设置 超过 End Time 没有 submit 提交的 计划过期
     */
    @Override
    public void updateStocktakingPlanExpired() {
        String bsDate = cm9060Service.getValByKey("0000");
        stocktakePlanMapper.updateStocktakingPlanExpired(bsDate);
    }


    /**
     * 将库存异动数据生成excel
     */
    @Override
    public HSSFWorkbook getInventoryHSSFWorkbook(PI0100ParamDTO pi0100Param, String tempTableName) {

        List<Map<String, Object>> list = stocktakePlanMapper.getInventoryData(pi0100Param,tempTableName);

        if (list == null || list.size() == 0) {
            return null;
        }

        String[] excelHeader0 = {"SAPItemCode", "FromDate", "Todate", "SalesQty", "ReceivingQty", "TransferInQty",
                "TransferOutQty", "ReturnQty","StockIn", "StockOut (writeoff, …)"};

        // 声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = wb.createSheet();

        // 生成一种样式
        HSSFCellStyle style = wb.createCellStyle();
        // 设置样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成一种字体
        HSSFFont font = wb.createFont();
        // 设置字体
        font.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 在样式中引用这种字体
        style.setFont(font);

        // 行号标记
        int rowNum = 0;
        // 表头
        HSSFRow row = sheet.createRow(rowNum++);
        for (int i = 0; i < excelHeader0.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader0[i]);
            cell.setCellStyle(style);
        }
        // 转换日期
        String startTime = CommonUtils.fmtDateAndTimeToStr(pi0100Param.getPiDate()+"060000");
        String endTime = CommonUtils.fmtDateAndTimeToStr(pi0100Param.getPiDate()+pi0100Param.getStartTime());
        // 遍历内容
        HSSFCell cell = null;
        Map<String, Object> map = null;
        for (int i = 0; i < list.size(); i++) {
            map = list.get(i);
            int callNum = 0;
            row = sheet.createRow(rowNum++);
            // article id
            cell = row.createCell(callNum++);
            cell.setCellValue(map.get("article_id").toString());
            cell.setCellStyle(style);

            // startTime
            cell = row.createCell(callNum++);
            cell.setCellValue(startTime);
            cell.setCellStyle(style);

            // endTime
            cell = row.createCell(callNum++);
            cell.setCellValue(endTime);
            cell.setCellStyle(style);

            double sale_qty = Double.valueOf(map.get("sale_qty").toString());
            double receive_qty = Double.valueOf(map.get("receive_qty").toString());
            double trans_in_qty = Double.valueOf(map.get("trans_in_qty").toString());
            double trans_out_qty = Double.valueOf(map.get("trans_out_qty").toString());
            double return_qty = Double.valueOf(map.get("return_qty").toString());
            double item_trans_in_qty = Double.valueOf(map.get("item_trans_in_qty").toString());
            double item_trans_out_qty = Double.valueOf(map.get("item_trans_out_qty").toString());
            double receive_corr_qty = Double.valueOf(map.get("receive_corr_qty").toString());
            double return_corr_qty = Double.valueOf(map.get("return_corr_qty").toString());
            double write_off_qty = Double.valueOf(map.get("write_off_qty").toString());
            double positive_adjust_qty = Double.valueOf(map.get("positive_adjust_qty").toString());
            double negative_adjust_qty = Double.valueOf(map.get("negative_adjust_qty").toString());
            receive_qty = receive_corr_qty != 0?receive_corr_qty:receive_qty;
            return_qty = return_corr_qty != 0?return_corr_qty:return_qty;

            // sale_qty
            cell = row.createCell(callNum++);
            cell.setCellValue(sale_qty);
            cell.setCellStyle(style);

            // receive_qty
            cell = row.createCell(callNum++);
            cell.setCellValue(receive_qty);
            cell.setCellStyle(style);

            // trans_in_qty
            cell = row.createCell(callNum++);
            cell.setCellValue(trans_in_qty);
            cell.setCellStyle(style);

            // trans_out_qty
            cell = row.createCell(callNum++);
            cell.setCellValue(trans_out_qty);
            cell.setCellStyle(style);

            // return_qty
            cell = row.createCell(callNum++);
            cell.setCellValue(return_qty);
            cell.setCellStyle(style);

            // 转入合计
//            double stockIn = receive_qty+trans_in_qty+item_trans_in_qty;
            double stockIn = positive_adjust_qty+item_trans_in_qty;
            // 转出合计
//            double stockOut = return_qty+trans_out_qty+write_off_qty+item_trans_out_qty;
            double stockOut = negative_adjust_qty+write_off_qty+item_trans_out_qty;

            // StockIn
            cell = row.createCell(callNum++);
            cell.setCellValue(stockIn);
            cell.setCellStyle(style);

            // StockOut
            cell = row.createCell(callNum++);
            cell.setCellValue(stockOut);
            cell.setCellStyle(style);
        }

        for (int i = 0; i < excelHeader0.length; i++) {
            //根据字段长度自动调整列的宽度
            sheet.autoSizeColumn(i, true);
        }
        return wb;
    }

    @Override
    public void insertInventoryToTemp(PI0100ParamDTO pi0100Param, String tempTableName) {
        // 将数据存入临时表
        stocktakePlanMapper.insertInventoryToTemp(pi0100Param,tempTableName);
    }


    @Override
    public HSSFWorkbook getExportHSSFWorkbook(List<StocktakeItemDTO> list) {

        if (list == null || list.size() == 0) {
            return null;
        }

        /**
         * 创建列名数据
         */
        // 声明String数组，并初始化元素（表头名称）
        //第二行表头字段，合并单元格时字段跨几列就将该字段重复几次
        String[] excelHeader0 = {"Item Code", "Item Name", "Barcode", "Spec", "UOM", "Quantity"};
        //  “0,2,0,0”  ===>  “起始行，截止行，起始列，截止列”
        String[] headnum0 = {"0,3,0,0", "2,3,1,1", "2,3,2,2", "2,3,2,2", "2,3,3,3", "2,3,4,4"};

        // 声明一个工作簿
        HSSFWorkbook wb = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = wb.createSheet("StocktakeItemExport");

        // 生成一种样式
        HSSFCellStyle style = wb.createCellStyle();
        // 设置样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成一种字体
        HSSFFont font = wb.createFont();
        // 设置字体
        font.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 在样式中引用这种字体
        style.setFont(font);

        // 生成并设置另一个样式
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体2
        HSSFFont font2 = wb.createFont();
        // 设置字体
        font2.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font2.setFontHeightInPoints((short) 10);
        // 在样式2中引用这种字体
        style2.setFont(font2);


        // 生成并设置另一个样式
        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style3.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style3.setBorderRight(HSSFCellStyle.BORDER_NONE);
        style3.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体3
        HSSFFont font3 = wb.createFont();
        // 设置字体
        font3.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font3.setFontHeightInPoints((short) 10);
        // 在样式2中引用这种字体
        style3.setFont(font3);

        // 生成并设置另一个样式
        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFillForegroundColor(HSSFColor.WHITE.index);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style4.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style4.setBorderRight(HSSFCellStyle.BORDER_NONE);
        style4.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        HSSFFont font4 = wb.createFont();
        // 设置字体
        font4.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font4.setFontHeightInPoints((short) 24);
        // 字体加粗
        font4.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 在样式4中引用这种字体
        style4.setFont(font4);

        // 设置列宽
        sheet.setColumnWidth(8, 30);
        sheet.setColumnWidth(9, 30);


        // 生成表格的第三行
        // 第三行表头
        HSSFRow row = sheet.createRow(0);
        row = sheet.createRow(0);
        for (int i = 0; i < excelHeader0.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader0[i]);
            cell.setCellStyle(style);
        }

        // 第五行数据
        for (int i = 0; i < list.size(); i++) {

            row = sheet.createRow(i + 1);
            StocktakeItemDTO report = list.get(i);

            // 导入对应列的数据
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(report.getArticleId());
            cell.setCellStyle(style2);

            HSSFCell cell1 = row.createCell(1);
            cell1.setCellValue(report.getArticleName());
            cell1.setCellStyle(style2);

            HSSFCell cell2 = row.createCell(2);
            cell2.setCellValue(report.getBarcode());
            cell2.setCellStyle(style2);

            HSSFCell cell3 = row.createCell(3);
            cell3.setCellValue(report.getSpec());
            cell3.setCellStyle(style2);

            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(report.getUom());
            cell4.setCellStyle(style2);

            HSSFCell cell5 = row.createCell(5);
            cell5.setCellValue(""); // Component Name
            cell5.setCellStyle(style2);
        }
        for (int i = 0; i <= 5; i++) {
            //根据字段长度自动调整列的宽度
            sheet.autoSizeColumn(i, true);
        }
        return wb;
    }


    @Override
    public File writeToTXTFile(HttpServletRequest request,List<StocktakeItemDTO> list,String piCd){

        FileOutputStream outStr = null;
        BufferedOutputStream buff = null;
        String path = fileDir + "ItemsmasterStocktake.txt";
        String tab = "\t";
        String enter = "\r\n";
        StocktakeItemDTO stockDTO;
        StringBuffer write;
        File outFile = null;
        try{
            outFile = new File(path);
            outStr = new FileOutputStream(outFile);
            buff = new BufferedOutputStream(outStr);
            // 设置每一列的最大长度
            int length = 20;
            int articleNameLength = 100;
            for(int i=0;i<list.size();i++){
                write = new StringBuffer();
                if(i == 0){
                    write.append("Item Code")
                            .append(tab)
                            .append("Item Name")
                            .append(tab)
                            .append("BarCode")
                            .append(tab)
                            .append("Unit")
                            .append(tab)
                            .append("Converter").append(enter);
                }
                stockDTO = list.get(i);
                write.append(stockDTO.getArticleId());
                write.append(tab);
                write.append(stockDTO.getArticleName());
                write.append(tab);
                write.append(stockDTO.getBarcode());
                write.append(tab);
                write.append(stockDTO.getUom());
                write.append(tab);
                write.append(stockDTO.getConverter());
                write.append(enter);
                buff.write(write.toString().getBytes("UTF-8"));
            }
            buff.flush();
            buff.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                buff.close();
                outStr.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return outFile;
    }

    /**
     * 将导出的商品 保存到数据库
     */
    @Transactional
    @Override
    @Async
    public Future<String> insertExportItemsToDB(String piCd, String piDate, String storeCd, List<StocktakeItemDTO> list) {
        String tempTableName = "temp_item";
        stocktakePlanMapper.createTempTable(tempTableName);
        // 批量保存数据到临时表
        int totalSize1 = list.size(); //总记录数
        int pageSize1 = 1000; //每页N条
        int totalPage1 = totalSize1/pageSize1; //共N页

        if (totalSize1 % pageSize1 != 0) {
            totalPage1 += 1;
            if (totalSize1 < pageSize1) {
                pageSize1 = list.size();
            }
        }
        for (int pageNum1 = 1; pageNum1 < totalPage1+1; pageNum1++) {
            int starNum1 = (pageNum1-1)*pageSize1;
            int endNum1 = Math.min(pageNum1 * pageSize1, totalSize1);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<>();
            for (int i = starNum1; i < endNum1; i++) {
                newList.add(list.get(i));
            }
            // 保存数据到临时表里
            stocktakePlanMapper.saveToTempTable(tempTableName,newList);
        }

        // 过滤掉 inventory =no/non-count/cost_item = Yes 的商品
        List<StocktakeItemDTO> list1 = stocktakePlanMapper.getstockList(tempTableName);
        List<StocktakeItemDTO> oldList = new ArrayList<>();
        for(StocktakeItemDTO dto:list1){
           boolean flg = false;
           for(StocktakeItemDTO oldItem:oldList){
               if(dto.getArticleId().equals(oldItem.getArticleId())){
                   flg = true;
               }
           }
           if(!flg){
               oldList.add(dto);
           }
        }
        stocktakeEntryMapper.deleteTempTable(tempTableName);
        // 获取导出时间
        String exportTime = ma4320Service.getNowDate();
//        String exportTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        //2.分页数据信息
        int totalSize = oldList.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize / pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = oldList.size();
            }
        }
        // 先查询是否保存过数据
        int count = stocktakePlanMapper.getPi0125CountByPicd(piCd, piDate, storeCd);

        if (count > 0) {
            // 先删除
            // stocktakePlanMapper.deletePi0125ByPicd(piCd,piDate,storeCd);
            // 只有第一次导出才可以生成库存数据
            return null;
        }
        // 没有保存过代表第一次 导出, 记录 导出时间
        stocktakePlanMapper.modifyPI0100ExportTime(piCd, piDate, storeCd, exportTime);
        // 将第一次的导出时间更新到Start Time 注释 2021/03/30
//        String startTime = exportTime.substring(8,14);
//        stocktakePlanMapper.modifyPI0100StartTime(piCd, piDate, storeCd, startTime);

        for (int pageNum = 1; pageNum < totalPage + 1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            int endNum = Math.min(pageNum * pageSize, totalSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                oldList.get(i).setExportTime(exportTime);
                newList.add(oldList.get(i));
            }

            // 保存到数据库
            stocktakePlanMapper.insertExportItemsToDB(piCd, piDate, storeCd, newList);
        }

        return new AsyncResult<String>("ok");
    }

    @Override
    public List<PI0100DTO> getPrintData(PI0100ParamDTO pi0100Param) {
        List<PI0100DTO> _list = stocktakePlanMapper.getPrintData(pi0100Param);
        if(_list.size()>0){
            for(PI0100DTO piDto:_list){
                Integer dayEndOfNow = 0;
                List<Integer> dateList = stocktakePlanMapper.getDayOfEnd(piDto.getPiDate(),piDto.getStoreCd());
                if(dateList.size()>0){
                    dayEndOfNow = dateList.get(0)-Integer.parseInt(piDto.getPiDate());
                }else {
                    int year = Integer.parseInt(piDto.getPiDate().substring(0,4));
                    String month = piDto.getPiDate().substring(4,6);
                    int day = Integer.parseInt(piDto.getPiDate().substring(6,8));
                    int nowDay = 0;int nextDay = 0;
                    switch(month){
                        case "01":
                            nowDay = 31;
                            if(year%4 == 0){
                                nextDay = 29;
                            }else {
                                nextDay = 28;
                            }
                            break;
                        case "02":
                            if(year%4 == 0){
                                nowDay = 29;
                            }else {
                                nowDay = 28;
                            }
                            nextDay = 31;
                            break;
                        case "03":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "04":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "05":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "06":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "07":
                            nowDay = 31;
                            nextDay = 31;
                            break;
                        case "08":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "09":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "10":
                            nowDay = 31;
                            nextDay = 30;
                            break;
                        case "11":
                            nowDay = 30;
                            nextDay = 31;
                            break;
                        case "12":
                            nowDay = 31;
                            nextDay = 31;
                            break;
                    }
                    dayEndOfNow = nowDay+nextDay-day;
                }
                piDto.setDayEndOfNow(dayEndOfNow);
            }
        }
        // 格式化日期
        for (PI0100DTO pi0100DTO : _list) {
            String piDate = pi0100DTO.getPiDate();
            String dateFmt = formatDate(piDate);
            pi0100DTO.setPiStartTime(formatTime(pi0100DTO.getPiStartTime()));
            pi0100DTO.setPiEndTime(formatTime(pi0100DTO.getPiEndTime()));
            pi0100DTO.setPiDate(dateFmt);
        }
        return _list;
    }

    /**
     * 获得店铺下所有商品的 department
     */
    @Override
    public ReturnDTO getAllItemDepartmentByStore(String storeCd) {
        String businessDate = cm9060Service.getValByKey("0000");
        List<PI0110DTO> result = stocktakePlanMapper.getAllItemDepartmentByStore(storeCd, businessDate);

        if (result == null || result.size() < 1) {
            return new ReturnDTO(false, "Query empty!");
        }

        return new ReturnDTO(true, "Query success!", result);
    }


    public String appentStr4Length(String str , int length){
        if(str == null){
            str = "";
        }
        try {
            int strLen = 0;//计算原字符串所占长度,规定中文占两个,其他占一个
            for(int i = 0 ; i<str.length(); i++){
                if(isChinese(str.charAt(i))){
                    strLen = strLen + 2;
                }else{
                    strLen = strLen + 1;
                }
            }
            if(strLen>=length){
                return str;
            }
            int remain = length - strLen;//计算所需补充空格长度
//            log.info("<<<"+remain);
            for(int i =0 ; i< remain ;i++){
                if(i == 0){
                    str = str + ",";
                }
                str = str + " ";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}
