package cn.com.bbut.iy.itemmaster.dto.od0010;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName od10010viewDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/6/16 13:44
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class od0010viewDTO extends GridParamDTO {
    private boolean flg = true;

    /** 业务日期 */
    private String businessDate;

    private String  vendorId;
    private String  warehouseName;

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


    private String orderQty;


    private String orderNochargeQty;


    private String orderPrice;


    private String orderAmt;


    private String orderAmtNotax;


    private String orderTax;


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
    private String vendorName;
    private String storeName;

    private String gQuantity;

    private String receivingDifferences;

    public boolean isFlg() {
        return flg;
    }

    public void setFlg(boolean flg) {
        this.flg = flg;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public List<ResourceViewDTO> getResources() {
        return resources;
    }

    public void setResources(List<ResourceViewDTO> resources) {
        this.resources = resources;
    }

    public String getRegionCd() {
        return regionCd;
    }

    public void setRegionCd(String regionCd) {
        this.regionCd = regionCd;
    }

    public String getCityCd() {
        return cityCd;
    }

    public void setCityCd(String cityCd) {
        this.cityCd = cityCd;
    }

    public String getDistrictCd() {
        return districtCd;
    }

    public void setDistrictCd(String districtCd) {
        this.districtCd = districtCd;
    }

    public String getStoreCd() {
        return storeCd;
    }

    public void setStoreCd(String storeCd) {
        this.storeCd = storeCd;
    }

    public Collection<String> getStores() {
        return stores;
    }

    public void setStores(Collection<String> stores) {
        this.stores = stores;
    }

    public String getOrderDirectSupplierDateStartDate() {
        return orderDirectSupplierDateStartDate;
    }

    public void setOrderDirectSupplierDateStartDate(String orderDirectSupplierDateStartDate) {
        this.orderDirectSupplierDateStartDate = orderDirectSupplierDateStartDate;
    }

    public String getOrderDirectSupplierDateEndDate() {
        return orderDirectSupplierDateEndDate;
    }

    public void setOrderDirectSupplierDateEndDate(String orderDirectSupplierDateEndDate) {
        this.orderDirectSupplierDateEndDate = orderDirectSupplierDateEndDate;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }

    public String getOrderDifferentiate() {
        return orderDifferentiate;
    }

    public void setOrderDifferentiate(String orderDifferentiate) {
        this.orderDifferentiate = orderDifferentiate;
    }

    public String getOptionTime() {
        return optionTime;
    }

    public void setOptionTime(String optionTime) {
        this.optionTime = optionTime;
    }

    public String getSearchJson() {
        return searchJson;
    }

    public void setSearchJson(String searchJson) {
        this.searchJson = searchJson;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(String orderUnit) {
        this.orderUnit = orderUnit;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderNochargeQty() {
        return orderNochargeQty;
    }

    public void setOrderNochargeQty(String orderNochargeQty) {
        this.orderNochargeQty = orderNochargeQty;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getOrderAmtNotax() {
        return orderAmtNotax;
    }

    public void setOrderAmtNotax(String orderAmtNotax) {
        this.orderAmtNotax = orderAmtNotax;
    }

    public String getOrderTax() {
        return orderTax;
    }

    public void setOrderTax(String orderTax) {
        this.orderTax = orderTax;
    }

    public String getReceivePrice() {
        return receivePrice;
    }

    public void setReceivePrice(String receivePrice) {
        this.receivePrice = receivePrice;
    }

    public String getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(String receiveQty) {
        this.receiveQty = receiveQty;
    }

    public String getReceiveNoChargeQty() {
        return receiveNoChargeQty;
    }

    public void setReceiveNoChargeQty(String receiveNoChargeQty) {
        this.receiveNoChargeQty = receiveNoChargeQty;
    }

    public String getReceiveTotalQty() {
        return receiveTotalQty;
    }

    public void setReceiveTotalQty(String receiveTotalQty) {
        this.receiveTotalQty = receiveTotalQty;
    }

    public String getReceiveTotalAmt() {
        return receiveTotalAmt;
    }

    public void setReceiveTotalAmt(String receiveTotalAmt) {
        this.receiveTotalAmt = receiveTotalAmt;
    }

    public String getReceiveTotalAmtNotax() {
        return receiveTotalAmtNotax;
    }

    public void setReceiveTotalAmtNotax(String receiveTotalAmtNotax) {
        this.receiveTotalAmtNotax = receiveTotalAmtNotax;
    }

    public String getReceiveTax() {
        return receiveTax;
    }

    public void setReceiveTax(String receiveTax) {
        this.receiveTax = receiveTax;
    }

    public String getUploadFlg() {
        return uploadFlg;
    }

    public void setUploadFlg(String uploadFlg) {
        this.uploadFlg = uploadFlg;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateYmd() {
        return createYmd;
    }

    public void setCreateYmd(String createYmd) {
        this.createYmd = createYmd;
    }

    public String getCreateHms() {
        return createHms;
    }

    public void setCreateHms(String createHms) {
        this.createHms = createHms;
    }

    @Override
    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateYmd() {
        return updateYmd;
    }

    public void setUpdateYmd(String updateYmd) {
        this.updateYmd = updateYmd;
    }

    public String getUpdateHms() {
        return updateHms;
    }

    public void setUpdateHms(String updateHms) {
        this.updateHms = updateHms;
    }

    public String getUpdateScreenId() {
        return updateScreenId;
    }

    public void setUpdateScreenId(String updateScreenId) {
        this.updateScreenId = updateScreenId;
    }

    public String getUpdateIpAddress() {
        return updateIpAddress;
    }

    public void setUpdateIpAddress(String updateIpAddress) {
        this.updateIpAddress = updateIpAddress;
    }

    public String getNrUpdateFlg() {
        return nrUpdateFlg;
    }

    public void setNrUpdateFlg(String nrUpdateFlg) {
        this.nrUpdateFlg = nrUpdateFlg;
    }

    public String getNrYmd() {
        return nrYmd;
    }

    public void setNrYmd(String nrYmd) {
        this.nrYmd = nrYmd;
    }

    public String getNrHms() {
        return nrHms;
    }

    public void setNrHms(String nrHms) {
        this.nrHms = nrHms;
    }

    public String getReasonId() {
        return reasonId;
    }

    public void setReasonId(String reasonId) {
        this.reasonId = reasonId;
    }

    public BigDecimal getAdjustAmt() {
        return adjustAmt;
    }

    public void setAdjustAmt(BigDecimal adjustAmt) {
        this.adjustAmt = adjustAmt;
    }

    public BigDecimal getAdjustQty() {
        return adjustQty;
    }

    public void setAdjustQty(BigDecimal adjustQty) {
        this.adjustQty = adjustQty;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderSpecifi() {
        return orderSpecifi;
    }

    public void setOrderSpecifi(String orderSpecifi) {
        this.orderSpecifi = orderSpecifi;
    }

    public String getLowestOrder() {
        return lowestOrder;
    }

    public void setLowestOrder(String lowestOrder) {
        this.lowestOrder = lowestOrder;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getgQuantity() {
        return gQuantity;
    }

    public void setgQuantity(String gQuantity) {
        this.gQuantity = gQuantity;
    }

    public String getReceivingDifferences() {
        return receivingDifferences;
    }

    public void setReceivingDifferences(String receivingDifferences) {
        this.receivingDifferences = receivingDifferences;
    }
}
