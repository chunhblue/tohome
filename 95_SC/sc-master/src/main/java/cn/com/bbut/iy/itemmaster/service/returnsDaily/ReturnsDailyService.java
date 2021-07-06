package cn.com.bbut.iy.itemmaster.service.returnsDaily;

import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;

import java.util.List;
import java.util.Map;

public interface ReturnsDailyService {
    // 查询退货数据
    Map<String,Object> getPrintDate(ReturnsDailyParamDTO param);

    Map<String,Object> search(ReturnsDailyParamDTO param);
    // 根据主键查询店铺信息
    Ma1000 selectByStoreCd(String storeCd);
}
