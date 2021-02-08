package cn.com.bbut.iy.itemmaster.dao.serviceTypeDailyReport;

import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.classifiedSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ServiceTypeDailyReportMapper {

    List<classifiedSaleReportDTO> search(@Param("classParamdto") clssifiedSaleParamReportDTO classParamdto);

    int searchCount(@Param("classParamdto")clssifiedSaleParamReportDTO param);
}
