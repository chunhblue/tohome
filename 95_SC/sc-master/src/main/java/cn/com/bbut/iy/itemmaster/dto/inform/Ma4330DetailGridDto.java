package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 通报日志 grid
 *
 * @author zcz
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Ma4330DetailGridDto extends GridDataDTO {
    /**
     *通报编号
     */
    private String informCd;

    /**
     *通报标题
     */
    private String informTitle;

    /**
     * 店铺编号
     */
    private String storeCd;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 录入日期
     */
    private String createYmd;

    /**
     * 录入时间
     */
    private String createHms;

    /**
     * 录入人id
     */
    private String createUserId;

    /**
     * 录入人id
     */
    private String createUserName;

    /**
     * 录入人id
     */
    private String logTypeName;
}
