package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4305GenMapper;
import cn.com.bbut.iy.itemmaster.dto.inform.Ma4305DetailGridDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MA4305Mapper extends MA4305GenMapper {
    List<Ma4305DetailGridDto> getList(@Param("informCd") String informCd);
}