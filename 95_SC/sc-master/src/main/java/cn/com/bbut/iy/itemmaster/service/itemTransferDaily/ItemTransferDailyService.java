package cn.com.bbut.iy.itemmaster.service.itemTransferDaily;

import cn.com.bbut.iy.itemmaster.dto.inventory.Sk0020DTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;

import java.util.List;
import java.util.Map;

public interface ItemTransferDailyService {

    Map<String,Object> search(VendorReceiptDailyParamDTO param);

    List<Sk0020DTO> getPrintData(VendorReceiptDailyParamDTO param);
}
