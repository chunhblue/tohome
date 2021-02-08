package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.SA0050GenMapper;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050GridDto;
import cn.com.bbut.iy.itemmaster.dto.sa0050.SA0050ParamDto;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SA0050Mapper extends SA0050GenMapper {
    List<SA0050GridDto> getCashierList(@Param("param") SA0050ParamDto param);

    long getCashierListCount(@Param("param") SA0050ParamDto param);


    List<SA0050GridDto> getCashierListExcel(@Param("param")SA0050ParamDto param);

    SA0050 selectByPrimaryKey1(String cashierId, String storeCd);

    int updateByPrimaryKeySelective1(SA0050 sa0050);
}