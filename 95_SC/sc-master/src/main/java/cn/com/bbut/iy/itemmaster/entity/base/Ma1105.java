package cn.com.bbut.iy.itemmaster.entity.base;

import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.role.ResourceViewDTO;
import cn.com.bbut.iy.itemmaster.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Ma1105 extends BaseEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.desc_g
     *
     * @mbg.generated
     */
    private String descG;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.loc
     *
     * @mbg.generated
     */
    private String loc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.item_code
     *
     * @mbg.generated
     */
    private String itemCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.product_name
     *
     * @mbg.generated
     */
    private String productName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.v_facing
     *
     * @mbg.generated
     */
    private String vFacing;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.h_facing
     *
     * @mbg.generated
     */
    private String hFacing;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.d_facing
     *
     * @mbg.generated
     */
    private String dFacing;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.total_facing
     *
     * @mbg.generated
     */
    private String totalFacing;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.shelf
     *
     * @mbg.generated
     */
    private String shelf;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.sub_shelf
     *
     * @mbg.generated
     */
    private String subShelf;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.create_date
     *
     * @mbg.generated
     */
    private String createDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1105.store_cd
     *
     * @mbg.generated
     */

    private String storeName;

    private String depCd;

    private String pmaCd;

    private String categoryCd;

    private String subCategoryCd;

    private String depName;

    private String pmaName;

    private String categoryName;

    private String subCategoryName;

    private String businessDate;

    private String storeTypeCd;

    private String storeTypeName;

    private String storeGroupName;

    private String maCd;

    private String maName;

    private int limitStart;

    private int limitEnd;

    private String orderByClause;

    private String pogCd;
    // 上传excel名称
    private String excelName;

    // 用户信息
    private CommonDTO commonDTO;

    // 是否分页
    private boolean flg = true;

    /**
     * 角色资源
     */
    private List<ResourceViewDTO> resources;

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
}