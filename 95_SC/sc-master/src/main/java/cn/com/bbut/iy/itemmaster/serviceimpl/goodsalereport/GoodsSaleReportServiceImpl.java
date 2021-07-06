package cn.com.bbut.iy.itemmaster.serviceimpl.goodsalereport;

import cn.com.bbut.iy.itemmaster.dao.goodsalereport.GoodsSaleReportMapper;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportDTO;
import cn.com.bbut.iy.itemmaster.dto.goodsalereport.goodSaleReportParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.goodsalereport.GoodsSaleReportService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
public class GoodsSaleReportServiceImpl implements GoodsSaleReportService {

    @Autowired
    private GoodsSaleReportMapper goodsSaleReportMapper;

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
    public Map<String,Object> getGoodSaleReportContent(goodSaleReportParamDTO param) {
         BigDecimal sumSaleAmount=BigDecimal.ZERO;
         BigDecimal sumSaleAmountQty=BigDecimal.ZERO;
        param.setStartDate(Utils.getTimeStamp(param.getStartDate()));
        param.setEndDate(Utils.getTimeStamp(param.getEndDate()));
        int itemSKU=0;


        // 获取总条数
        int count = goodsSaleReportMapper.searchCount(param);


        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        List<goodSaleReportDTO> goodsList = goodsSaleReportMapper.search(param);
        if (totalPage==param.getPage()){
            itemSKU=goodsSaleReportMapper.getArticleCount(param);
            sumSaleAmount=goodsSaleReportMapper.getTotalSaleAmount(param);
            param.setFlg(false);
            List<goodSaleReportDTO> goodsList1 = goodsSaleReportMapper.search(param);
            for (goodSaleReportDTO reportdto :goodsList1) {
                reportdto.setSaleDate(formatDate2(reportdto.getSaleDate()));
                reportdto.setAccDate(formatDate1(reportdto.getAccDate()));
//                sumSaleAmount=sumSaleAmount.add(new BigDecimal(reportdto.getSaleAmount()));
                sumSaleAmountQty=sumSaleAmountQty.add(reportdto.getSaleQty());
            }
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",goodsList);
        map.put("totalItemSKU",itemSKU);
        map.put("totalSaleAmount",formatNum(sumSaleAmount.toString()));
        map.put("totalSaleQty",formatNum(sumSaleAmountQty.toString()));
        return map;
    }

    @Override
    public Map<String, Object> getTotalSaleAmount(goodSaleReportParamDTO param) {
        Map<String,Object> map = new HashMap<String,Object>();
        param.setStartDate(Utils.getTimeStamp(param.getStartDate()));
        param.setEndDate(Utils.getTimeStamp(param.getEndDate()));
        BigDecimal totalSaleAmount = goodsSaleReportMapper.getTotalSaleAmount(param);
        map.put("totalSaleAmount",totalSaleAmount);
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
    public static String formatNum(String num) {
        if (org.apache.commons.lang.StringUtils.isBlank(num)) {
            return "";
        }
        int i = new BigDecimal(num).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        DecimalFormat df = new DecimalFormat("###,###");
        String result = df.format(i);
        return result;
    }
}
