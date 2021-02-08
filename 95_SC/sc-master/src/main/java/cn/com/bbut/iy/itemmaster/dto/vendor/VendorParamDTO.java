package cn.com.bbut.iy.itemmaster.dto.vendor;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商主档查询对象
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class VendorParamDTO extends GridParamDTO {

    private int limitStart;

    private String businessDate;

    private String vendorId;

    private String effectiveStartDate;

    private String vendorName;
    // 区域
    private String regionCd;

    private String businessType;
    // 一览页面多选查询条件
    private List<String> businessTypeList;

    private String deliveryType;

    private String orderSendMethod;

    private String adminName;

    private String behalfVendorId;

    // 是否分页
    private boolean flg = true;

}
