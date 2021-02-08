package cn.com.bbut.iy.itemmaster.dto.od0000_t;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 收货单查询参数DTO
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OD0000TParamDTO extends GridParamDTO {

    // 业务日期
    private String businessDate;

    // 业区分供应商订货||仓库订货
    private String orderType;

    // 收货单编号
    private String receiveId;

    // 订单编号
    private String orderId;

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
    /** 店铺CD */
    private String storeCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;

    private String physicalReceivingDate;

}
