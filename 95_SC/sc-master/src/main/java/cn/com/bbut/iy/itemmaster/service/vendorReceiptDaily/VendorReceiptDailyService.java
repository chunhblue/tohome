package cn.com.bbut.iy.itemmaster.service.vendorReceiptDaily;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.vendorReceiptDaily.VendorReceiptDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.ma0020.MA0020C;
import cn.com.bbut.iy.itemmaster.entity.ma0080.MA0080;

import java.util.List;
import java.util.Map;

public interface VendorReceiptDailyService {

    // 获取收货商品(Form 供应商)
//    Map<String,Object> search(VendorReceiptDailyParamDTO param);

    List<MA0080> getPmaList();

    List<AutoCompleteDTO> getAMList(String s, String v);

    List<AutoCompleteDTO> getOMList(String s, String v);

    List<MA0020C> getCity();

    // 获取收货商品(Form DC)
    Map<String,Object> deleteSearchDcReceipt(VendorReceiptDailyParamDTO param);
    // 获取收货商品(Form DC) 打印
    Map<String,Object> deleteGetprintData(VendorReceiptDailyParamDTO param);

    Map<String, Object> deleteSearch(VendorReceiptDailyParamDTO param);


}
