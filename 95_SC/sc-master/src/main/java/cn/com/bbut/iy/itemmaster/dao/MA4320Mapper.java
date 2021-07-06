package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4320GenMapper;
import cn.com.bbut.iy.itemmaster.entity.MA4320;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MA4320Mapper extends MA4320GenMapper {
    String getNowDate();
	
	Integer insertMA4320All(@Param("ma4320List") List<MA4320> ma4320List);
}