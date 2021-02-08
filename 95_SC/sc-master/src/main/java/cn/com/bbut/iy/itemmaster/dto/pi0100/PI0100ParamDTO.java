package cn.com.bbut.iy.itemmaster.dto.pi0100;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.Data;

import java.util.Collection;

@Data
public class PI0100ParamDTO extends GridParamDTO {
    private String piCd;
    private String piDate;
    private String piStartDate;
    private String piEndDate;
    private String piStatus;
    private String reviewStatus;
    // 分页
    private int limitStart;

    // 是否分页
    private boolean flg = true;

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

    public String getPiCd() {
        return piCd;
    }

    public void setPiCd(String piCd) {
        this.piCd = piCd;
    }

    public String getPiStartDate() {
        return piStartDate;
    }

    public void setPiStartDate(String piStartDate) {
        this.piStartDate = piStartDate;
    }

    public String getPiEndDate() {
        return piEndDate;
    }

    public void setPiEndDate(String piEndDate) {
        this.piEndDate = piEndDate;
    }

    public String getPiStatus() {
        return piStatus;
    }

    public void setPiStatus(String piStatus) {
        this.piStatus = piStatus;
    }

    @Override
    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
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

    public boolean getFlg() {
        return flg;
    }

    public void setFlg(boolean flg) {
        this.flg = flg;
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
}
