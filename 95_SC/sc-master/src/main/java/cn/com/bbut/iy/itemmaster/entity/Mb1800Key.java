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
public class Mb1800Key {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mb1800.store_cd
     *
     * @mbg.generated
     */
    private String storeCd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mb1800.store_name
     *
     * @mbg.generated
     */
    private String storeName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mb1800.barcode
     *
     * @mbg.generated
     */
    private String barcode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column mb1800.trans_date
     *
     * @mbg.generated
     */
    private Date transDate;
}