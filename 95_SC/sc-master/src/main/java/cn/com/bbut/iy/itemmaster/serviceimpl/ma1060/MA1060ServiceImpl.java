package cn.com.bbut.iy.itemmaster.serviceimpl.ma1060;

import cn.com.bbut.iy.itemmaster.dao.MA1060Mapper;
import cn.com.bbut.iy.itemmaster.entity.MA1060;
import cn.com.bbut.iy.itemmaster.entity.MA1060Example;
import cn.com.bbut.iy.itemmaster.service.ma1060.MA1060Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MA1060ServiceImpl implements MA1060Service {

    @Autowired
    private MA1060Mapper mapper;


    /**
     * MA1060查询
     */
    @Override
    public List<MA1060> getList() {
        MA1060Example example = new MA1060Example();
        MA1060Example.Criteria criteria = example.createCriteria();
        criteria.andEffectiveStsEqualTo("10");
        example.or(criteria);
        return mapper.selectByExample(example);
    }
}
