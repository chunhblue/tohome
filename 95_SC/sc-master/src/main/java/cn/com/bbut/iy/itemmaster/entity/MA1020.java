package cn.com.bbut.iy.itemmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MA1020 extends MA1020Key {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.effective_end_date
     *
     * @mbg.generated
     */
    private String effectiveEndDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.additional_attribure_value
     *
     * @mbg.generated
     */
    private String additionalAttribureValue;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.create_ymd
     *
     * @mbg.generated
     */
    private String createYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.create_hms
     *
     * @mbg.generated
     */
    private String createHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.update_user_id
     *
     * @mbg.generated
     */
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.update_ymd
     *
     * @mbg.generated
     */
    private String updateYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.update_hms
     *
     * @mbg.generated
     */
    private String updateHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.update_screen_id
     *
     * @mbg.generated
     */
    private String updateScreenId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.update_ip_address
     *
     * @mbg.generated
     */
    private String updateIpAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.nr_update_flg
     *
     * @mbg.generated
     */
    private String nrUpdateFlg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.nr_ymd
     *
     * @mbg.generated
     */
    private String nrYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma1020.nr_hms
     *
     * @mbg.generated
     */
    private String nrHms;
}