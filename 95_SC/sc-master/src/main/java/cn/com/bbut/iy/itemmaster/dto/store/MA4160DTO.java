package cn.com.bbut.iy.itemmaster.dto.store;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName JobStationDTO
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/5 12:04
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class MA4160DTO extends GridDataDTO {

    private String jobTypeCd;
    private String jobCatagoryName;
    private String effectiveSts;
    private String createUserId;
    private String createYmd;
    private String createHms;
    private String updateUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.update_ymd
     *
     * @mbg.generated
     */
    private String updateYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.update_hms
     *
     * @mbg.generated
     */
    private String updateHms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.update_screen_id
     *
     * @mbg.generated
     */
    private String updateScreenId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.update_ip_address
     *
     * @mbg.generated
     */
    private String updateIpAddress;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.nr_update_flg
     *
     * @mbg.generated
     */
    private String nrUpdateFlg;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.nr_ymd
     *
     * @mbg.generated
     */
    private String nrYmd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column ma4160.nr_hms
     *
     * @mbg.generated
     */
    private String nrHms;

    public MA4160DTO(List rows, long page, long records, long rowPerPage) {
        super(rows, page, records, rowPerPage);
    }
}