package cn.com.bbut.iy.itemmaster.dao.classifiedsalereport;

import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.classifiedSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ClassifiedSaleReportMapper {

    List<Ma1000> getAMList();

    List<MA0020C> getCity();

    List<MA0080> getPmaList();

    List<classifiedSaleReportDTO> search(@Param("classParamdto") clssifiedSaleParamReportDTO classParamdto);

    int searchCount(@Param("classParamdto")clssifiedSaleParamReportDTO param);

    BigDecimal getTotalSaleAmount(@Param("classParamdto") clssifiedSaleParamReportDTO param);
}
