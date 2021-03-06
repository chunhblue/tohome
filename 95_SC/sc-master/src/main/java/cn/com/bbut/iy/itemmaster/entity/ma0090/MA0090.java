package cn.com.bbut.iy.itemmaster.entity.ma0090;

import lombok.*;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class MA0090 extends MA0090Key implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.category_name
     *
     * @mbggenerated
     */
    private String categoryName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.effective_sts
     *
     * @mbggenerated
     */
    private String effectiveSts;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.new_article_period
     *
     * @mbggenerated
     */
    private Integer newArticlePeriod;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.good_sale_dms
     *
     * @mbggenerated
     */
    private Integer goodSaleDms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.no_sale_day
     *
     * @mbggenerated
     */
    private Integer noSaleDay;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.create_user_id
     *
     * @mbggenerated
     */
    private String createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.create_ymd
     *
     * @mbggenerated
     */
    private String createYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.create_hms
     *
     * @mbggenerated
     */
    private String createHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.update_user_id
     *
     * @mbggenerated
     */
    private String updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.update_ymd
     *
     * @mbggenerated
     */
    private String updateYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.update_hms
     *
     * @mbggenerated
     */
    private String updateHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.update_screen_id
     *
     * @mbggenerated
     */
    private String updateScreenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.update_ip_address
     *
     * @mbggenerated
     */
    private String updateIpAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.nr_update_flg
     *
     * @mbggenerated
     */
    private String nrUpdateFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.nr_ymd
     *
     * @mbggenerated
     */
    private String nrYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma0090.nr_hms
     *
     * @mbggenerated
     */
    private String nrHms;
}