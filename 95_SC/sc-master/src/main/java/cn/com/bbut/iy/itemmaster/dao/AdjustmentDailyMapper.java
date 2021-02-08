package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.*;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AdjustmentDailyMapper {

    /**
     * 条件查询记录
     * @param dto
     */
    List<AdjustmentDailyDTO> selectListByCondition(AdjustmentDailyParamDTO dto);

    int selectCountByCondition(AdjustmentDailyParamDTO dto);
    int getCountItemSKU(AdjustmentDailyParamDTO dto);

    AdjustmentDailyDTO getGeneralReason(@Param("adjustReason") String adjustReason);
}
