package cn.com.bbut.iy.itemmaster.serviceimpl.stocktake;

import cn.com.bbut.iy.itemmaster.constant.Constants;
import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakeEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.ExcelParam;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleDTO;
import cn.com.bbut.iy.itemmaster.dto.article.ArticleParamDTO;
import cn.com.bbut.iy.itemmaster.dto.audit.AuditBean;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.entity.Mb1200;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SA0070Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.ma1000.Ma1000Service;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakeEntryService;
import cn.com.bbut.iy.itemmaster.serviceimpl.base.ImportExcelBaseService;
import cn.com.bbut.iy.itemmaster.util.ExcelBaseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValue;
import static cn.com.bbut.iy.itemmaster.util.CommonUtils.setCellValueNo;

/**
 * @author lz
 */
@Service
@EnableAsync
@Slf4j
public class StocktakeEntryServiceImpl extends ImportExcelBaseService implements StocktakeEntryService {

    @Autowired
    private StocktakeEntryMapper stocktakeEntryMapper;
    @Autowired
    private StocktakePlanMapper stocktakePlanMapper;
    @Autowired
    private IAuditService auditServiceImpl;
    @Autowired
    private CM9060Service cm9060Service;
    @Autowired
    private SA0070Service sa0070Service;
    @Autowired
    private MA4320Mapper ma4320Mapper;
    @Autowired
    private StocktakeEntryService service;
    @Autowired
    private Ma1000Service ma1000Service;
    @Autowired
    private StocktakeEntryService stocktakeEntryService;

    /**
     * 查询商品
     */
    @Override
    public StocktakeItemDTO getItemInfo(String itemCode, String piCd, String piDate) {

        if (StringUtils.isEmpty(itemCode) ||
                StringUtils.isEmpty(piCd) ||
                StringUtils.isEmpty(piDate)) {
            return null;
        }

        return stocktakeEntryMapper.getItemInfo(itemCode,piCd,piDate);
    }

    /**
     * 保存
     */
    @Override
    @Transactional
    public int insert(String piCd, String piDate, String storeCd, List<StocktakeItemDTO> stocktakeItemList, PI0100DTO pi0100) {
        try {
            // 先查询是否保存过数据
            int count = stocktakeEntryMapper.getCountByPicd(piCd,piDate);

            if (count>0) {
                // 先删除
                stocktakeEntryMapper.deleteByPicd(piCd,piDate);
            }

            String reviewStatus = null;
            // 修改主档数据
            if (pi0100 != null) {
                stocktakePlanMapper.updatePI0100(pi0100);
                reviewStatus = pi0100.getReviewStatus();
                //删除附件信息
                MA4320Example example = new MA4320Example();
                example.or().andInformCdEqualTo(pi0100.getPiCd()).andFileTypeEqualTo("07");
                ma4320Mapper.deleteByExample(example);

                //添加附件信息
                if(org.apache.commons.lang.StringUtils.isNotBlank(pi0100.getFileDetailJson())){
                    List<MA4320> ma4320List = new Gson().fromJson(pi0100.getFileDetailJson(), new TypeToken<List<MA4320>>(){}.getType());
                    for (int j = 0; j < ma4320List.size(); j++) {
                        MA4320 ma4320 = ma4320List.get(j);
                        ma4320.setInformCd(pi0100.getPiCd());
                        ma4320.setCreateUserId(pi0100.getCreateUserId());
                        ma4320.setCreateYmd(pi0100.getCreateYmd());
                        ma4320.setCreateHms(pi0100.getCreateHms());
                        ma4320Mapper.insertSelective(ma4320);
                    }
                }
            }
            // 更改主档得 status 状态
            stocktakeEntryMapper.updateMainStatus(piCd,piDate,"02");
            // 分批次保存数据
            batchSave(stocktakeItemList);
            // 生成差异数据
//            updateStocktakingVarianceReport(piCd,piDate,storeCd,reviewStatus);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * 查询数据
     */
    @Override
    public PI0100DTO getData(String piCd, String piDate) {
        if (StringUtils.isEmpty(piCd)||StringUtils.isEmpty(piDate)) {
            return null;
        }

        // 获取主档信息
        PI0100DTO pi0100 = stocktakePlanMapper.getPI0100ByPrimary(piCd,piDate);

        if (pi0100==null) {
            return null;
        }
        // 格式化日期
        String date = formatDate(pi0100.getPiDate());
        pi0100.setPiDate(date);
        pi0100.setPiStartTime(formatTime(pi0100.getPiStartTime()));
        pi0100.setPiEndTime(formatTime(pi0100.getPiEndTime()));
        List<StocktakeItemDTO> list=stocktakeEntryMapper.getPI0120ByPrimary(piCd,piDate);
        // 取得异常表的数据
        List<StocktakeItemDTO> list1=stocktakeEntryMapper.getPI0120tBy(piCd,piDate);
        List<StocktakeItemDTO> newList = new ArrayList<>();
        for(StocktakeItemDTO oldItem:list1){
            Boolean checkFlg = true;
            for(StocktakeItemDTO st:newList){
                if(st.getArticleId().equals(oldItem.getArticleId()) && st.getStoreCd().equals(oldItem.getStoreCd())
                        && st.getBarcode().equals(oldItem.getBarcode())){
                    int firstQty = Integer.parseInt(st.getFirstQty())+Integer.parseInt(oldItem.getFirstQty());
                    int badQty = Integer.parseInt(st.getBadQty())+Integer.parseInt(oldItem.getBadQty());
                    st.setFirstQty(String.valueOf(firstQty));
                    st.setBadQty(String.valueOf(badQty));
                    checkFlg = false;
                }
            }
            if(checkFlg){
                newList.add(oldItem);
            }
        }
        list.addAll(newList);
        pi0100.setItemList(list);
        return pi0100;
    }

    /**
     * 更改主档 status 状态
     */
    @Override
    public ReturnDTO updateStatus(String piCd, String piDate, int typeId) {
        ReturnDTO _return = new ReturnDTO();

        if (StringUtils.isEmpty(piCd)||StringUtils.isEmpty(typeId)) {
            _return.setMsg("Paramter is null");
            _return.setSuccess(false);
            return _return;
        }
        AuditBean bean = auditServiceImpl.getIdByRecordId(typeId, piCd);
        if(bean == null){
            _return.setMsg("Null");
            _return.setSuccess(false);
            return _return;
        }
        // 判断记录审核状态
        int status = bean.getCAuditstatus();
        if(status==99) {
            //审核完成
            _return.setMsg("Complete");
            _return.setSuccess(true);
            // 更改状态
            stocktakeEntryMapper.updateMainStatus(piCd,piDate,"03");
        }else {
            _return.setMsg("Unfinished");
            _return.setSuccess(false);
        }
        return _return;
    }

    @Override
    public List<StocktakeItemDTO> getItemInfoByList(List<String> idList, String piCd, String piDate) {
        return stocktakeEntryMapper.getItemInfoByList(idList,piCd,piDate);
    }

    @Override
    public List<AutoCompleteDTO> getItemList(String piCd, String piDate,String piStoreCd, String v) {
        piDate = sa0070Service.formatDate(piDate);
        return stocktakeEntryMapper.getItemList(piCd,piDate,piStoreCd,v);
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

    /**
     *  生成盘点差异报表数据
     */
    @Override
    @Transactional
    public void updateStocktakingVarianceReport(String piCd, String piDate, String storeCd, String reviewStatus) {
        String businessDate = cm9060Service.getValByKey("0000");
        // 获取 商品最新状态, 并计算差异
        List<StocktakeItemDTO> list = this.getItemVarianceReport(piCd,piDate,businessDate,storeCd,reviewStatus);

        if (list==null||list.size()<1) {
            return;
        }

        this.insertVarianceTempTable(list);
        String tempvarTableName = "temp_variance_item";

        stocktakeEntryMapper.updateStocktakingVarianceReport(tempvarTableName);

        // 创建前先删除临时表
        stocktakeEntryMapper.deleteTempTable(tempvarTableName);

        // 处理 没有 盘到的商品
        stocktakeEntryMapper.updatePi0125(piCd,piDate,storeCd);
    }

    private void insertVarianceTempTable(List<StocktakeItemDTO> oldList) {
        if (oldList==null||oldList.size()<1) {
            return ;
        }
        String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String tempTableName = "temp_variance_item";
        // 创建前先删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        // 创建临时表
        stocktakeEntryMapper.createVarianceTempTable(tempTableName);

        // 批量保存数据到临时表
        int totalSize = oldList.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize/pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = oldList.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum-1)*pageSize;
            int endNum = Math.min(pageNum * pageSize, totalSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                oldList.get(i).setExportTime(endTime);
                newList.add(oldList.get(i));
            }
            // 保存数据到临时表里
            stocktakeEntryMapper.saveToVarianceTempTable(tempTableName,newList);
        }
    }


    // 获得 盘点商品的最新状态, 并且记录 差异
    private List<StocktakeItemDTO> getItemVarianceReport(String piCd, String piDate, String businessDate, String storeCd, String reviewStatus) {
        String tempSa0020TableName = "temp_sale_item";

        // 创建临时表
        stocktakeEntryMapper.createSaleTempTable(tempSa0020TableName);

        List<StocktakeItemDTO> saleList = stocktakeEntryService.getSalesItemReport(piCd,piDate,storeCd);

        // 批量保存数据到临时表
        int totalSize = saleList.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize/pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = saleList.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum - 1) * pageSize;
            int endNum = Math.min(pageNum * pageSize, totalSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(saleList.get(i));
            }
            // 保存数据到临时表里
            stocktakeEntryMapper.saveSaleTempTable(tempSa0020TableName, newList);
        }

        List<StocktakeItemDTO> list = stocktakeEntryService.getItemVarianceReport(tempSa0020TableName,piCd,piDate,storeCd);

        // 计算异动量
        for (StocktakeItemDTO item : list) {
            // 销售数量
            BigDecimal saleQtyTotal = item.getSaleQtyTotal();

            // 生成 盘点差异表的异动量 - 导出开始时的异动量
            // BigDecimal result = saleQtyTotal.subtract(item.getCQty1());
            // item.setCQty(saleQtyTotal);
            // item.setCQty2(saleQtyTotal);

            // 设置异动量: 盘点开始-盘点枪盘点商品 之间的销售量
            item.setCQty(saleQtyTotal);

            // 审核通过的状态, 需要记录审核通过时的差异量, 保存后会重新计算差异发起审核
            // 审核再次通过时就会重新计算实时库存
            /*if (!StringUtils.isEmpty(reviewStatus) && "10".equals(reviewStatus)) {
                item.setLastVarianceQty(item.getVarianceQty());
            }*/

            // 盘点量 = 异动量+盘点量
            String secondQty = item.getSecondQty();
            if (!StringUtils.isEmpty(secondQty)) {
                BigDecimal piQty =  item.getCQty().add(new BigDecimal(secondQty));
                item.setSecondQty(String.valueOf(piQty));
                if (!StringUtils.isEmpty(secondQty)) {
                    // 库存量 - 盘点量 , 获取差异量
                    // BigDecimal varianceQty = item.getRealTimeQty().subtract(piQty);
                    // 盘点量 - 库存量
                    BigDecimal varianceQty = piQty.subtract(item.getRealTimeQty());
                    item.setVarianceQty(varianceQty);
                }
            }
        }
        return list;
    }

    // 批量 添加数据
    private void batchSave(List<StocktakeItemDTO> list) {
        // 去除空白数据 （空白数据存入异常表里）
        List<StocktakeItemDTO> oldList = new ArrayList<>();
        Collection<String> itemBarcodes = new ArrayList<>();
        Collection<String> items = new ArrayList<>();
        for(StocktakeItemDTO item:list){
            if(item.getPiCd()!=null&&!item.getPiCd().equals("")
                    && item.getPiDate()!=null&&!item.getPiDate().equals("")
                    && item.getStoreCd()!=null&&!item.getStoreCd().equals("")
                    && item.getArticleId()!=null&&!item.getArticleId().equals("")){
                oldList.add(item);
                items.add(item.getArticleId());
            }else {
                itemBarcodes.add(item.getArticleId()+"_"+item.getBarcode());
            }
        }
        List<String> articles = stocktakeEntryMapper.getArticles(items);
        List<StocktakeItemDTO> itemDTOS = new ArrayList<>();
        for(StocktakeItemDTO oldItem:oldList){
            for(String article : articles){
                if(oldItem.getArticleId().equals(article)){
                    itemDTOS.add(oldItem);
                }
            }
        }
         // List 差集
        List<StocktakeItemDTO> defactList = receiveDefectList(oldList,itemDTOS);
        for(StocktakeItemDTO def:defactList){
            itemBarcodes.add(def.getArticleId()+"_"+def.getBarcode());
        }

        String piCd = list.get(0).getPiCd();
        String piDate = list.get(0).getPiDate();
        String storeCd = list.get(0).getStoreCd();

        String exceptionTableName = "public.temp_stock_exception_item";
        // 判断临时表是否存在
        int num = stocktakeEntryMapper.getCountTable(exceptionTableName);
        if(num > 0){
            // 先删除
            stocktakeEntryMapper.deleteExByPicd(piCd,piDate);
            if(itemBarcodes.size()>0){
                List<StocktakeItemDTO> exceptionItemList = stocktakeEntryMapper.getTempExceptionItemList(exceptionTableName,itemBarcodes);
                if(exceptionItemList.size()>0){
                    stocktakeEntryMapper.tempToExcepition(exceptionItemList,piCd,piDate,storeCd);
                }

            }
        }else {
            if(itemBarcodes.size()>0) {
                stocktakeEntryMapper.deleteExMore(piCd, piDate, itemBarcodes);
            }
        }
        stocktakeEntryMapper.deleteTempTable(exceptionTableName);
        //2.分页数据信息
        int totalSize = itemDTOS.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize/pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = itemDTOS.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum-1)*pageSize;
            int endNum = Math.min(pageNum * pageSize, totalSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(itemDTOS.get(i));
            }
            if(newList.size()>0){
                // 保存到数据库
                stocktakeEntryMapper.save(newList);
            }
        }
    }

    @Override
    public void updateEndTime(String piCd, String piDate, String storeCd) {
        String endTime = ma4320Mapper.getNowDate().substring(8,14);
//        String endTime = new SimpleDateFormat("HHmmss").format(new Date());
        stocktakePlanMapper.modifyPI0100EndTime(piCd,piDate,storeCd,endTime);
    }

    /**
     * 读取文件写回页面
     */
    @Override
    public String insertFileUpload(MultipartFile file, HttpServletRequest request, HttpSession session,String storeCd,String piCd) {
        Gson gson = new Gson();
        // 获取文件路径
        AjaxResultDto _extjsFormResult = new AjaxResultDto();
        if (StringUtils.isEmpty(piCd) || StringUtils.isEmpty(storeCd)) {
            _extjsFormResult.setMessage("Parameter cannot be empty!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }
        try {
            if (file.getSize() > 0) {
                // 上传文件大小不能超过5M！
                if (file.getSize() > 5242880) {
                    _extjsFormResult.setMessage("Uploaded file size cannot exceed 5M!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }

                // 上传文件格式错误！
                /*if (!ExcelBaseUtil.isExcel(file)) {
                    _extjsFormResult.setMessage("Upload file format error!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }*/

                // 判断是否为csv格式
                String oldName = file.getOriginalFilename();
                if(oldName == null){
                    _extjsFormResult.setMessage("No file found!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }
                String suffix = oldName.substring(oldName.lastIndexOf("."));
                if (!".csv".equals(suffix)) {
                    _extjsFormResult.setMessage("Upload file format error!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }
                List<StocktakeItemDTO> itemList = null;
                String msg = null;
//                List<StocktakeItemDTO> itemList = insertImportExcel(file,storeCd);
                Map<String,Object> map = insertImportCsv(file,storeCd,piCd);
                for(Map.Entry<String,Object> entry : map.entrySet()){
                    if("list".equals(entry.getKey()) ){
                      Object list = entry.getValue();
                        itemList = (List<StocktakeItemDTO>) list;
                    }else if("msg".equals(entry.getKey())){
                        Object mapMsg = entry.getValue();
                        msg = (String) mapMsg;
                    }
                }

                if (itemList==null||itemList.size()<1) {
                    _extjsFormResult.setMessage(msg);
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }
                _extjsFormResult.setMessage(msg);
                _extjsFormResult.setSuccess(true);
                _extjsFormResult.setData(itemList);
                return gson.toJson(_extjsFormResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 上传失败
            _extjsFormResult.setMessage("Upload failed!");
            _extjsFormResult.setSuccess(false);
            return gson.toJson(_extjsFormResult);
        }
        // 上传成功
        _extjsFormResult.setMessage("Upload Succeed!");
        _extjsFormResult.setSuccess(true);
        return gson.toJson(_extjsFormResult);
    }


        /**
     * 查询数据
     *
     * @param pi0100Param
     * @return
     */
    @Override
    public GridDataDTO<PI0100DTO> search(PI0100ParamDTO pi0100Param) {
        // 获取总条数
        int count = stocktakeEntryMapper.selectCountByParam(pi0100Param);

        if (count < 1) {
            return new GridDataDTO<PI0100DTO>();
        }
        List<PI0100DTO> _list = stocktakeEntryMapper.search(pi0100Param);

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
     * 功能描述:导入excel
     */
    @Override
    public List<StocktakeItemDTO> insertImportExcel(MultipartFile file,String storeCd) {
        List<StocktakeItemDTO> list = new ArrayList<StocktakeItemDTO>();
        Workbook workbook = ExcelBaseUtil.createWorkbook(file);
        if (workbook != null) {
            // 获取工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 获取sheet中第一行行号
            int firstRowNum = sheet.getFirstRowNum();
            // 获取sheet中最后一行行号
            int lastRowNum = sheet.getLastRowNum();
            // 设置起始行
            int curRow = 2;

            String businessDate = cm9060Service.getValByKey("0000");

            // 遍历每一行
            for (int i = curRow; i < lastRowNum; i++) {
                // 取得每一行
                Row row = sheet.getRow(curRow++);
                int curCol = 1;
                // 封装返回对象
                StocktakeItemDTO item = new StocktakeItemDTO();
                // 商品id
                String articleId = ExcelBaseUtil.getVal(row.getCell(curCol++));
                // 商品name
                String articleName = ExcelBaseUtil.getVal(row.getCell(curCol++));
                // 不良商品数量
                String badQty = ExcelBaseUtil.getVal(row.getCell(curCol++));
                // 盘点数量
                String firstQty = ExcelBaseUtil.getVal(row.getCell(curCol++));
                item.setArticleId(articleId);
                item.setArticleName(articleName);
                item.setBadQty(badQty);
                item.setFirstQty(firstQty);
                // 盘点时间
                Cell cell = row.getCell(curCol++);
                if(HSSFDateUtil.isCellDateFormatted(cell)){
                    Date d = (Date) cell.getDateCellValue();
                    DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");//HH:mm:ss
                    String format = df2.format(d);
                    item.setStocktakeTime(format);
                }
                list.add(item);
            }
            // 将数据存入临时表, 补齐信息
            list = this.insertTempTable(list,businessDate,storeCd);
        }
        return list;
    }

    /**
     * 功能描述:导入txt
     */
    @Override
    public Map<String,Object> insertImportCsv(MultipartFile file,String storeCd,String piCd) {
        List<StocktakeItemDTO> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        String businessDate = cm9060Service.getValByKey("0000");

        PI0100DTO piInfo = stocktakeEntryMapper.getPi0100Info(storeCd,piCd);
        String piDate = piInfo.getPiDate();
        String piStartTime = piInfo.getPiStartTime();
        String piEndTime = piInfo.getPiEndTime();
        long piDateStartTime = 0,piDateEndTime=0;

        if(piDate.matches("[0-9]+") && piStartTime.matches("[0-9]+") && piEndTime.matches("[0-9]+")){
            piDateStartTime = Long.parseLong(piInfo.getPiDate()+piInfo.getPiStartTime());
            piDateEndTime = Long.parseLong(piInfo.getPiDate()+piInfo.getPiEndTime());
        }

        try{
            File toFile = null;
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
            // MultipartFile -> File
            inputStreamToFile(ins, toFile);
            ins.close();

            String path = toFile.getAbsolutePath();
            // 设定UTF-8字符集，使用带缓冲区的字符输入流BufferedReader读取文件内容
            BufferedReader csvfile = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));

            //跳过表头所在的行
            csvfile.readLine();
            String record;
            // 遍历数据行
            while ((record = csvfile.readLine()) != null) {
                String[] fields = record.split(",");
                int length = fields.length;

                // 封装返回对象
                StocktakeItemDTO item = new StocktakeItemDTO();
                // 商品id
                String articleId = replaceBlank(fields[0]).trim();
                // 商品barcode
                String barcode = replaceBlank(fields[1]).trim();
                StringBuilder name = new StringBuilder();
                for(int i=2;i<length-5;i++){
                    if(i>2){
                        name.append(",");
                    }
                    name.append(fields[i]);
                }
                // 商品name
                String articleName = name.toString().trim();
                // 商品单位
                String uom = replaceBlank(fields[length-5]).trim();
                // 盘点数量
                String firstQty = replaceBlank(fields[length-4]).trim();

                String converted = replaceBlank(fields[length-3]).trim();
                // 区域
                String region = replaceBlank(fields[length-2]).trim();
                // 盘点时间
                String stocktakeTime = replaceBlank(fields[length-1]).trim();

                // 判断是否为科学计数法
                if(Pattern.matches("(.*)[+-](.*)",barcode) && Pattern.matches("(.*)[+-](.*)",stocktakeTime)){
                    String checkMsg = "Failed to Uplaod!<br>Please uplaod the original stocktake result file exported from stocktake tool.";
                    map.put("list",null);
                    map.put("msg",checkMsg);
                    return map;
                }

                item.setArticleId(articleId);
                item.setBarcode(barcode);
                item.setArticleName(articleName);
                item.setUom(uom);
                item.setConverter(converted);
                item.setRegion(region);
                // region:Bad Merchandise;  Backroom;  Display
                if(region.equals("BadMerchandise")){
                    item.setBadQty(firstQty);
                    item.setFirstQty("0");
                }else if(region.equals("Backroom") || region.equals("Display")){
                    item.setFirstQty(firstQty);
                    item.setBadQty("0");
                }else {
                    item.setFirstQty(firstQty);
                    item.setBadQty("0");
                }
                item.setStocktakeTime(stocktakeTime);
                list.add(item);
            }
            csvfile.close();

            File outfile = new File(path);
            //如果文件存在并且是文件，删除改文件
            if(outfile.exists()&&outfile.isFile()) {
               boolean flg = outfile.delete();
               if(flg){
                  log.error("删除文件成功:"+path);
               }else {
                   log.error("删除文件失败:"+path);
               }
            }
            boolean stTimeFlg = true;
            List<StocktakeItemDTO> allList = new ArrayList<>();
            for(StocktakeItemDTO dto:list){
                if(dto.getFirstQty() == null || "".equals(dto.getFirstQty())){
                    dto.setFirstQty("0");
                }
                if(dto.getBadQty() == null || "".equals(dto.getBadQty())){
                    dto.setBadQty("0");
                }
                if(dto.getStocktakeTime().matches("[0-9]+")){
                    long time = Long.parseLong(dto.getStocktakeTime());
                    if(time<piDateStartTime || time>piDateEndTime){
                        stTimeFlg = false;
                    }
                }

                /**
                 * 自定义类 可以先克隆出自定义类，再把它赋值给新的类，来覆盖原来类的引用
                 * 调用了getPropertyUtils().copyProperties(newBean, bean);若为基础类，实际上还是复制的引用
                 */
                allList.add((StocktakeItemDTO) BeanUtils.cloneBean(dto));
            }
            if(!stTimeFlg){
                map.put("list",null);
                map.put("msg","Please upload stocktake result file done during target stocktake date!");
                return map;
            }

            // 判断List中是否有相同的数据，有的话数量就相加
            List<StocktakeItemDTO> newList = new ArrayList<>();
            for(StocktakeItemDTO oldItem:list){
                boolean flg = true;
                for(StocktakeItemDTO newItem:newList){
                    if(newItem.getArticleId().equals(oldItem.getArticleId()) && newItem.getBarcode().equals(oldItem.getBarcode())){
                        Integer badQty = Integer.parseInt(newItem.getBadQty())+Integer.parseInt(oldItem.getBadQty());
                        newItem.setBadQty(badQty.toString());
                        Integer firstQty = Integer.parseInt(newItem.getFirstQty())+Integer.parseInt(oldItem.getFirstQty());
                        Integer allQty = firstQty+badQty;
                        newItem.setFirstQty(allQty.toString());
                        flg = false;
                        if(oldItem.getStocktakeTime().matches("[0-9]+") && newItem.getStocktakeTime().matches("[0-9]+")){
                            if(Long.parseLong(oldItem.getStocktakeTime()) > Long.parseLong(newItem.getStocktakeTime())){
                                newItem.setStocktakeTime(oldItem.getStocktakeTime());
                            }
                        }
                    }
                }
                if(flg){
                    Integer firstQty = Integer.parseInt(oldItem.getFirstQty())+Integer.parseInt(oldItem.getBadQty());
                    oldItem.setFirstQty(firstQty.toString());
                    newList.add(oldItem);
                }
            }

            // 将数据存入临时表, 补齐信息
            map = this.insertCsvTempTable(newList,businessDate,storeCd,allList);

        }catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 在字符串中去除\t制表符
     * @param str
     * @return
     */
    public String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    //获取流文件 (MultipartFile -> File)
    @Override
    public void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String insertNonCountExcel(MultipartFile file, CommonDTO dto) {
        String msg = "";

        int rowNum = 0;//已取值的行数
        int colNum = 0;//列号
        int realRowCount = 0;//真正有数据的行数

        //得到工作空间
        Workbook workbook = null;
        try {
            // 使用这个方法，excel必须是2007版本及以上(.xlsx)，否则workbook运行后为null
            workbook = super.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到工作表
        Sheet sheet = super.getSheetByWorkbook(workbook, 0);
        if (sheet.getRow(2000) != null){
            throw new RuntimeException("系统已限制单批次导入必须小于或等于2000！");
        }

        realRowCount = sheet.getPhysicalNumberOfRows();
        String businessDate = cm9060Service.getValByKey("0000");
        List<String> oldList = new ArrayList<>();

        for(Row row:sheet) {
            if (realRowCount == rowNum) {
                break;
            }

            if (super.isBlankRow(row)) {//空行跳过
                continue;
            }

            if (row.getRowNum() == -1) {
                continue;
            } else {
                if (row.getRowNum() == 0) {//第一行表头跳过
                    continue;
                }
            }
            rowNum++;
            colNum = 0;//从第一列开始

            MA1100 ma1100 = new MA1100();

            ma1100.setArticleId(super.getCellValue(sheet, row, 0));
            ma1100.setArticleName(super.getCellValue(sheet, row, 1));
            oldList.add(ma1100.getArticleId());
        }
        if(oldList.size()==0){
            return null;
        }
        log.info("上传文档的商品个数："+oldList.size());
        Collection<MA1100> nonCountList = stocktakeEntryMapper.getItemInformation(oldList,businessDate);
        for(MA1100 ma1100 : nonCountList){
            ma1100.setCreateUserId(dto.getCreateUserId());
            ma1100.setCreateYmd(dto.getCreateYmd());
            ma1100.setCreateHms(dto.getCreateHms());
        }
        // 导入之前先清空表
        stocktakeEntryMapper.deleteItem();
        int i = stocktakeEntryMapper.insertNonCountListToDb(nonCountList);
        if(i > 0){
            if(i<oldList.size()) {
                msg = "Upload Succeed,but some items are duplicated or expired！";
            }else {
                msg = "Upload Succeed!";
            }
        }else {
            msg = null;
        }

        return msg;
    }

    @Override
    public GridDataDTO<ArticleDTO> getList(ArticleParamDTO dto) {

        int count = stocktakeEntryMapper.countRawItemList(dto);
        if(count == 0){
            return new GridDataDTO<>();
        }
        List<ArticleDTO> list = stocktakeEntryMapper.getRawItemList(dto);
        return new GridDataDTO<>(list, dto.getPage(), count, dto.getRows());
    }

    /**
     * 将数据存入临时表, 补充商品信息 uom
     */
    private Map<String,Object> insertCsvTempTable(List<StocktakeItemDTO> oldList, String businessDate,
                                                  String storeCd,List<StocktakeItemDTO> allList) throws ParseException {
        Map<String,Object> map = new HashMap<>();
        if ((oldList==null||oldList.size()<1) && (allList==null||allList.size()<1)) {
            map.put("list",oldList);
            map.put("msg","No data found!");
            return map;
        }

        String exceptionTableName = "public.temp_stock_exception_item";
        String tempTableName = "temp_item_csv";
        // 创建前先删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        // 若存在则删除，创建临时表
        stocktakeEntryMapper.createTxtTempTable(exceptionTableName);
        stocktakeEntryMapper.createTxtTempTable(tempTableName);

        // 批量保存数据到临时表
        int totalSize = oldList.size(); //总记录数
        int totalAllListSize = allList.size();
        int pageSize = 1000; //每页N条
        int pageAllListSize = 1000; //每页N条
        int totalPage = totalSize/pageSize; //共N页
        int totalAllListPage = totalAllListSize/pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = oldList.size();
            }
        }
        if (totalAllListSize % pageAllListSize != 0) {
            totalAllListPage += 1;
            if (totalAllListSize < pageAllListSize) {
                pageAllListSize = allList.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum-1)*pageSize;
            int endNum = Math.min(pageNum * pageSize, totalSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(oldList.get(i));
            }
            // 保存数据到临时表里
            stocktakeEntryMapper.saveToTxtTempTable(tempTableName,newList);
        }

        for (int pageNum = 1; pageNum < totalAllListPage+1; pageNum++) {
            int starNum = (pageNum-1)*pageAllListSize;
            int endNum = Math.min(pageNum * pageAllListSize, totalAllListSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newAllList = new ArrayList<>();
            for (int i = starNum; i < endNum; i++) {
                newAllList.add(allList.get(i));
            }
            // 保存数据到临时表里
            stocktakeEntryMapper.saveToTxtTempTable(exceptionTableName,newAllList);
        }

        StringBuilder sb = new StringBuilder();
        boolean checkFlg = true;
        List<Ma1000> storeList = ma1000Service.selectByStoreCd(storeCd);
        if (StringUtils.isEmpty(storeList)) {
            map.put("list",oldList);
            map.put("msg","No data found!");
            return map;
        }
        String structureCd = storeList.get(0).getZoCd();
        // 补充信息, 获取数据
        List<StocktakeItemDTO> tempItemList = stocktakeEntryMapper.getTempTxtItemList(tempTableName,businessDate,storeCd,structureCd);
        if(tempItemList.size() > 0){
            /*if(tempItemList.size()<oldList.size()){
                List<StocktakeItemDTO> tempList = new ArrayList<>();
                List<StocktakeItemDTO> oldList1 = new ArrayList<>();
                     //用并集 减去 交集 (tempList，oldList1只取itemCode,itemName信息)
                sb.append("Upload Succeed,but").append("&nbsp;&nbsp");
                for (int j=0;j<oldList.size();j++){

                    for(int i=0;i<tempItemList.size();i++){

                    }
                   if(1==0){
                      sb.append("Item:") .append(oldList.get(j).getArticleId()).append(" ").append(oldList.get(j).getArticleName())
                              .append("&nbsp;&nbsp;&nbsp;&nbsp").append("has expired!").append(")!<br>");
                       checkFlg = false;
                   }
                }
            }*/
            // HH 表示时间为24h制
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String nowTime = sdf.format(sdf.parse(ma4320Mapper.getNowDate()).getTime()-5*60*1000);
            for(StocktakeItemDTO dto:tempItemList){
                if(dto.getStocktakeTime().matches("[0-9]+")){
                    if(Long.parseLong(dto.getStocktakeTime())>Long.parseLong(nowTime)){
                        checkFlg = false;
                        break;
                    }
                }
            }
        }
        if(!checkFlg){
            sb.append("Please upload again in five minutes!");
        }
        // 删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        map.put("list",tempItemList);
        if(!checkFlg){
            map.put("msg",sb.toString());
            map.put("list",null);
        }else {
            map.put("msg","Upload Succeed!");
        }
        return map;
    }

    /**
     * 将数据存入临时表, 补充商品信息, barcode, spec, uom等
     */
    private List<StocktakeItemDTO> insertTempTable(List<StocktakeItemDTO> oldList, String businessDate,String storeCd) {

        if (oldList==null||oldList.size()<1) {
            return oldList;
        }

        String tempTableName = "temp_item";
        // 创建前先删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        // 创建临时表
        stocktakeEntryMapper.createTempTable(tempTableName);

        // 批量保存数据到临时表
        int totalSize = oldList.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize/pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = oldList.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum-1)*pageSize;
            int endNum = Math.min(pageNum * pageSize, totalSize);

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(oldList.get(i));
            }
            // 保存数据到临时表里
            stocktakeEntryMapper.saveToTempTable(tempTableName,newList);
        }

        // 补充信息, 获取数据
        List<StocktakeItemDTO> tempItemList = stocktakeEntryMapper.getTempItemList(tempTableName,businessDate,storeCd);

        // 删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        return tempItemList;
    }


    /**
     * 格式化盘点时间
     * 03/08/2020 10:36:31 -> 20200803103631
     * @param time
     * @return
     */
    private String formatStockTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return "";
        }
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(time);
            return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
        } catch (ParseException e) {
            log.error("盘点时间格式化错误--"+time);
            return "";
        }
    }

    /**
     * 格式化时间 2020 07 04 17 05 16
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
        return hh+":"+mm+":"+ss;
    }

    @Override
    public SXSSFWorkbook getExcel(PI0100DTO pi0100DTO,List<StocktakeItemDTO> stockList) {
        // 声明一个工作簿
        SXSSFWorkbook wb = new SXSSFWorkbook();
        // 生成一个表格
        SXSSFSheet sheet = wb.createSheet("STOCK_TAKE_RESULT");

        // 生产excel内容
        createExcelBody(sheet, wb, pi0100DTO,stockList);
        return wb;
    }

    @Override
    public List<StocktakeItemDTO> getSalesItemReport(String piCd, String piDate, String storeCd) {
        List<StocktakeItemDTO> saleList = stocktakeEntryMapper.getSalesItemReport(piCd,piDate,storeCd);
        return saleList;
    }

    @Override
    public List<StocktakeItemDTO> getItemVarianceReport(String tempTableName, String piCd, String piDate, String storeCd) {
        List<StocktakeItemDTO> list = stocktakeEntryMapper.getItemVarianceReport(tempTableName,piCd,piDate,storeCd);
        return list;
    }

    /**
     * 生产excel内容
     *
     * @param sheet
     */
    private void createExcelBody(SXSSFSheet sheet, SXSSFWorkbook wb, PI0100DTO pi0100DTO,List<StocktakeItemDTO> _list) {

        // 去除网格线
        sheet.setDisplayGridlines(false);

        // 大标题
        String row0 = "Stocktake Take Result";

        // 二号标题
        String row1 = "";

        // 生成另一种字体4
        Font font1 = wb.createFont();
        // 设置字体
        font1.setFontName("Microsoft JhengHei");
        // 设置字体大小
        font1.setFontHeightInPoints((short) 10);

        // 生成并设置另一个样式
        CellStyle style4 = wb.createCellStyle();
        style4.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style4.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        style4.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style4.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        style4.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
        style4.setBorderLeft(CellStyle.BORDER_THIN);
        style4.setBorderRight(CellStyle.BORDER_THIN);
        style4.setBorderTop(CellStyle.BORDER_THIN);

        // 在样式4中引用这种字体
        style4.setFont(font1);



        /**
         * 这里是创建标题
         */
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        SXSSFRow row = sheet.createRow(0);
        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        Cell titilecell = row.createCell(0);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        // 设置单元格内容
        titilecell.setCellValue(row0);
        titilecell.setCellStyle(titleStyle0(wb));

        // 行号
        int rowNum = 1;

        // title 第一行
        row = sheet.createRow(rowNum++);
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue("");
        cell.setCellStyle(style4);

        cell = row.createCell(1);
        cell.setCellValue("Store ID:"+pi0100DTO.getStoreCd());
        cell.setCellStyle(style4);


        cell = row.createCell(2);
        cell.setCellValue("Store Name:");
        cell.setCellStyle(style4);

        cell = row.createCell(3);
        cell.setCellValue(pi0100DTO.getStoreName());
        cell.setCellStyle(style4);

        cell = row.createCell(4);
        cell.setCellValue("Citizen");
        cell.setCellStyle(style4);

        // 第二行
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("");

        cell = row.createCell(1);
        cell.setCellValue("Stock-take Date:");
        cell.setCellStyle(style4);

        cell = row.createCell(2);
        cell.setCellValue(formatDate(pi0100DTO.getPiDate()));
        cell.setCellStyle(style4);

        // 第三行
        row = sheet.createRow(rowNum++);
        cell = row.createCell(0);
        cell.setCellValue("No.");
        cell.setCellStyle(style4);

        cell = row.createCell(1);
        cell.setCellValue("Item Code");
        cell.setCellStyle(style4);

        cell = row.createCell(2);
        cell.setCellValue("Standard Barcode");
        cell.setCellStyle(style4);

        cell = row.createCell(3);
        cell.setCellValue("Item Name");
        cell.setCellStyle(style4);

        cell = row.createCell(4);
        cell.setCellValue("Total Physical Count");
        cell.setCellStyle(style4);


        // 遍历数据
        int no = 1;
        for (StocktakeItemDTO ls : _list) {
            int curCol = 0;
            row = sheet.createRow(rowNum++);
            cell = row.createCell(curCol++);
            cell.setCellStyle(style4);
            setCellValueNo(cell, no++);

            cell = row.createCell(curCol++);
            cell.setCellStyle(style4);
            setCellValue(cell, ls.getArticleId());

            cell = row.createCell(curCol++);
            cell.setCellStyle(style4);
            setCellValue(cell, ls.getBarcode());

            cell = row.createCell(curCol++);
            cell.setCellStyle(style4);
            setCellValue(cell, ls.getArticleName());


            cell = row.createCell(curCol++);
            cell.setCellStyle(style4);
            setCellValue(cell, ls.getFirstQty());
        }
        // 设置列宽
        int columnIndex = 0;
        sheet.setColumnWidth(columnIndex++, 5 * 256);
        sheet.setColumnWidth(columnIndex++, 25 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 30 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
        sheet.setColumnWidth(columnIndex++, 20 * 256);
    }

    /** 大标题样式 */
    private CellStyle titleStyle0(Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 20);// 字号
        font.setFontName(Constants.DEFAULT_FONT);// 字体设置（宋体）
        font.setColor(IndexedColors.BLACK.index);// 字体颜色
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);// 粗体

        CellStyle style = wb.createCellStyle();
        style.setFont(font);// 设置字体样式
        style.setAlignment((short) 2);// 单元格内容左右对其方式
        style.setVerticalAlignment((short) 2);// 单元格内容上下对其方式
        style.setFillForegroundColor(IndexedColors.WHITE.index);// 设置单元格背景颜色
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);// 配合FillForegroundColor一起使用
        style.setBorderBottom(CellStyle.BORDER_THIN);// 边框设置
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setWrapText(false);// 文本是否自动换行
        return style;
    }

    /**
     * @方法描述：获取两个ArrayList的差集
     * @param firstArrayList 第一个ArrayList
     * @param secondArrayList 第二个ArrayList
     * @return resultList 差集ArrayList
     */
    public List<StocktakeItemDTO> receiveDefectList(List<StocktakeItemDTO> firstArrayList, List<StocktakeItemDTO> secondArrayList) {
        List<StocktakeItemDTO> resultList = new ArrayList<>();
        LinkedList<StocktakeItemDTO> result = new LinkedList<StocktakeItemDTO>(firstArrayList);// 大集合用linkedlist
        HashSet<StocktakeItemDTO> othHash = new HashSet<StocktakeItemDTO>(secondArrayList);// 小集合用hashset
        Iterator<StocktakeItemDTO> iter = result.iterator();// 采用Iterator迭代器进行数据的操作
        while(iter.hasNext()){
            if(othHash.contains(iter.next())){
                iter.remove();
            }
        }
        resultList = new ArrayList<>(result);
        return resultList;
    }

}
