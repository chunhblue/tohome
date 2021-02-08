package cn.com.bbut.iy.itemmaster.dto.store;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 店铺主档查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class StoreParamDTO extends GridParamDTO {
    private String storeName;
    /**
     * 所在位置
     */
    private String currentLocation;

    /**
     * 所在位置
     */
    private String currentLocation1;

    /**
     * 周边环境
     */
    private String surroundings1;

    /**
     * 周边环境
     */
    private String surroundings;

    /**
     * 操作状态值
     */
    private int operationStatus;

    /**
     * 店铺CD
     */
    private String storeCd;

    /**
     * 法人CD
     */
    private String corpCd;

    /**
     * ZO(大区域总部)CD
     */
    private String zoCd;

    /**
     * 区域总部CD
     */
    private String doCd;

    /**
     * 督导CD
     */
    private String ofc;

    /**
     * 经营区域CD
     */
    private String maCd;

    /**
     * 店铺业态CD
     */
    private String storeTypeCd;

    /**
     * 店铺 group CD
     */
    private String storeGroupCd;

    /**
     * 契约类型CD
     */
    private String licenseType;

    /**
     * 店铺规模CD
     */
    private String storeScope;

    /**
     * 业务日期
     */
    private String businessDate;

    /**
     * 地址
     */
    private String province;
    private String district;
    private String ward;
    private String storeType;

    /**
     * 生效开始日
     */
    private String effectiveStartDate;

    /**
     * 生效状态
     */
    private String effectiveSts;

    /**
     * 生效时间
     */
    private String effectiveDate;

    /**
     * 生效结束日
     */
    private String effectiveEndDate;

    // 是否需要分页
    private boolean flg = true;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
