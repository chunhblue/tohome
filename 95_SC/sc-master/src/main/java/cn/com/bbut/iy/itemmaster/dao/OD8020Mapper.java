package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dao.gen.OD8020GenMapper;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsResult;
import cn.com.bbut.iy.itemmaster.dto.returnOrder.returnVendor.RVItemsSelection;
import cn.com.bbut.iy.itemmaster.entity.od8020.OD8020;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OD8020Mapper extends OD8020GenMapper {
    //可选择商品
    List<RVItemsSelection> selectByStoreCd(@Param("storeCd") String storeCd);
    //根据商品编号查找相关信息
    List<RVItemsResult> selectByArticleId(@Param("articleId") String articleId);
    //根据店铺查找已订货仓库信息
    List<OD8020> selectWHByStoreCd(@Param("storeCd") String storeCd);
    //根据店铺查找已订货供应商信息
    List<OD8020> selectVByStoreCd(String storeCd);

    List<OD8020> selectVendorNameAndId();

}