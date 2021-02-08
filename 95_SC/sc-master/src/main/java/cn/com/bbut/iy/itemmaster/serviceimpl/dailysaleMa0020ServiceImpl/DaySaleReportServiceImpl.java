package cn.com.bbut.iy.itemmaster.serviceimpl.dailysaleMa0020ServiceImpl;

import cn.com.bbut.iy.itemmaster.dao.DaSaleReportMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.daysalereport.DaySaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.service.dailysalereferma0020.DaySaleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DaySaleReportServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/25 14:39
 * @Version 1.0
 */
@Service
public class DaySaleReportServiceImpl implements DaySaleReportService {

    @Autowired
    private DaSaleReportMapper daSaleReportMapper;

    @Override
    public Map<String,Object> search(DaySaleReportParamDTO param) {

        // 获取 总数
        int count = daSaleReportMapper.selectDaySaleReportCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;
        List<DaySaleReportDTO> result = daSaleReportMapper.selectDaySaleReport(param);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);

        return map;
    }

    @Override
    public List<AutoCompleteDTO> getAMList(String v) {

        return daSaleReportMapper.getAMList(v);

    }

    @Override

    public List<MA0020C> getCity() {
        return daSaleReportMapper.getCity();
    }


    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(5, 7);
        String day = piDate.substring(8, 10);
        return day+"/"+month+"/"+year;
    }
}
