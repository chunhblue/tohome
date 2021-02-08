package cn.com.bbut.iy.itemmaster.dto.priceChange;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 紧急变价一览 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDetailParamDto extends GridParamDTO {
    /**
     *业务开始日
     */
    private String startDate;
    /**
     *业务结束日
     */
    private String endDate;

    /**
     * 商品条码
     */
    private String barcode;

    private String articleId;
    private String articleName;

    /**
     * 变价单号开始
     */
    private String startChangeId;

    /**
     * 变价单号结束
     */
    private String endChangeId;

    /**
     * 操作人
     */
    private String createUserId;

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
