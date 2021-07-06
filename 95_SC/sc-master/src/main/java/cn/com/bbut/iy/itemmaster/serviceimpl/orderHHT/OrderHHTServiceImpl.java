package cn.com.bbut.iy.itemmaster.serviceimpl.orderHHT;
import cn.com.bbut.iy.itemmaster.dao.orderHHT.OrderHHTMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTGridDTO;
import cn.com.bbut.iy.itemmaster.dto.orderHHT.OrderHHTParamDto;
import cn.com.bbut.iy.itemmaster.service.orderHHT.OrderHHTService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @ClassName OrderHHTServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/5/25 11:19
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service
public class OrderHHTServiceImpl implements OrderHHTService {
    @Autowired
    private OrderHHTMapper mapper;

    @Override
    public GridDataDTO<OrderHHTGridDTO> getDataList(OrderHHTParamDto param) {
        int count = mapper.getDataListCount(param);
        List<OrderHHTGridDTO> _list=null;
        if (count>0){
            _list  = mapper.getDataList(param);
        }
        GridDataDTO<OrderHHTGridDTO> data = new GridDataDTO<OrderHHTGridDTO>(_list,
                param.getPage(), count, param.getRows());
        return data;
    }
}
