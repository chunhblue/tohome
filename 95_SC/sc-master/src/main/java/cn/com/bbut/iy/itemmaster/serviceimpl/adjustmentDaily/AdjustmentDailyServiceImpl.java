package cn.com.bbut.iy.itemmaster.serviceimpl.adjustmentDaily;

import cn.com.bbut.iy.itemmaster.dao.AdjustmentDailyMapper;
import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.inventory.InventoryVouchersMapper;
import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.adjustmentDaily.AdjustmentDailyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门店报废日报
 * 
 * @author mxy
 */
@Slf4j
@Service
public class AdjustmentDailyServiceImpl implements AdjustmentDailyService {

    @Autowired
    private AdjustmentDailyMapper adjustmentDailyMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;
    @Autowired
    private InventoryVouchersMapper inventoryVouchersMapper;

    /**
     * 获取当前业务日期
     */
    public String getBusinessDate() {
        Cm9060 dto =  cm9060Mapper.selectByPrimaryKey("0000");
        return dto.getSpValue();
    }

    /**
     * 条件查询数据
     *
     * @param dto
     * @return
     */
    @Override
    public Map<String,Object> deleteGetList(AdjustmentDailyParamDTO dto) {

        dto.setBusinessDate(getBusinessDate());
        // 获取总条数
        int count = adjustmentDailyMapper.selectCountByCondition(dto);
        int totalItemSKU1 = adjustmentDailyMapper.getCountItemSKU(dto);

        // 获取总页数
        int totalPage = (count % dto.getRows() == 0) ? (count / dto.getRows()) : (count / dto.getRows()) + 1;

        List<AdjustmentDailyDTO> result = adjustmentDailyMapper.selectListByCondition(dto);

        if(result == null){
            log.info("<<<<<<<<<< list is null");
        }else {
            for (AdjustmentDailyDTO adjustDto : result) {
                AdjustmentDailyDTO generalReason = adjustmentDailyMapper.getGeneralReason(adjustDto.getAdjustReason());
                if(generalReason != null && !generalReason.getGeneralReasonText().equals("")) {
                    adjustDto.setGeneralReasonText(generalReason.getGeneralReasonText());
                }

            }
        }
        // 获取list总数量
        AdjustmentDailyDTO adDto = getItemQty(dto);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        map.put("totalItemSKU1",totalItemSKU1);
        map.put("ItemQty",adDto.getAdjustmentQty());

        return map;
    }

    /**
     * 条件查询数据
     *
     * @param dto
     * @return
     */
    @Override
    public List<AdjustmentDailyDTO> deleteGetList1(AdjustmentDailyParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        dto.setFlg(false);
        List<AdjustmentDailyDTO> result = adjustmentDailyMapper.selectListByCondition(dto);
        if(result == null){
            log.info("<<<<<<<<<< result is null");
        }else {
            for (AdjustmentDailyDTO adjustDto : result) {
                AdjustmentDailyDTO generalReason = adjustmentDailyMapper.getGeneralReason(adjustDto.getAdjustReason());
                if(generalReason != null && !generalReason.getGeneralReasonText().equals("")) {
                    adjustDto.setGeneralReasonText(generalReason.getGeneralReasonText());
                }
            }
        }
   return result;
    }

    @Override
    public AdjustmentDailyDTO getItemQty(AdjustmentDailyParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        int itemQty = 0;
        dto.setFlg(false);
        List<AdjustmentDailyDTO> AllItemQty=adjustmentDailyMapper.selectListByCondition(dto);
        for (AdjustmentDailyDTO item:AllItemQty) {
            BigDecimal bigDecimal = item.getAdjustmentQty();
            itemQty+=bigDecimal.intValue();
        }
        int totalItemSKU1 = adjustmentDailyMapper.getCountItemSKU(dto);
        AdjustmentDailyDTO adDto = new AdjustmentDailyDTO();
        BigDecimal num = BigDecimal.valueOf(itemQty);
        adDto.setAdjustmentQty(num);
        adDto.setRecords(totalItemSKU1);
        return adDto;
    }
    @Override
    public AdjustmentDailyDTO deleteGetItemQty(AdjustmentDailyParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        int itemQty = 0;
        dto.setFlg(false);
        List<AdjustmentDailyDTO> AllItemQty=adjustmentDailyMapper.selectListByCondition(dto);
        for (AdjustmentDailyDTO item:AllItemQty) {
            BigDecimal bigDecimal = item.getAdjustmentQty();
            itemQty+=bigDecimal.intValue();
        }
        int totalItemSKU1 = adjustmentDailyMapper.getCountItemSKU(dto);
        AdjustmentDailyDTO adDto = new AdjustmentDailyDTO();
        BigDecimal num = BigDecimal.valueOf(itemQty);
        adDto.setAdjustmentQty(num);
        adDto.setRecords(totalItemSKU1);
        return adDto;
    }
}
