package cn.com.bbut.iy.itemmaster.serviceimpl.reconciliationMng;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.Mb0030Mapper;
import cn.com.bbut.iy.itemmaster.dao.ReconciliationMngMapper;
import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngDto;
import cn.com.bbut.iy.itemmaster.dto.ReconciliationMng.ReconciliationMngParamDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.reconciliationMng.ReconciliationMngService;
import cn.com.bbut.iy.itemmaster.serviceimpl.base.ImportExcelBaseService;
import cn.com.bbut.iy.itemmaster.util.SpiltExcel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("ReconciliationMngServiceImpl")
public class ReconciliationMngServiceImpl  extends ImportExcelBaseService implements ReconciliationMngService {

    @Autowired
    private ReconciliationMngMapper reconciliationMngMapper;
    @Autowired
    private Mb0030Mapper mb0030Mapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Override
    public GridDataDTO<ReconciliationMngDto> getByTypeCondition(ReconciliationMngParamDto reParam){
        List<ReconciliationMngDto> _list = new ArrayList<>();
        // 数据总条数
        int count = 0;
        reParam.setBusinessDate(getBusinessDate());

        switch (reParam.getExcelGroupCd()){
            case "201": // CK SAP (Paybill)
                _list = reconciliationMngMapper.selectMb1200List(reParam);
                count = reconciliationMngMapper.selectCountMb1200List(reParam);
                break;
            case "202": // CK SAP (Paycode)
                _list = reconciliationMngMapper.selectMb1300List(reParam);
                count = reconciliationMngMapper.selectCountMb1300List(reParam);
                break;
            case "302":  // Viettel
                _list = reconciliationMngMapper.selectMb1700List(reParam);
                count = reconciliationMngMapper.selectCountMb1700List(reParam);
                break;
        }

        if(count == 0){
            return new GridDataDTO<>();
        }

        GridDataDTO<ReconciliationMngDto> data = new GridDataDTO<>(_list,
                reParam.getPage(), count, reParam.getRows());

        return data;
    }

    @Override
    public String insert(ReconciliationMngDto recon) {
        String i = "";
        //添加附件信息
        if(StringUtils.isNotBlank(recon.getFileDetailJson())){
            List<Mb0030> mb0030List = new Gson().fromJson(recon.getFileDetailJson(), new TypeToken<List<Mb0030>>(){}.getType());
            for (int j = 0; j < mb0030List.size(); j++) {
                Mb0030 mb0030 = mb0030List.get(j);
                mb0030.setCreateUserId(recon.getCommonDTO().getCreateUserId());
                mb0030.setCreateYmd(recon.getCommonDTO().getCreateYmd());
                mb0030.setCreateHms(recon.getCommonDTO().getCreateHms());
                mb0030Mapper.insertSelective(mb0030);
            }
            i = recon.getCommonDTO().getCreateYmd();
        }
        return i;
    }

    @Override
    public List<AutoCompleteDTO> getMb0010(String v) {
        return reconciliationMngMapper.getMb0010(v);
    }

    @Override
    public List<AutoCompleteDTO> getMb0020(String documentReconCd, String v) {
        return reconciliationMngMapper.getMb0020(documentReconCd,v);
    }

    @Override
    public List<Mb0030> getMb0030List(String createUserId) {
        Mb0030Example mb0030Example = new Mb0030Example();
        Mb0030Example.Criteria criteria = mb0030Example.createCriteria();
        criteria.andCreateUserIdEqualTo(createUserId);
        List<Mb0030> list = mb0030Mapper.selectByExample(mb0030Example);
        list.forEach(mb0030 -> {
            mb0030.setFilePath(mb0030.getFileName()+","+mb0030.getFilePath());
        });
        return list;
    }

    @Override
    public Mb0030 selectFile(String excelGroupCd) {
        return mb0030Mapper.selectFile(excelGroupCd);
    }

    @Override
    public int countExcelGroupCd(String excelGroupCd) {
        return mb0030Mapper.countExcelGroupCd(excelGroupCd);
    }

    @Override
    public int delFile(String filePath) {
        return mb0030Mapper.delFile(filePath);
    }


    public List<ReconciliationMngDto> importExcelTomb1000(ReconciliationMngParamDto reParam,MultipartFile file,String filePath, HttpServletRequest req, HttpServletResponse resp) throws ParseException {

        int rowNum = 0;//已取值的行数
        int colNum = 0;//列号
        int realRowCount = 0;//真正有数据的行数

        //得到工作空间
        Workbook workbook = null;
        try {
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat createDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<ReconciliationMngDto>  list = new ArrayList<>();
        MB1000 mb1000 = null;
        Mb1200 mb1200 = null;
        String storeCd = "";
        String businessDate = getBusinessDate();
        List<ReconciliationMngDto> _list = reconciliationMngMapper.selectListByCondition(businessDate);
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
            rowNum ++;
            colNum = 0;//从第一列开始
            mb1000 = new MB1000();
            String isStoreCd = super.getCellValue(sheet, row, colNum);

            if("Store ID".equalsIgnoreCase(isStoreCd)){
                storeCd = super.getCellValue(sheet, row, 1);
                continue;
            }

            //开始读取数据
            if(StringUtils.isNotBlank(isStoreCd)){
                mb1000.setCreateDate(createDate.format(new Date()));
                mb1000.setStoreCd(super.getCellValue(sheet, row, 0));
                mb1000.setStoreName(super.getCellValue(sheet, row, 1));
                mb1000.setId(super.getCellValue(sheet, row, 2));
                mb1000.setInvoiceNo(super.getCellValue(sheet, row, 3));
                String date = sdf2.format(sdf1.parse(super.getCellValue(sheet, row, 4)));
                mb1000.setBusinessDate(Timestamp.valueOf(date));
                String transDate = sdf2.format(sdf1.parse(super.getCellValue(sheet, row, 5)));
                mb1000.setTransDate(Timestamp.valueOf(transDate));
                mb1000.setReceiptNo(super.getCellValue(sheet, row, 6));
                BigDecimal amount=new BigDecimal(super.getCellValue(sheet, row, 7));
                mb1000.setAmount(amount);
                mb1000.setStatus(super.getCellValue(sheet, row, 8));
                BigDecimal amountCk=new BigDecimal(super.getCellValue(sheet, row, 9));
                mb1000.setAmountCk(amountCk);
                mb1000.setReference(super.getCellValue(sheet, row, 10));
                mb1000.setRefDate(sdf.parse(super.getCellValue(sheet, row, 11)));

                for(ReconciliationMngDto recdto: _list) {
                    if(mb1200.getStoreCd().equals(recdto.getStoreCd())){
                        recdto.setTransDate(mb1200.getTransDate());
                        recdto.setThirdQty(mb1200.getPayooQty());
                        recdto.setThirdAmount(mb1200.getPayooAmount());

                        if(recdto.getThirdQty() == null){
                            recdto.setThirdQty(BigDecimal.ZERO);
                        }else if(recdto.getThirdAmount() == null){
                            recdto.setThirdAmount(BigDecimal.ZERO);
                        }else if(recdto.getCkQty() == null){
                            recdto.setCkQty(BigDecimal.ZERO);
                        }else if(recdto.getCkAmount() == null){
                            recdto.setCkAmount(BigDecimal.ZERO);
                        }
                        recdto.setVaryQty(recdto.getThirdQty().subtract(recdto.getCkQty()));
                        recdto.setVaryAmount(recdto.getThirdAmount().subtract(recdto.getCkAmount()));
                        list.add(recdto);
                    }
                }
            }
        }
        return list;
    }

    @Override
    public int insertMb1700(MultipartFile file,String userId){
        int rowNum = 0;//已取值的行数
        int colNum = 0;//列号
        int realRowCount = 0;//真正有数据的行数

        //得到工作空间
        Workbook workbook = null;
        try {
            workbook = super.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到工作表
        Sheet sheet = super.getSheetByWorkbook(workbook, 0);
        if (sheet.getRow(2000) != null){
            throw new RuntimeException("系统已限制单批次导入必须小于或等于2000！");
        }
        int i = 0;
        realRowCount = sheet.getPhysicalNumberOfRows();
        SimpleDateFormat createDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat createDate2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        List<ReconciliationMngDto> list = new ArrayList<>();
        List<Mb1700> oldList = new ArrayList<>();
        String requestId = "";
        String businessDate = getBusinessDate();

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
            rowNum ++;
            colNum = 0;//从第一列开始
            Mb1700 mb1700 = new Mb1700();
            String isrequestId = super.getCellValue(sheet, row, colNum);
            if("REQUEST_ID".equalsIgnoreCase(isrequestId)){
                requestId = super.getCellValue(sheet, row, 1);
                continue;
            }
            //开始读取数据
            if(StringUtils.isNotBlank(isrequestId)){
                mb1700.setRequestId(super.getCellValue(sheet, row, 0));
                mb1700.setOrderId(super.getCellValue(sheet, row, 1));
                String requestDate = createDate.format(row.getCell(2).getDateCellValue());
                mb1700.setRequestDate(requestDate);
                BigDecimal transAmount=new BigDecimal(super.getCellValue(sheet, row, 3));
                mb1700.setTransAmount(transAmount);
                mb1700.setPartnerUsername(super.getCellValue(sheet, row, 4));
                mb1700.setShopName(super.getCellValue(sheet, row, 5));
                oldList.add(mb1700);
            }
        }
        Collection<String> dbStores = new ArrayList<>();
        List<Mb1700> newList = new ArrayList<>();
        if(oldList.size()>0){
            for(Mb1700 mbOld:oldList){
                boolean flag = true;
                for(Mb1700 mbNew:newList) {
                    if (mbNew.getRequestId().equals(mbOld.getRequestId())
                            && mbNew.getRequestDate().equals(mbOld.getRequestDate())
                            && mbNew.getShopName().equals(mbOld.getShopName())) {
                        mbNew.setTransAmount(mbNew.getTransAmount().add(mbOld.getTransAmount()));
                        dbStores.add(mbOld.getShopName());
                        flag = false;
                    }
                }
                if (flag) {
                    newList.add(mbOld);
                    dbStores.add(mbOld.getShopName());
                }
            }
            List<ReconciliationMngDto> _list = reconciliationMngMapper.selectListForMb1300(businessDate,dbStores);
            Collection<String> storeItems = new ArrayList<>();
            List<Mb1700> insertList = new ArrayList<>();
            for(ReconciliationMngDto recdto: _list) {
                for(Mb1700 mb1700 : newList){
                    if(mb1700.getRequestId().equals(recdto.getArticleId())
                    && mb1700.getRequestDate().equals(recdto.getTransDate())
                    && mb1700.getShopName().equals(recdto.getStoreCd())){
                        mb1700.setCkAmount(recdto.getCkAmount());
                        mb1700.setVaryAmount(mb1700.getTransAmount().subtract(recdto.getCkAmount()));
                        mb1700.setVaryQty(recdto.getCkQty().multiply(new BigDecimal(-1)));
                        mb1700.setCkQty(recdto.getCkQty());
                        mb1700.setCreateUserId(userId);
                        insertList.add(mb1700);
                        storeItems.add(recdto.getStoreCd()+" "+recdto.getArticleId());
                    }
                }
            }
            List<String> storeDateItems = new ArrayList<>();
            if(insertList.size()>0 && storeItems.size()>0){
                List<Mb1700> mb17List = reconciliationMngMapper.getMb1700List(storeItems);
                if(mb17List.size()>0){
                    for(Mb1700 mbDb : mb17List){
                        for(Mb1700 mbIn : insertList){
                            if(mbIn.getShopName().equals(mbDb.getShopName())
                                    && mbIn.getRequestDate().equals(mbDb.getRequestDate())
                                    && mbIn.getRequestId().equals(mbDb.getRequestId())){
                                storeDateItems.add(mbIn.getShopName()+" "+mbIn.getRequestDate()+" "+mbIn.getRequestId());
                            }
                        }
                    }
                }
                if(storeDateItems.size()>0){
                    reconciliationMngMapper.deleteMb1700ByCondition(storeDateItems);
                }

                i = reconciliationMngMapper.insertMb1700(insertList);
            }
        }
        return i;
    }

    @Override
    public int insertExcelToMb1200(String userId, MultipartFile file) {
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
        SimpleDateFormat createDate = new SimpleDateFormat("yyyyMMdd");
        List<Mb1200> oldList = new ArrayList<>();
        String storeId = "";
        String businessDate = getBusinessDate();
        int i = 0;


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
            rowNum ++;
            colNum = 0;//从第一列开始
            Mb1200 mb1200 = new Mb1200();
            String isStoreId = super.getCellValue(sheet, row, colNum);
            if("STORE ID".equalsIgnoreCase(isStoreId)){
                storeId = super.getCellValue(sheet, row, 1);
                continue;
            }

            //开始读取数据
            if(StringUtils.isNotBlank(isStoreId)) {
                mb1200.setStoreCd(super.getCellValue(sheet, row, 0));
                mb1200.setStoreName(super.getCellValue(sheet, row, 1));
                String transDate = createDate.format(row.getCell(2).getDateCellValue());
                mb1200.setTransDate(transDate);
                BigDecimal payooQty = new BigDecimal(super.getCellValue(sheet, row, 3));
                mb1200.setPayooQty(payooQty);
                BigDecimal commission = new BigDecimal(super.getCellValue(sheet, row, 4));
                mb1200.setCommission(commission);
                BigDecimal payooAmount = new BigDecimal(super.getCellValue(sheet, row, 5));
                mb1200.setPayooAmount(payooAmount);
                oldList.add(mb1200);
            }
        }
        Collection<String> dbStores = new ArrayList<>();
        List<Mb1200> newList = new ArrayList<>();
        if(oldList.size()>0){
            for(Mb1200 mbOld:oldList){
                boolean flag = true;
                for(Mb1200 mbNew:newList) {
                    if (mbNew.getStoreCd().equals(mbOld.getStoreCd())
                            && mbNew.getTransDate().equals(mbOld.getTransDate())) {
                        mbNew.setPayooQty(mbNew.getPayooQty().add(mbOld.getPayooQty()));
                        mbNew.setPayooAmount(mbNew.getPayooAmount().add(mbOld.getPayooAmount()));
                        dbStores.add(mbOld.getStoreCd());
                        flag = false;
                    }
                }
                if (flag) {
                    newList.add(mbOld);
                    dbStores.add(mbOld.getStoreCd());
                }
            }
            List<ReconciliationMngDto> _list = reconciliationMngMapper.selectListForMb1200(businessDate,dbStores);
            Collection<String> stores = new ArrayList<>();
            List<Mb1200> insertList = new ArrayList<>();
            for(ReconciliationMngDto recdto: _list) {
                for(Mb1200 mb1200 : newList){
                    if(mb1200.getStoreCd().equals(recdto.getStoreCd())
                            && mb1200.getTransDate().equals(recdto.getTransDate())){
                        mb1200.setCkAmount(recdto.getCkAmount());
                        mb1200.setVaryQty(mb1200.getPayooQty().subtract(recdto.getCkQty()));
                        mb1200.setVaryAmount(mb1200.getPayooAmount().subtract(recdto.getCkAmount()));
                        mb1200.setCkQty(recdto.getCkQty());
                        mb1200.setCreateUserId(userId);
                        mb1200.setStoreName(recdto.getStoreName());
                        insertList.add(mb1200);
                        stores.add(recdto.getStoreCd());
                    }
                }
            }
            List<String> storeDates = new ArrayList<>();
            if(insertList.size()>0 && stores.size()>0){
                List<Mb1200> mb12List = reconciliationMngMapper.getMb1200List(stores);
                if(mb12List.size()>0){
                    for(Mb1200 mbDb : mb12List){
                        for(Mb1200 mbIn : insertList){
                            if(mbIn.getStoreCd().equals(mbDb.getStoreCd())
                            && mbIn.getTransDate().equals(mbDb.getTransDate())){
                                storeDates.add(mbIn.getStoreCd()+" "+mbIn.getTransDate());
                            }
                        }
                    }
                }
                if(storeDates.size()>0){
                    reconciliationMngMapper.deleteMb1200ByCondition(storeDates);
                }

                i = reconciliationMngMapper.insertExcelToMb1200(insertList);
            }
        }
        return i;
    }

    @Override
    public int insertExcelTomb1300(MultipartFile file,String userId)  {
        int rowNum = 0;//已取值的行数
        int colNum = 0;//列号
        int realRowCount = 0;//真正有数据的行数

        //得到工作空间
        Workbook workbook = null;
        try {
            workbook = super.getWorkbookByInputStream(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //得到工作表
        Sheet sheet = super.getSheetByWorkbook(workbook, 0);
        if (sheet.getRow(30000) != null){
            throw new RuntimeException("系统已限制单批次导入必须小于或等于30000！");
        }
        int i = 0;
        realRowCount = sheet.getPhysicalNumberOfRows();
        SimpleDateFormat createDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        List<Mb1300> oldList = new ArrayList<>();
        String whscode = "";
        String businessDate = getBusinessDate();

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
            rowNum ++;
            colNum = 0;//从第一列开始
            Mb1300 mb1300 = new Mb1300();
            String isWhscode = super.getCellValue(sheet, row, colNum);
            if("whscode".equalsIgnoreCase(isWhscode)){
                whscode = super.getCellValue(sheet, row, 1);
                continue;
            }

            //开始读取数据
            if(StringUtils.isNotBlank(isWhscode)){
                mb1300.setStoreCd(super.getCellValue(sheet, row, 0));
                if(row.getCell(1).getDateCellValue() == null){
                    continue;
                }
                String da = createDate.format(row.getCell(1).getDateCellValue());
                mb1300.setBusinessDate(da);
                mb1300.setBarcode(super.getCellValue(sheet, row, 2));
                mb1300.setItemName(super.getCellValue(sheet, row, 3));
                BigDecimal payooQty=new BigDecimal(super.getCellValue(sheet, row, 6));
                mb1300.setPayooQty(payooQty);
                BigDecimal pucharseAmt = new BigDecimal(super.getCellValue(sheet, row, 7));
                mb1300.setPucharseAmt(pucharseAmt);
                BigDecimal payooAmount = new BigDecimal(super.getCellValue(sheet, row, 8));
                mb1300.setPayooAmount(payooAmount);
                oldList.add(mb1300);
            }
        }
        Collection<String> dbStores = new ArrayList<>();
        List<Mb1300> newList = new ArrayList<>();
        if(oldList.size()>0) {
            for (Mb1300 mbOld : oldList) {
                boolean flag = true;
                for (Mb1300 mbNew : newList) {
                    if (mbNew.getStoreCd().equals(mbOld.getStoreCd())
                            && mbNew.getBusinessDate().equals(mbOld.getBusinessDate())
                            && mbNew.getBarcode().equals(mbOld.getBarcode())) {
                        mbNew.setPayooQty(mbNew.getPayooQty().add(mbOld.getPayooQty()));
                        mbNew.setPayooAmount(mbNew.getPayooAmount().add(mbOld.getPayooAmount()));
                        dbStores.add(mbOld.getStoreCd());
                        flag = false;
                    }
                }
                if (flag) {
                    newList.add(mbOld);
                    dbStores.add(mbOld.getStoreCd());
                }
            }
            List<ReconciliationMngDto> _list = reconciliationMngMapper.selectListForMb1300(businessDate,dbStores);
            Collection<String> storeItems = new ArrayList<>();
            List<Mb1300> insertList = new ArrayList<>();
            for(ReconciliationMngDto recdto: _list) {
                for(Mb1300 mb1300 : newList){
                    if(mb1300.getStoreCd().equals(recdto.getStoreCd())
                            && mb1300.getBusinessDate().equals(recdto.getTransDate())
                            && mb1300.getBarcode().equals(recdto.getBarcode())){
                        mb1300.setCkAmount(recdto.getCkAmount());
                        mb1300.setVaryQty(mb1300.getPayooQty().subtract(recdto.getCkQty()));
                        mb1300.setVaryAmount(mb1300.getPayooAmount().subtract(recdto.getCkAmount()));
                        mb1300.setCkQty(recdto.getCkQty());
                        mb1300.setCreateUserId(userId);
                        mb1300.setItemCode(recdto.getArticleId());
                        mb1300.setItemName(recdto.getArticleName());
                        insertList.add(mb1300);
                        storeItems.add(recdto.getStoreCd()+" "+recdto.getBarcode());
                    }
                }
            }
            List<String> storeDateItems = new ArrayList<>();
            if(insertList.size()>0 && storeItems.size()>0){
                List<Mb1300> mb13List = reconciliationMngMapper.getMb1300List(storeItems);
                if(mb13List.size()>0){
                    for(Mb1300 mbDb : mb13List){
                        for(Mb1300 mbIn : insertList){
                            if(mbIn.getStoreCd().equals(mbDb.getStoreCd())
                                    && mbIn.getBusinessDate().equals(mbDb.getBusinessDate())
                                    && mbIn.getItemCode().equals(mbDb.getItemCode())
                                    && mbIn.getBarcode().equals(mbDb.getBarcode())){
                                storeDateItems.add(mbIn.getStoreCd()+" "+mbIn.getBusinessDate()+" "+mbIn.getItemCode()+" "+mbIn.getBarcode());
                            }
                        }
                    }
                }
                if(storeDateItems.size()>0){
                    reconciliationMngMapper.deleteMb1300ByCondition(storeDateItems);
                }

                i = reconciliationMngMapper.insertExcelToMb1300(insertList);
            }
        }
        return i;
    }

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

}
