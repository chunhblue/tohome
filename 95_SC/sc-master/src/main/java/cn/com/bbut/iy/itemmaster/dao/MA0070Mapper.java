package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA0070GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0070.MA0070;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MA0070Mapper extends MA0070GenMapper {
    List<AutoCompleteDTO> getListDepartMent(MA0070 ma0070);

    List<AutoCompleteDTO>  getListAll(String v);
}