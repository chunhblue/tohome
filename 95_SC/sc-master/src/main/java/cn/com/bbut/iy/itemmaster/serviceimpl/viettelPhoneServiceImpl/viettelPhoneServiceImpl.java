package cn.com.bbut.iy.itemmaster.serviceimpl.viettelPhoneServiceImpl;

import cn.com.bbut.iy.itemmaster.dao.viettelPhoneMapper.ViettelPhoneMapper;
import cn.com.bbut.iy.itemmaster.dto.viettelParamPhone.ma8407Paramdto;
import cn.com.bbut.iy.itemmaster.dto.viettelPhone.ma8407dto;
import cn.com.bbut.iy.itemmaster.service.viettelPhoneService.ViettelPhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName viettelPhineServiceImpl
 * @Description TODO
 * @Author Ldd
 * @Date 2021/2/18 18:41
 * @Version 1.0
 * @Description
 */
@Service
public class viettelPhoneServiceImpl  implements ViettelPhoneService {

    @Autowired
    private ViettelPhoneMapper viettelPhoneMapper;
    @Override
    public Map<String, Object> searchData(ma8407Paramdto param) {
        int count = 0;
        // 获取总页数
        int totalPage = 0;
        if (param.isFlg()) {
            count = viettelPhoneMapper.searchCount(param);
                // 获取总页数
            totalPage = (count % param.getRows() == 0) ? (count / param.getRows()) : (count / param.getRows()) + 1;

        }
        List<ma8407dto> list = viettelPhoneMapper.searchData(param);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("totalPage",totalPage);
        map.put("count",count);
        map.put("data",list);
        return map;
    }
}
