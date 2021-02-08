package cn.com.bbut.iy.itemmaster.service;

import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsSelection;
import cn.com.bbut.iy.itemmaster.entity.od8020.OD8020;

import java.util.List;

/**
 * OD8020
 * 
 * @author lz
 */
public interface OD8020Service {

    //根据店铺查找商品
    List<RVItemsSelection> selectByStoreCd(String storeCd);
    //根据商品查找相关信息
    List<RVItemsResult> selectByArticleId(String article_id);
    //加载仓库
    List<OD8020> selectWHByStoreCd(String storeCd);
    //加载供应商
    List<OD8020> selectVByStoreCd(String storeCd);

    List<OD8020> selectVendorNameAndId();

}
