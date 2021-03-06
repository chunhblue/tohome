package cn.com.bbut.iy.itemmaster.dao.hhtReport;

        import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportDto;
        import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportParamDto;
        import org.apache.ibatis.annotations.Param;
        import org.springframework.stereotype.Repository;

        import java.util.List;

@Repository
public interface hhtReportMapper {
    List<hhtReportDto> selectDataToal(@Param("param") hhtReportParamDto param);
    List<hhtReportDto> selectReceivingPend(@Param("param") hhtReportParamDto param);
    List<hhtReportDto> selectApproved(@Param("param") hhtReportParamDto param);
    List<hhtReportDto> selectReceived(@Param("param") hhtReportParamDto param);
    Integer selectDataCount(@Param("param") hhtReportParamDto param);
}
