package cn.com.bbut.iy.itemmaster.dto.invoiceEntry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItemsDTO extends GridDataDTO {
    private int no;
    private String articleId;
    private String articleName;
    private String qty;
    private String amt;
    private int num;
}
