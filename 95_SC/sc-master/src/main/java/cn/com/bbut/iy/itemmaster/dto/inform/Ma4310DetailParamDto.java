package cn.com.bbut.iy.itemmaster.dto.inform;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridParamDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

/**
 * 通报消息 门店范围/回复通知 param
 *
 * @author zcz
 * @date:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ma4310DetailParamDto extends GridParamDTO {
    /**
     *通报编号
     */
    private String informCd;

    /**
     *通报标题
     */
    private String informTitle;

    /**
     * 查看日期 开始
     */
    private String startDate;

    /**
     * 查看日期 结束
     */
    private String endDate;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 是否回复 1回复 0未回复
     */
    private String isReply;

    /**
     * 店铺权限列表
     */
    private Collection<String> storeList;

    /**
     * 角色列表
     */
    private Collection<Integer> roleList;

    /**
     * 业务时间
     */
    private String businessDate;
}
