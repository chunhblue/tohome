package cn.com.bbut.iy.itemmaster.service.master;

import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.priceByDay.PriceByDayDTO;
import cn.com.bbut.iy.itemmaster.dto.priceByDay.PriceByDayParamDTO;


public interface PriceByDayService {

    /**
     *
     * 条件查询记录
     */
    GridDataDTO<PriceByDayDTO> getList(PriceByDayParamDTO dto);

}
