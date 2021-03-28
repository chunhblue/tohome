package cn.com.bbut.iy.itemmaster.serviceimpl.cashierDetail;

import cn.com.bbut.iy.itemmaster.dao.CashierDetailMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.service.cashierDetail.CashierDetailService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CashierDetailServiceImpl implements CashierDetailService {
    @Autowired
    private CashierDetailMapper cashierDetailMapper;

    @Override
    public List<PayMethod> getAllPay() {
        return cashierDetailMapper.getAllPay();
    }

    @Override
    public GridDataDTO<SaleHead> getSaleHeadList(CashierDetailParam dto) {
        dto.setStartDate(getTimeStamp(dto.getStartDate()));
        dto.setEndDate(getTimeStamp(dto.getEndDate()));
        List<SaleHead> list = cashierDetailMapper.getSaleHead(dto);
        long count = cashierDetailMapper.getSaleHeadCount(dto);
        return new GridDataDTO<>(list, dto.getPage(), count,
                dto.getRows());
    }

    // "yyyyMMdd" -->  "yyyy-MM-dd"
    public String getTimeStamp(String date){
        String str = "";
        if(date == null || "".equals(date) || date.length()<8){
            return str;
        }
        str = date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6,8);
        return str;
    }

    @Override
    public GridDataDTO<SaleDetail> getSaleDetailList(SaleHeadParam param) {
        List<SaleDetail> list = cashierDetailMapper.getSaleDetail(param);
        long count = cashierDetailMapper.getSaleDetailCount(param);
        return new GridDataDTO<>(list, param.getPage(), count,
                param.getRows());
    }
    @Override
    public SaleDetail getSaleDetailTotal(SaleHeadParam param){
        SaleDetail saleDetail = new SaleDetail();
        List<SaleDetail> list = cashierDetailMapper.getSaleDetailTotal(param);
        if(list.size() == 0)
            return saleDetail;
        if(list.size() > 1){
            for(int i = 0;i < list.size()-1;i++){
                list.get(i+1).setSaleQty(list.get(i+1).getSaleQty().add(list.get(i).getSaleQty()));
                list.get(i+1).setSaleAmount(list.get(i+1).getSaleAmount().add(list.get(i).getSaleAmount()));
                list.get(i+1).setDiscountAmount(list.get(i+1).getDiscountAmount().add(list.get(i).getDiscountAmount()));
                saleDetail.setSaleQty(list.get(i+1).getSaleQty());
                saleDetail.setSaleAmount(list.get(i+1).getSaleAmount());
                saleDetail.setDiscountAmount(list.get(i+1).getDiscountAmount());
            }
        }else {
            saleDetail.setSaleQty(list.get(0).getSaleQty());
            saleDetail.setSaleAmount(list.get(0).getSaleAmount());
            saleDetail.setDiscountAmount(list.get(0).getDiscountAmount());
        }
        return saleDetail;
    }

    @Override
    public List<PayMethod> getPayDetail(SaleHeadParam param) {
        return cashierDetailMapper.getPayDetail(param);
    }

    /**
     * 根据选中得 店铺和pos id 获得收银员信息
     */
    @Override
    public List<SA0050> getCashier(String storeCd, String posId) {
        return cashierDetailMapper.getCashier(storeCd,posId);
    }

    @Override
    public Map getTotalAmount(CashierDetailParam param) {
        param.setStartDate(Utils.getTimeStamp(param.getStartDate()));
        param.setEndDate(Utils.getTimeStamp(param.getEndDate()));
        BigDecimal totalAmount = cashierDetailMapper.getSaleHeadAmount(param);
        HashMap<String, Object> map = new HashMap<>();
        map.put("totalAmount",totalAmount);
        return map;
    }
}
