package cn.com.bbut.iy.itemmaster.serviceimpl.CustomerCount;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.customercount.CustomerCountMapper;
import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentDto;
import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentParamDto;
import cn.com.bbut.iy.itemmaster.dto.customercount.CustomerCountDto;
import cn.com.bbut.iy.itemmaster.dto.customercount.CustomerCountParamDto;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.customerCount.CustomerCountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CustomerCountServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/20 15:46
 * @Version 1.0
 * @Description
 */
@Slf4j
@Service
public class CustomerCountServiceImpl  implements CustomerCountService {

    @Autowired
    private CustomerCountMapper mapper;

    @Autowired
    private Cm9060Mapper cm9060Mapper;


    @Override
    public Map<String, Object> search(CustomerCountParamDto param) {
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
        String bussinessDate = cm9060.getSpValue();
        param.setBussinessDate(bussinessDate);
        int count = 0;
        // 获取总页数
        int totalPage = 0;
        if (param.isFlg()) {
            count = mapper.searchCount(param);
            // 获取总页数
            totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        }
        List<CustomerCountDto> list = mapper.searchData(param);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalPage", totalPage);
        map.put("count", count);
        map.put("data", list);
        return map;
    }
}
