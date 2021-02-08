package cn.com.bbut.iy.itemmaster.serviceimpl.mmPromotionSaleDaily;


import cn.com.bbut.iy.itemmaster.dao.MMPromotionSaleDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionDataDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionItemsDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionSaleDailyParamDTO;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.mmPromotionSaleDaily.MMPromotionSaleDailyService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class MMPromotionSaleDailyServiceImpl implements MMPromotionSaleDailyService {

    @Autowired
    private MMPromotionSaleDailyMapper mmPromotionSaleDailyMapper;
    @Autowired
    private CM9060Service cm9060Service;

    /**
     * 查询 mm 促销商品销售数据
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> search(MMPromotionSaleDailyParamDTO param) {
        String businessDate = cm9060Service.getValByKey("0000");

        param.setBusinessDate(businessDate);

        // 获取总条数
        int count = mmPromotionSaleDailyMapper.searchCount(param);

        // 查询数据
        List<MMPromotionDataDTO> result = mmPromotionSaleDailyMapper.search(param);

        // 获取总页数
        int totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        // 获取 商品数据
        for (MMPromotionDataDTO item : result) {
            List<MMPromotionItemsDTO> list = mmPromotionSaleDailyMapper.searchItems(param,item.getTranSerialNo(),item.getPromotionCd(),item.getAccDate());
            BigDecimal totalQty = new BigDecimal("0");
            BigDecimal totalAmt = new BigDecimal("0");
            BigDecimal totalPrice = new BigDecimal("0");
            for (MMPromotionItemsDTO mmItem : list) {
                totalQty = totalQty.add(mmItem.getSaleQty());
                totalAmt = totalAmt.add(mmItem.getSaleAmt());
                totalPrice = totalPrice.add(mmItem.getSellingPrice());
                mmItem.setSellingPrice(getPrice(item,mmItem));
            }
            item.setTotalSaleAmt(totalAmt);
            item.setTotalSellingPrice(totalPrice);
            item.setTotalSaleQty(totalQty);
            item.setList(list);
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        return map;
    }

    @Override
    public List<AutoCompleteDTO> getPromotionPattern(String v) {
        return mmPromotionSaleDailyMapper.getPromotionPattern(v);
    }

    @Override
    public List<AutoCompleteDTO> getPromotionType(String v) {
        return mmPromotionSaleDailyMapper.getPromotionType(v);
    }

    @Override
    public List<AutoCompleteDTO> getDistributionType(String v) {
        return mmPromotionSaleDailyMapper.getDistributionType(v);
    }


    /**
     * 分摊售价
     * @param item
     * @return
     */
    private BigDecimal getPrice(MMPromotionDataDTO item,MMPromotionItemsDTO mmItem) {
        // 判断分摊类型
        // 1:手动比例分摊    2:自动比例分摊    3:不分摊
        if ("1".equals(item.getPromotionAllotCd())) {
            // 手动分配比例值
            BigDecimal allotValue = mmItem.getPromotionAllotValue();
            if (allotValue==null||"".equals(allotValue)) {
                return mmItem.getSellingPrice();
            } else {
                // (促销抵扣金额 * 分摊比例) + 原售价
                BigDecimal discountPrice = item.getDiscountAmt().multiply(allotValue);
                BigDecimal price = discountPrice.add(mmItem.getSellingPrice());
                return price;
            }
        } else if ("2".equals(item.getPromotionAllotCd())) {
            // 自动分配比例
            // 分摊比例 = 商品售价/总售价
            BigDecimal allotValue = mmItem.getSellingPrice().divide(item.getTotalSellingPrice(),2, RoundingMode.HALF_UP);
            // (促销抵扣金额 * 分摊比例) + 原售价
            BigDecimal discountPrice = item.getDiscountAmt().multiply(allotValue);
            BigDecimal price = discountPrice.add(mmItem.getSellingPrice());
            return price;
        }
        return mmItem.getSellingPrice();
    }
}
