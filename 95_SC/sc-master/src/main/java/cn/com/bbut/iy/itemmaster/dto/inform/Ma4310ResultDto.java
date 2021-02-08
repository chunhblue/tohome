package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 通报消息 回复通知信息
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4310ResultDto{
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
     * 通报内容
     */
    private String informContent;

    /**
     * 回复时间
     */
    private String informReplyDate;

    /**
     * 回复内容
     */
    private String informReplyContent;

}
