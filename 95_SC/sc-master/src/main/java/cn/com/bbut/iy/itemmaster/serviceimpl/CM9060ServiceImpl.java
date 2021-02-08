package cn.com.bbut.iy.itemmaster.serviceimpl;

import cn.com.bbut.iy.itemmaster.dao.Cm9060Mapper;
import cn.com.bbut.iy.itemmaster.entity.base.Cm9060;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CM9060ServiceImpl implements CM9060Service {

    @Autowired
    private Cm9060Mapper cm9060Mapper;

    /**
     * get value by key
     *
     * key
     */
    @Override
    public String getValByKey(String key) {
        Cm9060 cm9060 = cm9060Mapper.selectByPrimaryKey(key);
        if(cm9060==null){
            return null;
        }
        return cm9060.getSpValue();
    }
}
