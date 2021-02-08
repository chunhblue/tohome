package cn.com.bbut.iy.itemmaster.service.order;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.order.OrderDcInfo;
import cn.com.bbut.iy.itemmaster.dto.order.OrderDcParamDTO;
import cn.com.bbut.iy.itemmaster.dto.order.OrderItemDetailDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface OrderDcUrgentService {
    /**
     * 获取订货一览
     * @param dto
     * @return
     */
    GridDataDTO<OrderVendorGridDTO> GetOrderList(OrderVendorParamDTO dto);
    /**
     * 获取订货详细一览
     * @param dto
     * @return
     */
    List<OrderVendorDetailInfo> getItemsByDc(OrderDcParamDTO dto);

    /**
     * 自动获取订货详细一览
     * @param dto
     * @return
     */
    GridDataDTO<OrderVendorDetailInfo> getOrderDetailListByAuto(OrderVendorParamDTO dto);

    /**
     * 获取订货商品税率一览
     * @param orderDate
     * @param storeCd
     * @return
     */
    List<AutoCompleteDTO> getOrderTaxRate(String orderDate,String storeCd);

    /**
     * 获取商品信息
     * @param dto
     * @return
     */
    OrderVendorDetailInfo getItemInfo(OrderVendorParamDTO dto);

    /**
     * 获取搭赠商品信息
     * @param dto
     * @return
     */
    List<OrderVendorDetailInfo> getFreeItemInfo(OrderItemDetailDTO dto);

    /**
     *  获取dc订货信息
     * @param orderId 订单号
     * @return
     */
    OrderDcInfo getOrderDcInfo(String orderId);

    /**
     *  获取dc订货商品信息
     * @param dto
     * @return
     */
    List<AutoCompleteDTO> getItems(OrderVendorParamDTO dto);

    /**
     *  新增dc订单信息
     * @param param
     * @return
     */
    OD0000 insertOrderByDc(OD0000 param, String orderDetailJson);

    /**
     *  修改dc订单信息
     * @param param
     * @return
     */
    OD0000 updateOrderByDc(OD0000 param, String orderDetailJson);

    /**
     * 更新订单折扣金额税率
     * @param recordCd 订单cd
     * @param auditDate 审核通过时间
     * @return
     */
    int updateDiscount(String recordCd, String auditDate);

    /**
     * 判断订单是否上传
     * @param orderId 订单cd
     * @return 0 < 为上传
     */
    Integer checkUploadOrder(String orderId);

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
    BigDecimal getInventoryQty(String storeCd, String articleId);

    /**
     * 获取dc店铺
     * @return
     */
    Collection<String> getDcStores();
}
