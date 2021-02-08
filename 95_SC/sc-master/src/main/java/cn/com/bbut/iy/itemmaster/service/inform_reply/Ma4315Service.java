package cn.com.bbut.iy.itemmaster.service.inform_reply;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4315DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4315DetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.MA4315;

import java.util.Collection;

public interface Ma4315Service {
    /**
     *  回复通知信息
     * @param param
     * @return
     */
    int insertInformReply(MA4315 param);


    /**
     * 通报消息 回复信息一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4315DetailGridDto> getReplyList(Ma4315DetailParamDto param, Collection<Integer> roleId);
}
