package cn.com.bbut.iy.itemmaster.dto.ma4170;

import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 门店原因 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4170DetailParamDto extends GridParamDTO {
    /**
     *原因cd
     */
    private String reasonCd;

    /**
     *原因名称
     */
    private String reasonName;

    /**
     * 原因类型
     */
    private String reasonType;

}
