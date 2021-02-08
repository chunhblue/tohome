package cn.com.bbut.iy.itemmaster.dto.electronicReceipt;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * 小票查询DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ElectronicReceiptParam {

    // 销售日期
    private String saleStartDate;
    private String saleEndDate;
    // 交易类型
    private String tranType;
    // POS机号
    private String posNo;
   // 商品Id
    private String articleId;
    // 销售序号
    private Integer saleNo;

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
