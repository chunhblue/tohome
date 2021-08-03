package cn.com.bbut.iy.itemmaster.service.inventory;


import cn.com.bbut.iy.itemmaster.dto.base.AutoCompleteDTO;
import cn.com.bbut.iy.itemmaster.dto.base.CommonDTO;
import cn.com.bbut.iy.itemmaster.dto.base.GridDataDTO;
import cn.com.bbut.iy.itemmaster.dto.inventory.*;
import cn.com.bbut.iy.itemmaster.dto.ma100Ld.Ma1000DTO;
import cn.com.bbut.iy.itemmaster.dto.ma8350.MA8350dto;
import cn.com.bbut.iy.itemmaster.entity.SK0010;
import cn.com.bbut.iy.itemmaster.entity.SK0010Key;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * InventoryVouchers
 * 
 * @author
 */
public interface InventoryVouchersService {

    /*
     * 条件查询一览数据
     *
     */
    GridDataDTO<InventoryVouchersGridDTO> getListByCondition(InventoryVouchersParamDTO param);

    /*
     * 类型&条件查询数据
     *
     */
    GridDataDTO<InventoryVouchersGridDTO> getByTypeCondition(InventoryVouchersParamDTO param);

    /*
     * 店间转入查询数据
     *
     */
    GridDataDTO<InventoryVouchersGridDTO> getByTypeInCondition(InventoryVouchersParamDTO param);

    /*
     * 查询商品信息
     *
     */
    ItemInfoDTO getItemInfoByCode(String storeCd, String itemCode);

    /*
     * 查询店铺信息
     *
     */
    StoreInfoDTO getStoreByCode(String storeCd);

    /**
     * 保存传票
     *
     * @return
     */
    String insert(Sk0010DTO sk0010, List<Sk0020DTO> sk0020List);

    /**
     * 修改传票
     *
     */
    int update(SK0010 sk0010, List<Sk0020DTO> sk0020List);

    /**
     *更新转出货物的实际转入数量
     */
    int updateQty1(Sk0020DTO sk0020DTO);

    /**
     *同步调拨商品数量不一致的原因
     * @return
     */
    int updateDiffReasons(Sk0020DTO sk0020DTO);

    /**
     * 修改传票商品详情
     *
     */
    int updateSk0020(CommonDTO dto, List<Sk0020DTO> sk0020List);

    /**
     * 查询头档数据
     *
     */
    SK0010 getSk0010(SK0010Key sk0010);

    /**
     * 查询详情数据
     *
     */
    GridDataDTO<Sk0020DTO> getSk0020(Sk0020ParamDTO sk0020);

    /**
     * 查询详情数据
     *
     */
    GridDataDTO<Sk0020DTO> selectDetailSk0020(Sk0020ParamDTO sk0020);

    /**
     * 查询传票编号
     *
     */
    int getSk0010ByVoucherNo(String voucherNo);

    // 自动加载store 下拉
    List<AutoCompleteDTO> getStoreList(String v);

    // 自动加载 item 下拉
    List<AutoCompleteDTO> getItemList(String storeCd, String v);
    List<AutoCompleteDTO> getInventoryItemList(String storeCd, String v);

    //  DOC No. 下拉
    List<AutoCompleteDTO> getDOCList(String storeCd,String storeCd1,String v);

    List<InventoryVouchersGridDTO> existsDOC(String storeCd,String storeCd1);

    // 判断转出商品是否审核通过
    List<InventoryVouchersGridDTO> getApprovedList(String storeCd,String storeCd1);

    /**
     * 获取实时库存数量
     *
     */
    String getStock(String storeCd, String itemId);

    /**
     *获取一般水平的理由
     * @param v
     * @return
     */
    List<AutoCompleteDTO> generalReason(String v);

    List<AutoCompleteDTO> detailReason(String v,int num);

    Map<String, Object> Total(Sk0020ParamDTO sk0020);

    List<AutoCompleteDTO> Reasondetail(String v);

    MA8350dto getGeneralReason(String generalLevelCd);

    MA8350dto getGeneralReasonDetail(String detailedLevelCd);

    GridDataDTO<Sk0020DTO> getSk0020Out(Sk0020ParamDTO sk0020);

    Ma1000DTO getSouthOrNouth(String storeCd);

    List<AutoCompleteDTO> getOutStoreList(String v, String zoCd);
    List<AutoCompleteDTO> getStoreListByInStore(String v, String vstore);

    List<AutoCompleteDTO> getMa1172OutItemList(String storeCd, String v);

    List<AutoCompleteDTO> getMa1172InItemList(String outArticleId, String storeCd, String v);

    SK0010 getStoreSeries(String storeCd);

    List<SK0010> getApprovedInfo(String voucherNo);
}
