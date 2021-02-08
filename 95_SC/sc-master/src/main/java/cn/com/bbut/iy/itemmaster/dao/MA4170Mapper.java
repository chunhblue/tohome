package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.MA4170GenMapper;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailGridDto;
import cn.com.bbut.iy.itemmaster.dto.ma4170.Ma4170DetailParamDto;
import cn.com.bbut.iy.itemmaster.entity.ma4170.MA4170;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MA4170Mapper extends MA4170GenMapper {
    /**
     * 门店原因一览
     * @param param
     * @return
     */
    List<Ma4170DetailGridDto> getList(Ma4170DetailParamDto param);

    long getListCount(Ma4170DetailParamDto param);
}