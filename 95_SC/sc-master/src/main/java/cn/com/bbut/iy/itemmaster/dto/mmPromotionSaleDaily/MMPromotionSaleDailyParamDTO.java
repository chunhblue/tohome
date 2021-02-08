package cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.Data;

import java.util.Collection;
import java.util.List;

/**
 * 日报的查询条件
 */
@Data
public class MMPromotionSaleDailyParamDTO extends GridParamDTO {
    // 开始事件
    private String startDate;
    // 结束事件
    private String endDate;
    // 商品id
    private String articleId;
    private String barcode;
    private String articleName;
    // 区域经理
    private String am;
    // 业务日期
    private String businessDate;
    // 促销cd
    private String promotionCd;
    // 促销模式
    private String promotionPattern;
    // 促销类型
    private String promotionType;
    // 分摊类型
    private String distributionType;

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
