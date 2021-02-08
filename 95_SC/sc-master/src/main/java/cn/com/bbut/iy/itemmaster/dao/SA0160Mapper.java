package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.SA0160GenMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SA0160Mapper extends SA0160GenMapper {

    /**
     * 查询可选店铺信息
     *
     */
    List<AutoCompleteDTO> selectStoreList(@Param("v") String v);
}