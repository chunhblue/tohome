package cn.com.bbut.iy.itemmaster.dto.returnsDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReturnsDailyParamDTO  extends GridParamDTO {

    // 商品id
    private String articleId;
    private String articleName;
    // 退货日期
    private String returnDate;
    // 收银cd
    private String cashierCd;
    // 区域经理 Area Manager
    private String am;
    // 班次
    private String shift;

    private String depCd;
    private String pmaCd;
    private String categoryCd;
    private String subCategoryCd;
    private String barcode;

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

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

    private String storeName;

    // 合计金额
    private String totalAmt;

    private String calType;
    private String nonSaleType;

    private int limitStart;

    // 是否分页
    private boolean flg = true;

}
