package cn.com.bbut.iy.itemmaster.dto.materialentry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 盘点查询商品信息
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StocktakeItemDTOE {
    // 商品条码
    private String barcode;
    // 商品编号
    private String itemCode;
    private String storeCd;
    // 商品名称
    private String itemName;
    // 命名没有统一, 临时用一下
    private String itemSpec;
    private String itemUnit;
    private String initNum;
    private String dupNum;
    private String pmaCd;
    private String rowIndex;
    // end
    private String piCd;
    private String piDate;
    private String piStatus;
    // 生效开始日期
    private String effectiveStartDate;

    // 生效结束日期
    private String effectiveEndDate;
    // 单位
    private String uomCd;
    private String uom;

    // 规格
    private String spec;

    //初盘品项数
    private String firstQty;

    //复盘品项数
    private String secondQty;

    //盘点儿商品价格
   private String expensePrice;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getStoreCd() {
        return storeCd;
    }

    public void setStoreCd(String storeCd) {
        this.storeCd = storeCd;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemSpec() {
        return itemSpec;
    }

    public void setItemSpec(String itemSpec) {
        this.itemSpec = itemSpec;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getInitNum() {
        return initNum;
    }

    public void setInitNum(String initNum) {
        this.initNum = initNum;
    }

    public String getDupNum() {
        return dupNum;
    }

    public void setDupNum(String dupNum) {
        this.dupNum = dupNum;
    }

    public String getPmaCd() {
        return pmaCd;
    }

    public void setPmaCd(String pmaCd) {
        this.pmaCd = pmaCd;
    }

    public String getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(String rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getPiCd() {
        return piCd;
    }

    public void setPiCd(String piCd) {
        this.piCd = piCd;
    }

    public String getPiDate() {
        return piDate;
    }

    public void setPiDate(String piDate) {
        this.piDate = piDate;
    }

    public String getPiStatus() {
        return piStatus;
    }

    public void setPiStatus(String piStatus) {
        this.piStatus = piStatus;
    }

    public String getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(String effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    public String getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(String effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }

    public String getUomCd() {
        return uomCd;
    }

    public void setUomCd(String uomCd) {
        this.uomCd = uomCd;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFirstQty() {
        return firstQty;
    }

    public void setFirstQty(String firstQty) {
        this.firstQty = firstQty;
    }

    public String getSecondQty() {
        return secondQty;
    }

    public void setSecondQty(String secondQty) {
        this.secondQty = secondQty;
    }

    public String getExpensePrice() {
        return expensePrice;
    }

    public void setExpensePrice(String expensePrice) {
        this.expensePrice = expensePrice;
    }
}
