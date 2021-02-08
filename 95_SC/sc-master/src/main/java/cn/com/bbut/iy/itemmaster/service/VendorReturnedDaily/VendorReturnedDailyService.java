package cn.com.bbut.iy.itemmaster.service.VendorReturnedDaily;

import cn.com.bbut.iy.itemmaster.constant.ConstantsCache;
import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.VendorReturnedDaily.VendorReturnedDailyParamDTO;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

public interface VendorReturnedDailyService {

    /**
     * 供应商退货商品
     * @param param
     * @return
     */
    @Cacheable(value = ConstantsCache.CACHE_PMA, key = "#param.toString()")
    Map<String,Object> deleteSearch(VendorReturnedDailyParamDTO param);

    List<VendorReturnedDailyDTO> deleteVendorSearchPrint(VendorReturnedDailyParamDTO param);

    List<AutoCompleteDTO> getAMList(String flag, String v);

    List<AutoCompleteDTO> getOMList(String s, String v);

    /**
     * DC退货商品日报
     * @param param
     * @return
     */
    Map<String,Object> deleteDcDailySearch(VendorReturnedDailyParamDTO param);

   // List<VendorReturnedDailyDTO> dcDailySearchPrint(VendorReturnedDailyParamDTO param);

   // List<VendorReturnedDailyDTO> DeletedcDailySearchPrint(VendorReturnedDailyParamDTO param);

    List<VendorReturnedDailyDTO> deleteDcDailySearchPrint(VendorReturnedDailyParamDTO param);
}
