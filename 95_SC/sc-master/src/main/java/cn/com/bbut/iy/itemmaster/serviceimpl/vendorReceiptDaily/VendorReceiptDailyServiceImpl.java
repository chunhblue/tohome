package cn.com.bbut.iy.itemmaster.serviceimpl.vendorReceiptDaily;

import cn.com.bbut.iy.itemmaster.dao.VendorReceiptDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.vendorReceiptDaily.VendorReceiptDailyService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VendorReceiptDailyServiceImpl implements VendorReceiptDailyService {

    @Autowired
    private VendorReceiptDailyMapper vendorReceiptDailyMapper;

    @Autowired
    private CM9060Service cm9060Service;

    // 获取收货商品(Form 供应商)
    @Override
    public Map<String,Object> deleteSearch(VendorReceiptDailyParamDTO param) {

        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);

        // 总条数
        int count = 0;
        // 获取总页数
        int totalPage = 0;
        if (param.isFlg()) {
//            // 创建前先删除临时表 2021/1/30
           vendorReceiptDailyMapper.deleteTempTable("tmp_order_article");
            // 总条数
            count = vendorReceiptDailyMapper.SearchCount(param);
            // 获取总页数
            totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;
        }
//        // 创建前先删除临时表 2021/1/30
        vendorReceiptDailyMapper.deleteTempTable("tmp_order_article");
        List<VendorReceiptDailyDTO> result = vendorReceiptDailyMapper.search(param);
        for (VendorReceiptDailyDTO item : result) {
            item.setReceiveDate(formatDate(item.getReceiveDate()));
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        return map;
    }

    /**
     * 格式化日期
     * @param piDate
     * @return
     */
    private String formatDate(String piDate) {
        if (StringUtils.isEmpty(piDate)) {
            return "";
        }
        String year = piDate.substring(0, 4);
        String month = piDate.substring(4, 6);
        String day = piDate.substring(6, 8);
        return day+"/"+month+"/"+year;
    }

    @Override
    public List<MA0080> getPmaList() {
        return vendorReceiptDailyMapper.getPmaList();
    }

    @Override
    public List<AutoCompleteDTO> getAMList(String flag, String v) {
        return vendorReceiptDailyMapper.getAMList(flag,v);
    }

    @Override
    public List<AutoCompleteDTO> getOMList(String flag, String v) {
        return vendorReceiptDailyMapper.getOMList(flag,v);
    }

    @Override
    public List<MA0020C> getCity() {
        return vendorReceiptDailyMapper.getCity();
    }

    // 获取收货商品(Form DC)
    @Override
    public Map<String,Object> deleteSearchDcReceipt(VendorReceiptDailyParamDTO param) {
        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);
        Integer ItemQty=0;

        // 获取总条数
        vendorReceiptDailyMapper.deleteTempTable("tmp_dc_order_article");
        int count = vendorReceiptDailyMapper.searchDcReceiptCount(param);
        int totalItemSKU = vendorReceiptDailyMapper.getItemCount(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;
        vendorReceiptDailyMapper.deleteTempTable("tmp_dc_order_article");
        List<VendorReceiptDailyDTO> result = vendorReceiptDailyMapper.searchDcReceipt(param);
        param.setFlg(false);
        vendorReceiptDailyMapper.deleteTempTable("tmp_dc_order_article");
        List<VendorReceiptDailyDTO> AllArticle = vendorReceiptDailyMapper.searchDcReceipt(param);
        for (VendorReceiptDailyDTO item:AllArticle) {
            BigDecimal bigDecimal = new BigDecimal(item.getReceiveTotalQty());
            ItemQty+=bigDecimal.intValue();
        }
        for (VendorReceiptDailyDTO item : result) {
            item.setReceiveDate(formatDate(item.getReceiveDate())); }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        map.put("totalItemSKU",totalItemSKU);
        map.put("totalReceiveQty",ItemQty);
        return map;
    }

    @Override
    public Map<String,Object> deleteGetprintData(VendorReceiptDailyParamDTO param) {
        String date = cm9060Service.getValByKey("0000");
        param.setBusinessDate(date);
        Integer ItemQty=0;
        // 获取总条数
        vendorReceiptDailyMapper.deleteTempTable("tmp_dc_order_article");
//        List<VendorReceiptDailyDTO> result = vendorReceiptDailyMapper.getprintData(param);
        param.setFlg(false);
        List<VendorReceiptDailyDTO> result = vendorReceiptDailyMapper.searchDcReceipt(param);
        for (VendorReceiptDailyDTO item : result) {
            item.setReceiveDate(formatDate(item.getReceiveDate()));
        }
        param.setFlg(false);
        vendorReceiptDailyMapper.deleteTempTable("tmp_dc_order_article");
        List<VendorReceiptDailyDTO> AllArticle = vendorReceiptDailyMapper.searchDcReceipt(param);
        for (VendorReceiptDailyDTO item:AllArticle) {
            BigDecimal bigDecimal = new BigDecimal(item.getReceiveTotalQty());
            ItemQty+=bigDecimal.intValue();
        }
        int totalItemSKU = vendorReceiptDailyMapper.getItemCount(param);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("data",result);
        map.put("totalItemSKU",totalItemSKU);
        map.put("totalReceiveQty",ItemQty);
        return map;
    }

}
