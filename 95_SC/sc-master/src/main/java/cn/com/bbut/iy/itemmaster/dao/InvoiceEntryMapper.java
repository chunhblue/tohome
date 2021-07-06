package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceDataDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceEntryDTO;
import cn.com.bbut.iy.itemmaster.dto.invoiceEntry.InvoiceItemsDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface InvoiceEntryMapper {

    List<InvoiceEntryDTO> searchInvoice(@Param("param") InvoiceEntryDTO invoiceEntryParam);

    List<InvoiceItemsDTO> searchInvoiceItem(@Param("param") InvoiceEntryDTO invoiceEntryParam);

    Integer selectMaxCount();

    void insertInvoice(@Param("param") InvoiceDataDTO invoice);

    List<InvoiceDataDTO> searchList(@Param("param") InvoiceEntryDTO invoiceEntryParam);

    InvoiceDataDTO getData(@Param("accId") String accId, @Param("storeNo") String storeNo);

    List<InvoiceEntryDTO> getInvoiceByReceiptNo(@Param("storeNo") String storeNo,@Param("posId") String posId,
                                                @Param("list") List<String> receiptNos,
                                                @Param("rows")int rows, @Param("limitStart")int limitStart);

    void updateStatus(@Param("param")InvoiceDataDTO invoice);

    List<InvoiceDataDTO> getReceiptNoByStore(@Param("storeNo") String storeNo,@Param("posId") String posId);

}
