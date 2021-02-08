package cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VendorReturnedDailyParamDTO extends GridParamDTO {

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
    private String articleName;
    private String vendorName;
    private String articleId1;
    private String articleName1;
    // 退货单号
    private String orderId;
    // 供应商id
    private String vendorCd;

    // DC供应商ID
    private String deliveryCenterId;
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

    private int limitStart;
    private String barcode;
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
