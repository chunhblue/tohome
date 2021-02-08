package cn.com.bbut.iy.itemmaster.dto.paymentMode;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * POS支付方式详细 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SA0005DetailGridDto extends GridDataDTO {
    /**
     * 支付cd
     */
    private String payCd;

    /**
     *店铺cd
     */
    private String storeCd;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * pos Id
     */
    private String posId;

    /**
     * pos name
     */
    private String posName;

    /**
     * pos 是否显示
     */
    private String posDisplay;

    /**
     * pos 是否显示
     */
    private String posDisplayName;
    /**
     * 创建日期
     */
    private String  createYmd;
    /**
     *创建人员ID
     */
    private String createUserId;
    /**
     * 创建人员名字
     */
    private String empName;
    /**
     * 创建时分秒
     */
    private String createHms;
    /**
     * 信息状态
     */
    private String recordStatus;
}
