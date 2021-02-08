package cn.com.bbut.iy.itemmaster.entity.od8020;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class OD8020 extends OD8020Key {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.vendor_id
     *
     * @mbggenerated
     */
    private String vendorNameShort;
    private String vendorName;
    private String vendorId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.receive_unit_id
     *
     * @mbggenerated
     */
    private String receiveUnitId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.order_qty
     *
     * @mbggenerated
     */
    private BigDecimal orderQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.order_nocharge_qty
     *
     * @mbggenerated
     */
    private BigDecimal orderNochargeQty;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.order_price
     *
     * @mbggenerated
     */
    private BigDecimal orderPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.upload_flg
     *
     * @mbggenerated
     */
    private String uploadFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.upload_date
     *
     * @mbggenerated
     */
    private String uploadDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.create_user_id
     *
     * @mbggenerated
     */
    private String createUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.create_ymd
     *
     * @mbggenerated
     */
    private String createYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.create_hms
     *
     * @mbggenerated
     */
    private String createHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.update_user_id
     *
     * @mbggenerated
     */
    private String updateUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.update_ymd
     *
     * @mbggenerated
     */
    private String updateYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.update_hms
     *
     * @mbggenerated
     */
    private String updateHms;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.update_screen_id
     *
     * @mbggenerated
     */
    private String updateScreenId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.update_ip_address
     *
     * @mbggenerated
     */
    private String updateIpAddress;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.nr_update_flg
     *
     * @mbggenerated
     */
    private String nrUpdateFlg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.nr_ymd
     *
     * @mbggenerated
     */
    private String nrYmd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column od8020.nr_hms
     *
     * @mbggenerated
     */
    private String nrHms;
}