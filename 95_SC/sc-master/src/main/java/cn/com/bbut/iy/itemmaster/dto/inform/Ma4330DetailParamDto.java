package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通报日志 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4330DetailParamDto extends GridParamDTO {
    /**
     *通报编号
     */
    private String informCd;

    /**
     *通报标题
     */
    private String informTitle;

    /**
     * 日志类型
     */
    private String logType;

    /**
     * 录入日期 开始
     */
    private String startDate;

    /**
     * 录入日期 结束
     */
    private String endDate;

    /**
     * 业务日期
     */
    private String businessDate;
}
