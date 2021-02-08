package cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * 日报的查询条件
 */
@Data
public class VendorReceiptDailyParamDTO extends GridParamDTO {
    // 开始时间(订货)
    private String startDate;
    // 结束时间
    private String endDate;
    // 部门
    private String depCd;
    // 大分类
    private String pmaCd;
    private String categoryCd;
    private String subCategoryCd;
    // 商品id
    private String articleId;
    private String barcode;
    private String barcode1;
    private String articleName;
    private String vendorName;
    private String articleId1;
    private String articleName1;
    // 供应商id
    private String vendorCd;
    // 区域经理
    private String am;
    private String om;
    private String reason;
    private String dc;
    // 城市
    private String city;
    // 对方店铺
    private String storeCd1;
    // 业务日期
    private String businessDate;
    // 收货单号
    private String receiveId;

    private int limitStart;

    // 是否分页
    private boolean flg = true;

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
}
