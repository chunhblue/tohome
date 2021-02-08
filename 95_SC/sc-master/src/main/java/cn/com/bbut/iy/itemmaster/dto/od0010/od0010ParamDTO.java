package cn.com.bbut.iy.itemmaster.dto.od0010;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName od0010ParamDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/17 9:58
 * @Version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class od0010ParamDTO extends GridParamDTO {
    private boolean flg = true;

    /** 业务日期 */
    private String businessDate;

    private String  vendorId;
    private String orderMethod;

    /**
     * 角色资源信息
     */
    List<ResourceViewDTO> resources;

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

    private String orderDirectSupplierDateStartDate;


    private String reviewStatus;

    private String orderDirectSupplierDateEndDate;

    private String dc;

    private String orderDifferentiate;

    private String optionTime;

    private String searchJson;

    private String orderId;
    private String deliveryDate;

    private String serialNo;


    private String articleId;


    private String barcode;


    private String promotionId;


    private String orderUnit;


    private BigDecimal orderQty;


    private BigDecimal orderNochargeQty;


    private BigDecimal orderPrice;


    private BigDecimal orderAmt;


    private BigDecimal orderAmtNotax;


    private BigDecimal orderTax;


    private String receivePrice;


    private String receiveQty;


    private String receiveNoChargeQty;

    private String receiveTotalQty;


    private String receiveTotalAmt;


    private String receiveTotalAmtNotax;


    private String receiveTax;

    private String uploadFlg;


    private String uploadDate;


    private String createUserId;

    private String createYmd;


    private String createHms;

    private int limitStart;


    private String updateUserId;


    private String updateYmd;


    private String updateHms;


    private String updateScreenId;


    private String updateIpAddress;


    private String nrUpdateFlg;


    private String nrYmd;


    private String nrHms;


    private String reasonId;


    private BigDecimal adjustAmt;


    private BigDecimal adjustQty;


    private String orderDate;

    private String productName;


    private String orderSpecifi;


    private String lowestOrder;


    private String gQuantity;

    private String receivingDifferences;

    
}
