package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.MA1100Mapper;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100;
import cn.com.bbut.iy.itemmaster.entity.ma1100.MA1100Example;
import cn.com.bbut.iy.itemmaster.service.CM9060Service;
import cn.com.bbut.iy.itemmaster.service.MA1100Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lz
 */
@Service
public class MA1100ServiceImpl implements MA1100Service {

    @Autowired
    private MA1100Mapper ma1100Mapper;
    @Autowired
    private CM9060Service cm9060Service;

    @Override
    public List<MA1100> selectById(String articleId) {
        MA1100Example ma1100Example = new MA1100Example();
        MA1100Example.Criteria criteria = ma1100Example.createCriteria();
        //添加查询条件
        criteria.andArticleIdEqualTo(articleId);
        return ma1100Mapper.selectByExample(ma1100Example);
    }

    @Override
    public String selectByarticleId(String articleId) {
        return ma1100Mapper.selectByarticleName(articleId);
    }

    @Override
    public List<AutoCompleteDTO> getItemAll(String storeCd,String v) {
        String businessDate =  cm9060Service.getValByKey("0000");
        List<AutoCompleteDTO> list = ma1100Mapper.getItemAll(businessDate,v,storeCd);
        return list;
    }
    @Override
    public List<MA1100> getItemNo() {
        List<MA1100> ma1100s=ma1100Mapper.selectItem();
        return ma1100s;
    }
}
