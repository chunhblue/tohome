package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.promotion.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PromotionMapper {

    /**
     * 条件查询记录总数
     * @param dto
     */
    int selectCountByCondition(PromotionParamDTO dto);

    /**
     * 条件查询记录
     * @param dto
     */
    List<PromotionDTO> selectListByCondition(PromotionParamDTO dto);

    /**
     * 查询促销基本信息
     * @param dto
     */
    PromotionDTO selectPromotion(PromotionParamDTO dto);

    /**
     * 查询促销条件设定信息
     * @param dto
     */
    List<Ma4060DTO> selectMa4060(PromotionParamDTO dto);

    /**
     * 查询促销商品设定信息
     * @param dto
     */
    List<Ma4070DTO> selectMa4070(PromotionParamDTO dto);

    /**
     * 查询促销类别设定信息
     * @param dto
     */
    List<Ma4080DTO> selectMa4080(PromotionParamDTO dto);

    /**
     * 查询促销类别设定信息
     * @param dto
     */
    List<Ma4081DTO> selectMa4081(PromotionParamDTO dto);

    /**
     * 查询促销品牌设定信息
     * @param dto
     */
    List<Ma4085DTO> selectMa4085(PromotionParamDTO dto);

    /**
     * 查询促销品牌设定信息
     * @param dto
     */
    List<Ma4086DTO> selectMa4086(PromotionParamDTO dto);

    /**
     * 查询促销例外设定信息
     * @param dto
     */
    List<Ma4090DTO> selectMa4090(PromotionParamDTO dto);

    /**
     * 查询促销例外店铺设定信息
     * @param dto
     */
    List<Ma4100DTO> selectMa4100(PromotionParamDTO dto);

}
