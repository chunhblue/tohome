package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MA5321Mapper {

    /**
     * 查询待选
     * @param v
     */
    List<AutoCompleteDTO> selectWarehouse(@Param("v") String v);

}
