package cn.com.bbut.iy.itemmaster.service.importantgoodsale;

import cn.com.bbut.iy.itemmaster.dto.coreItem.coreItemParamDTO;

import java.util.Map;

public interface CoreItemService {
    Map<String,Object> getData(coreItemParamDTO paramDTO );
}
