package cn.com.bbut.iy.itemmaster.serviceimpl.cashierDetail;

import cn.com.bbut.iy.itemmaster.dao.CashierDetailMapper;
import cn.com.bbut.iy.itemmaster.dao.MA4160DTOMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.cashierDetail.*;
import cn.com.bbut.iy.itemmaster.entity.User;
import cn.com.bbut.iy.itemmaster.entity.sa0050.SA0050;
import cn.com.bbut.iy.itemmaster.service.audit.IAuditService;
import cn.com.bbut.iy.itemmaster.service.cashierDetail.CashierDetailService;
import cn.com.bbut.iy.itemmaster.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CashierDetailServiceImpl implements CashierDetailService {
    @Autowired
    private CashierDetailMapper cashierDetailMapper;
    @Autowired
    private MA4160DTOMapper ma4160DTOMapper;
    @Autowired
    private IAuditService auditServiceImpl;
    @Override
    public List<PayMethod> getAllPay() {
        return cashierDetailMapper.getAllPay();
    }

    @Override
    public GridDataDTO<SaleHead> getSaleHeadList(CashierDetailParam dto) {
        dto.setStartDate(Utils.getDateTime(dto.getStartDate()));
        dto.setEndDate(Utils.getDateTime(dto.getEndDate()));
        if(dto.getBillSaleNo() != null && !"".equals(dto.getBillSaleNo())){
            if(dto.getBillSaleNo().contains("NRISV")){
                dto.setBillFlg(1);
            }else {
                dto.setBillFlg(0);
            }
        }
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
    public Map getTotalAmount(String userId, CashierDetailParam param) {
        HashMap<String, Object> map = new HashMap<>();
        Integer post=0;
        // SM 权限

        List<Integer> aMcount = ma4160DTOMapper.getPositionList(param.getStoreCd(),userId);
        for (Integer dto1  :aMcount) {
             if (dto1 !=4){
                 post=dto1;
             }
        }
        if (post>0){
            param.setStartDate(Utils.getDateTime(param.getStartDate()));
            param.setEndDate(Utils.getDateTime(param.getEndDate()));
            BigDecimal totalAmount = cashierDetailMapper.getSaleHeadAmount(param);
            map.put("totalAmount", totalAmount);
            map.put("status", "block");

        }else {
            map.put("status", "none");
        }
        if (param.getBillSaleNo() != null && !"".equals(param.getBillSaleNo())) {
            if (param.getBillSaleNo().contains("NRISV")) {
                param.setBillFlg(1);
            } else {
                param.setBillFlg(0);
            }
        }

        return map;
    }
}
