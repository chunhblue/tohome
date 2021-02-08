package cn.com.bbut.iy.itemmaster.serviceimpl.inventoryQtyCor;

import cn.com.bbut.iy.itemmaster.dao.inventoryQtyCorMapper;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import cn.com.bbut.iy.itemmaster.service.inventoryQtyCor.AllRtInCorrQtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AllRtInCorrQtyServiceImpl implements AllRtInCorrQtyService {

    @Autowired
    private inventoryQtyCorMapper inventoryQtyCorMapper;
    /**
     * 获取库存的articleId,qty
     * @param voucherNo
     * @return
     */
    @Override
    public List<Sk0020DTO> getVoucherItemList(String voucherNo) {
        return inventoryQtyCorMapper.getVoucherItemList(voucherNo);
    }

    @Override
    public Sk0010DTO getVoucherHead(String voucherNo, String storeCd) {
        return inventoryQtyCorMapper.getVoucherHead(voucherNo,storeCd);
    }

    @Override
    public List<Sk0020DTO> getTranferCorrList(String voucherNo1,String storeCd,String voucherType) {
        List<Sk0020DTO> newSk02List = new ArrayList<>();
        List<Sk0010DTO> sk0010List = inventoryQtyCorMapper.getTranferHeadList(voucherNo1,storeCd,voucherType);
        if(sk0010List.size()>0){
           if(sk0010List.size()>1){
               String oldVoucherNo = sk0010List.get(1).getVoucherNo();
               List<Sk0020DTO> oldSk02List = inventoryQtyCorMapper.getVoucherItemList(oldVoucherNo);

               String newVoucherNo = sk0010List.get(0).getVoucherNo();
               newSk02List = inventoryQtyCorMapper.getVoucherItemList(newVoucherNo);
               for(Sk0020DTO oldSk020 : oldSk02List){
                   for(int i=0;i<newSk02List.size();i++){
                        if(oldSk020.getArticleId().equals(newSk02List.get(i).getArticleId())){
                            // 修正时qty1存的是转移数量-修正数量，所以这里用上一次的差异数量-本次的差异数量
                            BigDecimal varQty = oldSk020.getQty1().subtract(newSk02List.get(i).getQty1());
                            if(varQty.equals(BigDecimal.ZERO)){
                                newSk02List.remove(i);
                            }else {
                                newSk02List.get(i).setQty1(varQty);
                            }
                        }
                   }
               }
               return newSk02List;
           }else {
               newSk02List = inventoryQtyCorMapper.getVoucherItemList(sk0010List.get(0).getVoucherNo());
               for(int i=0;i<newSk02List.size();i++){
                   // 修正时qty1存的是转移数量-修正数量,所以这里取相反值
                   BigDecimal varQty = newSk02List.get(i).getQty1();
                   if(varQty.equals(BigDecimal.ZERO)){
                       newSk02List.remove(i);
                   }else {
                       newSk02List.get(i).setQty1(varQty.multiply(new BigDecimal(-1)));
                   }
               }
           }
        }
        return newSk02List;
    }


    /**
     * 获取退货的articleId,qty
     * @param orderId
     * @return
     */
    @Override
    public List<OD0010TDTO> getReturnItemList(String orderId) {
        return inventoryQtyCorMapper.getReturnItemList(orderId);
    }

    /**
     * 取收货的articleId,qty
     * orderId 在这里指的是守收货单号
     * @param orderId
     * @return
     */
    @Override
    public List<OD0010TDTO> getReceiveItemList(String orderId) {
        return inventoryQtyCorMapper.getReceiveItemList(orderId);
    }

    @Override
    public List<OD0010TDTO> getReCorrItemList(String orderId) {
        List<OD0010TDTO> list = inventoryQtyCorMapper.getReturnItemList(orderId);
        for(int i=0;i<list.size();i++){
            OD0010TDTO od0010TDTO = list.get(i);
            if(od0010TDTO.getLastCorrectionDifference() == null){
                od0010TDTO.setLastCorrectionDifference(od0010TDTO.getReceiveQty());
            }
            // 本次修正数量-上次修正数量
            BigDecimal variance = od0010TDTO.getCorrectionDifference().subtract(od0010TDTO.getLastCorrectionDifference());
            if(variance.equals(BigDecimal.ZERO)) {
                list.remove(i);
            }else {
                od0010TDTO.setReceiveQty(variance);
            }
        }
        return list;
    }

    /**
     * 盘点商品list
     * @param piCd
     * @param storeCd
     * @return
     */
    @Override
    public List<StocktakeProcessItemsDTO> getStockItemList(String piCd, String storeCd) {
        //
        return inventoryQtyCorMapper.getStockItemList(piCd,storeCd);
    }
}
