package cn.com.bbut.iy.itemmaster.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * order 供应商详细
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderVendorDetailDTO implements Serializable {
    /**
     * 供应商ＩＤ
     */
    private String vendorId;
    /**
     * 供应商 名称
     */
    private String vendorName;
    /**
     * 最低订购数量
     */
    private BigDecimal minOrderQty;
    /**
     * 最低订购金额
     */
    private BigDecimal minOrderAmt;
    /**
     * 门店编号
     */
    private String storeCd;
    /**
     * 订货日
     */
    private String orderDate;
    /**
     * 订货数量
     */
    private BigDecimal orderQty;
    /**
     * 订货价格
     */
    private BigDecimal orderPrice;
    /**
     * 商品分组
     */
    private String productGroupCd;
    /**
     * 商品名称
     */
    private String productGroupName;

    /**
     * 商品分组编号
     */
    private String groupCode;


}
