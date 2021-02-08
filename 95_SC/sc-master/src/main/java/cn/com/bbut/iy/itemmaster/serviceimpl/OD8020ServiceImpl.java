package cn.com.bbut.iy.itemmaster.serviceimpl;


import cn.com.bbut.iy.itemmaster.dao.OD8020Mapper;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsSelection;
import cn.com.bbut.iy.itemmaster.entity.od8020.OD8020;
import cn.com.bbut.iy.itemmaster.service.OD8020Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lz
 */
@Service
public class OD8020ServiceImpl implements OD8020Service {

    @Autowired
    private OD8020Mapper od8020Mapper;

    //根据店铺查找商品
    @Override
    public List<RVItemsSelection> selectByStoreCd(String storeCd){
        return od8020Mapper.selectByStoreCd(storeCd);
    }
    //根据商品查找相关信息
    @Override
    public List<RVItemsResult> selectByArticleId(String article_id) {
        return od8020Mapper.selectByArticleId(article_id);
    }
    //加载仓库
    @Override
    public List<OD8020> selectWHByStoreCd(String storeCd) {
        return od8020Mapper.selectWHByStoreCd(storeCd);
    }
    //加载供应商
    @Override
    public List<OD8020> selectVByStoreCd(String storeCd) {
        return od8020Mapper.selectVByStoreCd(storeCd);
    }

    @Override
    public List<OD8020> selectVendorNameAndId() {
        return od8020Mapper.selectVendorNameAndId();

    }
}
