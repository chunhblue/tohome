package cn.com.bbut.iy.itemmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MA1140 extends MA1140Key {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.effective_end_date
     *
     * @mbggenerated
     */
    private String effectiveEndDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.create_user_id
     *
     * @mbggenerated
     */
    private String createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.create_ymd
     *
     * @mbggenerated
     */
    private String createYMD;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.create_hms
     *
     * @mbggenerated
     */
    private String createHMS;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.update_user_id
     *
     * @mbggenerated
     */
    private String updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.update_ymd
     *
     * @mbggenerated
     */
    private String updateYMD;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.update_hms
     *
     * @mbggenerated
     */
    private String updateHMS;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.update_screen_id
     *
     * @mbggenerated
     */
    private String updateScreenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.update_ip_address
     *
     * @mbggenerated
     */
    private String updateIpAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.nr_update_flg
     *
     * @mbggenerated
     */
    private String nrUpdateFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.nr_ymd
     *
     * @mbggenerated
     */
    private String nrYMD;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.nr_hms
     *
     * @mbggenerated
     */
    private String nrHMS;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1140.is_default
     *
     * @mbggenerated
     */
    private String isDefault;
}