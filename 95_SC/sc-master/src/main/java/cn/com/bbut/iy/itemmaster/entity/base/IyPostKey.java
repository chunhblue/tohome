package cn.com.bbut.iy.itemmaster.entity.base;

import lombok.Data;
import cn.com.bbut.iy.itemmaster.entity.BaseEntity;

@Data
public class IyPostKey extends BaseEntity {
    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_POST.COMPANY
     *
     * @mbg.generated
     */
    private String company;

    /**
     *
     * This field was generated by MyBatis Generator. This field corresponds to
     * the database column IY_POST.POST_CODE
     *
     * @mbg.generated
     */
    private String postCode;
}