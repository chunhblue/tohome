package cn.com.bbut.iy.itemmaster.dto.unpack;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 拆包销售查询DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UnpackParamDTO extends GridParamDTO {
 
    // 分页
    private int limitStart;
 
    // 业务日期：匹配商品主档记录
    private String businessDate;
 
    // 组合生效日期：匹配组合头档、明细档记录key
    private String effectiveDate;
 
    // 拆包日期：匹配拆包头档表，SK0000表生效日期获取售价
    private String unpackDate;
    private String unpackStartDate;
    private String unpackEndDate;

    // 拆包编号
    private String unpackId;
 
    // 母货号
    private String parentArticleId;

    /**
     * 角色资源信息
     */
    List<ResourceViewDTO> resources;

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