package cn.com.bbut.iy.itemmaster.dto.cashierDetail;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * 销售头档
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleHead extends GridDataDTO {

    /**
     * 营业时间
     */
    private String accDate;

    /**
     * 店铺号
     */
    private String storeCd;

    /**
     * 收银机号
     */
    private String posId;

    /**
     * 交易流水号
     */
    private int tranSerialNo;

    /**
     * 交易时间
     */
    private String tranDate;

    /**
     * 交易时间
     */
    private String tranTime;

    /**
     * 销售流水号
     */
    private int saleSerialNo;

    /**
     * 收营员编号
     */
    private String cashierId;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 合计金额
     */
    private BigDecimal saleAmount;

    /**
     * 让零金额
     */
    private BigDecimal overAmount;

    /**
     * 应收金额
     */
    private BigDecimal payAmount;

    /**
     * 支付类型1
     */
    private String payCd1;

    /**
     * 支付类型2
     */
    private String payCd2;

    /**
     * 支付类型3
     */
    private String payCd3;

    /**
     * 支付类型4
     */
    private String payCd4;

    /**
     * 支付金额1
     */
    private BigDecimal payAmount1;

    /**
     * 支付金额2
     */
    private BigDecimal payAmount2;
        
    /**
     * 支付金额3
     */
    private BigDecimal payAmount3;
    
    /**
     * 支付金额4
     */
    private BigDecimal payAmount4;
    /**
     * 班次
     */
    private String shift;

    /**
     * 销售明细
     */
    private List<SaleDetail> saleDetailList;

    /**
     * 支付方式
     */
    private List<PayMethod> payMethodList;
}
