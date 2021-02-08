package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.cm9070.Cm9070DTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CM9070Mapper {

    Cm9070DTO Select(String asType);

    int Update(Cm9070DTO record);
}
