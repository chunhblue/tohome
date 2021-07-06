package cn.com.bbut.iy.itemmaster.dto.invoiceEntry;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceDataDTO extends GridDataDTO {

    private String accId;
    private String accDate;

    // 店铺cd
    private String storeNo;
    // 店铺名称
    private String storeName;
    // 消费者姓名
    private String customerName;
    //公司名称
    private String companyName ;
    //免税代码
    private String tax ;
    //地址
    private String address ;
    //手机号
    private String phone ;
    //邮箱
    private String email ;
    //手机号
    private String phone2;
    //邮箱
    private String email2;
    //receipt_no ,多个, 用 ';' 隔开
    private String receiptNo ;
    //总金额
    private BigDecimal amt ;
    //开票状态
    private String status;
    private String statusName;
//    开票人
    private String issueUserId ;
    private String issueYmd ;
    private String issueHms ;
    private String createUserId ;
    private String createYmd ;
    private String createHms ;
    private String updateUserId ;
    private String updateYmd ;
    private String updateHms ;
    private String posId;
    private int num;
    private String  saleSerialNo;
    private String  tranSerialNo;
}
