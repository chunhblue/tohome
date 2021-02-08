package cn.com.bbut.iy.itemmaster.serviceimpl.ma1140;

import cn.com.bbut.iy.itemmaster.dao.MA1140Mapper;
import cn.com.bbut.iy.itemmaster.service.ma1140.MA1140Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("MA1140ServiceImpl")
public class MA1140ServiceImpl implements MA1140Service {
    @Autowired
    private MA1140Mapper ma1140Mapper;
    @Override
    public String selectBybarcode(String barcode) {
        return ma1140Mapper.selectBybarcode(barcode);
    }
}
