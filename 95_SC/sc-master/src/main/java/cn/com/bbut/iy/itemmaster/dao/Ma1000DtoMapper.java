package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Ma1000DtoMapper {
    List<Ma1000DTO> selectStoreCd(Ma1000DTO ma1000DTO);
}
