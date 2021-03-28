package cn.com.bbut.iy.itemmaster.serviceimpl.hhReport;

import cn.com.bbut.iy.itemmaster.dao.hhtReport.hhtReportMapper;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportDto;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportParamDto;
import cn.com.bbut.iy.itemmaster.service.hhtReportService.hhtReportService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName hhtReportServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/3/17 18:05
 * @Version 1.0
 * @Description
 */
@Service
public class hhtReportServiceImpl implements hhtReportService {
    @Autowired
    hhtReportMapper mapper;




    @Override
    public Map<String, Object> getList(hhtReportParamDto param) {
        // 总条数
        int count = mapper.selectDataCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;
        List<hhtReportDto> reportDTOList = mapper.selectData(param);

        HashMap<String, Object> mapper = new HashMap<>();
        mapper.put("data",reportDTOList);
        mapper.put("totalPage",totalPage);
        mapper.put("count",count);
        return mapper;
    }
}
