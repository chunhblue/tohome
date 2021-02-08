package cn.com.bbut.iy.itemmaster.dto.paymentMode;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POS支付方式设定 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SA0000DetailParamDto extends GridParamDTO {
    /**
     *支付cd
     */
    private String payCd;

    /**
     *支付名称
     */
    private String payName;

    /**
     * 审核状态
     */
    private Integer reviewStatus;


}
