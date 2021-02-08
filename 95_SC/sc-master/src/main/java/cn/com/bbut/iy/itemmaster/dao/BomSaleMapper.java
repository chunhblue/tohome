package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleDTO;
import cn.com.bbut.iy.itemmaster.dto.bomSale.BomSaleParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BomSaleMapper {

    /**
     * 条件查询记录
     * @param dto
     */
    List<BomSaleDTO> selectListByCondition(BomSaleParamDTO dto);

    /**
     * 获取Bom商品下拉
     */
    List<AutoCompleteDTO> getBomItemList(@Param("v") String v, @Param("businessDate") String businessDate);
}
