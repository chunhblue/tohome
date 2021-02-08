package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 通报消息 门店范围/回复通知 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4310DetailGridDto extends GridDataDTO {
    /**
     * 通报cd
     */
    private String informCd;

    /**
     *店铺cd
     */
    private String storeCd;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 通报标题
     */
    private String informTitle;

    /**
     * 发布时间
     */
    private String createYmd;

    /**
     * 是否回复 1回复 0未回复
     */
    private String isReply;

}
