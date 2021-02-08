package cn.com.bbut.iy.itemmaster.dto.returnsDaily;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReturnsDailyDTO {

    // 商品id
    private String articleId;
    // 商品名称
    private String articleName;
    // 商品条码
    private String barcode;
    // 销售日期
    private String saleDate;
    // pos机号
    private String posId;
    // 销售流水号
    private String saleSerialNo;
    // 退货数量
    private String orderQty;
    // 实际售价
    private String priceActual;
    // 退货金额
    private String orderAmt;
    // 收银人id
    private String cashierId;
    // 收银人姓名
    private String cashierName;
    // 区域经理 Id
    private String amCd;
    // 区域经理 名称
    private String amName;

    // 支付方式1
    private String payCd1;
    // 支付方式2
    private String payCd2;
    // 支付方式3
    private String payCd3;
    // 支付方式4
    private String payCd4;


    /**
     * 支付方式金额_1/现金
     */
    private BigDecimal cash;
    /**
     * 支付方式金额_2/信用卡支付
     */
    private BigDecimal cardPayment;
    /**
     * 支付方式金额_3/E-Voucher 支付
     */
    private BigDecimal eVoucher;
    /**
     * 支付方式金额_4/Momo
     */
    private BigDecimal momo;
    /**
     * 支付方式金额_5/Payoo
     */
    private BigDecimal payoo;
    /**
     * 支付方式金额_6/Viettel
     */
    private BigDecimal viettel;

    // 合计金额
    private BigDecimal totalAmt;



    // 更换了支付方式, 以下字段不用了 start
    /**
     * 支付方式金额_1/信用卡
     */
    private BigDecimal creditCard;
    /**
     * 支付方式金额_2/银联
     */
    private BigDecimal unionPay;
    /**
     * 支付方式金额3/商品券
     */
    private BigDecimal commodityCoupon;
    /**
     * 支付方式金额_4/打折券
     */
    private BigDecimal discountCoupon;
    /**
     * 支付方式金额_5/预付卡
     */
    private BigDecimal prepaidCard;
    /**
     * 支付方式金额_6/苏宁卡
     */
    private BigDecimal suNingCard;
    // end
    private String storeCd;
    private String storeName;
    private String saleFormtDate;
}
