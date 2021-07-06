package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 通报消息 回复信息 grid
 *
 * @author zcz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4315DetailGridDto extends GridDataDTO {
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
     * 回复时间
     */
//    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT+8")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date informReplyDate;

    /**
     * 回复内容
     */
    private String informReplyContent;

    /**
     * 发布时间
     */
    private String createYmd;

    /**
     * 回复用户id
     */
    private String userId;

    /**
     * 回复用户名称
     */
    private String userName;

}
