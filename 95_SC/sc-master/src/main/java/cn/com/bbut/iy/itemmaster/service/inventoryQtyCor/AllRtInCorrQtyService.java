package cn.com.bbut.iy.itemmaster.service.inventoryQtyCor;


import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0010DTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.od0010_t.OD0010TDTO;
import cn.com.bbut.iy.itemmaster.dto.receipt.vendor.VendorDetailsGridDTO;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsGridResult;
import cn.com.bbut.iy.itemmaster.dto.stocktakeProcess.StocktakeProcessItemsDTO;

import java.util.List;

public interface AllRtInCorrQtyService {
    /**
     * 获取库存的articleId,qty
     * @param voucherNo
     * @return
     */
    List<Sk0020DTO> getVoucherItemList(String voucherNo);

    Sk0010DTO getVoucherHead(String voucherNo,String storeCd);

    List<Sk0020DTO> getTranferCorrList(String voucherNo1,String storeCd,String voucherType);
    /**
     * 获取退货的articleId,qty
     * @param orderId
     * @return
     */
    List<OD0010TDTO> getReturnItemList(String orderId);

    List<OD0010TDTO> getReceiveItemList(String orderId);

    List<OD0010TDTO> getReCorrItemList(String orderId);

    /**
     * 盘点商品list
     * @param piCd
     * @param storeCd
     * @return
     */
    List<StocktakeProcessItemsDTO> getStockItemList(String piCd,String storeCd);
}
