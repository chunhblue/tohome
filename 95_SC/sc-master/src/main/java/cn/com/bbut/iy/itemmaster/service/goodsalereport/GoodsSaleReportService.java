package cn.com.bbut.iy.itemmaster.service.goodsalereport;

import cn.com.bbut.iy.itemmaster.constant.ConstantsCache;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface GoodsSaleReportService {


    List<MA0080> getPamList();

    List<Ma1000> getAMlist();

    List<MA0020C> getClityList();
    Map<String,Object> getGoodSaleReportContent(goodSaleReportParamDTO param);

    Map<String, Object> getTotalSaleAmount(goodSaleReportParamDTO param);
}
