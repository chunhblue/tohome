package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.order.*;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Mapper
public interface OrderDcUrgentMapper {
    List<OrderVendorGridDTO> getOrderList(OrderVendorParamDTO dto);

    List<OrderVendorDetailInfo> getOrderDetailList(OrderDcParamDTO dto);

    /**
     * 自动加载订货商品
     * @param dto
     * @return
     */
    List<OrderVendorDetailInfo> getOrderDetailListByAuto(OrderVendorParamDTO dto);

    Integer getOrderDetailListByAutoCount(OrderVendorParamDTO dto);

    long getOrderListCount(OrderVendorParamDTO dto);

    /**
     * 获取订货商品税率一览
     * @param orderDate
     * @param storeCd
     * @return
     */
    List<AutoCompleteDTO> getOrderTaxRate(@Param("orderDate") String orderDate,@Param("storeCd") String storeCd);

    /**
     *  获取dc订货信息
     * @param orderId 订单号
     * @return
     */
    OrderDcInfo getDcInfoByOrderId(@Param("orderId")String orderId);

    /**
     * 获取订货商品列表
     * @param dto
     * @return
     */
    List<AutoCompleteDTO> getItems(OrderVendorParamDTO dto);

    /**
     * 获取订货商品详细信息
     * @param dto
     * @return
     */
    OrderVendorDetailInfo getItemInfo(OrderVendorParamDTO dto);

    /**
     * 获取供应商订货截止时间
     * @param storeCd
     * @param vendorId
     * @param effectiveDate
     * @return 订货截止时间
     */
    String getExpireDate(@Param("storeCd") String storeCd,@Param("vendorId") String vendorId,@Param("effectiveDate") String effectiveDate);

    /**
     * 获取店铺是否参加折扣
     * @param recordCd
     * @param auditDate
     * @return
     */
    OD0010 getStoreDiscount(@Param("orderId") String recordCd, @Param("auditDate") String auditDate);

    /**
     * 获取供应商折扣率
     * @param vendorId
     * @param auditDate
     * @return
     */
    String getDiscountRate(@Param("vendorId") String vendorId,@Param("auditDate") String auditDate);

    /**
     * 修改订单折扣金额
     * @param orderId
     * @return
     */
    int updateOrderDiscount(@Param("orderId") String orderId, @Param("discountRate") BigDecimal discountRate);


    /**
     * 判断订单是否上传
     * @param orderId 订单cd
     * @return 0 < 为上传
     */
    Integer checkUploadOrder(@Param("orderId")String orderId);

    /**
     * 获取店铺地区
     * @param storeCd
     * @return
     */
    String getStoreRegion(String storeCd);

    /**
     * 获取店铺对应dc
     * @param storeCd
     * @return
     */
    OD0000 getDcInfo(String storeCd);

    /**
     * 获取商品实时库存
     * @param storeCd
     * @param articleId
     * @return
     */
    BigDecimal getInvertoryQty(@Param("storeCd") String storeCd, @Param("articleId") String articleId);

    /**
     * 获取dc店铺
     * @return
     */
    Collection<String> getDcStores();
}
