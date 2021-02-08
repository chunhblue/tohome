package cn.com.bbut.iy.itemmaster.serviceimpl.returnsDaily;

import cn.com.bbut.iy.itemmaster.dao.ReturnsDailyMapper;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyDTO;
import cn.com.bbut.iy.itemmaster.dto.returnsDaily.ReturnsDailyParamDTO;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.returnsDaily.ReturnsDailyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReturnsDailyServiceImpl implements ReturnsDailyService {

    @Autowired
    private ReturnsDailyMapper returnsDailyMapper;
    
    @Autowired
    private CM9060Service cm9060Service;

    // 查询退货数据
    @Override
    public List<ReturnsDailyDTO> search(ReturnsDailyParamDTO param) {

        List<ReturnsDailyDTO> result = returnsDailyMapper.search(param);

        return result;
    }

    // 查询店铺信息
    @Override
    public Ma1000 selectByStoreCd(String storeCd) {
        String businessDate = cm9060Service.getValByKey("0000");
        return returnsDailyMapper.selectByStoreCd(storeCd,businessDate);
    }
}
