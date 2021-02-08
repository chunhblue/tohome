package cn.com.bbut.iy.itemmaster.serviceimpl.stocktake;

import cn.com.bbut.iy.itemmaster.dao.StocktakeEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0110DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.Ma1200Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakeEntryService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakePlanService;
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
    private StocktakeEntryService stocktakeEntryService;

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
        List<String> articleIdArr = new ArrayList<String>();

        // 判断母货号是否允许操作, 为 '0' 可以操作, 为 '1' 不可操作
        String _val = cm9060Service.getValByKey("0634");

        for (StocktakeItemDTO item : list) {
            // 添加商品id
            articleIdArr.add(item.getArticleId());

            // 销售数量
            BigDecimal saleQtyTotal = item.getSaleQtyTotal();

            // 盘点异动只考虑销售数量
            item.setCQty1(saleQtyTotal);
        }

        // 不可操作, 需要过滤掉 导出对象里的 group 商品
        if (!"0".equals(_val)) {
            // 验证是否包含group sale 母货号
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
        }

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
    public File writeToCSVFile(HttpServletRequest request,List<StocktakeItemDTO> list,String piCd){
        String fileName = "Stocktake Items_"+piCd+".csv";
        String path = request.getSession().getServletContext().getRealPath(fileDir + "Stocktake Items_"+piCd+".csv");

        LinkedHashMap map = new LinkedHashMap();
        // 设置列名
        map.put("1","Item Code");
        map.put("2","Item Name");
        map.put("3","Barcode");
        map.put("4","Spec");
        map.put("5","UOM");
        map.put("6","Quantity");

        List exportData = new ArrayList<Map>();
        for(StocktakeItemDTO stockDTO:list){
            Map row = new LinkedHashMap<String, String>();
            row.put("1",stockDTO.getArticleId());
            row.put("2",stockDTO.getArticleName());
            row.put("3",stockDTO.getBarcode());
            row.put("4",stockDTO.getSpec());
            row.put("5",stockDTO.getUom());
            row.put("6","");
            exportData.add(row);
        }
        File file = createCSVFile(exportData, map, path, fileName);

        return file;
    }

    /**
     * 生成CVS文件
     * @param exportData 源数据List
     * @param map csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName 文件名称
     * @return
     */
    public File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,
                                     String fileName) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //定义文件名格式并创建
            csvFile =new File(outPutPath+fileName);
            file.createNewFile();
            // UTF-8使正确读取分隔符","
            //如果生产文件乱码，windows下用gbk，linux用UTF-8
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                    csvFile), "UTF-8"), 1024);

            //写入前段字节流，防止乱码
            csvFileOutputStream.write(getBOM());
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" );
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
                Object row = iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                    String str=row!=null?((String)((Map)row).get( propertyEntry.getKey())):"";

                    if(StringUtils.isEmpty(str)){
                        str="";
                    }else{
                        str=str.replaceAll("\"","\"\"");
                        if(str.indexOf(",")>=0){
                            str="\""+str+"\"";
                        }
                    }
                    csvFileOutputStream.write(str);
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

    /**
     * 将导出的商品 保存到数据库
     */
    @Transactional
    @Override
    @Async
    public Future<String> insertExportItemsToDB(String piCd, String piDate, String storeCd, List<StocktakeItemDTO> oldList) {
        // 获取导出时间
        String exportTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

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
        // 将第一次的导出时间更新到Start Time
        String startTime = exportTime.substring(8,14);
        stocktakePlanMapper.modifyPI0100StartTime(piCd, piDate, storeCd, startTime);

        for (int pageNum = 1; pageNum < totalPage + 1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            int endNum = pageNum * pageSize > totalSize ? (totalSize) : pageNum * pageSize;

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


    /**
     * 功能说明：获取UTF-8编码文本文件开头的BOM签名。
     * BOM(Byte Order Mark)，是UTF编码方案里用于标识编码的标准标记。例：接收者收到以EF BB BF开头的字节流，就知道是UTF-8编码。
     * @return UTF-8编码文本文件开头的BOM签名
     */
    public static String getBOM() {

        byte b[] = {(byte)0xEF, (byte)0xBB, (byte)0xBF};
        return new String(b);
    }
}
