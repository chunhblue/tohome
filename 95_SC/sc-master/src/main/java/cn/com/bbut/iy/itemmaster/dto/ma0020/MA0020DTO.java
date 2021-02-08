package cn.com.bbut.iy.itemmaster.dto.ma0020;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MA0020DTO extends Ma0020ParamDTO {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.structure_name
     *
     * @mbg.generated
     */
    // 获得子菜单集合 3/8
    private String structureName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.admin_structure_cd
     *
     * @mbg.generated
     */
    private String adminStructureCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.effective_sts
     *
     * @mbg.generated
     */
    private String effectiveSts;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.create_ymd
     *
     * @mbg.generated
     */
    private String createYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.create_hms
     *
     * @mbg.generated
     */
    private String createHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.update_user_id
     *
     * @mbg.generated
     */
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.update_ymd
     *
     * @mbg.generated
     */
    private String updateYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.update_hms
     *
     * @mbg.generated
     */
    private String updateHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.update_screen_id
     *
     * @mbg.generated
     */
    private String updateScreenId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.update_ip_address
     *
     * @mbg.generated
     */
    private String updateIpAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.nr_update_flg
     *
     * @mbg.generated
     */
    private String nrUpdateFlg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.nr_ymd
     *
     * @mbg.generated
     */
    private String nrYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.nr_hms
     *
     * @mbg.generated
     */
    private String nrHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.level
     *
     * @mbg.generated
     */
    private  String Level;

    private String structureLevel;

    private String adminStructureName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0020_c.structure_cd
     *
     * @mbg.generated
     */
    private String structureCd;

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getAdminStructureCd() {
        return adminStructureCd;
    }

    public void setAdminStructureCd(String adminStructureCd) {
        this.adminStructureCd = adminStructureCd;
    }

    public String getEffectiveSts() {
        return effectiveSts;
    }

    public void setEffectiveSts(String effectiveSts) {
        this.effectiveSts = effectiveSts;
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

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getStructureLevel() {
        return structureLevel;
    }

    public void setStructureLevel(String structureLevel) {
        this.structureLevel = structureLevel;
    }

    public String getAdminStructureName() {
        return adminStructureName;
    }

    public void setAdminStructureName(String adminStructureName) {
        this.adminStructureName = adminStructureName;
    }

    public String getStructureCd() {
        return structureCd;
    }

    public void setStructureCd(String structureCd) {
        this.structureCd = structureCd;
    }
}