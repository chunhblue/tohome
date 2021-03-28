package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;

import java.util.Map;

public interface StampSummaryReportSerivce {
    Map<String,Object> search(StampSummaryParamReportDto param);


    Map<String,Object> searchDetail(StampSummaryParamReportDto param);
}
