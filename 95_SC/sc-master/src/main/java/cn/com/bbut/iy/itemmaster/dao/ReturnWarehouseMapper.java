package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.OrderInfoDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.ReturnHeadResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.RealtimeStockItem;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVListResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnWarehouse.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReturnWarehouseMapper {
    /**
     * 退仓库一览条件查询
     * @param rwhListParam
     * @return
     */
    //query 再用
    List<RWHListResult> selectWHQueryListBy(RWHListParam rwhListParam);
    /**
     * 退仓库一览条件查询总数
     * @param rwhListParam
     * @return
     */
    long selectWHQueryListCount(RWHListParam rwhListParam);

    /**
     * 退仓库情根据单据编号查询头档
     * @param orderId
     * @return
     */
    ReturnHeadResult selectWHHeadByOrderId(@Param("orderId") String orderId);


    /**
     * 退仓库详情根据单据编号查询商品
     * @param orderId
     * @return
     */
    List<RWHItemsGridResult> getReturnItems(@Param("orderId")String orderId,@Param("flag")String flag);

    /**
     * 获取订单号列表
     * @param dto
     * @return
     */
    List<AutoCompleteDTO> getOrgOrderIdList(ReturnWarehouseParamDTO dto);
    /**
     * 获取订单信息
     * @param orgOrderId 订单号
     * @param businessDate 业务时间
     * @return
     */
    OrderInfoDTO getOrderInfo(@Param("orderId") String orgOrderId,@Param("businessDate") String businessDate);

    /**
     * 获取退货商品一览
     * @param dto
     * @return
     */
    List<AutoCompleteDTO> getItems(ReturnWarehouseParamDTO dto);
    List<AutoCompleteDTO> getdirectItems(@Param("dto") ReturnWarehouseParamDTO dto);
    /**
     * 获取商品详细信息
     * @param dto
     * @return
     */
    ReturnWarehouseDetailInfo getItemInfo( ReturnWarehouseParamDTO dto);
    ReturnWarehouseDetailInfo getdirectItemInfo(@Param("dto") ReturnWarehouseParamDTO dto,@Param("businessDate")String businessDate);
    /**
     * 获取商品实时库存
     * @param storeCd
     * @param stockDate
     * @param articleId
     * @return
     */
    RealtimeStockItem getRealTimeStock(@Param("storeCd") String storeCd, @Param("stockDate") String stockDate, @Param("articleId") String articleId);

    /**
     * 获取退货供应商合同信息
     * @param vendorId
     * @param businessDate
     * @return
     */
    OrderInfoDTO getReturnContract(@Param("vendorId") String vendorId,@Param("businessDate") String businessDate);

    /**
     * 获取dc一览
     * @return
     */
    List<AutoCompleteDTO> getDc();

    /**
     * 退仓库打印一览条件查询
     * @param rwhListParam
     * @return
     */
    List<RWHListResult> selectPrintWHQueryListBy(RWHListParam rwhListParam);

    List<AutoCompleteDTO> getdirectItemsList(@Param("dto") ReturnWarehouseParamDTO param);

    List<RWHItemsGridResult> getReturnVendorItemsList(@Param("dto")ReturnWarehouseParamDTO returnParamDTO);

    List<RWHItemsGridResult> getDcReturnWarehouseItemsList(@Param("dto") ReturnWarehouseParamDTO returnParamDTO);

    List<RWHListResult> selectArticleBySupplier(@Param("jsonParam") RWHListParam jsonParam);
}
