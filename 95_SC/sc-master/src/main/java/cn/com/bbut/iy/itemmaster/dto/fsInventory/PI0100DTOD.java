package cn.com.bbut.iy.itemmaster.dto.fsInventory;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PI0100DTOD extends GridDataDTO {

    private String piCd;
    private String piDate;
    private String storeCd;
    private String piType;
    private String piMethod;
    private String piStatus;
    private String piStartTime;
    private String piEndTime;
    private String remarks;
    private String createYmd;
    private String createHms;
    private String createUserId;
    private String flag;
    private String storeName;
    private String updateUserId;
    private String updateYmd;
    private String updateHms;

    // 经费
  private String expenseAmt;

    public String getExpenseAmt() {
        return expenseAmt;
    }

    public void setExpenseAmt(String expenseAmt) {
        this.expenseAmt = expenseAmt;
    }

    // 明细
    private List<PI0110DTOD> details;

    // 商品明细
    private List<StocktakeItemDTOD> itemList;

    public PI0100DTOD(List rows, long page, long records, long rowPerPage) {
        super(rows, page, records, rowPerPage);
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

    public String getStoreCd() {
        return storeCd;
    }

    public void setStoreCd(String storeCd) {
        this.storeCd = storeCd;
    }

    public String getPiType() {
        return piType;
    }

    public void setPiType(String piType) {
        this.piType = piType;
    }

    public String getPiMethod() {
        return piMethod;
    }

    public void setPiMethod(String piMethod) {
        this.piMethod = piMethod;
    }

    public String getPiStatus() {
        return piStatus;
    }

    public void setPiStatus(String piStatus) {
        this.piStatus = piStatus;
    }

    public String getPiStartTime() {
        return piStartTime;
    }

    public void setPiStartTime(String piStartTime) {
        this.piStartTime = piStartTime;
    }

    public String getPiEndTime() {
        return piEndTime;
    }

    public void setPiEndTime(String piEndTime) {
        this.piEndTime = piEndTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public List<PI0110DTOD> getDetails() {
        return details;
    }

    public void setDetails(List<PI0110DTOD> details) {
        this.details = details;
    }

    public List<StocktakeItemDTOD> getItemList() {
        return itemList;
    }

    public void setItemList(List<StocktakeItemDTOD> itemList) {
        this.itemList = itemList;
    }
}
