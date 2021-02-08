package cn.com.bbut.iy.itemmaster.serviceimpl.serviceTypeDaily;

import cn.com.bbut.iy.itemmaster.dao.serviceTypeDailyReport.ServiceTypeDailyReportMapper;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.classifiedSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.service.serviceTypeDaily.ServiceTypeDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceTypeDailyServiceImpl implements ServiceTypeDailyService {

    @Autowired
    private ServiceTypeDailyReportMapper ServiceTypeDailyReportMapper;

    @Override
    public Map<String,Object> search(clssifiedSaleParamReportDTO param) {

        // 总条数
        int count = ServiceTypeDailyReportMapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<classifiedSaleReportDTO> reportDTOList = ServiceTypeDailyReportMapper.search(param);
        for (classifiedSaleReportDTO reportdto : reportDTOList) {
            reportdto.setSaleDate(formatDate1(reportdto.getSaleDate()));
            reportdto.setAccDate(formatDate2(reportdto.getAccDate()));
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",reportDTOList);
        return map;
    }

    private String formatDate1(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

    private String formatDate2(String piDate) {
        String a="";
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String[] split = piDate.split("-");
        for (String sp:split) {
            a=a+sp;
        }
        String year = a.substring(0, 4);
        String month = a.substring(4, 6);
        String day = a.substring(6, 8);
        return   day+"/"+month+"/"+year;

    }
}
