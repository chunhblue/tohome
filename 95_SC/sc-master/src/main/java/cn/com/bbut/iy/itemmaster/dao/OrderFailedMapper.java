package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFailedMapper {

    /**
     * 条件查询失败订单
     */
    List<OrderFailedGridDTO> selectFailedList(OrderFailedParamDTO param);

    /**
     * 条件查询失败订单记录总数
     */
    int selectFailedCount(OrderFailedParamDTO param);

    /**
     * 供应商层次条件查询失败订单
     */
    List<OrderFailedGridDTO> VendorOrderFailedList(OrderFailedParamDTO param);

    /**
     * 供应商层次条件查询失败订单记录总数
     */
    int VendorOrderFailedCount(OrderFailedParamDTO param);

    /**
     * 商品明细
     */
    List<OrderFailedGridDTO> getDetaildata(OrderFailedParamDTO param);

    int getDetaildataCount(OrderFailedParamDTO param);
}
