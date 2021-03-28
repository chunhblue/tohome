package cn.com.bbut.iy.itemmaster.serviceimpl.operationmanagement;

import cn.com.bbut.iy.itemmaster.dao.CustEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.CustEntryPlanMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.ItemInStoreDto;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.*;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.opreationmanagement.CustOfEntryPlanService;
import cn.com.bbut.iy.itemmaster.util.CommonUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustOfEntryPlanServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/19 14:37
 * @Version 1.0
 */
@Service
public class CustOfEntryPlanServiceImpl implements CustOfEntryPlanService {
  @Autowired
  private CustEntryPlanMapper custOfEntryPlanMapper;

  @Autowired
  private CustEntryMapper custEntryMapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;



    @Override
    public int insert(PI0100DTOC pi0100c, List<PI0110DTOC> list) {
        String piCd = pi0100c.getPiCd();
        String piDate = pi0100c.getPiDate();
        String createUserId =pi0100c.getCreateUserId();
        String createYmd = pi0100c.getCreateYmd();
        String createHms = pi0100c.getCreateHms();
        for (PI0110DTOC pi0110c : list) {
            if (pi0110c!=null) {
                pi0110c.setPiCd(piCd);
                pi0110c.setPiDate(piDate);
                pi0110c.setCreateUserId(createUserId);
                pi0110c.setCreateYmd(createYmd);
                pi0110c.setCreateHms(createHms);
            }
        }

        // 保存数据
        try {
            custOfEntryPlanMapper.savePI0100(pi0100c);
            custOfEntryPlanMapper.savePI0110(list);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Paramc) {

        int count =custOfEntryPlanMapper.selectCountByParam(pi0100Paramc);
        if (count < 1) {
            return new GridDataDTO<PI0100DTOC>();
        }
        List<PI0100DTOC> _list = custOfEntryPlanMapper.search(pi0100Paramc);

        GridDataDTO<PI0100DTOC> data = new GridDataDTO<PI0100DTOC>(_list,pi0100Paramc.getPage(), count, pi0100Paramc.getRows());
        return data;
    }
    /**
     * 格式化日期
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
        return day+"/"+month+"/"+year;
    }


    @Override
    public List<Map<String, String>> getPmaList(HttpServletRequest request, HttpSession session) {
        return custOfEntryPlanMapper.getPmaList();
    }

    @Override
    public PI0100DTOC getData(String piCd,String storeCd) {
        String inEsTime = cm9060Service.getValByKey("1206");
        if (StringUtils.isEmpty(piCd)) {
            return null;
        }
        PI0100DTOC pi0100c = custOfEntryPlanMapper.getPI0100ByPrimary(piCd);

        if (pi0100c==null) {
            return null;
        }
        List<String> articles = new ArrayList<>();
        // 获取明细信息
        List<StocktakeItemDTOC> pi0110List =  custEntryMapper.getPI0130ByPrimaryIn(piCd);
        for(StocktakeItemDTOC item:pi0110List){
            articles.add(item.getArticleId());
        }
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" + storeCd + "/"
                + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
        if(rTdTOList.size()>0){
            for(RTInventoryQueryDTO rtDto:rTdTOList){
                for(StocktakeItemDTOC stockDto:pi0110List){
                    if(rtDto.getItemCode().equals(stockDto.getArticleId())){
                        stockDto.setStockQty(rtDto.getRealtimeQty().toString()); // 实时库存
                    }
                }
            }
        }
        pi0100c.setItemList(pi0110List);
        return pi0100c;
    }

    @Override
    public Map<String, List<Map<String, String>>> getComboxData() {
        List<Map<String,String>> piType =  custOfEntryPlanMapper.getStocktakeParam("00510");
        List<Map<String,String>> piMethod =  custOfEntryPlanMapper.getStocktakeParam("00520");
        List<Map<String,String>> piStatus =  custOfEntryPlanMapper.getStocktakeParam("00530");

        Map<String,List<Map<String,String>>> map = new HashMap<String,List<Map<String,String>>>();
        map.put("piType",piType);
        map.put("piMethod",piMethod);
        map.put("piStatus",piStatus);
        return map;
    }

    @Override
    public int update(PI0100DTOC pi0100c, List<PI0110DTOC> pi0110List) {
        String piCd = pi0100c.getPiCd();
        String piDate = pi0100c.getPiDate();
        for (PI0110DTOC pi0110c : pi0110List) {
            if (pi0110c!=null) {
                pi0110c.setPiCd(piCd);
                pi0110c.setPiDate(piDate);
            }
        }
        try {
            // 保存数据
            custOfEntryPlanMapper.deletePI0110(piCd,piDate);
            custOfEntryPlanMapper.updatePI0100(pi0100c);
            custOfEntryPlanMapper.savePI0110(pi0110List);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        return 1;
    }

    @Override
    public int checkPicd(String piCd) {
      return custOfEntryPlanMapper.checkPicd(piCd);
    }



    /*
    *
    * HSSFWorkboot 生成excel表格
    *
    * */
    @Override
    public HSSFWorkbook getExportHSSFWorkbook(PI0100ParamDTOC pi0100Paramc, String formCondition, String piCd, String piDate) {
        //获取需要导出的对象
        List<StocktakeItemDTOC> list =custOfEntryPlanMapper.queryExport(piCd,piDate);

        if(list == null || list.size() == 0) {
            return null;
        }
        /**
         * 创建列名数据
         */
        // 声明String数组，并初始化元素（表头名称）
        //第二行表头字段，合并单元格时字段跨几列就将该字段重复几次
        String[] excelHeader0 = {"Item Code","Item Name","Barcode","UOM","Quantity"};
        //  “0,2,0,0”  ===>  “起始行，截止行，起始列，截止列”
        String[] headnum0 = { "0,3,0,0", "2,3,1,1", "2,3,2,2", "2,3,3,3", "2,3,4,4"};

        // 声明一个工作簿
        HSSFWorkbook exl = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = exl.createSheet("StocktakeItemExport");
        // 生成一种样式
        HSSFCellStyle style =exl.createCellStyle();
        // 设置样式
        style.setFillForegroundColor(HSSFColor.WHITE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
         //生成一种字体
        HSSFFont font = exl.createFont();
        font.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font.setFontHeightInPoints((short) 10);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 在样式中引用这种字体
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = exl.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.WHITE.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体2
        HSSFFont font2 = exl.createFont();
        // 设置字体
        font2.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font2.setFontHeightInPoints((short) 10);
        // 在样式2中引用这种字体
        style2.setFont(font2);


        // 生成并设置另一个样式
        HSSFCellStyle style3 = exl.createCellStyle();
        style3.setFillForegroundColor(HSSFColor.WHITE.index);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style3.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style3.setBorderRight(HSSFCellStyle.BORDER_NONE);
        style3.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style3.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        style3.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体3
        HSSFFont font3 = exl.createFont();
        // 设置字体
        font3.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font3.setFontHeightInPoints((short) 10);
        // 在样式2中引用这种字体
        style3.setFont(font3);

        // 生成并设置另一个样式
        HSSFCellStyle style4 = exl.createCellStyle();
        style4.setFillForegroundColor(HSSFColor.WHITE.index);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(HSSFCellStyle.BORDER_NONE);
        style4.setBorderLeft(HSSFCellStyle.BORDER_NONE);
        style4.setBorderRight(HSSFCellStyle.BORDER_NONE);
        style4.setBorderTop(HSSFCellStyle.BORDER_NONE);
        style4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 生成另一种字体4
        HSSFFont font4 =exl.createFont();
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
            StocktakeItemDTOC report = list.get(i);

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
            cell3.setCellValue(report.getUom());
            cell3.setCellStyle(style2);

            HSSFCell cell4 = row.createCell(4);
            cell4.setCellValue(String.valueOf(0)); // Component Name
            cell4.setCellStyle(style2);
        }
        for(int i=0;i<=4;i++) {
            //根据字段长度自动调整列的宽度
            sheet.autoSizeColumn(i, true);
        }
        return exl;
    }

    @Override
    public List<PI0100DTOC> getPrintData(PI0100ParamDTOC pi0100Param) {
        List<PI0100DTOC> _list = custOfEntryPlanMapper.search(pi0100Param);
        for (PI0100DTOC item : _list) {
            if (!StringUtils.isEmpty(item.getReviewSts())) {
                item.setReviewSts(CommonUtils.getAuditStatus(item.getReviewSts()));
            }
        }
        return _list;
    }

    @Override
    public GridDataDTO<CostOfDTO> storeAllItem(ItemInStoreDto param) {
        List<String> articles = new ArrayList<>();
        List<CostOfDTO>  _list=custOfEntryPlanMapper.getAllItem(param);
        for (CostOfDTO dto:_list) {
            articles.add(dto.getArticleId());
        }
        String inEsTime = cm9060Service.getValByKey("1206");
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" +  param.getStoreCd()
                + "/*/*/*/*/*/" + inEsTime+"/*/*";
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
        if(rTdTOList.size()>0){
            for(RTInventoryQueryDTO rtDto:rTdTOList){
                for(CostOfDTO articlelist:_list){
                    if(rtDto.getItemCode().equals(articlelist.getArticleId())){
                        articlelist.setStockQty(rtDto.getRealtimeQty().toString()); // 实时库存
                    }
                }
            }
        }
        GridDataDTO<CostOfDTO> data = new GridDataDTO<CostOfDTO>();
        data.setRows(_list);
        return data;
    }

}
