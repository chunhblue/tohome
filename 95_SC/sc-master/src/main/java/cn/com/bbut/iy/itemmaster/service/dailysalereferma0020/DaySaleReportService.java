package cn.com.bbut.iy.itemmaster.service.dailysalereferma0020;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;

import java.util.List;
import java.util.Map;

public interface DaySaleReportService  {

//   GridDataDTO<DaySaleReportDTO> getSaleReport(DaySaleReportParamDTO daySaleReportDTO);


    Map<String,Object> search(DaySaleReportParamDTO param);

//    List<Ma1000> getAMList();
   List<AutoCompleteDTO> getAMList(String v);

    List<MA0020C> getCity();
}
