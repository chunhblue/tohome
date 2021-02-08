package cn.com.bbut.iy.itemmaster.dto.ma4350;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 *  配方销售头档 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4350DetailParamDto extends GridParamDTO {

    /**
     * 传票号
     */
    private String voucherCd;

    /**
     * 销售开始日期
     */
    private String saleStartDate;

    /**
     * 销售结束日期
     */
    private String saleEndDate;

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
    private String formulaArticleName;

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

    /**
     * 业务时间
     */
    private String businessDate;

    // 是否分页
    private boolean flg = true;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
