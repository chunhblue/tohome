package cn.com.bbut.iy.itemmaster.serviceimpl.cardPayment;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.cardpayment.CardPaymentMapper;
import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentDto;
import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentParamDto;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.classifiedSaleReportDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.cardPaymentReport.CardPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName cardPaymentServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/19 18:16
 * @Version 1.0
 * @Description
 */
@Service
@Slf4j
public class cardPaymentServiceImpl  implements CardPaymentService {

    @Autowired
    private CardPaymentMapper mapper;

    @Autowired
    private Cm9060Mapper cm9060Mapper;


    @Override
    public Map<String, Object> search(cardPaymentParamDto param) {
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
        String bussinessDate = cm9060.getSpValue();
        param.setBussinessDate(bussinessDate);
         Integer count = mapper.DataCount(param);

     //    获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<cardPaymentDto> reportDTOList = mapper.searchData(param);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",reportDTOList);
        return map;

    }
}
