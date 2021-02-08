package cn.com.bbut.iy.itemmaster.serviceimpl.ma2000;

import cn.com.bbut.iy.itemmaster.dao.MA2000Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.vendor.VendorDTO;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000;
import cn.com.bbut.iy.itemmaster.entity.ma2000.MA2000Key;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.ma2000.Ma2000Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Ma2000ServiceImpl implements Ma2000Service {
    @Autowired
    private MA2000Mapper ma2000Mapper;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public List<AutoCompleteDTO> getListAll(String v,String storeCd) {
        String businessDate =  cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma2000Mapper.getListAll(businessDate,v,storeCd);
        return list;
    }

    @Override
    public MA2000 getVendorById(String v) {
        String businessDate =  cm9060Service.getValByKey("0000");
        return ma2000Mapper.selectById(businessDate,v);
    }
    @Override
    public List<AutoCompleteDTO> getAllVendor(String vendorId) {
        List<AutoCompleteDTO> vendors= ma2000Mapper.getVendor(vendorId);
        return vendors;
    }
}
