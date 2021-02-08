package cn.com.bbut.iy.itemmaster.serviceimpl.writeOff;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.WriteOffMapper;
import cn.com.bbut.iy.itemmaster.dto.adjustmentDaily.AdjustmentDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffDTO;
import cn.com.bbut.iy.itemmaster.dto.writeOff.WriteOffParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.writeOff.WriteOffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class WriteOffServiceImpl implements WriteOffService {

    @Autowired
    private WriteOffMapper writeOffMapper;
    @Autowired
    private Cm9060Mapper cm9060Mapper;


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
    public Map<String,Object> deleteGetList(WriteOffParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());

        // 获取总条数
        int count = writeOffMapper.selectCountByCondition(dto);

        WriteOffDTO offDTO = this.deleteGetOffQty(dto);

        // 获取总页数
        int totalPage = (count % dto.getRows() == 0) ? (count / dto.getRows()) : (count / dto.getRows()) + 1;

        List<WriteOffDTO> result = writeOffMapper.selectListByCondition(dto);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",result);
        map.put("ItemQty",offDTO.getWriteOffQty());
        map.put("itemSaleQty",offDTO.getSaleQty());
        map.put("totalItem",offDTO.getRecords());
        return map;
    }

    @Override
    public List<WriteOffDTO> deleteGetList1(WriteOffParamDTO dto) {
        dto.setBusinessDate(getBusinessDate());
        return writeOffMapper.selectListByCondition(dto);

    }

    @Override
    public WriteOffDTO deleteGetOffQty(WriteOffParamDTO dto){
        dto.setBusinessDate(getBusinessDate());
        Integer ItemQty=0;
        Integer saleQty=0;
        dto.setFlg(false);
        int countItemSku = writeOffMapper.getCountItemSku(dto);
        List<WriteOffDTO> ItemAllQty = writeOffMapper.selectListByCondition(dto);
        for (WriteOffDTO item:ItemAllQty) {
            BigDecimal bigDecimal =item.getWriteOffQty();
            BigDecimal bigSaleQty =item.getSaleQty();
            ItemQty+=bigDecimal.intValue();
            saleQty+=bigSaleQty.intValue();
        }
        WriteOffDTO offDTO = new WriteOffDTO();
        offDTO.setWriteOffQty(BigDecimal.valueOf(ItemQty));
        offDTO.setSaleQty(BigDecimal.valueOf(saleQty));
        offDTO.setRecords(countItemSku);
        return offDTO;
    }
}
