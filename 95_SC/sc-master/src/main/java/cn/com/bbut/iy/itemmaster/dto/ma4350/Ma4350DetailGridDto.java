package cn.com.bbut.iy.itemmaster.dto.ma4350;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 配方销售头档 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4350DetailGridDto extends GridDataDTO {
    /**
     * 传票号
     */
    private String voucherCd;

    /**
     * 销售日期
     */
    private String saleDate;

    /**
     * 店铺cd
     */
    private String storeCd;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商品id
     */
    private String formulaArticleId;

    /**
     * 商品名称
     */
    private String articleName;

    /**
     * 商品条码
     */
    private String barcode;

    /**
     * 销售数量
     */
    private BigDecimal saleQty;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 销售总价
     */
    private BigDecimal saleAmt;

    /**
     * pos id
     */
    private String posId;

    /**
     * 销售单号
     */
    private String tranSerialNo;
}
