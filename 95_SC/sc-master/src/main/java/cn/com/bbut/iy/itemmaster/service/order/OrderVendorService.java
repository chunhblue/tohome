package cn.com.bbut.iy.itemmaster.service.order;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.InventoryVouchersGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorDetailInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorInfo;
import cn.com.bbut.iy.itemmaster.dto.orderVendor.OrderVendorParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;

import java.util.Collection;
import java.util.List;

public interface OrderVendorService {
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
    List<OrderVendorDetailInfo> getItemsByVendor(OrderVendorParamDTO dto);

    /**
     * 自动获取订货详细一览
     * @param dto
     * @return
     */
    GridDataDTO<OrderVendorDetailInfo> getOrderDetailListByAuto(OrderVendorParamDTO dto);

    /**
     * 获取供应商信息
     * @param vendorId
     * @param storeCd
     * @return
     */
    MA2000 getVendorInfo(String vendorId,String storeCd);

    /**
     * 获取商品信息
     * @param dto
     * @return
     */
    OrderVendorDetailInfo getItemInfo(OrderVendorParamDTO dto);

    /**
     *  获取供应商订货信息
     * @param orderId 订单号
     * @return
     */
    OrderVendorInfo updateAndGetVendorInfoByOrderId(String orderId);

    /**
     *  获取供应商订货商品信息
     * @param dto
     * @return
     */
    List<AutoCompleteDTO> getItems(OrderVendorParamDTO dto);

    /**
     *  新增订单信息
     * @param param
     * @return
     */
    OD0000 insertOrderByVendor(OD0000 param,String orderDetailJson);

    /**
     *  修改订单信息
     * @param param
     * @return
     */
    OD0000 updateOrderByVendor(OD0000 param,String orderDetailJson);

    /**
     * 更新订单折扣金额税率
     * @param recordCd 订单cd
     * @param auditDate 审核通过时间
     * @return
     */
    int updateDiscount(String recordCd,String auditDate);

    /**
     * 判断订单是否上传
     * @param orderId 订单cd
     * @return 0 < 为上传
     */
    Integer checkUploadOrder(String orderId);
}
