package cn.com.bbut.iy.itemmaster.dto.fsInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PI0110DTOD {

    private String piCd;
    private String piDate;
//    private String piTypeCode;
//    private String piTypeName;
    private String pmaCd;
    private String pmaName;
    private String createYmd;
    private String createHms;
    private String createUserId;

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

    public String getPmaCd() {
        return pmaCd;
    }

    public void setPmaCd(String pmaCd) {
        this.pmaCd = pmaCd;
    }

    public String getPmaName() {
        return pmaName;
    }

    public void setPmaName(String pmaName) {
        this.pmaName = pmaName;
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
}
