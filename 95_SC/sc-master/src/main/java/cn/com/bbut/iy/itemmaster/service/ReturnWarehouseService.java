package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.RealtimeStockItem;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.*;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.entity.od0010.OD0010;

import java.util.List;

/**
 * Created by lz on 2020/01/09.
 */
public interface ReturnWarehouseService {
    /**
     * 详情页面头档查询
     * @param orderId
     * @return
     */
    ReturnHeadResult getRWHHeadQuery(String orderId);

    /**
     * 退货单详情页面商品信息
     * @param orderId
     * @return
     */
    List<RWHItemsGridResult> getReturnItems(String orderId,String flag);

    /**
     * 一览页面条件查询
     */
    GridDataDTO<RWHListResult> getReturnWHQueryList(RWHListParamDTO rwhListParamDTO);
    /**
     * 获取订单号列表
     */
    List<AutoCompleteDTO> getOrgOrderIdList(ReturnWarehouseParamDTO dto);

    /**
     * 获取订单信息
     * @param orgOrderId 订单号
     * @param returnDiff 订单区别 1:dc 0:供应商
     * @return
     */
    OrderInfoDTO getOrderInfo(String orgOrderId,String returnDiff);

    /**
     * 查询订单商品
     */
    List<AutoCompleteDTO> getItems(ReturnWarehouseParamDTO dto);

    /**
     * 获取商品详细信息
     */
    ReturnWarehouseDetailInfo getItemInfo(ReturnWarehouseParamDTO dto);


    /**
     *  新增退货订单信息
     * @param param
     * @return
     */
    OD0000 insertReturnOrder(OD0000 param, String orderDetailJson);

    /**
     *  修改退货订单信息
     * @param param
     * @return
     */
    OD0000 updateReturnOrder(OD0000 param,String orderDetailJson);

    /**
     * 修改退货商品实际退货量
     * @param orderDetailJson
     * @return
     */
    int updateActualReturnQty(String orderDetailJson,String orderRemark);

    /**
     * 获取商品实时库存
     * @param storeCd
     * @param stockDate
     * @param articleId
     * @return
     */
    RealtimeStockItem getRealTimeStock(String storeCd, String stockDate, String articleId);

    /**
     * 打印页面条件查询
     */
    List<RWHListResult> getPrintReturnWHList(RWHListParamDTO rwhListParamDTO);

    /**
     * 获取dc一览
     * @return
     */
    List<AutoCompleteDTO> getDc();

    List<AutoCompleteDTO>   getdirectItems(ReturnWarehouseParamDTO param);

    ReturnWarehouseDetailInfo getdirectItemInfo(ReturnWarehouseParamDTO param);

    List<AutoCompleteDTO> getdirectItemsList(ReturnWarehouseParamDTO param);

   List<RWHItemsGridResult> getReturnItemsDetail(ReturnWarehouseParamDTO returnParamDTO);

    List<RWHItemsGridResult> getDcReturnItemsDetail(ReturnWarehouseParamDTO returnParamDTO);

}
