package cn.com.bbut.iy.itemmaster.serviceimpl.stampReport;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.StampSummaryReportMapper;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryParamReportDto;
import cn.com.bbut.iy.itemmaster.dto.StampSummaryDaily.StampSummaryReportDto;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.StampSummaryReportSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StampSummaryReportSerivceImpl implements StampSummaryReportSerivce {
    @Autowired
    private StampSummaryReportMapper mapper;

    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * Stamp Summary Report At Store Level
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> search(StampSummaryParamReportDto param) {
        // 获取业务日期
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        // 总条数
        int count = mapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<StampSummaryReportDto> reportDTOList = mapper.search(param);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",reportDTOList);
        return map;
    }


    /**
     * Stamp Detail Report At Store Level
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> searchDetail(StampSummaryParamReportDto param) {
        // 获取业务日期
        String businessDate = getBusinessDate();
        param.setBusinessDate(businessDate);

        // 总条数
        int count = mapper.searchDetailCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<StampSummaryReportDto> reportDTOList = mapper.searchDetail(param);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",reportDTOList);
        return map;
    }

    /**
     * 获取当前业务日期
     */
    private String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }
}
