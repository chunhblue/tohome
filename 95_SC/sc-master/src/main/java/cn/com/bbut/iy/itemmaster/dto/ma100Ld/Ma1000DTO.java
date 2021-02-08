package cn.com.bbut.iy.itemmaster.dto.ma100Ld;

import cn.com.bbut.iy.itemmaster.entity.base.Ma1000Key;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Ma1000DTO extends Ma1000Key {

    private String StoreCd;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.effective_end_date
     *
     * @mbg.generated
     */
    private String effectiveStartDate;

    private  String effectiveEndDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_name
     *
     * @mbg.generated
     */
    private String storeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_name_short
     *
     * @mbg.generated
     */
    private String storeNameShort;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.corp_cd
     *
     * @mbg.generated
     */
    private String corpCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.zo_cd
     *
     * @mbg.generated
     */
    private String zoCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.do_cd
     *
     * @mbg.generated
     */
    private String doCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.ofc
     *
     * @mbg.generated
     */
    private String ofc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.ma_cd
     *
     * @mbg.generated
     */
    private String maCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.structure_cd
     *
     * @mbg.generated
     */
    private String structureCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_type_cd
     *
     * @mbg.generated
     */
    private String storeTypeCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.open_date
     *
     * @mbg.generated
     */
    private String openDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.close_date
     *
     * @mbg.generated
     */
    private String closeDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.renovation_start_date
     *
     * @mbg.generated
     */
    private String renovationStartDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.renovation_end_date
     *
     * @mbg.generated
     */
    private String renovationEndDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.original_open_date
     *
     * @mbg.generated
     */
    private String original_open_date;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.old_store_cd
     *
     * @mbg.generated
     */
    private String old_store_cd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_owner_name
     *
     * @mbg.generated
     */
    private String storeOwnerName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.district_cd
     *
     * @mbg.generated
     */
    private String districtCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_address_1
     *
     * @mbg.generated
     */
    private String storeAddress1;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_address_2
     *
     * @mbg.generated
     */
    private String storeAddress2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_zip_cd
     *
     * @mbg.generated
     */
    private String storeZipCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_phone_no
     *
     * @mbg.generated
     */
    private String storePhoneNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_fax_no
     *
     * @mbg.generated
     */
    private String storeFaxNo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_phone_no2
     *
     * @mbg.generated
     */
    private String storePhoneNo2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_fax_no2
     *
     * @mbg.generated
     */
    private String storeFaxNo2;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.license_type
     *
     * @mbg.generated
     */
    private String licenseType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_scope
     *
     * @mbg.generated
     */
    private String storeScope;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.store_display_type
     *
     * @mbg.generated
     */
    private String store_display_type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.selling_floor_space
     *
     * @mbg.generated
     */
    private BigDecimal sellingFloorSpace;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.backroom_area
     *
     * @mbg.generated
     */
    private BigDecimal backroomArea;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.num_of_pos
     *
     * @mbg.generated
     */
    private BigDecimal numOfPos;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.num_of_scale
     *
     * @mbg.generated
     */
    private BigDecimal numOfScale;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.num_of_label
     *
     * @mbg.generated
     */
    private BigDecimal numOfLabel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.biz_hours_from
     *
     * @mbg.generated
     */
    private String bizHoursFrom;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.biz_hours_to
     *
     * @mbg.generated
     */
    private String bizHoursTo;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.shelves_type
     *
     * @mbg.generated
     */
    private String shelvesType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.create_ymd
     *
     * @mbg.generated
     */
    private String createYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.create_hms
     *
     * @mbg.generated
     */
    private String createHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.update_user_id
     *
     * @mbg.generated
     */
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.update_ymd
     *
     * @mbg.generated
     */
    private String update_ymd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.update_hms
     *
     * @mbg.generated
     */
    private String update_hms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.update_screen_id
     *
     * @mbg.generated
     */
    private String update_screen_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.update_ip_address
     *
     * @mbg.generated
     */
    private String update_ip_address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.nr_update_flg
     *
     * @mbg.generated
     */
    private String nr_update_flg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.nr_ymd
     *
     * @mbg.generated
     */
    private String nr_ymd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1000.nr_hms
     *
     * @mbg.generated
     */
    private String nr_hms;
    private String v;
    private String k;
}