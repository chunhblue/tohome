package cn.com.bbut.iy.itemmaster.service.orderreport;

import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportDTO;
import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportParamDTO;

import java.util.Map;

public interface OrderReportService {



   Map<String,Object>  getData(OrderReportParamDTO param);

}
