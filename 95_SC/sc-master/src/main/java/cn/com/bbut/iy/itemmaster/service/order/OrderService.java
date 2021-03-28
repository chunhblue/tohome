package cn.com.bbut.iy.itemmaster.service.order;

import cn.com.bbut.iy.itemmaster.dto.AjaxResultDto;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.order.*;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OrderService {
    /**
     * 获取订货一览
     * @param dto
     * @return
     */
    GridDataDTO<OrderItemDTO> GetOrderList(OrderItemParamDTO dto);

    /**
     * 获取订货详情
     * @param dto
     * @return
     */
    GridDataDTO<OrderItemDetailDTO> GetOrderDetailList(OrderItemParamDTO dto);

    /**
     * 获取店铺信息
     * @param code
     * @return
     */
    Ma1000 getStoreInfoByCode(String code);

    /**
     * 获取商品详细信息
     * @param dto
     * @return
     */
    OrderDetailInfo getOrderItemDetail(OrderItemParamDTO dto);

    /**
     * 修改订单信息
     * @param dto
     * @return
     */
    int updateOrder(OrderItemDetailDTO dto);

    /**
     * 获取进销存信息
     * @param dto
     * @return
     */
    List<OrderInvoicingDTO> getReferenceInfo(OrderItemParamDTO dto);

    /**
     * 根据权限获取店铺 没有下订单的店铺
     * @param storeCds 店铺权限id
     * @param v  模糊搜索店铺cd 店铺名称 可为空
     * @return
     */
    List<AutoCompleteDTO> getStoreListNotOrder(Collection<String> storeCds, String v);

    /**
     * 判断是否是新店
     * @param storeCd
     * @return 1 为新店
     */
    int checkNewStore(String storeCd);

    /**
     * 修改订货状态信息
     * @param storeCd
     * @return 1 为新店
     */
     String updateOrderSts(String storeCd);

    /**
     *检查订单是否超过MOA MOQ
     * @param storeCd
     * @return
     */
    String checkItem(String storeCd);

    /**
     * 检查订货是否没有订货
     * @param storeCd
     * @param orderDate
     * @return
     */
    String checkOrder(String storeCd,String orderDate);

    /**
     * 添加审核数据
     * @param storeCd
     * @param orderDate
     * @return
     */
    int insertAudit(String storeCd,String orderDate);

    List<AutoCompleteDTO> getMa1107(String storeCd,String v);

    List<AutoCompleteDTO> getMa1108(String storeCd,String excelName,String v);

    List<AutoCompleteDTO> getMa1109(String storeCd,String excelName,String paramShelf,String v);

    List<OrderItemDTO> getUserPosition(String storeCd, String userId);

    /**
     * 获取跨天订货一览
     * @param dto
     * @return
     */
    GridDataDTO<OrderItemDTO> GetOrderSpecialList(OrderItemParamDTO dto);

    /**
     * 获取跨天订货详情信息
     * @param dto
     * @return
     */
    GridDataDTO<OrderItemDetailDTO> GetOrderSpecialListDetail(OrderItemParamDTO dto);

    /**
     * 获取跨天商品详细信息
     * @param dto
     * @return
     */
    OrderDetailInfo getOrderSecialItemDetail(OrderItemParamDTO dto);

    /**
     * 修改订单信息
     */
    int updateSpecialOrder(OrderItemDetailDTO dto);

    /**
     * 修改跨天订货信息
     * @param storeCd
     * @return
     */
    String updateSpecialOrderSts(String storeCd);

    // 检查跨天订货信息是否未生效
    String checkSpecialOrder(String storeCd,String orderDate);

    /**
     *检查跨天订单是否超过MOA MOQ
     * @param storeCd
     * @return
     */
    String checkSpecialItem(String storeCd);

    Integer insertQuickCopy(String storeCd,String businessDate);
}
