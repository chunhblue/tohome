package cn.com.bbut.iy.itemmaster.serviceimpl.od0010RefServiceImpl;

import cn.com.bbut.iy.itemmaster.dao.Ma1000Mapper;
import cn.com.bbut.iy.itemmaster.entity.Ma1010aboutOd0000;
import cn.com.bbut.iy.itemmaster.entity.Ma1010aboutOd0000Example;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000;
import cn.com.bbut.iy.itemmaster.entity.base.Ma1000Example;
import cn.com.bbut.iy.itemmaster.service.od0010refer.od0010RefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @ClassName od0010RefServiceImpl
 * @Description TODO
 * @Author Administrator
 * @Date 2020/3/16 14:23
 * @Version 1.0
 */
@Service
public class od0010RefServiceImpl implements od0010RefService {
    @Autowired
    private Ma1000Mapper ma1000Mapper;
    @Override
    public Collection<Ma1000> getAllInfoStode(Ma1000 ma1000) {
        Ma1000Example ma1000Example = new Ma1000Example();

        return  ma1000Mapper.selectByExample(ma1000Example);
    }
}
