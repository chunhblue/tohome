package cn.com.bbut.iy.itemmaster.entity;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class IymDefAssignment extends BaseEntity {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.ID
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.DPT
     *
     * @mbg.generated
     */
    private String dpt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.POST_CODE
     *
     * @mbg.generated
     */
    private String postCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.OCCUP_CODE
     *
     * @mbg.generated
     */
    private String occupCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.STORE
     *
     * @mbg.generated
     */
    private String store;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.ROLE_ID
     *
     * @mbg.generated
     */
    private Integer roleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.UPDATE_USERID
     *
     * @mbg.generated
     */
    private String updateUserid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_M_DEF_ASSIGNMENT.UPDATE_TIME
     *
     * @mbg.generated
     */
    private Date updateTime;
}