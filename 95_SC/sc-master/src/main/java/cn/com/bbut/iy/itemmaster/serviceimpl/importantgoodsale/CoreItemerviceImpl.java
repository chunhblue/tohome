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
        if (paramDTO.getCoreItemType().equals("01")) {
            datafor= mapper.getDataforg(paramDTO);

        }
        if (paramDTO.getCoreItemType().equals("02")){
            datafor = mapper.getDataforcity(paramDTO);
        }
        if (paramDTO.getCoreItemType().equals("03")){
            datafor= mapper.getDatafordistrict(paramDTO);
        }
        if (paramDTO.getCoreItemType().equals("04")){
            datafor= mapper.getDataforStoreCd(paramDTO);
        }
        // 获取总页数
        int totalPage = (datafor.size() % paramDTO.getRows() == 0) ? (datafor.size() / paramDTO.getRows()) : (datafor.size() / paramDTO.getRows()) + 1;
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("data",datafor);
        map.put("count",datafor.size()+1);
        return map;
    }
}
