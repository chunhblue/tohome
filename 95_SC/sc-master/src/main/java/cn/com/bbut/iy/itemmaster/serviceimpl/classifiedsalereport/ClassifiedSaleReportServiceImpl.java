package cn.com.bbut.iy.itemmaster.serviceimpl.classifiedsalereport;

import cn.com.bbut.iy.itemmaster.dao.classifiedsalereport.ClassifiedSaleReportMapper;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.classifiedSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.classifiedsalereport.clssifiedSaleParamReportDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.classifiedsalereport.ClassifiedSaleReportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ClassifiedSaleReportServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/26 18:13
 * @Version 1.0
 */
@Service
public class ClassifiedSaleReportServiceImpl implements ClassifiedSaleReportService {


    @Autowired
    private ClassifiedSaleReportMapper classifiedMapper;

    @Override
    public Map<String,Object> search(clssifiedSaleParamReportDTO param) {

        // 总条数
        int count = classifiedMapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<classifiedSaleReportDTO> reportDTOList = classifiedMapper.search(param);
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

    @Override
    public List<Ma1000> getAMList() {
        return classifiedMapper.getAMList();
    }

    @Override
    public List<MA0020C> getCity() {
        return classifiedMapper.getCity();
    }

    @Override
    public List<MA0080> getPmaList() {
        return classifiedMapper.getPmaList();
    }

    private String formatDate1(String piDate) {
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

    private String formatDate2(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }
}
