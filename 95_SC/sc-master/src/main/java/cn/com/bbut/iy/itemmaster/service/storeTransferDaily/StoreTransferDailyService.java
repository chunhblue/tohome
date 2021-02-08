package cn.com.bbut.iy.itemmaster.service.storeTransferDaily;

import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;

import java.util.List;
import java.util.Map;

public interface StoreTransferDailyService {

    // 查询门店调拨商品
    Map<String,Object> search(VendorReceiptDailyParamDTO param);

    // 查询门店库存数据
    Map<String,Object> getList(VendorReceiptDailyParamDTO param);

    List<Sk0020DTO> getListPrintData(VendorReceiptDailyParamDTO param);

    List<Sk0020DTO> getPrintData(VendorReceiptDailyParamDTO param);

    // 获取调拨总数量
    Sk0020DTO getTranferQty(VendorReceiptDailyParamDTO param);
}
