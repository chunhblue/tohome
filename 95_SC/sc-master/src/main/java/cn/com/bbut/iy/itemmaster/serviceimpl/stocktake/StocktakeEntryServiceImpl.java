package cn.com.bbut.iy.itemmaster.serviceimpl.stocktake;

import cn.com.bbut.iy.itemmaster.dao.MA4320Mapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakeEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.StocktakePlanMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.audit.AuditBean;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100DTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.PI0100ParamDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100.StocktakeItemDTO;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import cn.com.bbut.iy.itemmaster.entity.MA4320Example;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.SA0070Service;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.stocktake.StocktakeEntryService;
import cn.com.bbut.iy.itemmaster.util.ExcelBaseUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * @author lz
 */
@Service
@EnableAsync
@Slf4j
public class StocktakeEntryServiceImpl implements StocktakeEntryService {

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
            updateStocktakingVarianceReport(piCd,piDate,storeCd,reviewStatus);
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
//    @Transactional
//    @Async
    public void updateStocktakingVarianceReport(String piCd, String piDate, String storeCd, String reviewStatus) {
        String businessDate = cm9060Service.getValByKey("0000");
        // 获取 商品最新状态, 并计算差异
        List<StocktakeItemDTO> list = this.getItemVarianceReport(piCd,piDate,businessDate,storeCd,reviewStatus);

        if (list==null||list.size()<1) {
            return;
        }
        String endTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        int totalSize = list.size(); //总记录数
        int pageSize = 1000; //每页N条
        int totalPage = totalSize/pageSize; //共N页

        if (totalSize % pageSize != 0) {
            totalPage += 1;
            if (totalSize < pageSize) {
                pageSize = list.size();
            }
        }

        for (int pageNum = 1; pageNum < totalPage+1; pageNum++) {
            int starNum = (pageNum-1)*pageSize;
            int endNum = pageNum*pageSize>totalSize?(totalSize):pageNum*pageSize;

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                list.get(i).setExportTime(endTime);
                newList.add(list.get(i));
            }

            stocktakeEntryMapper.updateStocktakingVarianceReport(newList);
        }

        // 处理 没有 盘到的商品
        stocktakeEntryMapper.updatePi0125(piCd,piDate,storeCd);
    }

    // 获得 盘点商品的最新状态, 并且记录 差异
    private List<StocktakeItemDTO> getItemVarianceReport(String piCd, String piDate, String businessDate, String storeCd, String reviewStatus) {

        List<StocktakeItemDTO> list = stocktakeEntryMapper.getItemVarianceReport(piCd,piDate,businessDate,storeCd);

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
            if (!StringUtils.isEmpty(reviewStatus) && "10".equals(reviewStatus)) {
                item.setLastVarianceQty(item.getVarianceQty());
            }

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
    private void batchSave(List<StocktakeItemDTO> oldList) {
        //2.分页数据信息
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
            int endNum = pageNum*pageSize>totalSize?(totalSize):pageNum*pageSize;

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(oldList.get(i));
            }

            // 保存到数据库
            stocktakeEntryMapper.save(newList);
        }
    }

    @Override
    public void updateEndTime(String piCd, String piDate, String storeCd) {
        String endTime = new SimpleDateFormat("HHmmss").format(new Date());
        stocktakePlanMapper.modifyPI0100EndTime(piCd,piDate,storeCd,endTime);
    }

    /**
     * 读取文件写回页面
     */
    @Override
    public String insertFileUpload(MultipartFile file, HttpServletRequest request, HttpSession session,String storeCd) {
        Gson gson = new Gson();
        // 获取文件路径
        AjaxResultDto _extjsFormResult = new AjaxResultDto();
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
                Map<String,Object> map = insertImportTxt(file,storeCd);
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
                    _extjsFormResult.setMessage("No data found!");
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
    public Map<String,Object> insertImportTxt(MultipartFile file,String storeCd) {
        List<StocktakeItemDTO> list = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        String businessDate = cm9060Service.getValByKey("0000");
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

                // 封装返回对象
                StocktakeItemDTO item = new StocktakeItemDTO();
                // 商品id
                String articleId = replaceBlank(fields[0]).trim();
                // 商品barcode
                String barcode = replaceBlank(fields[1]).trim();
                // 商品name
                String articleName = replaceBlank(fields[2]).trim();
                // 商品单位
                String uom = replaceBlank(fields[3]).trim();
                // 盘点数量
                String firstQty = replaceBlank(fields[4]).trim();

                String converted = replaceBlank(fields[5]).trim();
                // 不良商品数量
                String badQty = replaceBlank(fields[6]).trim();
                // 区域
                String region = replaceBlank(fields[7]).trim();
                // 盘点时间
                String stocktakeTime = replaceBlank(fields[8]).trim();

                item.setArticleId(articleId);
                item.setBarcode(barcode);
                item.setArticleName(articleName);
                item.setUom(uom);
                item.setBadQty(badQty);
                item.setFirstQty(firstQty);
                item.setStocktakeTime(stocktakeTime);
                list.add(item);
            }
            csvfile.close();

            // 将数据存入临时表, 补齐信息
            map = this.insertTxtTempTable(list,businessDate,storeCd);

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

    /**
     * 将数据存入临时表, 补充商品信息 uom
     */
    private Map<String,Object> insertTxtTempTable(List<StocktakeItemDTO> oldList, String businessDate,String storeCd) {
        Map<String,Object> map = new HashMap<>();
        if (oldList==null||oldList.size()<1) {
            map.put("list",oldList);
            map.put("msg",null);
            return map;
        }

        String tempTableName = "temp_item_csv";
        // 创建前先删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        // 创建临时表
        stocktakeEntryMapper.createTxtTempTable(tempTableName);

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
            int endNum = pageNum*pageSize>totalSize?(totalSize):pageNum*pageSize;

            // 用来存入新的集合
            List<StocktakeItemDTO> newList = new ArrayList<StocktakeItemDTO>();
            for (int i = starNum; i < endNum; i++) {
                newList.add(oldList.get(i));
            }
            // 保存数据到临时表里
            stocktakeEntryMapper.saveToTxtTempTable(tempTableName,newList);
        }
        StringBuilder sb = new StringBuilder();
        Boolean checkFlg = true;
        // 补充信息, 获取数据
        List<StocktakeItemDTO> tempItemList = stocktakeEntryMapper.getTempTxtItemList(tempTableName,businessDate);
        if(tempItemList != null){
            // 去掉txt标题
            if(tempItemList.size()<oldList.size()-1){
                for (int j=1;j<oldList.size();j++){
                   int i = stocktakeEntryMapper.countOldItem(oldList.get(j).getArticleId(),businessDate);
                   if(i==0){
                      sb.append("Item:") .append(oldList.get(j).getArticleId()).append(" ").append(oldList.get(j).getArticleName())
                              .append("&nbsp;&nbsp;&nbsp;&nbsp").append("has expired!").append(")!<br>");
                       checkFlg = false;
                   }
                }
            }
        }

        // 删除临时表
        stocktakeEntryMapper.deleteTempTable(tempTableName);

        map.put("list",tempItemList);
        if(!checkFlg){
            map.put("msg",sb.toString());
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
            int endNum = pageNum*pageSize>totalSize?(totalSize):pageNum*pageSize;

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

}
