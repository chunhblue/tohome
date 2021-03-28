package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryReportDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StampSummaryReportMapper {
    List<StampSummaryReportDto> search(@Param("param") StampSummaryParamReportDto classParamdto);

    int searchCount(@Param("param")StampSummaryParamReportDto param);

    List<StampSummaryReportDto> searchDetail(@Param("param") StampSummaryParamReportDto classParamdto);

    int searchDetailCount(@Param("param")StampSummaryParamReportDto param);
}
