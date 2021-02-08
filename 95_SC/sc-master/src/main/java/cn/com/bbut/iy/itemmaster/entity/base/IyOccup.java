package cn.com.bbut.iy.itemmaster.entity.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class IyOccup extends IyOccupKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column IY_OCCUP.OCCUP_NAME
     *
     * @mbg.generated
     */
    private String occupName;
}