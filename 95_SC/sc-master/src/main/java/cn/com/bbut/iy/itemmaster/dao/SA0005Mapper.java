package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.SA0005GenMapper;
import cn.com.bbut.iy.itemmaster.dto.sa0005.SA0005ParamDTO;

public interface SA0005Mapper extends SA0005GenMapper {
    int deleteSa005(SA0005ParamDTO sa0005);

    int inserttoSa0005(SA0005ParamDTO sa0005);
}