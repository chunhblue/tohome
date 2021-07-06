package cn.com.bbut.iy.itemmaster.serviceimpl.hhReport;

import cn.com.bbut.iy.itemmaster.dao.hhtReport.hhtReportMapper;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportDto;
import cn.com.bbut.iy.itemmaster.dto.hhtReport.hhtReportParamDto;
import cn.com.bbut.iy.itemmaster.service.hhtReportService.hhtReportService;
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
        List<hhtReportDto> reportDTOList1 = mapper.selectDataToal(param);
        List<hhtReportDto> reportDTOList2 = mapper.selectReceivingPend(param);
        List<hhtReportDto> reportDTOList3 = mapper.selectApproved(param);
        List<hhtReportDto> reportDTOList4 = mapper.selectReceived(param);
        for (hhtReportDto dto : reportDTOList1) {
            for (hhtReportDto dto1 : reportDTOList2) {
//                if (dto1.getStoreCd().equals(dto.getStoreCd()) && dto1.getOrderDate().equals(dto.getOrderDate())) {
                if (dto1.getStoreCd().equals(dto.getStoreCd())) {
                    if (dto.getNoOfPendingGrpo() == null  || dto.getNoOfPendingGrpo()==0 ){
                        dto.setNoOfPendingGrpo(dto1.getNoOfPendingGrpo());
                    }
                } else {
                    dto.setNoOfPendingGrpo(0);
                }
            }
            for (hhtReportDto dto1 : reportDTOList3) {
//                if (dto1.getStoreCd().equals(dto.getStoreCd()) && dto1.getOrderDate().equals(dto.getOrderDate())) {
                if (dto1.getStoreCd().equals(dto.getStoreCd())) {
                    if (dto.getGrpoOfQty() == null  || dto.getGrpoOfQty()==0 ){
                        dto.setGrpoOfQty(dto1.getGrpoOfQty());
                    }
                } else {
                    if (dto.getGrpoOfQty() == null  || dto.getGrpoOfQty()==0 ){
                        dto.setGrpoOfQty(0);
                    }else {
                        dto.setGrpoOfQty(dto.getGrpoOfQty());
                    }

                }
            }
            for (hhtReportDto dto1 : reportDTOList4) {
//                if (dto1.getStoreCd().equals(dto.getStoreCd()) && dto1.getOrderDate().equals(dto.getOrderDate())) {
                if (dto1.getStoreCd().equals(dto.getStoreCd())) {
                    if (dto.getNoOfConfiPo() == null  || dto.getNoOfConfiPo()==0 ){
                        dto.setNoOfConfiPo(dto1.getNoOfConfiPo());
                    }
                } else {
                    if (dto.getNoOfConfiPo() == null  || dto.getNoOfConfiPo()==0 ){
                        dto.setNoOfConfiPo(dto1.getNoOfConfiPo());
                    }else {
                        dto.setNoOfConfiPo(dto.getNoOfConfiPo());
                    }
                }
            }
        }

        HashMap<String, Object> mapper = new HashMap<>();
        mapper.put("data", reportDTOList1);
        mapper.put("totalPage", totalPage);
        mapper.put("count", count);
        return mapper;
    }
}
