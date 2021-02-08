package cn.com.bbut.iy.itemmaster.service.ma2000;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.VendorDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;

import java.util.Collection;
import java.util.List;

public interface Ma2000Service {
    List<AutoCompleteDTO> getListAll(String v,String storeCd);

    MA2000 getVendorById(String v);

    List<AutoCompleteDTO> getAllVendor(String vendorId);
}
