package cn.com.bbut.iy.itemmaster.service.ma1000;


import cn.com.bbut.iy.itemmaster.dao.OD0000Mapper;
import cn.com.bbut.iy.itemmaster.entity.od0000.OD0000;
import cn.com.bbut.iy.itemmaster.service.OD0000Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lz
 */
@Service
public class OD0000ServiceImpl implements OD0000Service {

    @Autowired
    private OD0000Mapper od0000Mapper;

    //新增退仓库订单
    @Override
    public Integer insertOrder(OD0000 od0000) {
        return od0000Mapper.insertSelective(od0000);
    }
    //更新单据头档信息
    @Override
    public Integer updateOrder(OD0000 od00000) {
        return od0000Mapper.updateByPrimaryKeySelective(od00000);
    }
}
