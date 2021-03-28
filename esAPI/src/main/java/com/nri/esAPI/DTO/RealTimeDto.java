package com.nri.esAPI.DTO;

import java.sql.Timestamp;
import java.util.Date;

public class RealTimeDto {
    private String storeCd;// 店铺cd
    private String articleId;//商品Id
    private Float saleQty;//销售数量
    private Float onHandQty;//昨日在库量
    private Float onOrderQty;//在途量
    private Float writeOffQty;//报废量
    private Float returnQty;//退货量
    private Float adjustmentQty;//库存调整量
    private Float transferOutQty;//转出数量
    private Float transferInQty;//转入数量
    private Float receiveQty;//收货数量
    private Float receiveCorrQty;//收货更正数量
    private Float returnCorrQty;//退货更正数量
    private Float transferOutCorrQty;//转出更正数量
    private Float transferInCorrQty;//转入更正数量
    private Date inEsTime;//创建时间
    private Float discountAmount;
    private Float priceOriginal;
    private String detailVoid;
    private String impFlag;
    private Timestamp saleDate;
    private String posId;
    private String accDate;
    private Float priceActual;
    private String barcode;
    private String tranSerialNo;
    private String detailType;
    private String subCategoryCd;
    private String categoryCd;
    private Float saleSeqNo;
    private String pmaCd;
    private String depCd;
    private Float saleSerialNo;
    private Float saleAmount;
    private String posFlg;
    private String createYmdHms;
    /**
     * @return the storeCd
     */
    public String getStoreCd() {
        return storeCd;
    }
    /**
     * @param storeCd the storeCd to set
     */
    public void setStoreCd(String storeCd) {
        this.storeCd = storeCd;
    }
    /**
     * @return the articleId
     */
    public String getArticleId() {
        return articleId;
    }
    /**
     * @param articleId the articleId to set
     */
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    /**
     * @return the saleQty
     */
    public Float getSaleQty() {
        return saleQty;
    }
    /**
     * @param saleQty the saleQty to set
     */
    public void setSaleQty(Float saleQty) {
        this.saleQty = saleQty;
    }
    /**
     * @return the onHandQty
     */
    public Float getOnHandQty() {
        return onHandQty;
    }
    /**
     * @param onHandQty the onHandQty to set
     */
    public void setOnHandQty(Float onHandQty) {
        this.onHandQty = onHandQty;
    }
    /**
     * @return the onOrderQty
     */
    public Float getOnOrderQty() {
        return onOrderQty;
    }
    /**
     * @param onOrderQty the onOrderQty to set
     */
    public void setOnOrderQty(Float onOrderQty) {
        this.onOrderQty = onOrderQty;
    }
    /**
     * @return the receiveQty
     */
    public Float getReceiveQty() {
        return receiveQty;
    }
    /**
     * @param receiveQty the receiveQty to set
     */
    public void setReceiveQty(Float receiveQty) {
        this.receiveQty = receiveQty;
    }
    /**
     * @return the discountAmount
     */
    public Float getDiscountAmount() {
        return discountAmount;
    }
    /**
     * @param discountAmount the discountAmount to set
     */
    public void setDiscountAmount(Float discountAmount) {
        this.discountAmount = discountAmount;
    }
    /**
     * @return the priceOriginal
     */
    public Float getPriceOriginal() {
        return priceOriginal;
    }
    /**
     * @param priceOriginal the priceOriginal to set
     */
    public void setPriceOriginal(Float priceOriginal) {
        this.priceOriginal = priceOriginal;
    }
    /**
     * @return the detailVoid
     */
    public String getDetailVoid() {
        return detailVoid;
    }
    /**
     * @param detailVoid the detailVoid to set
     */
    public void setDetailVoid(String detailVoid) {
        this.detailVoid = detailVoid;
    }
    /**
     * @return the impFlag
     */
    public String getImpFlag() {
        return impFlag;
    }
    /**
     * @param impFlag the impFlag to set
     */
    public void setImpFlag(String impFlag) {
        this.impFlag = impFlag;
    }
    /**
     * @return the saleDate
     */
    public Timestamp getSaleDate() {
        return saleDate;
    }
    /**
     * @param saleDate the saleDate to set
     */
    public void setSaleDate(Timestamp saleDate) {
        this.saleDate = saleDate;
    }
    /**
     * @return the posId
     */
    public String getPosId() {
        return posId;
    }
    /**
     * @param posId the posId to set
     */
    public void setPosId(String posId) {
        this.posId = posId;
    }
    /**
     * @return the accDate
     */
    public String getAccDate() {
        return accDate;
    }
    /**
     * @param accDate the accDate to set
     */
    public void setAccDate(String accDate) {
        this.accDate = accDate;
    }
    /**
     * @return the priceActual
     */
    public Float getPriceActual() {
        return priceActual;
    }
    /**
     * @param priceActual the priceActual to set
     */
    public void setPriceActual(Float priceActual) {
        this.priceActual = priceActual;
    }
    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }
    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    /**
     * @return the tranSerialNo
     */
    public String getTranSerialNo() {
        return tranSerialNo;
    }
    /**
     * @param tranSerialNo the tranSerialNo to set
     */
    public void setTranSerialNo(String tranSerialNo) {
        this.tranSerialNo = tranSerialNo;
    }
    /**
     * @return the detailType
     */
    public String getDetailType() {
        return detailType;
    }
    /**
     * @param detailType the detailType to set
     */
    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }
    /**
     * @return the subCategoryCd
     */
    public String getSubCategoryCd() {
        return subCategoryCd;
    }
    /**
     * @param subCategoryCd the subCategoryCd to set
     */
    public void setSubCategoryCd(String subCategoryCd) {
        this.subCategoryCd = subCategoryCd;
    }
    /**
     * @return the categoryCd
     */
    public String getCategoryCd() {
        return categoryCd;
    }
    /**
     * @param categoryCd the categoryCd to set
     */
    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }
    /**
     * @return the saleSeqNo
     */
    public Float getSaleSeqNo() {
        return saleSeqNo;
    }
    /**
     * @param saleSeqNo the saleSeqNo to set
     */
    public void setSaleSeqNo(Float saleSeqNo) {
        this.saleSeqNo = saleSeqNo;
    }
    /**
     * @return the pmaCd
     */
    public String getPmaCd() {
        return pmaCd;
    }
    /**
     * @param pmaCd the pmaCd to set
     */
    public void setPmaCd(String pmaCd) {
        this.pmaCd = pmaCd;
    }
    /**
     * @return the saleSerialNo
     */
    public Float getSaleSerialNo() {
        return saleSerialNo;
    }
    /**
     * @param saleSerialNo the saleSerialNo to set
     */
    public void setSaleSerialNo(Float saleSerialNo) {
        this.saleSerialNo = saleSerialNo;
    }
    /**
     * @return the saleAmount
     */
    public Float getSaleAmount() {
        return saleAmount;
    }
    /**
     * @param saleAmount the saleAmount to set
     */
    public void setSaleAmount(Float saleAmount) {
        this.saleAmount = saleAmount;
    }
    /**
     * @return the writeOffQty
     */
    public Float getWriteOffQty() {
        return writeOffQty;
    }
    /**
     * @param writeOffQty the writeOffQty to set
     */
    public void setWriteOffQty(Float writeOffQty) {
        this.writeOffQty = writeOffQty;
    }
    /**
     * @return the returnQty
     */
    public Float getReturnQty() {
        return returnQty;
    }
    /**
     * @param returnQty the returnQty to set
     */
    public void setReturnQty(Float returnQty) {
        this.returnQty = returnQty;
    }
    /**
     * @return the adjustmentQty
     */
    public Float getAdjustmentQty() {
        return adjustmentQty;
    }
    /**
     * @param adjustmentQty the adjustmentQty to set
     */
    public void setAdjustmentQty(Float adjustmentQty) {
        this.adjustmentQty = adjustmentQty;
    }
    /**
     * @return the transferOutQty
     */
    public Float getTransferOutQty() {
        return transferOutQty;
    }
    /**
     * @param transferOutQty the transferOutQty to set
     */
    public void setTransferOutQty(Float transferOutQty) {
        this.transferOutQty = transferOutQty;
    }
    /**
     * @return the transferInQty
     */
    public Float getTransferInQty() {
        return transferInQty;
    }
    /**
     * @param transferInQty the transferInQty to set
     */
    public void setTransferInQty(Float transferInQty) {
        this.transferInQty = transferInQty;
    }
    /**
     * @return the inEsTime
     */
    public Date getInEsTime() {
        return inEsTime;
    }
    /**
     * @param inEsTime the inEsTime to set
     */
    public void setInEsTime(Date inEsTime) {
        this.inEsTime = inEsTime;
    }
    /**
     * @return the ReceiveCorrQty
     */
    public Float getReceiveCorrQty() {
        return receiveCorrQty;
    }
    /**
     * @param receiveCorrQty the receiveCorrQty to set
     */
    public void setReceiveCorrQty(Float receiveCorrQty) {
        this.receiveCorrQty = receiveCorrQty;
    }
    /**
     * @return the returnCorrQty
     */
    public Float getReturnCorrQty() {
        return returnCorrQty;
    }
    /**
     * @param returnCorrQty the returnCorrQty to set
     */
    public void setReturnCorrQty(Float returnCorrQty) {
        this.returnCorrQty = returnCorrQty;
    }
    /**
     * @return the transferOutCorrQty
     */
    public Float getTransferOutCorrQty() {
        return transferOutCorrQty;
    }
    /**
     * @param transferOutCorrQty the transferOutCorrQty to set
     */
    public void setTransferOutCorrQty(Float transferOutCorrQty) {
        this.transferOutCorrQty = transferOutCorrQty;
    }
    /**
     * @return the transferInCorrQty
     */
    public Float getTransferInCorrQty() {
        return transferInCorrQty;
    }
    /**
     * @param transferInCorrQty the transferInCorrQty to set
     */
    public void setTransferInCorrQty(Float transferInCorrQty) {
        this.transferInCorrQty = transferInCorrQty;
    }
    /**
     * @return the depCd
     */
    public String getDepCd() {
        return depCd;
    }
    /**
     * @param depCd the depCd to set
     */
    public void setDepCd(String depCd) {
        this.depCd = depCd;
    }
    /**
     * @return the posFlg
     */
    public String getPosFlg() {
        return posFlg;
    }
    /**
     * @param posFlg the posFlg to set
     */
    public void setPosFlg(String posFlg) {
        this.posFlg = posFlg;
    }
    /**
     * @return the createYmdHms
     */
    public String getCreateYmdHms() {
        return createYmdHms;
    }
    /**
     * @param createYmdHms the createYmdHms to set
     */
    public void setCreateYmdHms(String createYmdHms) {
        this.createYmdHms = createYmdHms;
    }
}
