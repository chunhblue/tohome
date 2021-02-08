package cn.com.bbut.iy.itemmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class SA0000 {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.pay_cd
     *
     * @mbggenerated
     */
    private String payCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.pay_name
     *
     * @mbggenerated
     */
    private String payName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.pay_name_print
     *
     * @mbggenerated
     */
    private String payNamePrint;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.pay_parameter
     *
     * @mbggenerated
     */
    private String payParameter;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.effective_sts
     *
     * @mbggenerated
     */
    private String effectiveSts;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.create_user_id
     *
     * @mbggenerated
     */
    private String createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.create_ymd
     *
     * @mbggenerated
     */
    private String createYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.create_hms
     *
     * @mbggenerated
     */
    private String createHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.update_user_id
     *
     * @mbggenerated
     */
    private String updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.update_ymd
     *
     * @mbggenerated
     */
    private String updateYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.update_hms
     *
     * @mbggenerated
     */
    private String updateHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.update_screen_id
     *
     * @mbggenerated
     */
    private String updateScreenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.update_ip_address
     *
     * @mbggenerated
     */
    private String updateIpAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.nr_update_flg
     *
     * @mbggenerated
     */
    private String nrUpdateFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.nr_ymd
     *
     * @mbggenerated
     */
    private String nrYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sa0000.nr_hms
     *
     * @mbggenerated
     */
    private String nrHms;

    private String interfaceSts;

    private String interfaceAddress;
}