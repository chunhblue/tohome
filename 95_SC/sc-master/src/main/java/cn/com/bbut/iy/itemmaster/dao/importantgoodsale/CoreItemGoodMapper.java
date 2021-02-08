package cn.com.bbut.iy.itemmaster.dao.importantgoodsale;

import cn.com.bbut.iy.itemmaster.dto.coreItem.coreItemDTO;
import cn.com.bbut.iy.itemmaster.dto.coreItem.coreItemParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CoreItemGoodMapper {

    List<coreItemDTO> getDataforg(@Param("param") coreItemParamDTO param);

    List<coreItemDTO> getDataforcity(@Param("param")coreItemParamDTO paramDTO);

    List<coreItemDTO> getDatafordistrict(@Param("param")coreItemParamDTO paramDTO);

    List<coreItemDTO> getDataforStoreCd(@Param("param")coreItemParamDTO paramDTO);
}
