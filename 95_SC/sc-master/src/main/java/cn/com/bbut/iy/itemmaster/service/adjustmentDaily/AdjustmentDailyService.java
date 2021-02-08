package cn.com.bbut.iy.itemmaster.service.adjustmentDaily;

import java.util.List;
import java.util.Map;

import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.*;

/**
 * @author mxy
 */
public interface AdjustmentDailyService {

    /**
     * 条件查询数据
     * @param dto
     * @return
     */
    Map<String,Object> deleteGetList(AdjustmentDailyParamDTO dto);

    List<AdjustmentDailyDTO> deleteGetList1(AdjustmentDailyParamDTO dto);

     AdjustmentDailyDTO getItemQty(AdjustmentDailyParamDTO dto);

    AdjustmentDailyDTO deleteGetItemQty(AdjustmentDailyParamDTO jsonParam);

//    List<AdjustmentDailyDTO> deleteGetList1(AdjustmentDailyParamDTO param);
}
