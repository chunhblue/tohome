package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface inventoryQtyCorMapper {

    List<Sk0020DTO> getVoucherItemList(String voucherNo);


    List<OD0010TDTO> getReturnItemList(String orderId);


    List<OD0010TDTO> getReceiveItemList(String orderId);

    List<StocktakeProcessItemsDTO> getStockItemList(String piCd, String storeCd);

    Sk0010DTO getVoucherHead(String voucherNo, String storeCd);

    List<Sk0010DTO> getTranferHeadList(String voucherNo1,String storeCd,String voucherType);
}
