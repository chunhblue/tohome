package cn.com.bbut.iy.itemmaster.service.hhtReportService;

import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportParamDto;
import java.util.Map;

public interface hhtReportService {

    Map<String,Object> getList(hhtReportParamDto param);
}
