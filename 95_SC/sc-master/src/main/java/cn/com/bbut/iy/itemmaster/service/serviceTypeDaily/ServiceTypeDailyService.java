package cn.com.bbut.iy.itemmaster.service.serviceTypeDaily;

import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;

import java.util.Map;

public interface ServiceTypeDailyService {

    Map<String,Object> search(clssifiedSaleParamReportDTO param);
}
