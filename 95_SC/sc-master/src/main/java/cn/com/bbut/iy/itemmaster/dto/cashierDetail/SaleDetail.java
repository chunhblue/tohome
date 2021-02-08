package cn.com.bbut.iy.itemmaster.dto.cashierDetail;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 销售详细
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetail extends GridDataDTO {
    /**
     * 营业日期
     */
    private String AccDate;
    /**
     * 门店号
     */
    private String storeCd;
    /**
     * 收银机号
     */
    private String posId;
    /**
     * 交易流水号
     */
    private int tranSerialNo;
    /**
     * 销售序号
     */
    private int saleSeqNo;
    /**
     * 商品条码
     */
    private String barcode;
    /**
     * 商品名称
     */
    private String articleShortName;
    /**
     * 规格
     */
    private String spec;
    /**
     * 单位
     */
    private String unitName;
    /**
     * 原售价
     */
    private BigDecimal priceOriginal;
    /**
     * 实际售价
     */
    private BigDecimal priceActual;
    /**
     * 销售数量
     */
    private BigDecimal saleQty;
     /**
      * 销售金额
      */
    private BigDecimal saleAmount;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
}
