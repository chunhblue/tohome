package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaSaleReportMapper {

//   List<DaySaleReportDTO> selectDaySaleReport(DaySaleReportParamDTO daySaleReportDTO);

   List<DaySaleReportDTO> selectDaySaleReport(@Param("daySaleReportParamdto") DaySaleReportParamDTO daySaleReportParamdto);


//    List<Ma1000> getAMList();
   List<AutoCompleteDTO> getAMList( @Param("v") String v);

    List<MA0020C> getCity();

    int selectDaySaleReportCount(@Param("daySaleReportParamdto")DaySaleReportParamDTO param);

    int selectDayPosSaleReportCount(@Param("daySaleReportParamdto")DaySaleReportParamDTO param);

    List<DaySaleReportDTO> selectDayPosSaleReport(@Param("daySaleReportParamdto") DaySaleReportParamDTO param);
}
