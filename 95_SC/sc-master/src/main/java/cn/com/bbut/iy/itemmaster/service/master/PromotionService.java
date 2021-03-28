package cn.com.bbut.iy.itemmaster.service.master;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.promotion.*;


public interface PromotionService {

    /**
     *
     * 条件查询促销记录
     */
    GridDataDTO<PromotionDTO> getList(PromotionParamDTO dto);

    /**
     *
     * 查询促销基本信息
     */
    PromotionDTO getBasicInfo(PromotionParamDTO dto);

    /**
     *
     * 查询促销条件设定
     */
    GridDataDTO<Ma4060DTO> getMa4060(PromotionParamDTO dto);

    /**
     *
     * 查询促销商品设定
     */
    GridDataDTO<Ma4070DTO> getMa4070(PromotionParamDTO dto);

    /**
     *
     * 查询促销类别设定
     */
    GridDataDTO<Ma4080DTO> getMa4080(PromotionParamDTO dto);

    /**
     *
     * 查询促销类别例外设定
     */
    GridDataDTO<Ma4081DTO> getMa4081(PromotionParamDTO dto);

    /**
     *
     * 查询促销品牌设定
     */
    GridDataDTO<Ma4085DTO> getMa4085(PromotionParamDTO dto);

    /**
     *
     * 查询促销品牌例外设定
     */
    GridDataDTO<Ma4086DTO> getMa4086(PromotionParamDTO dto);

    /**
     *
     * 查询促销区域设定
     */
    GridDataDTO<Ma4090DTO> getMa4090(PromotionParamDTO dto);

    /**
     *
     * 查询促销例外店铺设定
     */
    GridDataDTO<Ma4100DTO> getMa4100(PromotionParamDTO dto);

    /**
     * 查询直接促销
     */
    GridDataDTO<Ma4150DTO> getMa4150(PromotionParamDTO param);

    /**
     * 查询bill value
     */
    GridDataDTO<Ma4155DTO> getMa4155(PromotionParamDTO param);
}
