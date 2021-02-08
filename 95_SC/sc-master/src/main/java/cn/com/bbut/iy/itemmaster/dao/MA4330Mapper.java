package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4330GenMapper;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4330DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4330DetailParamDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MA4330Mapper extends MA4330GenMapper {

    /**
     * 通报日志一览
     * @param param
     * @return
     */
    List<Ma4330DetailGridDto> getList(Ma4330DetailParamDto param);

    long getListCount(Ma4330DetailParamDto param);
}