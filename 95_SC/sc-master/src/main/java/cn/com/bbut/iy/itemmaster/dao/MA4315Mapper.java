package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4315GenMapper;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4315DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4315DetailParamDto;

import java.util.List;

public interface MA4315Mapper extends MA4315GenMapper {
    /**
     * 通报消息 回复信息
     * @param param
     * @return
     */
    List<Ma4315DetailGridDto> getReplyList(Ma4315DetailParamDto param);

    long getReplyListCount(Ma4315DetailParamDto param);
}