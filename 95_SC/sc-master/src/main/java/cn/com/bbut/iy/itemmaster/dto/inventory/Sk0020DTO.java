package cn.com.bbut.iy.itemmaster.dto.inventory;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 传票明细
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Sk0020DTO extends GridDataDTO {

    // 店号
    private String storeCd;
    private String storeName;

    // 传票编号
    private String voucherNo;

    // 传票类型
    private String voucherType;

    // 传票日期
    private String voucherDate;
    // 店间转入日期(调拨报表用)
    private String tranInDate;

    // 商品编号
    private String articleId;
    private String articleName;
    private String articleNameEn;

    private String articleId1;
    private String articleName1;

    // 对方店号
    private String storeCd1;
    private String storeName1;

    // 调整原因
    private String adjustReason;
    private String adjustReasonText;

    // 店间调拨数量不一致的原因
    private String differenceReason;
    private String differenceReasonText;

    // 商品条码
    private String barcode;
    private String barcode1;

    // 商品单位
    private String salesUnitId;
    private String uom;
    private String spec;
    private String depCd;
    private String depName;
    private String pmaCd;
    private String pmaName;
    private String categoryCd;
    private String categoryName;
    private String subCategoryCd;
    private String subCategoryName;

    private String salesUnitId1;
    private String uom1;
    private String spec1;

    private String amCd;
    private String amName;

    // 店间调入的数量
    private BigDecimal qty1;
    private BigDecimal bqty1;
    // 店内转入的数量
    private BigDecimal transferInQty;

    // 店间调出的数量
    private BigDecimal qty2;

    // 含税单价
    private BigDecimal price;
    private BigDecimal price1;

    // 未税单价
    private BigDecimal priceNoTax;
    private BigDecimal priceNoTax1;

    // 未税金额(小计)
    private BigDecimal amtNoTax;

    // 税额(小计)
    private BigDecimal amtTax;

    // 税率
    private BigDecimal taxRate;
    private BigDecimal taxRate1;

    // 含税单价(小计)
    private BigDecimal amt;

    // 显示顺序
    private BigDecimal displaySeq;

    // 上传flg
    private String uploadFlg;

    // 上传日期
    private String uploadDate;

    private String nrUpdateFlg;

    // 用户信息
    private CommonDTO commonDTO;

    // 实际调拨量
    private BigDecimal actualQty;

    // 实际调拨金额
    private BigDecimal actualAmt;

    // 差异数量
    private BigDecimal differenQty;

    // 实时库存数量
    private String inventoryQty;

    // 费用管理传票序号
    private String expenditureNo;
    private String expenditureNoText;
    // 费用管理作用描述
    private String description;

    // 一般水平的原因
    private String generalReason;
    private String generalReasonText;



    public BigDecimal getQty1() {
        return qty1;
    }

    public void setQty1(BigDecimal qty1) {
        this.qty1 = qty1;
    }

    public BigDecimal getQty2() {
        return qty2;
    }

    public void setQty2(BigDecimal qty2) {
        this.qty2 = qty2;
    }

    public BigDecimal getDifferenQty() {
        return differenQty;
    }

    public void setDifferenQty(BigDecimal differenQty) {
        this.differenQty = differenQty;
    }
}
