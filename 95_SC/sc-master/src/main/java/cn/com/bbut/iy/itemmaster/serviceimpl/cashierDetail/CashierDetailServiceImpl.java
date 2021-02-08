package cn.com.bbut.iy.itemmaster.serviceimpl.cashierDetail;

import cn.com.bbut.iy.itemmaster.dao.CashierDetailMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.service.cashierDetail.CashierDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.rmi.runtime.Log;

import java.util.List;

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
        List<SaleHead> list = cashierDetailMapper.getSaleHead(dto);
        long count = cashierDetailMapper.getSaleHeadCount(dto);
        return new GridDataDTO<>(list, dto.getPage(), count,
                dto.getRows());
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
}
