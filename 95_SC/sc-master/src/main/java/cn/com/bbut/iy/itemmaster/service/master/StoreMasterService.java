package cn.com.bbut.iy.itemmaster.service.master;

import cn.com.bbut.iy.itemmaster.dto.article.*;
import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.store.*;
import cn.com.bbut.iy.itemmaster.entity.MA0050;
import cn.com.bbut.iy.itemmaster.entity.MA1020;
import cn.com.bbut.iy.itemmaster.entity.MA1040;

import java.util.List;


public interface StoreMasterService {
    /**
     * 条件查询主档记录
     */
    GridDataDTO<StoreGridDTO> getList(StoreParamDTO dto);

    /**
     * 获取店铺基本信息
     * @param storeCd
     * @param effectiveStartDate
     * @return
     */
    StoreInfoDTO getStoreInfo(String storeCd, String effectiveStartDate);

    /**
     * 获取动态单选框
     * @param code
     * @param attributeType
     * @return
     */
    List<MA0050> getAttachedGroup(String code, String attributeType);

    /**
     * 附加属性
     * @param storeCd
     * @param effectiveStartDate
     * @param attributeType
     * @return
     */
    List<MA1020> getAttachedInfo(String storeCd,String effectiveStartDate, String attributeType);

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
    List<AutoCompleteDTO> getMa0030(String v);

    /**
     * 获取Store Group信息
     * @return
     */
    List<AutoCompleteDTO> getMa0035(String storeTypeCd,String v);

    /**
     * 通过店铺信息取得Store Cluster信息
     * @param storeCd
     * @return
     */
    StoreInfoDTO getClusterByStoreCd(String storeCd);

    /**
     * 通过店铺信息取得Store Group信息
     * @param storeCd
     * @return
     */
    StoreInfoDTO getGroupByStoreCd(String storeCd);
    /**
     * 获取地区信息
     * @param level
     * @return
     */
    List<AutoCompleteDTO> getMa0025(String level,String adminAddressCd,String v);

    /**
     * 获取附加信息
     * @param attributeType
     * @return
     */
    List<AutoCompleteDTO> getMa0050(String attributeType);
}
