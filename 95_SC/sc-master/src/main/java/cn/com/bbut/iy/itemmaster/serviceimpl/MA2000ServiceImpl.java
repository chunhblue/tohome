package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.MA2000Mapper;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000Example;
import cn.com.bbut.iy.itemmaster.entity.od8030.OD8030Example;
import cn.com.bbut.iy.itemmaster.service.MA2000Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lz
 */
@Service
public class MA2000ServiceImpl implements MA2000Service {

    @Autowired
    private MA2000Mapper ma2000Mapper;

    @Override
    public String selectByVendorId(String vendorId) {
        return ma2000Mapper.selectByVendorId(vendorId);
    }
}
