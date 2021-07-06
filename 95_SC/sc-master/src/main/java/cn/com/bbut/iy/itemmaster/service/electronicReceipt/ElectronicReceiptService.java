package cn.com.bbut.iy.itemmaster.service.electronicReceipt;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.electronicReceipt.ElectronicReceiptParam;
import cn.com.bbut.iy.itemmaster.entity.SA0060;

import java.util.List;

public interface ElectronicReceiptService {

    /**
     * 条件查询记录
     */
    List<SA0060> getList(ElectronicReceiptParam dto);

   List<AutoCompleteDTO> getReceiptType(String v, String storeCd, String posId,String startDate,String endDate);

    List<AutoCompleteDTO> getItemInfo(String v, String storeCd, String posId, String salesDate);

}
