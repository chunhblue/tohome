package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA1140GenMapper;
import org.apache.ibatis.annotations.Param;

public interface MA1140Mapper extends MA1140GenMapper {
    String selectBybarcode(@Param("barcode") String barcode);
}