package cn.com.bbut.iy.itemmaster.dto.cash;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 金种一览
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashDetail  extends GridDataDTO {
    /**
     * 业务编号
     */
    private String storeName;
    private String ofc;
    private String amName;
    private String payId;
    /**
     * 店铺编号
     */
    private String storeCd;
    /**
     * 支付方式CD
     */
    private String payCd;
    /**
     * 支付方式名称
     */
    private String payName;
    /**
     * 支付方式金额
     */
    private BigDecimal payInAmt;
    /**
     * 现金CD
     */
    private String cashCd;
    /**
     * 现金名称
     */
    private String cashName;
    /**
     * 现金数量
     */
    private BigDecimal cashQty;
    // 早班现金数量
    private BigDecimal shift1;
    // 中班现金数量
    private BigDecimal shift2;
    // 晚班现金数量
    private BigDecimal shift3;
    /**
     * 员工号
     */
    private String userId;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 业务日期
     */
    private String payDate;
    private String voucherDate;
    /**
     * 支付方式金额_1/现金
     */
    private BigDecimal payInAmt0;
    /**
     * 支付方式金额_1/苏宁卡
     */
    private BigDecimal payInAmt1;
    /**
     * 支付方式金额_2/银联
     */
    private BigDecimal payInAmt2;
    /**
     * 支付方式金额3/商品券
     */
    private BigDecimal payInAmt3;
    /**
     * 支付方式金额_4/打折券
     */
    private BigDecimal payInAmt4;
    /**
     * 支付方式金额_5/预付卡
     */
    private BigDecimal payInAmt5;
    /**
     * 应收合计金额
     */
    private BigDecimal payAmt;
    /**
     * hht日销售金额
     */
    private BigDecimal salesByHht;

    /**
     * 应收/支付方式金额_1/现金
     */
    private BigDecimal payAmt0;
    /**
     * 应收/支付方式金额_1/苏宁卡
     */
    private BigDecimal payAmt1;
    /**
     * 应收/支付方式金额_2/银联
     */
    private BigDecimal payAmt2;
    /**
     * 应收/支付方式金额3/商品券
     */
    private BigDecimal payAmt3;
    /**
     * 应收/支付方式金额_4/打折券
     */
    private BigDecimal payAmt4;
    /**
     * 应收/支付方式金额_5/预付卡
     */
    private BigDecimal payAmt5;
    /**
     * HHT早班销售现金金额
     */
    private BigDecimal payHHTAmt0;
    /**
     * HHT中班销售现金金额
     */
    private BigDecimal payHHTAmt1;
    /**
     * HHT晚班销售现金金额
     */
    private BigDecimal payHHTAmt2;
    /**
     * 差异金额
     */
    private BigDecimal payAmtDiff;
    /**
     * 差异原因cd
     */
    private String differenceReasonCd;
    /**
     * 差异原因
     */
    private String differenceReason;
    /**
     * pos机id
     */
    private String posId;
    private String createUserId;
    private String createYmd;
    private String createHms;
    private String createYmdFull;
    /**
     * 班次
     */
    private String shift;
    /**
     * 现金是否拆分
     */
    private String cashSplitFlag;
    /**
     * 客数
     */
    private BigDecimal customerQty;
    /**
     * 客单价
     */
    private BigDecimal customerAvgPrice;

    private String updateUserId;

    private String updateYmd;

    private String updateHms;

    // 业务日期
    private String businessDate;

    // 审核状态
    private Integer reviewSts;

    // 评论
    private String remark;
    /**
     * 修正录入金额
     */
    private BigDecimal additional;
    /**
     * 报销费用
     */
    private BigDecimal offsetClaim;

    /**
     * 支出管理收据序号
     */
    private String voucherNo;
    private List<String> voucherNoList;

    // 统计总数量
    private BigDecimal tcustomerQty;
    private BigDecimal tpayInAmt0;
    private BigDecimal tpayInAmt1;
    private BigDecimal tpayInAmt2;
    private BigDecimal tadditional;
    private BigDecimal toffsetClaim;
    private BigDecimal tpayAmt;
    private BigDecimal tpayAmt0;
    private BigDecimal tpayAmt1;
    private BigDecimal tpayAmt2;
    private BigDecimal tpayAmtDiff;
    private BigDecimal tpayInAmt;

}
