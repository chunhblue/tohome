package cn.com.bbut.iy.itemmaster.dto.orderHHT;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * @ClassName OrderHHTParam
 * @Description TODO
 * @Author Ldd
 * @Date 2021/5/24 14:13
 * @Version 1.0
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderHHTParamDto extends GridParamDTO {
    // 业务日期
    private String businessDate;
    private String startDate;

    // 订货日期
    private String orderStartDate;
    private String orderEndDate;

    private String orderDate;

    // 失败原因
    private String failedReason;

    // 商品条码
    private String barcode;
    // 供应商ID
    private String vendorId;
    private String vendorName;
    // 商品ID
    private String articleId;

    private String articleName;

    private String orderId;

    private String vendorInfo;
    private String ItemInfo;

    private String orderDifferentiate;

    // 共通
    private CommonDTO commonDTO;

    private String createUserId;

    // 分页
    private int limitStart;

    // 是否分页
    private boolean flg = true;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    // 店铺cd
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
