package cn.com.bbut.iy.itemmaster.service.orderHHT;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTParamDto;


public interface OrderHHTService {
    GridDataDTO<OrderHHTGridDTO> getDataList(OrderHHTParamDto param);
}
