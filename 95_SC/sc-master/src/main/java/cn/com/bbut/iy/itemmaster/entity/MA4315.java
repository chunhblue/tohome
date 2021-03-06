package cn.com.bbut.iy.itemmaster.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class MA4315 extends MA4315Key {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.store_cd
     *
     * @mbg.generated
     */
    private String storeCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.inform_reply_content
     *
     * @mbg.generated
     */
    private String informReplyContent;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.inform_reply_date
     *
     * @mbg.generated
     */
    private Date informReplyDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.reply_user_id
     *
     * @mbg.generated
     */
    private String replyUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.create_ymd
     *
     * @mbg.generated
     */
    private String createYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.create_hms
     *
     * @mbg.generated
     */
    private String createHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.update_user_id
     *
     * @mbg.generated
     */
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.update_ymd
     *
     * @mbg.generated
     */
    private String updateYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.update_hms
     *
     * @mbg.generated
     */
    private String updateHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.update_screen_id
     *
     * @mbg.generated
     */
    private String updateScreenId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.update_ip_address
     *
     * @mbg.generated
     */
    private String updateIpAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.nr_update_flg
     *
     * @mbg.generated
     */
    private String nrUpdateFlg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.nr_ymd
     *
     * @mbg.generated
     */
    private String nrYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4315.nr_hms
     *
     * @mbg.generated
     */
    private String nrHms;
}