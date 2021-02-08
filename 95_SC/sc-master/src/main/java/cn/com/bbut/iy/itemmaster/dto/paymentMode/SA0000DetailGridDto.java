package cn.com.bbut.iy.itemmaster.dto.paymentMode;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * POS支付方式设定 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SA0000DetailGridDto extends GridDataDTO {
    /**
     *支付cd
     */
    private String payCd;

    /**
     *支付名称
     */
    private String payName;

    /**
     * 打印名称
     */
    private String payNamePrint;

    /**
     * 是否调用外部接口
     */
    private String interfaceSts;

    /**
     * 外部接口地址
     */
    private String interfaceAddress;

    /**
     * 打印名称
     */
    private String printName;

    /**
     * 排序
     */
    private String paySeq;

    /**
     * 修改时间
     */
    private String updateDate;

    /**
     * 审核状态
     */
    private String reviewStatus;

    /**
     * 操作员
     */
    private String updateUserId;
    private String updateUserName;
}
