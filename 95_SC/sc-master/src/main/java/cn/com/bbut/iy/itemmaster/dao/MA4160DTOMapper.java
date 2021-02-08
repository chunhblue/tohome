package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.store.MA4160DTO;
import cn.com.bbut.iy.itemmaster.dto.store.MA4160ParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MA4160DTOMapper {
    /**
     * 条件查询主档记录
     * @param
     */
   List<MA4160DTO> selectListByCondition(MA4160ParamDTO ma4160ParamDTO);
    int selectCountByCondition(MA4160ParamDTO ma4160ParamDTO);

    List<MA4160DTO> selectAllma4160();
//
//    List<MA4160DTO> search(MA4160ParamDTO ma4160ParamDTO);
    List<MA4160DTO> search(@Param("ma4160ParamDTO") MA4160ParamDTO ma4160ParamDTO);
}
