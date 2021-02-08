package cn.com.bbut.iy.itemmaster.serviceimpl.fsInvenoryentry;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.fsInventoryEntryMapper;
import cn.com.bbut.iy.itemmaster.dao.fsInventoryPlanMapper;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100DTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.PI0100ParamDTOC;
import cn.com.bbut.iy.itemmaster.dto.pi0100c.StocktakeItemDTOC;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RTInventoryQueryDTO;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RealTimeDto;
import cn.com.bbut.iy.itemmaster.dto.rtInventory.RtInvContent;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.RealTimeInventoryQueryService;
import cn.com.bbut.iy.itemmaster.service.SequenceService;
import cn.com.bbut.iy.itemmaster.service.cm9070.Cm9070Service;
import cn.com.bbut.iy.itemmaster.service.fsInvenoryentry.fsInventoryEntryService;
import cn.com.bbut.iy.itemmaster.serviceimpl.RealTimeInventoryQueryServiceImpl;
import cn.com.bbut.iy.itemmaster.util.ExcelBaseUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @ClassName fsInventoryEntryServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/23 10:38
 * @Version 1.0
 */

@Service
@Slf4j
public class fsInventoryEntryServiceImpl  implements fsInventoryEntryService {
    @Autowired
    private fsInventoryEntryMapper fsInventoryentryMapper;

    @Autowired
    private fsInventoryPlanMapper fsInventoryplanMapper;

    @Autowired
    private Cm9070Service cm9070ServiceImpl;

    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private CM9060Service cm9060Service;
    @Value("${esUrl.inventoryUrl}")
    private String inventoryUrl;
    @Autowired
    private RealTimeInventoryQueryService rtInventoryService;

    @Override
    public GridDataDTO<PI0100DTOC> search(PI0100ParamDTOC pi0100Param) {
        int count = fsInventoryplanMapper.selectCountByParam(pi0100Param);

        if (count < 1) {
            return new GridDataDTO<PI0100DTOC>();
        }

        List<PI0100DTOC> _list = fsInventoryplanMapper.search(pi0100Param);

        GridDataDTO<PI0100DTOC> data = new GridDataDTO<PI0100DTOC>(_list,pi0100Param.getPage(), count, pi0100Param.getRows());
        return data;
    }
    @Override
    public List<PI0100DTOC> getPrintData(PI0100ParamDTOC param) {

        List<PI0100DTOC> _list = fsInventoryplanMapper.search(param);
        return  _list;
    }

    @Override
    public String fileUpload(MultipartFile file, HttpServletRequest request, HttpSession session,String storeCd) {
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
                if (!ExcelBaseUtil.isExcel(file)) {
                    _extjsFormResult.setMessage("Upload file format error!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }

                List<StocktakeItemDTOC> itemList = importSimpleExcel(file,storeCd);

                if (itemList==null||itemList.size()<1) {
                    _extjsFormResult.setMessage("No data found!");
                    _extjsFormResult.setSuccess(false);
                    return gson.toJson(_extjsFormResult);
                }
                _extjsFormResult.setMessage("Upload successfully!");
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
     * 功能描述:导入excel
     */
    private List<StocktakeItemDTOC> importExcel(MultipartFile file) {
        List<StocktakeItemDTOC> list = new ArrayList<StocktakeItemDTOC>();
        Workbook workbook = ExcelBaseUtil.createWorkbook(file);
        if (workbook != null) {
            // 获取工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 获取sheet中第一行行号
            int firstRowNum = sheet.getFirstRowNum();
            // 获取sheet中最后一行行号
            int lastRowNum = sheet.getLastRowNum();
            // 获得总列数
            int columnNum = sheet.getRow(lastRowNum).getPhysicalNumberOfCells();

            // 遍历每一行
            for (int i = firstRowNum; i <= lastRowNum; i++) {
                // 标题行
                if (i == firstRowNum) {
                    continue;
                }
                // 取得每一行
                Row row = sheet.getRow(i);
                StocktakeItemDTOC item = new StocktakeItemDTOC();
                item.setArticleId(ExcelBaseUtil.getVal(row.getCell(0)));
                item.setArticleName(ExcelBaseUtil.getVal(row.getCell(1)));
                item.setBarcode(ExcelBaseUtil.getVal(row.getCell(2)));
                item.setSpec(ExcelBaseUtil.getVal(row.getCell(3)));
                item.setUom(ExcelBaseUtil.getVal(row.getCell(4)));
                String qty = ExcelBaseUtil.getVal(row.getCell(5));
                if (StringUtils.isEmpty(qty)) {
                    item.setQty(BigDecimal.ZERO);
                } else {
                    item.setQty(new BigDecimal(qty));
                }
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 获取简易的Excel内容
     * @param file
     * @return
     */
    private List<StocktakeItemDTOC> importSimpleExcel(MultipartFile file,String storeCd) {
        List<StocktakeItemDTOC> list = new ArrayList<>();
        Workbook workbook = ExcelBaseUtil.createWorkbook(file);
        if (workbook != null) {
            // 获取工作表
            Sheet sheet = workbook.getSheetAt(0);
            // 获取sheet中第一行行号
            int firstRowNum = sheet.getFirstRowNum();
            // 获取sheet中最后一行行号
            int lastRowNum = sheet.getLastRowNum();
            // 获得总列数
            int columnNum = sheet.getRow(lastRowNum).getPhysicalNumberOfCells();

            Collection<String> articles = new ArrayList<>();
            // 遍历每一行
            for (int i = firstRowNum; i <= lastRowNum; i++) {
                // 标题行
                if (i == firstRowNum) {
                    continue;
                }
                // 取得每一行
                Row row = sheet.getRow(i);
                StocktakeItemDTOC item = new StocktakeItemDTOC();
                item.setArticleId(ExcelBaseUtil.getVal(row.getCell(0)));
                String qty = ExcelBaseUtil.getVal(row.getCell(1));
                if (StringUtils.isEmpty(qty)) {
                    item.setQty(BigDecimal.ZERO);
                } else {
                    item.setQty(new BigDecimal(qty));
                }
                articles.add(ExcelBaseUtil.getVal(row.getCell(0)));
                list.add(item);
            }
            if(articles.size()>0){
                List<StocktakeItemDTOC> newList = fsInventoryplanMapper.getDetailList(storeCd,articles);
                for(StocktakeItemDTOC stockDtoc : list){
                    for(StocktakeItemDTOC newDetail : newList){
                        if(stockDtoc.getArticleId().equals(newDetail.getArticleId())){
                            stockDtoc.setArticleName(newDetail.getArticleName());
                            stockDtoc.setBarcode(newDetail.getBarcode());
                            stockDtoc.setSpec(newDetail.getSpec());
                            stockDtoc.setUom(newDetail.getUom());
                        }
                    }
                }
                // 获取实时库存信息
                list = getStockList(list,articles,storeCd);
            }

        }
        return list;
    }

    // 获取实时库存信息
    public List<StocktakeItemDTOC> getStockList(List<StocktakeItemDTOC> list,Collection<String> articles,String storeCd){
        String inEsTime = cm9060Service.getValByKey("1206");
        String connUrl = inventoryUrl + "GetRelTimeInventory/"+"/"+storeCd
                +"/*/*/*/*/*/" + inEsTime+"/*/*";
        List<String> articleList = (List<String>) articles;
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articleList,connUrl);

            if(rTdTOList.size()>0) {
                for (StocktakeItemDTOC rtDto : list) {
                    for (RTInventoryQueryDTO realTimeDto : rTdTOList) {
                        if (realTimeDto.getItemCode().equals(rtDto.getArticleId())) {
                            // 获取实时库存数量
                          rtDto.setStockQty(realTimeDto.getRealtimeQty().toString());
                        }
                    }
                }
            }

        return list;
    }

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
    public PI0100DTOC getData(String piCd) {
        String inEsTime = cm9060Service.getValByKey("1206");
        if (StringUtils.isEmpty(piCd)) {
            return null;
        }
        // 获取主档信息
        PI0100DTOC pi0100c = fsInventoryplanMapper.getPI0145ByPrimary(piCd);
        if (pi0100c==null) {
            return null;
        }
        List<String> articles = new ArrayList<>();
        List<StocktakeItemDTOC> list= fsInventoryentryMapper.getPI0140ByPrimary(piCd,pi0100c.getStoreCd());
        for(StocktakeItemDTOC item:list){
            articles.add(item.getArticleId());
        }
        //拼接url，转义参数
        String connUrl = inventoryUrl + "GetRelTimeInventory/" + pi0100c.getStoreCd() + "/"
                + "*" + "/*/*/*/*/" + inEsTime+"/*/*";
        List<RTInventoryQueryDTO> rTdTOList = rtInventoryService.getStockList(articles,connUrl);
        if(rTdTOList.size()>0){
            for(RTInventoryQueryDTO rtDto:rTdTOList){
                for(StocktakeItemDTOC stockDto:list){
                    if(rtDto.getItemCode().equals(stockDto.getArticleId())){
                        stockDto.setStockQty(rtDto.getRealtimeQty().toString()); // 实时库存
                    }
                }
            }
        }

        pi0100c.setItemList(list);
        return pi0100c;
    }



    @Override
    public StocktakeItemDTOC getItemInfo(String itemCode,String storeCd) {

        if (StringUtils.isEmpty(itemCode) || StringUtils.isEmpty(storeCd)) {
            return null;
        }
        String businessDate = getBusinessDate();
        return fsInventoryentryMapper.getItemInfo(itemCode,businessDate,storeCd);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PI0100DTOC insertData(List<StocktakeItemDTOC> stocktakeItemList, PI0100DTOC item, HttpServletRequest request, HttpSession session) {

        String dateStr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];

        if (item.getPiCd()==null||StringUtils.isEmpty(item.getPiCd())) {
            //获取序列号
//            String piCd = sequenceService.getSequence("pi0145_pi_cd_seq");
            String piCd = sequenceService.getSequence("pi0145_ifs_id_seq","IFS",item.getStoreCd());
            if(org.apache.commons.lang.StringUtils.isBlank(piCd)){
                //获取序列失败
                log.error("获取序列失败 getSequence: {}", "pi0145_ifs_id_seq");
                RuntimeException e = new RuntimeException("获取序列失败[ pi0145_ifs_id_seq ]");
                throw e;
            }
            // 新增
            item.setPiCd(piCd);
            item.setCreateYmd(ymd);
            item.setCreateHms(hms);
            // 补充明细数据
            for (StocktakeItemDTOC dto : stocktakeItemList) {
                String[] dateAndTime = formatLastUpdateTime(dto.getLastUpdateTime());
                dto.setPiCd(item.getPiCd());
                dto.setStoreCd(item.getStoreCd());
                dto.setUpdateYmd(dateAndTime[0]);
                dto.setUpdateHms(dateAndTime[1]);
            }

            try {
                // 保存数据
                // 保存头档
                fsInventoryentryMapper.saveItem(item);
                // 保存明细
                fsInventoryentryMapper.saveAllItem(stocktakeItemList);
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // 修改
            // 补充明细数据
            for (StocktakeItemDTOC dto : stocktakeItemList) {
                String[] dateAndTime = formatLastUpdateTime(dto.getLastUpdateTime());
                dto.setPiCd(item.getPiCd());
                dto.setStoreCd(item.getStoreCd());
                dto.setUpdateYmd(dateAndTime[0]);
                dto.setUpdateHms(dateAndTime[1]);
            }
            item.setUpdateYmd(ymd);
            item.setUpdateHms(hms);

            try {
                // 修改头档
                fsInventoryentryMapper.updateItem(item);
                // 修改明细
                // 先删除, 再添加
                fsInventoryentryMapper.deleteByPicd(item.getPiCd());
                fsInventoryentryMapper.saveAllItem(stocktakeItemList);
                return item;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    private String[] formatLastUpdateTime(String dateStr) {
        String[] strArr = null;
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateStr);
            strArr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(date).split("-");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strArr;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto = cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}
