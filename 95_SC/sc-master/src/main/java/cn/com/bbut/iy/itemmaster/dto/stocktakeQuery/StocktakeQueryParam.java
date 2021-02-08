package cn.com.bbut.iy.itemmaster.dto.stocktakeQuery;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.util.Collection;

/**
 * @author lz
 *
 */
@Data
public class StocktakeQueryParam extends GridParamDTO {
    // 条件查询
    private String piCd; // 盘点编号
    private String accDate;//日期
    private String depCd;//部门编号
    private String pmaCd;//大分类编号
    private String qns;//只查询未盘点商品 0

    private int limitStart;
    private int limitEnd;
    private String orderByClause;

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
