package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA0080GenMapper;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MA0080Mapper extends MA0080GenMapper {
	 List<MA0080> selectBigCatage(MA0080 ma0080);
}