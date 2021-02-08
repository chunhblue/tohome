package cn.com.bbut.iy.itemmaster.serviceimpl.orderreport;


import cn.com.bbut.iy.itemmaster.dao.OrderReportMapper;

import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportDTO;
import cn.com.bbut.iy.itemmaster.dto.orderReport.OrderReportParamDTO;
import cn.com.bbut.iy.itemmaster.service.orderreport.OrderReportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName OrderReportServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/7/31 17:16
 * @Version 1.0
 */
@Service
public class OrderReportServiceImpl implements OrderReportService {
@Autowired
private OrderReportMapper orderReportMapper;


    @Override
    public Map<String, Object> getData(OrderReportParamDTO param) {

        if (param == null) {
            param = new OrderReportParamDTO();
        }
       List<OrderReportDTO> addItemList=new ArrayList<>();
        List<OrderReportDTO> items = orderReportMapper.getOrderInfo(param);
        for (OrderReportDTO item : items) {
            item.setArticleId(param.getArticleId());
            item.setArticleName(param.getArticleName());
            item.setDepCd(param.getDepCd());
            item.setCategoryCd(param.getCategoryCd());
            item.setSubCategoryCd(param.getSubCategoryCd());
            item.setAm(param.getAm());
            List<OrderReportDTO> addItem = orderReportMapper.getItemInfo(item);
            for (OrderReportDTO dtoam : addItem) {
                dtoam.setOrderDate(formatDate(item.getOrderDate()));
                addItemList.add(dtoam);
            }
        }
        for (OrderReportDTO dto : addItemList) {
            if (!dto.getAutoOrderQty().equals(new BigDecimal(0)) && !dto.getVarianceQty().equals(new BigDecimal(0))) {
                BigDecimal autoOrderQty = dto.getAutoOrderQty();
                BigDecimal varianceQty = dto.getVarianceQty();
                BigDecimal divide = autoOrderQty.divide(varianceQty);
                dto.setVariance(divide);
            } else {
                dto.setVariance(new BigDecimal(0));
            }
        }
        int  start=(param.getPage() - 1) * param.getRows();
         int end=param.getPage()* param.getRows();
         if (end>addItemList.size()){
             end=addItemList.size();
         }
        int dataCount =addItemList.size();
        int totalPage = ( dataCount % param.getRows() == 0) ? ( dataCount / param.getRows()) : ( dataCount / param.getRows()) + 1;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",  dataCount);
        map.put("data", addItemList.subList(start,end));
        return map;
    }
    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }
}
