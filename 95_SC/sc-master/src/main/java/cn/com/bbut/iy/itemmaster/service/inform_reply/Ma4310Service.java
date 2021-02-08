package cn.com.bbut.iy.itemmaster.service.inform_reply;


import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310DetailParamDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4310ResultDto;
import cn.com.bbut.iy.itemmaster.dto.mRoleStore.MRoleStoreParam;
import cn.com.bbut.iy.itemmaster.entity.ma4310.MA4310;

import java.util.Collection;

public interface Ma4310Service {
    /**
     * 通报消息 回复一览
     * @param param
     * @return
     */
    GridDataDTO<Ma4310DetailGridDto> getReplyList(Ma4310DetailParamDto param);

    /**
     * 通报消息 回复详细信息
     * @param informCd
     * @param storeCd
     * @return
     */
    Ma4310ResultDto getInformReply(String informCd,String storeCd);
}
