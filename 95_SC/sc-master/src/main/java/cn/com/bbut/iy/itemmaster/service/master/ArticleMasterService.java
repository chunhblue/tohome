package cn.com.bbut.iy.itemmaster.service.master;

import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;

import java.util.List;


public interface ArticleMasterService {

    /**
     *
     * 条件查询主档记录
     */
    GridDataDTO<ArticleDTO> getList(ArticleParamDTO dto);

    /**
     *
     * 查询主档基本信息
     */
    ArticleDTO getBasicInfo(ArticleParamDTO dto);

    /**
     *
     * 查询条码信息
     */
    GridDataDTO<BarcodeDTO> getBarcode(ArticleParamDTO dto);

    /**
     *
     * 查询进货控制信息
     */
    GridDataDTO<OrderControlDTO> getOrderControl(ArticleParamDTO dto);

    /**
     *
     * 查询销售控制信息
     */
    GridDataDTO<SalesControlDTO> getSalesControl(ArticleParamDTO dto);

    /**
     *
     * 查询口味关系信息
     */
    GridDataDTO<FlavorDTO> getFlavor(ArticleParamDTO dto);

    /**
     *
     * 查询厨打关系信息
     */
    FoodServiceDTO  getFoodService(ArticleParamDTO dto);

    /**
     * 检索供应商
     *
     */
    List<AutoCompleteDTO> getList(String v);

    /**
     * 品牌检索
     *
     */
    List<AutoCompleteDTO> getBrand(String v);

    /**
     * 材料类型
     *
     */
    List<Cm9010> getMaterialType();

    /**
     *
     * 查询包装规格信息
     */
    CartonSpecificationDTO  getCartonSpecification(ArticleParamDTO dto);
}
