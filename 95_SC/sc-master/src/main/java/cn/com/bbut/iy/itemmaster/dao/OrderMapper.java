package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.order.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Mapper
@Component
public interface OrderMapper {
    List<OrderItemDTO> getOrderListByDpt(OrderItemParamDTO dto);

    long getOrderListByDptCount(OrderItemParamDTO dto);

    List<OrderItemDTO> getOrderListByShelf(OrderItemParamDTO dto);

    long getOrderListByShelfCount(OrderItemParamDTO dto);

    /**
     * 获取订货商品一览
     * @param dto
     * @return
     */
    List<OrderItemDetailDTO> getOrderListDetail(OrderItemParamDTO dto);

    long getOrderListDetailCount(OrderItemParamDTO dto);

    /**
     *
     * @param dto
     * @return
     */
    List<OrderItemDetailDTO> getOrderListDetailByDpt(OrderItemParamDTO dto);

    long getOrderListDetailByDptCount(OrderItemParamDTO dto);

    OrderDetailInfo getOrderItemDetail(OrderItemParamDTO dto);

    List<OrderItemDetailDTO> selectOrder(OrderItemDetailDTO dto);

    int updateOrder(OrderItemDetailDTO dto);

    int insertOrder(OrderItemDetailDTO dto);

    /**
     * 获取促销例外或添加的信息
     * @param dto
     * @return
     */
    PromotionDTO getPromotionInfo(PromotionDTO dto);

    /**
     * 查询订货用商品信息
     * @param dto
     * @return
     */
    OrderItemDetailDTO getMa8040(OrderItemDetailDTO dto);

    /**
     * 根据权限获取店铺 没有下订单的店铺
     * @param storeCds 店铺权限id
     * @return
     */
    List<AutoCompleteDTO> getStoreListNotOrder(@Param("stores")Collection<String> storeCds, @Param("v")String v, @Param("businessDate")String businessDate);

    /**
     * 获取开店日期
     * @param storeCd
     * @return
     */
    String getOrderOpenDate(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    /**
     * 获取订货信息 by供应商
     * @return
     */
    List<OrderItemDetailDTO> getOrderInfoByVendor(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    // 获取订货信息 by供应商分组
    OrderItemDetailDTO getOrderInfoByVendorGroup(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate,
                                                       @Param("vendorId")String vendorId,@Param("groupCode")String groupCode);

    /**
     * 获取订货信息 by item
     * @return
     */
    List<OrderItemDetailDTO> getOrderInfoByItem(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    /**
     * 获取供应商MOA MOQ by city
     * @return
     */
    OrderVendorDetailDTO getVendorDetailByCity(@Param("storeCd")String storeCd,@Param("vendorId")String vendorId,@Param("businessDate")String businessDate);

    /**
     * 获取供应商特殊商品的MOA MOQ by city
     * @return
     */
    List<OrderVendorDetailDTO> getGroupDetailByVendor(@Param("storeCd")String storeCd,@Param("vendorId")String vendorId,@Param("businessDate")String businessDate);


    /**
     * 获取单个商品的MOQ (所有的)
     * @return
     */
    OrderVendorDetailDTO getItemDerailByCity(@Param("storeCd")String storeCd,@Param("vendorId")String vendorId,@Param("businessDate")String businessDate,@Param("articleId")String articleId);

    /**
     * 修改订货状态为生效
     * @param businessDate
     * @return
     */
    int updateOrderEffStsByVendor(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd);

    /**
     * 修改供应商订货状态失效
     * @param businessDate
     * @return
     */
    int updateOrderExpStsByVendor(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd);

    /**
     * 修改product group订货状态为生效
     * @param businessDate
     * @return
     */
    int updateOrderEffStsByGroup(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd,@Param("groupCode") String groupCode);

    /**
     * 修改product group订货状态失效
     * @param businessDate
     * @return
     */
    int updateOrderExpStsByGroup(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd,@Param("productGroupCd") String productGroupCd);

    /**
     * 获取促销满足搭赠量的数量
     * @param promotionCd
     * @param articleId
     * @return
     */
    BigDecimal getConditionQty(@Param("promotionCd") String promotionCd,@Param("articleId") String articleId);

    /**
     * 获取有搭赠量的商品
     * @param promotionCd
     * @param articleId
     * @return
     */
    List<PromotionDTO> getPresentArticle(@Param("promotionCd") String promotionCd,@Param("articleId") String articleId);

    /**
     * 添加审核数据
     * @param storeCd
     * @param orderDate
     * @return
     */
    int insertAudit(String storeCd, String orderDate);

    /**
     * 检查订货是否没有订货
     * @param storeCd
     * @param orderDate
     * @return
     */
    int checkOrder(String storeCd, String orderDate);

    /**
     * 检查vendor订货是否没有订货
     * @param storeCd
     * @param orderDate
     * @return
     */
    int checkVendorOrder(String storeCd, String orderDate);

    List<AutoCompleteDTO> getMa1107(@Param("storeCd") String storeCd,@Param("v")String v);

    List<AutoCompleteDTO> getMa1108(@Param("storeCd") String storeCd,@Param("excelName") String excelName,@Param("v")String v);

    List<AutoCompleteDTO> getMa1109(@Param("storeCd") String storeCd,@Param("excelName") String excelName,
                                    @Param("paramShelf") String paramShelf,@Param("v")String v);

    List<OrderItemDTO> getUserPosition(String storeCd, String userId);


    /**
     * 特殊订单查询
     * @param dto
     * @return
     */
    List<OrderItemDTO> getOrderSpecialListByDpt(OrderItemParamDTO dto);

    long getOrderSpecialListByDptCount(OrderItemParamDTO dto);

    List<OrderItemDTO> getOrderSpecialListByShelf(OrderItemParamDTO dto);

    long getOrderSpecialListByShelfCount(OrderItemParamDTO dto);
    /**
     * 获取特殊订货商品一览
     * @param dto
     * @return
     */
    List<OrderItemDetailDTO> getOrderSpecialListDetail(OrderItemParamDTO dto);

    long getOrderSpecialListDetailCount(OrderItemParamDTO dto);

    List<OrderItemDetailDTO> getOrderSpecialListDetailByDpt(OrderItemParamDTO dto);

    long getOrderSpecialListDetailByDptCount(OrderItemParamDTO dto);

    OrderDetailInfo getOrderSpecialItemDetail(OrderItemParamDTO dto);

    /*
     * 修改订单信息
     */
    List<OrderItemDetailDTO> selectSpecialOrder(OrderItemDetailDTO dto);

    int updateSpecialOrder(OrderItemDetailDTO dto);

    int insertSpecialOrder(OrderItemDetailDTO dto);

    /**
     * 修改供应商订货状态失效
     * @param businessDate
     * @return
     */
    int updateSpecialOrderExpStsByVendor(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd);

    /**
     * 获取跨天订货信息 by供应商
     * @return
     */
    List<OrderItemDetailDTO> getSpecialOrderInfoByVendor(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    // 获取跨天订货信息 by供应商分组
    OrderItemDetailDTO getSpecialOrderInfoByVendorGroup(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate,
                                                 @Param("vendorId")String vendorId,@Param("groupCode")String groupCode);

    /**
     * 修改跨天订货信息product group订货状态为生
     */
    int updateSpecialOrderEffStsByGroup(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd,@Param("groupCode") String groupCode);

    // 修改跨天订货信息订货状态为生效
    int updateSpecialOrderEffStsByVendor(@Param("businessDate") String businessDate,@Param("vendorId") String vendorId,@Param("storeCd") String storeCd);

    /**
     * 检查跨天订货信息是否没有订货
     */
    int checkSpecialOrder(String storeCd, String orderDate);

    /**
     * 检查vendor跨天订货信息是否没有订货
     */
    int checkVendorSpecialOrder(String storeCd, String orderDate);

    /**
     * 获取跨天订货信息 by item
     * @return
     */
    List<OrderItemDetailDTO> getSpecialOrderInfoByItem(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    List<OrderItemDetailDTO> getMiddleListBy(@Param("storeCd")String storeCd);

    int insertItemByCopy(@Param("storeCd")String storeCd,@Param("businessDate")String businessDate);

    int deleteByCopy(@Param("storeCd")String storeCd);
}
