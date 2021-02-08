package cn.com.bbut.iy.itemmaster.dto.expenditure;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 经费登录查询DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureParamDTO extends GridParamDTO {

    // 分页
    private int limitStart;

    // 业务日期
    private String businessStartDate;
    private String businessEndDate;

    // 编号
    private String expenditureCd;

    // Key
    private String accDate;
    private String voucherNo;

    // 部门编号
    private String department;

    // 支取方式
    private String paymentType;

    // 经费科目
    private String expenditureSubject;

    // 经费状态
    private String expenditureStatus;

    // 经办人
    private String operator;

    // 审核状态
    private String status;

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
