package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.cm9010.Cm9010;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMasterMapper {

    /**
     * 条件查询主档记录总数
     * @param dto
     */
    int selectCountByCondition(ArticleParamDTO dto);

    /**
     * 条件查询主档记录
     * @param dto
     */
    List<ArticleDTO> selectListByCondition(ArticleParamDTO dto);

    /**
     * 查询主档基本信息
     * @param dto
     */
    ArticleDTO selectArticle(ArticleParamDTO dto);

    /**
     * 查询条码信息
     * @param dto
     */
    List<BarcodeDTO> selectBarcode(ArticleParamDTO dto);

    /**
     * 查询进货控制信息
     * @param dto
     */
    List<OrderControlDTO> selectOrderControl(ArticleParamDTO dto);

    /**
     * 查询销售控制信息
     * @param dto
     */
    List<SalesControlDTO> selectSalesControl(ArticleParamDTO dto);

    /**
     * 查询口味关系信息
     * @param dto
     */
    List<FlavorDTO> selectFlavor(ArticleParamDTO dto);

    /**
     * 查询厨打关系信息
     * @param dto
     */
    FoodServiceDTO selectFoodService(ArticleParamDTO dto);

    /**
     * 检索供应商
     * @param v
     */
    List<AutoCompleteDTO> selectList(@Param("businessDate") String businessDate, @Param("v") String v);

    /**
     * 检索供应商
     * @param v
     */
    List<AutoCompleteDTO> selectBrand(@Param("v") String v);

    /**
     * 获取材料类型
     * @param
     */
    List<Cm9010> selectMaterialType();

    /**
     * 查询包装规格信息
     * @param dto
     */
    CartonSpecificationDTO selectCartonSpecification(ArticleParamDTO dto);
}
