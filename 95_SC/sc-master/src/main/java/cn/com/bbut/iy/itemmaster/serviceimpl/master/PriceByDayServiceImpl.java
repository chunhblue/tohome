package cn.com.bbut.iy.itemmaster.serviceimpl.master;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.dao.PriceByDayMapper;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.priceByDay.PriceByDayDTO;
import cn.com.bbut.iy.itemmaster.dto.priceByDay.PriceByDayParamDTO;
import cn.com.bbut.iy.itemmaster.dto.promotion.*;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.master.PriceByDayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PriceByDayServiceImpl implements PriceByDayService {

    @Autowired
    private PriceByDayMapper mapper;
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
     * 条件查询主档记录
     *
     * @param dto
     */
    @Override
    public GridDataDTO<PriceByDayDTO> getList(PriceByDayParamDTO dto) {
        if(StringUtils.isBlank(dto.getEffectiveDate())){
            dto.setEffectiveDate(getBusinessDate());
        }

        int count = mapper.selectCountByCondition(dto);
        if(count == 0){
            return new GridDataDTO<PriceByDayDTO>();
        }

        List<PriceByDayDTO> _list = mapper.selectListByCondition(dto);

        GridDataDTO<PriceByDayDTO> data = new GridDataDTO<PriceByDayDTO>(_list,
                dto.getPage(), count, dto.getRows());
        return data;
    }

}
