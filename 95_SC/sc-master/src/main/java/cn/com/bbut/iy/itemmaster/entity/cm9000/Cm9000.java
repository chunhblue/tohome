package cn.com.bbut.iy.itemmaster.entity.cm9000;

import cn.com.bbut.iy.itemmaster.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Cm9000 extends BaseEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.code_type
     *
     * @mbg.generated
     */
    private String codeType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.code_type_name
     *
     * @mbg.generated
     */
    private String codeTypeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.digit
     *
     * @mbg.generated
     */
    private Integer digit;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.effective_sts
     *
     * @mbg.generated
     */
    private String effectiveSts;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.create_user_id
     *
     * @mbg.generated
     */
    private String create_user_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.create_ymd
     *
     * @mbg.generated
     */
    private String create_ymd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.create_hms
     *
     * @mbg.generated
     */
    private String create_hms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.update_user_id
     *
     * @mbg.generated
     */
    private String update_user_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.update_ymd
     *
     * @mbg.generated
     */
    private String update_ymd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.update_hms
     *
     * @mbg.generated
     */
    private String update_hms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.update_screen_id
     *
     * @mbg.generated
     */
    private String update_screen_id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.update_ip_address
     *
     * @mbg.generated
     */
    private String update_ip_address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.nr_update_flg
     *
     * @mbg.generated
     */
    private String nr_update_flg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.nr_ymd
     *
     * @mbg.generated
     */
    private String nr_ymd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cm9000.nr_hms
     *
     * @mbg.generated
     */
    private String nr_hms;
}