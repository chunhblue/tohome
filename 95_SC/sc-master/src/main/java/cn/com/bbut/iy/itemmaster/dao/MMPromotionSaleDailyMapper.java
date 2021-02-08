package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionItemsDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionSaleDailyParamDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MMPromotionSaleDailyMapper {

    /**
     * mm 促销 商品销售查询
     */
    List<MMPromotionDataDTO> search(@Param("param") MMPromotionSaleDailyParamDTO param);

    List<AutoCompleteDTO> getPromotionPattern(@Param("v")String v);

    List<AutoCompleteDTO> getPromotionType(@Param("v")String v);

    List<AutoCompleteDTO> getDistributionType(@Param("v")String v);

    int searchCount(@Param("param")MMPromotionSaleDailyParamDTO param);

    List<MMPromotionItemsDTO> searchItems(@Param("param") MMPromotionSaleDailyParamDTO param,
                                              @Param("tranSerialNo")Integer tranSerialNo,
                                              @Param("promotionCd")String promotionCd,
                                              @Param("accDate")String accDate);
}
