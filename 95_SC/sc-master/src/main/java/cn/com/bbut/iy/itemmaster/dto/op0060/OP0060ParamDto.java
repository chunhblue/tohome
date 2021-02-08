package cn.com.bbut.iy.itemmaster.dto.op0060;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

/**
 * OP0060 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OP0060ParamDto extends GridParamDTO {
    /**
     *业务开始日
     */
    private String startDate;
    /**
     *业务结束日
     */
    private String endDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.store_cd
     *
     * @mbg.generated
     */
    private String storeCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.delta_no
     *
     * @mbg.generated
     */
    private Integer deltaNo;
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.pay_person
     *
     * @mbg.generated
     */
    private String payPerson;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.description
     *
     * @mbg.generated
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.pay_amt
     *
     * @mbg.generated
     */
    private BigDecimal payAmt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.record_sts
     *
     * @mbg.generated
     */
    private String recordSts;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.create_user_id
     *
     * @mbg.generated
     */
    private String createUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.create_ymd
     *
     * @mbg.generated
     */
    private String createYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.create_hms
     *
     * @mbg.generated
     */
    private String createHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.update_user_id
     *
     * @mbg.generated
     */
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.update_ymd
     *
     * @mbg.generated
     */
    private String updateYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.update_hms
     *
     * @mbg.generated
     */
    private String updateHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.update_screen_id
     *
     * @mbg.generated
     */
    private String updateScreenId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.update_ip_address
     *
     * @mbg.generated
     */
    private String updateIpAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.nr_update_flg
     *
     * @mbg.generated
     */
    private String nrUpdateFlg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.nr_ymd
     *
     * @mbg.generated
     */
    private String nrYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column op0060.nr_hms
     *
     * @mbg.generated
     */
    private String nrHms;

    // 是否分页
    private boolean flg = true;

    /**
     * 业务日期
     */
    private String businessDate;

    /** 大区CD */
    private String regionCd;
    /** 城市CD */
    private String cityCd;
    /** 区域CD */
    private String districtCd;
    /** 权限包含的所有店铺编号 */
    private Collection<String> stores;
    private String ofc;
    private String ofcName;
    private String oc;
    private String ocName;
    private String om;
    private String omName;
}
