package cn.com.bbut.iy.itemmaster.service.mmPromotionSaleDaily;

import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.mmPromotionSaleDaily.MMPromotionSaleDailyParamDTO;

import java.util.List;
import java.util.Map;

public interface MMPromotionSaleDailyService {

    Map<String, Object> search(MMPromotionSaleDailyParamDTO param);

    List<AutoCompleteDTO> getPromotionPattern(String v);

    List<AutoCompleteDTO> getPromotionType(String v);

    List<AutoCompleteDTO> getDistributionType(String v);
}
