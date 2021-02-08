package cn.com.bbut.iy.itemmaster.dto.sa0050;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * SA0050 param
 *
 * @author zcz
 * @date:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SA0050ParamDto extends GridParamDTO {

     /**
     * 员工号
     */
    private String cashierId;

    /**
     *  姓名
     */
    private String cashierName;

    /**
     *  职务
     */
    private String duty;

    /**
     *  权限级别
     */
    private String cashierLevel;

    /**
     *  权限参数
     */
    private String cashierParameter;

    /**
     *密码
     */
    private String cashierPassword;

    /**
     * 生效状态
     */
    private String effectiveSts;
    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /**
     *门店cd
     */
    private String storeCd;

    /**
     *门店Name
     */
    private String storeName;

    /**
     * 业务日期
     */
    private String businessDate;

    private String cashierEmail;
    private String ofc;
    private boolean flg = true;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
}
