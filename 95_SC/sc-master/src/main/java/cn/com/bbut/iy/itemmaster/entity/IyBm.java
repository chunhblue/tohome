package cn.com.bbut.iy.itemmaster.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class IyBm extends BaseEntity {
    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.COMPANY
     *
     * @mbg.generated
     */
    private String company;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.STORE
     *
     * @mbg.generated
     */
    private String store;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.DPT
     *
     * @mbg.generated
     */
    private String dpt;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_TYPE
     *
     * @mbg.generated
     */
    private String bmType;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_CODE
     *
     * @mbg.generated
     */
    private String bmCode;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_NAME
     *
     * @mbg.generated
     */
    private String bmName;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.ITEM_PRICE
     *
     * @mbg.generated
     */
    private Integer itemPrice;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_NUMBER
     *
     * @mbg.generated
     */
    private Integer bmNumber;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_PRICE
     *
     * @mbg.generated
     */
    private Integer bmPrice;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_EFF_FROM
     *
     * @mbg.generated
     */
    private Integer bmEffFrom;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_EFF_TO
     *
     * @mbg.generated
     */
    private Integer bmEffTo;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.ONLINE_DATE
     *
     * @mbg.generated
     */
    private Integer onlineDate;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_DISCOUNT_RATE
     *
     * @mbg.generated
     */
    private BigDecimal bmDiscountRate;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.BM_GROSS
     *
     * @mbg.generated
     */
    private Integer bmGross;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.DPT_ALL
     *
     * @mbg.generated
     */
    private String dptAll;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.NUM_A
     *
     * @mbg.generated
     */
    private Integer numA;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_BM.NUM_B
     *
     * @mbg.generated
     */
    private Integer numB;
}