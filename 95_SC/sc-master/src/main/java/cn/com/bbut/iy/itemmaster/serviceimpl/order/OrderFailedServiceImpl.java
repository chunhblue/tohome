package cn.com.bbut.iy.itemmaster.serviceimpl.order;

import cn.com.bbut.iy.itemmaster.dao.OrderFailedMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderFailed.OrderFailedParamDTO;
import cn.com.bbut.iy.itemmaster.service.order.OrderFailedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderFailedServiceImpl implements OrderFailedService {

    @Autowired
    private OrderFailedMapper mapper;

    /**
     * 获取订货失败一览
     *
     * @param param
     * @return
     */
    @Override
    public GridDataDTO<OrderFailedGridDTO> getFailedList(OrderFailedParamDTO param) {

        List<OrderFailedGridDTO> _list = mapper.selectFailedList(param);

        int count = mapper.selectFailedCount(param);

        GridDataDTO<OrderFailedGridDTO> data = new GridDataDTO<OrderFailedGridDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }

    @Override
    public GridDataDTO<OrderFailedGridDTO> dayOrderFailure(OrderFailedParamDTO param) {
        List<OrderFailedGridDTO> _list = mapper.VendorOrderFailedList(param);

        int count = mapper.VendorOrderFailedCount(param);

        GridDataDTO<OrderFailedGridDTO> data = new GridDataDTO<OrderFailedGridDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }

    @Override
    public GridDataDTO<OrderFailedGridDTO> getDetaildata(OrderFailedParamDTO param) {
        List<OrderFailedGridDTO> _list = mapper.getDetaildata(param);

        int count = mapper.getDetaildataCount(param);

        GridDataDTO<OrderFailedGridDTO> data = new GridDataDTO<OrderFailedGridDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }
}
