package cn.com.bbut.iy.itemmaster.dto.ma0020;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @ClassName Ma0020ParamDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/9 15:06
 * @Version 1.0
 */

public class Ma0020ParamDTO  extends GridParamDTO {


    private int limitStart;
    private  String Level;

//    public String getSearchJson() {
//        return searchJson;
//    }
//
//    public void setSearchJson(String searchJson) {
//        this.searchJson = searchJson;
//    }

    /**
     * text
     */
    @SuppressWarnings("unused")


    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column ma0020.STRUCTURE_LEVEL
     *
     * @mbggenerated Sat Jan 04 19:22:07 CST 2014
     */
    private String  structureLevel;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column ma0020.STRUCTURE_CD
     *
     * @mbggenerated Sat Jan 04 19:22:07 CST 2014
     */
    private String structureCd;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column ma0020.STRUCTURE_NAME
     *
     * @mbggenerated Sat Jan 04 19:22:07 CST 2014
     */
    private String structureName;

    /**
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column ma0020.ADMIN_STRUCTURE_CD
     *
     * @mbggenerated Sat Jan 04 19:22:07 CST 2014
     */
    private String adminStructureCd;

    private String effectiveSts;

    @Override
    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
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

    public String getStructureCd() {
        return structureCd;
    }

    public void setStructureCd(String structureCd) {
        this.structureCd = structureCd;
    }

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
}
