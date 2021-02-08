package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4340GenMapper;
import cn.com.bbut.iy.itemmaster.dto.ma4340.Ma4340DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4340.Ma4340DetailParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MA4340Mapper extends MA4340GenMapper {
    /**
     * 新品信息一览
     * @param param
     * @return
     */
    List<Ma4340DetailGridDto> getList(Ma4340DetailParamDto param);

    long getListCount(Ma4340DetailParamDto param);
}