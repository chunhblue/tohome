package cn.com.bbut.iy.itemmaster.serviceimpl.cm9030;

import cn.com.bbut.iy.itemmaster.dao.CM9030Mapper;
import cn.com.bbut.iy.itemmaster.entity.CM9030;
import cn.com.bbut.iy.itemmaster.entity.CM9030Example;
import cn.com.bbut.iy.itemmaster.service.cm9030.CM9030Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CM9030ServiceImpl implements CM9030Service {

    @Autowired
    private CM9030Mapper cm9030Mapper;

    /**
     * cm9030共通查询
     */
    @Override
    public List<CM9030> getList() {
        CM9030Example example = new CM9030Example();
//        CM9030Example.Criteria criteria = example.createCriteria();
//        example.or(criteria);
        return cm9030Mapper.selectByExample(example);
    }
}
