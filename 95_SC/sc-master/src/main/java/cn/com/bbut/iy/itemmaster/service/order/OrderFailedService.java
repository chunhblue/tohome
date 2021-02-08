package cn.com.bbut.iy.itemmaster.service.order;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedParamDTO;

import java.util.List;

public interface OrderFailedService {
    /**
     * 获取订货失败一览
     * @param param
     * @return
     */
    GridDataDTO<OrderFailedGridDTO> getFailedList(OrderFailedParamDTO param);

    GridDataDTO<OrderFailedGridDTO> dayOrderFailure(OrderFailedParamDTO param);

    GridDataDTO<OrderFailedGridDTO> getDetaildata(OrderFailedParamDTO param);
}
