package cn.com.bbut.iy.itemmaster.dto.electronicReceipt;

import cn.com.bbut.iy.itemmaster.dto.PermissionResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName ReceiptTypeDto
 * @Description TODO
 * @Author Administrator
 * @Date 2020/7/9 15:28
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptTypeDto {
    private String accDate;
    private String storeCd;
    private String posId;
    private String tranSerialNo;
    private String tranDate;
    private String tranType;
    private String saleType;
}
