package cn.com.bbut.iy.itemmaster.dto.orderHHT;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName OrderHHTGridDTO
 * @Description TODO
 * @Author Ldd
 * @Date 2021/5/24 14:14
 * @Version 1.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class OrderHHTGridDTO extends GridDataDTO {
    private String articleId;
    private String articleName;
    private String storeCd;
    private String storeName;
    private String orderDate;
    private String vendorId;
    private String vendorName;
    private String itemCode;
    private String itemName;
    private String uploadTime;
    private String ordDate;
    private String barcode;
    private BigDecimal qty;

}
