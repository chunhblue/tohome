package cn.com.bbut.iy.itemmaster.serviceimpl.yearPromotion;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.cardpayment.CardPaymentMapper;
import cn.com.bbut.iy.itemmaster.dao.yearPromotion.yearPromotionMapper;
import cn.com.bbut.iy.itemmaster.dto.cardPaymentReport.cardPaymentDto;
import cn.com.bbut.iy.itemmaster.dto.staffAttendanceDaily.StaffAttendanceParamDailyDto;
import cn.com.bbut.iy.itemmaster.dto.yearendpromotion.yearEndPromotionDto;
import cn.com.bbut.iy.itemmaster.dto.yearendpromotion.yearEndPromotionParamDto;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.yearPromotionService.yearEndPromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName yearEndPromotionServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/2 17:40
 * @Version 1.0
 * @Description
 */
@Service
public class yearEndPromotionServiceImpl implements yearEndPromotionService {

    @Autowired
    private Cm9060Mapper cm9060Mapper;

    @Autowired
    private yearPromotionMapper mapper;
    @Override
    public Map<String, Object> search(yearEndPromotionParamDto param) {
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey("0000");
        String bussinessDate = cm9060.getSpValue();
        param.setBusinessDate(bussinessDate);
        Integer count = mapper.searchDataCount(param);

        //    获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<yearEndPromotionDto> reportDTOList = mapper.search(param);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",reportDTOList);
        return map;
    }

}
