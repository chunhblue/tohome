package cn.com.bbut.iy.itemmaster.serviceimpl.ma1040;

import cn.com.bbut.iy.itemmaster.dao.MA1040Mapper;
import cn.com.bbut.iy.itemmaster.entity.MA1040;
import cn.com.bbut.iy.itemmaster.entity.MA1040Example;
import cn.com.bbut.iy.itemmaster.service.ma1040.MA1040Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MA1040ServiceImpl implements MA1040Service {

    @Autowired
    private MA1040Mapper mapper;

    /**
     * MA1040查询
     *
     * @param storeCd
     */
    @Override
    public List<MA1040> getList(String storeCd) {
        MA1040Example example = new MA1040Example();
        MA1040Example.Criteria criteria = example.createCriteria();
        criteria.andStoreCdEqualTo(storeCd).andEffectiveStsEqualTo("10");
        example.or(criteria);
        return mapper.selectByExample(example);
    }
}
