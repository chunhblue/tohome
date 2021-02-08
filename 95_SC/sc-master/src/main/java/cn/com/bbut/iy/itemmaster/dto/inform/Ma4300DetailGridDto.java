package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 通报消息 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4300DetailGridDto extends GridDataDTO {
    /**
     *通报编号
     */
    private String informCd;

    /**
     *通报标题
     */
    private String informTitle;

    /**
     * 发送范围
     */
    private String sendScope;

    /**
     * 发送范围 显示名称
     */
    private String sendScopeName;

    /**
     * 录入日期
     */
    private String createYmd;

    /**
     * 通报显示日期 开始
     */
    private String informStartDate;

    /**
     * 通报显示日期 结束
     */
    private String informEndDate;
}
