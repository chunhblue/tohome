package cn.com.bbut.iy.itemmaster.dto.businessDaily;

import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusinessDailyDto {
    /**
     * 营业日期
     */
    private String businessDate;
    /**
     * 销售毛额
     */
    private BigDecimal grossSaleAmount;
    /**
     * 折扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 应收金额
     */
    private BigDecimal saleAmount;
    /**
     * 溢收金额
     */
    private BigDecimal spillAmount;
    /**
     * 服务费用
     */
    private BigDecimal serviceAmount;
    /**
     * 充值费用
     */
    private BigDecimal chargeAmount;
    /**
     * 充值退款费用
     */
    private BigDecimal chargeRefundAmount;
    /**
     * 舍去金额
     */
    private BigDecimal overAmount;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 支付方式金额/合计
     */
    private BigDecimal payAmt;
    /**
     * 支付方式金额_1/Cash
     */
    private BigDecimal payAmt0;
    /**
     * 支付方式金额_1/Card Payment
     */
    private BigDecimal payAmt1;
    /**
     * 支付方式金额_2/E-Voucher
     */
    private BigDecimal payAmt2;
    /**
     * 支付方式金额3/Momo
     */
    private BigDecimal payAmt3;
    /**
     * 支付方式金额_4/Payoo
     */
    private BigDecimal payAmt4;
    /**
     * 支付方式金额_5/Viettel
     */
    private BigDecimal payAmt5;
    /**
     * 支付方式金额_6/GrabMoca
     */
    private BigDecimal payAmt6;
    /**
     * 支付方式金额_7/ShopeePay
     */
    private BigDecimal payAmt7;
    /**
     * 大分类cd
     */
    private String pmaCd;

    /**
     * 大分类
     */
    private String pmaName;

    /**
     * 中分类cd
     */
    private String categoryCd;

    /**
     * 中分类
     */
    private String categoryName;

    /**
     * 角色资源组
     */
    private List<ResourceViewDTO> resources;

    /**
     * 银行缴款
     */
    private BigDecimal bankDepositAmount;

    /**
     * 现金小计
     */
    private BigDecimal cashAmount;

    /**
     * 昨日留存金
     */
    private BigDecimal retentionAmount;

    /**
     * 支付方式名称
     */
    private String payCd0;
    private String payCd1;
    private String payCd2;
    private String payCd3;
    private String payCd4;
    private String payCd5;
    private String payCd6;
    private String payCd7;

    /**
     * 根据支付方式统计客数
     */
    private Integer customerCount;
    private Integer customerCount0;
    private Integer customerCount1;
    private Integer customerCount2;
    private Integer customerCount3;
    private Integer customerCount4;
    private Integer customerCount5;
    private Integer customerCount6;
    private Integer customerCount7;

    /**
     * service 金额
     * Mo mo cash - IN : 218556
     * Payoo: 168604, 240448
     * Viettel on POS: all item code with the sub-catecode = 432
     */
    private BigDecimal momoCashInAmt;
    private BigDecimal payooAmt;// payoo 合计金额
    private BigDecimal payooCodeAmt;
    private BigDecimal payooBillAmt;
    private BigDecimal viettelAmt;
    private BigDecimal otherServiceAmt;
    private BigDecimal servicePayAmt; // 合计金额

    private Integer momoCashIncount;
    private Integer payooCount; // payoo 合计人数
    private Integer payooCodeCount;
    private Integer payooBillCount;
    private Integer viettelCount;
    private Integer otherServiceCount;
    private Integer serviceCount;  // 合计人数

}
