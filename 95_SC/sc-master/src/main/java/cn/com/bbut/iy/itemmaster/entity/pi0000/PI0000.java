package cn.com.bbut.iy.itemmaster.entity.pi0000;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class PI0000 extends PI0000Key {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.pi_type
     *
     * @mbggenerated
     */
    private String piType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.pi_account_flg
     *
     * @mbggenerated
     */
    private String piAccountFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.pi_first_finish_flg
     *
     * @mbggenerated
     */
    private String piFirstFinishFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.pi_commit_flg
     *
     * @mbggenerated
     */
    private String piCommitFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.pi_status
     *
     * @mbggenerated
     */
    private String piStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.article_count
     *
     * @mbggenerated
     */
    private Integer articleCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.upload_flg
     *
     * @mbggenerated
     */
    private String uploadFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.upload_date
     *
     * @mbggenerated
     */
    private String uploadDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.create_user_id
     *
     * @mbggenerated
     */
    private String createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.create_ymd
     *
     * @mbggenerated
     */
    private String createYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.create_hms
     *
     * @mbggenerated
     */
    private String createHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.update_user_id
     *
     * @mbggenerated
     */
    private String updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.update_ymd
     *
     * @mbggenerated
     */
    private String updateYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.update_hms
     *
     * @mbggenerated
     */
    private String updateHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.update_screen_id
     *
     * @mbggenerated
     */
    private String updateScreenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.update_ip_address
     *
     * @mbggenerated
     */
    private String updateIpAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.nr_update_flg
     *
     * @mbggenerated
     */
    private String nrUpdateFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.nr_ymd
     *
     * @mbggenerated
     */
    private String nrYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pi0000.nr_hms
     *
     * @mbggenerated
     */
    private String nrHms;
}