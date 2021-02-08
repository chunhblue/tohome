package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.ma1200.MA1200DTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface Ma1200Mapper {
 
    /**
     * 主键查询
     * @param dto
     */
    MA1200DTO selectByParentId(MA1200DTO dto);

    /**
     * 获取所有ID
     * @param
     */
    List<String> selectAllParentId();

    /**
     * 获取待选List
     * @param v
     */
    List<AutoCompleteDTO> selectList(@Param("v")String v);

    List<MA1200DTO> getChildInfo(@Param("groupParentIdList")Collection<String> groupParentIdList);

    /**
     * 获取所有的BOM商品配方id
     * @return
     */
    List<String> selectBOMAllParentId();

    List<MA1200DTO> getBOMChildInfo(@Param("bomParentIdList")Collection<String> bomParentIdList);
}