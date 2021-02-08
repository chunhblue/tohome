package cn.com.bbut.iy.itemmaster.service.classifiedsalereport;


import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;

import java.util.List;
import java.util.Map;

public interface ClassifiedSaleReportService {

    Map<String,Object> search(clssifiedSaleParamReportDTO param);

    List<Ma1000> getAMList();

    List<MA0020C> getCity();

    List<MA0080> getPmaList();
}
