package cn.com.bbut.iy.itemmaster.dao;

import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.dto.store.*;
import cn.com.bbut.iy.itemmaster.entity.MA1040;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StoreMasterMapper {

    /**
     * 条件查询主档记录总数
     * @param dto
     */
    long selectCountByCondition(StoreParamDTO dto);

    /**
     * 条件查询主档记录
     * @param dto
     */
    List<StoreGridDTO> selectListByCondition(StoreParamDTO dto);

    /**
     * 获取店铺基本信息
     * @param storeCd
     * @param effectiveStartDate
     * @return
     */
    StoreInfoDTO getStoreInfo(String storeCd, String effectiveStartDate);

    /**
     * license
     * @param storeCd
     * @param effectiveStartDate
     * @return
     */
    List<MA1040> getLicenseInfo(String storeCd, String effectiveStartDate);
    /**
     * accounting
     * @param storeCd
     * @param effectiveStartDate
     * @return
     */
    List<MA1050GridDTO> getAccountingInfo(String storeCd, String effectiveStartDate);
    /**
     * 竞争者信息
     * @param storeCd
     * @param effectiveStartDate
     * @return
     */
    List<MA1060GridDTO> getCompetitorInfo(String storeCd, String effectiveStartDate);

    /**
     * 获取法人信息
     * @return
     */
    List<AutoCompleteDTO> getMa0010();

    /**
     * 获取price group信息
     * @return
     */
    List<AutoCompleteDTO> getMa0200();

    /**
     * 获取Store Cluster信息
     * @return
     */
    List<AutoCompleteDTO> getMa0030(@Param("v")String v);

    /**
     * 获取Store Group信息
     * @return
     */
    List<AutoCompleteDTO> getMa0035(@Param("storeTypeCd")String storeTypeCd,@Param("v")String v);

    // 通过店铺信息取得Store Cluster信息
    StoreInfoDTO getClusterByStoreCd(@Param("storeCd")String storeCd);

    StoreInfoDTO getGroupByStoreCd(@Param("storeCd")String storeCd);
    /**
     * 获取地区信息
     * @return
     */
    List<AutoCompleteDTO> getMa0025(@Param("level") String level,@Param("adminAddressCd")String adminAddressCd,@Param("v")String v);

    /**
     * 获取附加信息
     * @param attributeType
     * @return
     */
    List<AutoCompleteDTO> getMa0050(@Param("attributeType") String attributeType);
}
