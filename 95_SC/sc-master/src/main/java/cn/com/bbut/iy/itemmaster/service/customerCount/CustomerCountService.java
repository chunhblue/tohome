package cn.com.bbut.iy.itemmaster.service.customerCount;

import cn.com.bbut.iy.itemmaster.dto.customercount.CustomerCountParamDto;

import java.util.Map;

public interface CustomerCountService {
    Map<String, Object> search(CustomerCountParamDto param);
}
