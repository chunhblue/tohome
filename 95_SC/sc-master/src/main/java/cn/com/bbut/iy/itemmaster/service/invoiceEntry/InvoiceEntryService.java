package cn.com.bbut.iy.itemmaster.service.invoiceEntry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.ReturnDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceDataDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceEntryDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public interface InvoiceEntryService {

    GridDataDTO<InvoiceEntryDTO> searchInvoice(String searchJson, int page, int rows);

    GridDataDTO<InvoiceItemsDTO> searchInvoiceItem(String searchJson, int page, int rows);

    ReturnDTO insertInvoice(String record, User user, HttpServletRequest request, HttpSession session);

    GridDataDTO<InvoiceDataDTO> searchInvoiceList(InvoiceEntryDTO param, int page, int rows);

    ReturnDTO getData(String accId, String storeNo);

    GridDataDTO<InvoiceEntryDTO> getInvoiceByReceiptNo(String searchJson, int page, int rows);

    ReturnDTO updateStatus(String accId, String storeNo, User user, HttpServletRequest request, HttpSession session);
}
