package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.priceByDay.PriceByDayDTO;
import cn.com.bbut.iy.itemmaster.dto.priceByDay.PriceByDayParamDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PriceByDayMapper {

    /**
     * 条件查询记录总数
     * @param dto
     */
    int selectCountByCondition(PriceByDayParamDTO dto);

    /**
     * 条件查询记录
     * @param dto
     */
    List<PriceByDayDTO> selectListByCondition(PriceByDayParamDTO dto);

}
