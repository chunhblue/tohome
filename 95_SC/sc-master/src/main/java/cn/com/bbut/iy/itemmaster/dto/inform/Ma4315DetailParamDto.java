package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 通报消息 回复信息 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4315DetailParamDto extends GridParamDTO {
    /**
     *通报编号
     */
    private String informCd;

    /**
     *通报标题
     */
    private String informTitle;

    /**
     * 店铺cd
     */
    private String storeCd;

    /**
     * 查看日期 开始
     */
    private String startDate;

    /**
     * 查看日期 结束
     */
    private String endDate;

    /**
     * 店铺权限列表
     */
    private List<AutoCompleteDTO> storeList;

    /**
     * 角色列表
     */
    private Collection<Integer> roleList;


    /**
     * 业务时间
     */
    private String businessDate;

    /**
     * 是否为回复
     */
    private String informReply;

    /**
     * 用户id
     */
    private String userId;
}
