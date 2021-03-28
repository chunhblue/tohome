package cn.com.bbut.iy.itemmaster.serviceimpl.importantgoodsale;

import cn.com.bbut.iy.itemmaster.dao.SA0050Mapper;
import cn.com.bbut.iy.itemmaster.dao.importantgoodsale.CoreItemGoodMapper;
import cn.com.bbut.iy.itemmaster.dto.coreItem.coreItemDTO;
import cn.com.bbut.iy.itemmaster.dto.coreItem.coreItemParamDTO;
import cn.com.bbut.iy.itemmaster.service.importantgoodsale.CoreItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CoreItemerviceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/8/5 19:46
 * @Version 1.0
 */
@Service
public class CoreItemerviceImpl implements CoreItemService {
    @Autowired
    CoreItemGoodMapper mapper;
    @Override

    public Map<String, Object> getData(coreItemParamDTO paramDTO) {
        List<coreItemDTO> datafor=null;
        Integer dataforCoun=0;
        if (paramDTO.getCoreItemType().equals("01")) {
            datafor= mapper.getDataforg(paramDTO);
            dataforCoun=mapper.getDataforgCount(paramDTO);

        }
        if (paramDTO.getCoreItemType().equals("02")){
            datafor = mapper.getDataforcity(paramDTO);
            dataforCoun = mapper.getDataforcityCount(paramDTO);
        }
        if (paramDTO.getCoreItemType().equals("03")){
            datafor= mapper.getDatafordistrict(paramDTO);
            dataforCoun= mapper.getDatafordistrictCount(paramDTO);
        }
        if (paramDTO.getCoreItemType().equals("04")){
            datafor= mapper.getDataforStoreCd(paramDTO);
            dataforCoun= mapper.getDatafordistrictCount(paramDTO);
        }
        // 获取总页数
        int totalPage = (dataforCoun % paramDTO.getRows() == 0) ? (dataforCoun / paramDTO.getRows()) : (dataforCoun / paramDTO.getRows()) + 1;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("data",datafor);
        map.put("count",dataforCoun);
        return map;
    }
}
