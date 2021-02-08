package cn.com.bbut.iy.itemmaster.serviceimpl.invoiceEntry;

import cn.com.bbut.iy.itemmaster.dao.InvoiceEntryMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceDataDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceEntryDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.service.invoiceEntry.InvoiceEntryService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InvoiceEntryServiceImpl implements InvoiceEntryService {

    @Autowired
    private InvoiceEntryMapper invoiceEntryMapper;

    /**
     * 查询发票信息
     */
    @Override
    public GridDataDTO<InvoiceEntryDTO> searchInvoice(String searchJson, int page, int rows) {

        InvoiceEntryDTO invoiceEntryParam = null;
        if (searchJson == null || StringUtils.isEmpty(searchJson)) {
            invoiceEntryParam = new InvoiceEntryDTO();
        } else {
            Gson gson = new Gson();
            invoiceEntryParam = gson.fromJson(searchJson, InvoiceEntryDTO.class);
        }
        invoiceEntryParam.setPage(page);
        invoiceEntryParam.setRows(rows);
        invoiceEntryParam.setLimitStart((page - 1) * rows);
        List<InvoiceEntryDTO> list = invoiceEntryMapper.searchInvoice(invoiceEntryParam);

        if (list == null || list.size() < 1) {
            return new GridDataDTO<InvoiceEntryDTO>();
        }
        return new GridDataDTO<InvoiceEntryDTO>(list, invoiceEntryParam.getPage(), list.get(0).getNum(), invoiceEntryParam.getRows());
    }

    /**
     * 查询发票消费明细档
     */
    @Override
    public GridDataDTO<InvoiceItemsDTO> searchInvoiceItem(String searchJson, int page, int rows) {
        InvoiceEntryDTO invoiceEntryParam = null;
        if (searchJson == null || StringUtils.isEmpty(searchJson)) {
            invoiceEntryParam = new InvoiceEntryDTO();
        } else {
            Gson gson = new Gson();
            invoiceEntryParam = gson.fromJson(searchJson, InvoiceEntryDTO.class);
        }
        invoiceEntryParam.setPage(page);
        invoiceEntryParam.setRows(rows);
        invoiceEntryParam.setLimitStart((page - 1) * rows);
        List<InvoiceItemsDTO> list = invoiceEntryMapper.searchInvoiceItem(invoiceEntryParam);

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setNo(i + 1);
        }

        if (list == null || list.size() < 1) {
            return new GridDataDTO<InvoiceItemsDTO>();
        }
        return new GridDataDTO<InvoiceItemsDTO>(list, invoiceEntryParam.getPage(), list.get(0).getNum(), invoiceEntryParam.getRows());
    }

    /**
     * 提交保存
     */
    @Override
    public ReturnDTO insertInvoice(String record, User user, HttpServletRequest request, HttpSession session) {

        ReturnDTO _return = new ReturnDTO();

        if (record == null || StringUtils.isEmpty(record)) {
            _return.setSuccess(false);
            _return.setMsg("Parameter is empty!");
            return _return;
        }

        Date date = new Date();
        Gson gson = new Gson();
        InvoiceDataDTO invoice = gson.fromJson(record, InvoiceDataDTO.class);
        String dateStr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(date);
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];

        invoice.setCreateUserId(user.getUserId());
        invoice.setCreateYmd(ymd);
        invoice.setCreateHms(hms);

        Integer max = invoiceEntryMapper.selectMaxCount();
        if (max == null) {
            max = 0;
        }
//        String accDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date);
        invoice.setAccId(String.valueOf(max + 1));
        invoice.setAccDate(ymd);

        try {
            invoiceEntryMapper.insertInvoice(invoice);
        } catch (Exception e) {
            e.printStackTrace();
            _return.setSuccess(false);
            _return.setMsg("Data saved failed！");
            return _return;
        }
        _return.setSuccess(true);
        _return.setMsg("Data saved successfully！");
        _return.setO(invoice);
        return _return;
    }

    /**
     * 查询发票一览
     */
    @Override
    public GridDataDTO<InvoiceDataDTO> searchInvoiceList(InvoiceEntryDTO param, int page, int rows) {

        List<InvoiceDataDTO> list = invoiceEntryMapper.searchList(param);

        if (list == null || list.size() < 1) {
            return new GridDataDTO<InvoiceDataDTO>();
        }
        return new GridDataDTO<InvoiceDataDTO>(list, param.getPage(), list.get(0).getNum(), param.getRows());
    }

    /**
     * 查询发票详细信息
     */
    @Override
    @NotBlank
    public ReturnDTO getData(String accId, String storeNo) {
        ReturnDTO _return = new ReturnDTO();
        if (accId == null || StringUtils.isEmpty(accId) || storeNo == null || StringUtils.isEmpty(storeNo)) {
            _return.setSuccess(false);
            _return.setMsg("Parameter is empty!");
            return _return;
        }

        InvoiceDataDTO result = invoiceEntryMapper.getData(accId, storeNo);

        if (result == null) {
            _return.setSuccess(false);
            _return.setMsg("No data found!");
            return _return;
        }

        if (!StringUtils.isEmpty(result.getCreateYmd())) {
            result.setCreateYmd(formatDate(result.getCreateYmd()));
        }
        if (!StringUtils.isEmpty(result.getIssueYmd())) {
            result.setIssueYmd(formatDate(result.getIssueYmd()));
        }

        _return.setSuccess(true);
        _return.setO(result);
        return _return;
    }

    /**
     * 查询提交的小票信息
     */
    @Override
    public GridDataDTO<InvoiceEntryDTO> getInvoiceByReceiptNo(String searchJson, int page, int rows) {

        if (searchJson == null || StringUtils.isEmpty(searchJson)) {
            return new GridDataDTO<InvoiceEntryDTO>();
        }
        Gson gson = new Gson();
        InvoiceEntryDTO invoiceEntryParam = gson.fromJson(searchJson, InvoiceEntryDTO.class);
        int limitStart = (page - 1) * rows;

        if (invoiceEntryParam.getReceiptNo()==null||StringUtils.isEmpty(invoiceEntryParam.getReceiptNo()) ||
                invoiceEntryParam.getStoreNo()==null || StringUtils.isEmpty(invoiceEntryParam.getStoreNo())) {
            return new GridDataDTO<InvoiceEntryDTO>();
        }
        // 切分拼接的 Receipt No.
        List<String> receiptNos = CollectionUtils.arrayToList(invoiceEntryParam.getReceiptNo().split(";"));

        List<InvoiceEntryDTO> list = invoiceEntryMapper.getInvoiceByReceiptNo(invoiceEntryParam.getStoreNo(),receiptNos,rows,limitStart);

        if (list == null || list.size() < 1) {
            return new GridDataDTO<InvoiceEntryDTO>();
        }
        return new GridDataDTO<InvoiceEntryDTO>(list, page, list.get(0).getNum(), rows);
    }

    /**
     * 修改发票状态
     */
    @Override
    public ReturnDTO updateStatus(String accId, String storeNo, User user, HttpServletRequest request, HttpSession session) {
        ReturnDTO _return = new ReturnDTO();

        if (accId == null || StringUtils.isEmpty(accId) || storeNo == null || StringUtils.isEmpty(storeNo)) {
            _return.setSuccess(false);
            _return.setMsg("Parameter is empty!");
            return _return;
        }

        Date date = new Date();
        InvoiceDataDTO invoice = new InvoiceDataDTO();
        String dateStr = new SimpleDateFormat("yyyyMMdd-HHmmss").format(date);
        String ymd = dateStr.split("-")[0];
        String hms = dateStr.split("-")[1];

        invoice.setAccId(accId);
        invoice.setStoreNo(storeNo);
        invoice.setIssueYmd(ymd);
        invoice.setIssueHms(hms);
        invoice.setIssueUserId(user.getUserId());
        invoice.setStatus("02");
        invoice.setUpdateUserId(user.getUserId());
        invoice.setUpdateYmd(ymd);
        invoice.setUpdateHms(hms);

        try {
            // 执行修改
            invoiceEntryMapper.updateStatus(invoice);
        } catch (Exception e) {
            e.printStackTrace();
            _return.setSuccess(false);
            _return.setMsg("Data modify failed！");
            return _return;
        }

        _return.setSuccess(true);
        _return.setMsg("Data modify succeed！");
        _return.setO(invoice);
        return _return;
    }

    /**
     * 格式化日期
     */
    private String formatDate(String date) {
        if (StringUtils.isEmpty(date)) {
            return "";
        }
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        return day + "/" + month + "/" + year;
    }

}
