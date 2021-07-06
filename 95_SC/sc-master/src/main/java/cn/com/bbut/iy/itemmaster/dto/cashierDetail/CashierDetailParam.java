package cn.com.bbut.iy.itemmaster.dto.cashierDetail;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * 收银明细查询参数
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashierDetailParam  extends GridParamDTO {
    /**
     * 销售开始时间
     */
    private String startDate;
    /**
     * 销售结束时间
     */
    private String endDate;

    private String accDate;
    /**
     * 班次
     */
    private String shift;
    /**
     * 营业员id
     */
    private String cashierId;
    /**
     * 会员卡号
     */
    private String memberId;
    /**
     * posid
     */
    private String posId;
    /**
     * 支付类型
     */
    private String payType;
    /**
     * 比较类型
     */
    private String calType;
    /**
     * 支付金额
     */
    private BigDecimal payAmt;
    /**
     * articleId
     */
    private String articleId;
    /**
     * barcode
     */
    private String barcode;
    /**
     * Item Name
     */
    private String articleShortName;

    /**
     * 是否分页
     */
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

    /**
     * 销售流水号
     */
    private String saleSerialNo;

    private String tranSerialNo;

    private String billSaleNo;

    // 1:JE  0:AR
    private Integer billFlg;
}
