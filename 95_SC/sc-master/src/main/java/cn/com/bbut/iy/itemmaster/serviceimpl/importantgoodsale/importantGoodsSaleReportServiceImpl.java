package cn.com.bbut.iy.itemmaster.serviceimpl.importantgoodsale;

import cn.com.bbut.iy.itemmaster.dao.importantgoodsale.importantGoodsSaleReportMapper;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.importantgoodsale.importantgoodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.importantgoodsale.importantGoodsSaleReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GoodsSaleReportServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/30 14:11
 * @Version 1.0
 */
@Service
public class importantGoodsSaleReportServiceImpl implements importantGoodsSaleReportService {

    @Autowired
    private importantGoodsSaleReportMapper goodsSaleReportMapper;



    @Override
    public List<MA0080> getPamList() {

        return  goodsSaleReportMapper.getPamList();

    }

    @Override
    public List<Ma1000> getAMlist() {

        return  goodsSaleReportMapper.getAMlist();
    }

    @Override
    public List<MA0020C> getClityList() {
        return goodsSaleReportMapper.getCityList();
    }

    @Override
    public Map<String,Object> getGoodSaleReportContent(importantgoodSaleReportParamDTO param) {
        // 获取总条数
        int count = goodsSaleReportMapper.searchCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<importantgoodSaleReportDTO> goodsList = goodsSaleReportMapper.search(param);
        for (int i = 0; i <goodsList.size(); i++) {
            goodsList.get(i).setSaleDate(formatDate2( goodsList.get(i).getSaleDate()));
            goodsList.get(i).setAccDate(formatDate2( goodsList.get(i).getSaleDate()));
            goodsList.get(i).setSeqNo(i+1);
        }
//        for (importantgoodSaleReportDTO reportdto :goodsList) {
//            reportdto.setSaleDate(formatDate2(reportdto.getSaleDate()));
//            reportdto.setAccDate(formatDate1(reportdto.getAccDate()));
//        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",goodsList);
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
