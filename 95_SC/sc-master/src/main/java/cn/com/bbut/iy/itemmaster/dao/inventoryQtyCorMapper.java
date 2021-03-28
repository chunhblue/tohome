package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.od0000_t.OD0000TDTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface inventoryQtyCorMapper {

    List<Sk0020DTO> getVoucherItemList(String voucherNo);

    /**
     * 获取收退货修正头档信息
     * @param orderId
     * @return
     */
    OD0000 getReCorrHead(String orderId);

    List<OD0000> getReCorrList(String orgorderId);

    OD0000TDTO getReceiveCorrHead(String orderId);

    List<OD0010TDTO> getReItemList(String orderId);


    List<OD0010TDTO> getReceiveItemList(String orderId);

    List<StocktakeProcessItemsDTO> getStockItemList(String piCd, String storeCd);

    Sk0010DTO getVoucherHead(String voucherNo, String storeCd);

    List<Sk0010DTO> getTranferHeadList(String voucherNo1,String storeCd,String voucherType);

    int updateStockLastVariance(String piCd, String storeCd);
}
